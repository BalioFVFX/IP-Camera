package com.ipcamera

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ipcamera.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        private const val PERMISSION_REQ_CODE = 1000
    }

    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        EdgeToEdge.setInsetsHandler(
            root = binding.root,
            handler = DefaultInsetsHandler(),
        )

        val prefs = SettingsPreferences(requireContext().applicationContext)

        binding.buttonStream.setOnClickListener {
            if (CameraPermissionActivity.hasPermission(requireContext())) {
                if (prefs.getIpAddress() == null) {
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, SettingsFragment(), "SettingsFragment")
                        .addToBackStack("MainFragment")
                        .commit()

                    Toast.makeText(
                        requireContext(),
                        "Server IP address is required",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    startActivity(Intent(requireContext(), StreamActivity::class.java))
                }

            } else {
                startActivityForResult(
                    Intent(
                        requireContext(),
                        CameraPermissionActivity::class.java
                    ), PERMISSION_REQ_CODE
                )
            }
        }

        binding.buttonSettings.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment(), "SettingsFragment")
                .addToBackStack("MainFragment")
                .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PERMISSION_REQ_CODE) {
            if (CameraPermissionActivity.hasPermission(requireContext())) {
                startActivity(Intent(requireContext(), StreamActivity::class.java))
            }
        }
    }
}