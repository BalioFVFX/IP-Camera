package com.ipcamera

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ipcamera.databinding.CameraPermissionActivityBinding

class CameraPermissionActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQ_CODE = 1000

        fun hasPermission(context: Context) : Boolean {
            return ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EdgeToEdge.setDecorFitsSystemWindows(
            window = window,
            fitSystemWindows = false,
        )

        val binding = CameraPermissionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EdgeToEdge.setInsetsHandler(
            root = binding.root,
            handler = DefaultInsetsHandler(),
        )

        binding.buttonContinue.setOnClickListener {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.CAMERA,
                ),
                PERMISSION_REQ_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults.size != 1 ||
                grantResults[0] != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
            }

            finish()
        }
    }
}