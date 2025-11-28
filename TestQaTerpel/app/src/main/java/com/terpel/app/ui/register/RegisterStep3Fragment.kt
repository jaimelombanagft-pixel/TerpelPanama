package com.terpel.app.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.terpel.app.databinding.FragmentRegisterStep3Binding
import coil.load

class RegisterStep3Fragment : Fragment() {
    private var _binding: FragmentRegisterStep3Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.headerImage.load("https://www.terpelpanama.com/images/linea-lubricantes-h.png") {
            crossfade(true)
            placeholder(com.terpel.app.R.drawable.ic_terpel_placeholder)
        }

        binding.authorizationCheck.setOnCheckedChangeListener { _, checked ->
            binding.btnNextOtp.isEnabled = checked
        }

        binding.btnNextOtp.setOnClickListener {
            if (binding.authorizationCheck.isChecked) {
                findNavController().navigate(com.terpel.app.R.id.action_registerStep3_to_otp)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
