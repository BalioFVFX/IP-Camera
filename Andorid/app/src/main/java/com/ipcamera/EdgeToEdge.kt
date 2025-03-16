package com.ipcamera

import android.view.View
import android.view.Window
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

abstract class WindowInsetsHandler {
    abstract fun onApplyWindowInsets(
        root: View,
        windowInsets: WindowInsetsCompat
    ) : WindowInsetsCompat
}

class DefaultInsetsHandler : WindowInsetsHandler() {

    override fun onApplyWindowInsets(
        root: View,
        windowInsets: WindowInsetsCompat,
        ): WindowInsetsCompat {

        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

        root.setPadding(
            0,
            insets.top,
            0,
            insets.bottom,
        )

        return WindowInsetsCompat.CONSUMED
    }
}

class StreamActivityInsetsHandler(
    private val onApplySystemBarsInsets: (insets: Insets) -> Unit,
) : WindowInsetsHandler() {

    override fun onApplyWindowInsets(
        root: View,
        windowInsets: WindowInsetsCompat,
    ): WindowInsetsCompat {

        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

        root.setPadding(
            0,
            0,
            0,
            0,
        )

        onApplySystemBarsInsets.invoke(insets)

        return WindowInsetsCompat.CONSUMED
    }
}

object EdgeToEdge {

    fun setDecorFitsSystemWindows(
        window: Window,
        fitSystemWindows: Boolean
    ) {
        WindowCompat.setDecorFitsSystemWindows(window, fitSystemWindows)
    }

    fun setInsetsHandler(
        root: View,
        handler: WindowInsetsHandler,
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(
            root
        ) { view, insets ->

            handler.onApplyWindowInsets(
                root = view,
                windowInsets = insets,
            )
        }
    }

    fun enableImmersiveMode(window: Window) {
        val insetsController = WindowCompat.getInsetsController(
            window,
            window.decorView,
        )

        insetsController.systemBarsBehavior = WindowInsetsControllerCompat
            .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        insetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}