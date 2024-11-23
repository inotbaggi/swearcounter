package me.baggi.swears.service

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import me.baggi.swears.data.SwearStorage
import java.awt.im.InputContext

class WordService(val isDebug: Boolean = false) {
    private val stringBuffer = StringBuilder()
    val inputContext = InputContext.getInstance()

    private var lastKey: String = ""
    private var lastKeyTime: Long = -1

    init {
        registerWordDetecting()
    }

    private fun onWordDetect(word: String) {
        when {
            SwearStorage.swears.contains(word) -> {
                println("100% swear: $word")
            }
            SwearStorage.swears.any { it.contains(word) } -> {
                println("соу соу: $word")
            }
        }
    }

    private fun registerWordDetecting() {
        GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
            override fun nativeKeyPressed(event: NativeKeyEvent) {

                val key = NativeKeyEvent.getKeyText(event.keyCode)

                when {
                    key.length == 1 && key[0].isLetter() -> {
                        if (inputContext.locale.language == "ru") {
                            stringBuffer.append(convertToCyrillic(key.lowercase()[0]))
                        } else {
                            stringBuffer.append(key.lowercase()[0])
                        }

                    }
                    key == "Space" || key == "Enter" -> {
                        val word = stringBuffer.toString()
                        onWordDetect(word)
                        stringBuffer.clear()
                    }
                    key == "Backspace" -> {
                        if (stringBuffer.isNotEmpty()) {
                            stringBuffer.setLength(stringBuffer.length - 1)
                        }
                    }
                }
                if (isDebug) println("Buffer: $stringBuffer " +
                        "(lastKey: $lastKey " +
                        "time: ${System.currentTimeMillis() - lastKeyTime} " +
                        "lang: ${inputContext.locale.language}" +
                        ")"
                )
                lastKey = key
                lastKeyTime = System.currentTimeMillis()
            }

            override fun nativeKeyReleased(event: NativeKeyEvent) {}
            override fun nativeKeyTyped(event: NativeKeyEvent) {}
        })
    }

    fun convertToCyrillic(char: Char): Char {
        val map = mapOf(
            'q' to 'й', 'w' to 'ц', 'e' to 'у', 'r' to 'к', 't' to 'е', 'y' to 'н',
            'u' to 'г', 'i' to 'ш', 'o' to 'щ', 'p' to 'з', '[' to 'х', ']' to 'ъ',
            'a' to 'ф', 's' to 'ы', 'd' to 'в', 'f' to 'а', 'g' to 'п', 'h' to 'р',
            'j' to 'о', 'k' to 'л', 'l' to 'д', ';' to 'ж', '\'' to 'э', 'z' to 'я',
            'x' to 'ч', 'c' to 'с', 'v' to 'м', 'b' to 'и', 'n' to 'т', 'm' to 'ь',
            ',' to 'б', '.' to 'ю'
        )
        return map[char] ?: char
    }
}