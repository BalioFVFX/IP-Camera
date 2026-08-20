package com.ipcamera

import android.content.Context

class SettingsPreferences(context: Context) {

    companion object {
        private const val IP_KEY = "ip"
        private const val WIDTH_KEY = "stream_width"
        private const val HEIGHT_KEY = "stream_height"
        private const val DEFAULT_WIDTH = 1280
        private const val DEFAULT_HEIGHT = 720
    }

    private val sharedPreferences =
        context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

    fun saveIpAddress(ip: String) {
        sharedPreferences.edit()
            .putString(IP_KEY, ip)
            .apply()
    }

    fun getIpAddress(): String? {
        return sharedPreferences.getString(IP_KEY, "192.168.0.101:4321")
    }

    fun getStreamWidth(): Int {
        val v = sharedPreferences.getInt(WIDTH_KEY, DEFAULT_WIDTH)
        return if (v in 160..3840) v else DEFAULT_WIDTH
    }

    fun getStreamHeight(): Int {
        val v = sharedPreferences.getInt(HEIGHT_KEY, DEFAULT_HEIGHT)
        return if (v in 120..2160) v else DEFAULT_HEIGHT
    }

    fun setStreamResolution(width: Int, height: Int) {
        var w = width.coerceIn(160, 3840)
        var h = height.coerceIn(120, 2160)
        if (w % 2 != 0) w += 1
        if (h % 2 != 0) h += 1
        sharedPreferences.edit()
            .putInt(WIDTH_KEY, w)
            .putInt(HEIGHT_KEY, h)
            .apply()
    }
}