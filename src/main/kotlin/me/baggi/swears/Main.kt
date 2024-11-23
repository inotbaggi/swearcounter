package me.baggi.swears

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.kwhat.jnativehook.GlobalScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.baggi.swears.service.WordService

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

fun main() = application {
    GlobalScreen.registerNativeHook()

    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch(Dispatchers.IO) {
        WordService(true)
    }

    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
