package com.ipcamera

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ipcamera.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EdgeToEdge.setDecorFitsSystemWindows(
            window = window,
            fitSystemWindows = false,
        )

        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, MainFragment(), "MainFragment")
            .commit()
    }
}