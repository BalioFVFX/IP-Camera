package com.ipcamera

import android.content.Context

class SettingsPreferences(context: Context) {

    companion object {
        private const val IP_KEY = "ip"
    }

    private val sharedPreferences =
        context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)


    fun saveIpAddress(ip: String) {
        sharedPreferences.edit()
            .putString(IP_KEY, ip)
            .apply()
    }

    fun getIpAddress() : String? {
        return sharedPreferences.getString(IP_KEY, "192.168.0.101:4321")
    }
}