package org.khushhal.noted

import App
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.khushhal.noted.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Noted",
    ) {
        App()
    }
}