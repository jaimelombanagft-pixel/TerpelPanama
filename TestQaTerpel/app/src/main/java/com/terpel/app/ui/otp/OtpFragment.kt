package com.terpel.app.ui.otp

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.terpel.app.R
import com.terpel.app.databinding.FragmentOtpBinding

class OtpFragment : Fragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.otpEditText.filters = arrayOf(InputFilter.LengthFilter(6))
        binding.otpEditText.doOnTextChanged { text, _, _, _ ->
            binding.btnVerify.isEnabled = (text?.length ?: 0) == 6
        }
        viewModel.startTimer()
        viewModel.remaining.observe(viewLifecycleOwner) { seconds ->
            binding.timerText.text = getString(R.string.resend_in, seconds)
        }
        viewModel.canResend.observe(viewLifecycleOwner) { can ->
            binding.btnResend.isEnabled = can
        }
        viewModel.verified.observe(viewLifecycleOwner) { ok ->
            if (ok) findNavController().navigate(R.id.action_otp_to_home)
            else Snackbar.make(binding.root, "Código inválido", Snackbar.LENGTH_SHORT).show()
        }
        binding.btnVerify.setOnClickListener {
            viewModel.verify(binding.otpEditText.text?.toString().orEmpty())
        }
        binding.btnResend.setOnClickListener {
            viewModel.startTimer()
            Snackbar.make(binding.root, "Código reenviado", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

