package service

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener

class WordService {
    private val stringBuffer = StringBuilder()

    init {
        registerWordDetecting()
    }

    fun registerWordDetecting() {
        GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
            override fun nativeKeyPressed(event: NativeKeyEvent) {
                val key = NativeKeyEvent.getKeyText(event.keyCode)
                if (key.length == 1 && key[0].isLetterOrDigit()) {
                    stringBuffer.append(key.lowercase())
                } else if (key == "Space") {
                    val word = stringBuffer.toString()
                    println(word)
                    stringBuffer.clear()
                }
            }

            override fun nativeKeyReleased(event: NativeKeyEvent) {}
            override fun nativeKeyTyped(event: NativeKeyEvent) {}
        })
    }
}