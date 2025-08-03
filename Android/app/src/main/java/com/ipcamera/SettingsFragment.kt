package com.ipcamera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.ipcamera.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: SettingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        EdgeToEdge.setInsetsHandler(
            root = binding.root,
            handler = DefaultInsetsHandler(),
        )

        val prefs = SettingsPreferences(requireContext().applicationContext)

        prefs.getIpAddress()?.let { ipAddress ->
            binding.editTextIp.setText(ipAddress)
        }

        binding.editTextIp.addTextChangedListener {
            if (binding.textInputIp.error != null) {
                binding.textInputIp.error = null
            }
        }

        binding.btnSave.setOnClickListener {
            val input = binding.editTextIp.text?.toString() ?: ""

            val portSeparatorCount = input.count { it == ':' }

            if (portSeparatorCount != 1 || input.length <= 10) {
                binding.textInputIp.error = "Invalid IP format provided"
                return@setOnClickListener
            }

            prefs.saveIpAddress(input)

            activity?.onBackPressed()
        }
    }
}
