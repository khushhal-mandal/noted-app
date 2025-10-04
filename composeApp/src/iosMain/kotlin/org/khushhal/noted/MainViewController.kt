package org.khushhal.noted

import App
import androidx.compose.ui.window.ComposeUIViewController
import org.khushhal.noted.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    App()
}