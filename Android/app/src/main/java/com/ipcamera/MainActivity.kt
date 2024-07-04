package com.ipcamera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ipcamera.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, MainFragment(), "MainFragment")
            .commit()
    }
}