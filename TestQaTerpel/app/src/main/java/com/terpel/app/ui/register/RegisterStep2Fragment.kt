package com.terpel.app.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.terpel.app.databinding.FragmentRegisterStep2Binding
import android.util.Log

class RegisterStep2Fragment : Fragment() {
    private var _binding: FragmentRegisterStep2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("RegisterStep2", "onViewCreated binding initialized=${_binding != null}")
        Log.d("RegisterStep2", "btnRegisterFinal present=${binding.btnRegisterFinal != null}")
        Log.d("RegisterStep2", "btnRegisterFinal enabled=${binding.btnRegisterFinal.isEnabled} visible=${binding.btnRegisterFinal.visibility == View.VISIBLE}")

        var phoneValid = false
        var documentValid = false
        var termsAccepted = false
        var passwordValid = false
        var confirmValid = false

        fun refreshButton() {
            binding.btnRegisterFinal.isEnabled = phoneValid && documentValid && termsAccepted && passwordValid && confirmValid && !(viewModel.state.value?.isLoading ?: false)
        }

        fun computeInitialValidity() {
            val phoneText = binding.phoneEditText.text?.toString().orEmpty()
            val docText = binding.documentEditText.text?.toString().orEmpty()
            termsAccepted = binding.termsCheck.isChecked
            phoneValid = phoneText.length >= 7
            documentValid = docText.length >= 6
            binding.phoneInputLayout.error = if (phoneValid) null else "Celular inválido"
            binding.documentInputLayout.error = if (documentValid) null else "Documento inválido"
            refreshButton()
        }

        binding.phoneEditText.doOnTextChanged { text, _, _, _ ->
            Log.d("RegisterStep2", "phone changed=$text")
            phoneValid = (text?.length ?: 0) >= 7
            binding.phoneInputLayout.error = if (phoneValid) null else "Celular inválido"
            refreshButton()
        }
        binding.documentEditText.doOnTextChanged { text, _, _, _ ->
            Log.d("RegisterStep2", "document changed=$text")
            documentValid = (text?.length ?: 0) >= 6
            binding.documentInputLayout.error = if (documentValid) null else "Documento inválido"
            refreshButton()
        }
        binding.termsCheck.setOnCheckedChangeListener { _, checked ->
            Log.d("RegisterStep2", "terms checked=$checked")
            termsAccepted = checked
            refreshButton()
        }

        binding.passwordEditTextStep2.doOnTextChanged { text, _, _, _ ->
            val value = text?.toString().orEmpty()
            viewModel.onPasswordChanged(value)
            passwordValid = value.length >= 8
            binding.passwordInputLayoutStep2.error = if (passwordValid) null else "Mínimo 8 caracteres"
            val strength = computeStrength(value)
            binding.passwordStrengthIndicator.progress = strength
            refreshButton()
        }
        binding.confirmEditTextStep2.doOnTextChanged { text, _, _, _ ->
            val value = text?.toString().orEmpty()
            viewModel.onConfirmChanged(value)
            confirmValid = value == (viewModel.state.value?.password ?: "")
            binding.confirmInputLayoutStep2.error = if (confirmValid) null else "No coincide"
            refreshButton()
        }

        viewModel.state.observe(viewLifecycleOwner) { s ->
            binding.progressStep2.visibility = if (s.isLoading) View.VISIBLE else View.GONE
        }

        binding.btnRegisterFinal.setOnClickListener {
            Log.d("RegisterStep2", "btnRegisterFinal clicked enabled=${binding.btnRegisterFinal.isEnabled}")
            Log.d("RegisterStep2", "valids phone=$phoneValid doc=$documentValid terms=$termsAccepted")
            if (phoneValid && documentValid && termsAccepted && passwordValid && confirmValid) {
                try {
                    binding.progressStep2.visibility = View.VISIBLE
                    android.util.Log.d("Navigation", "step2 -> step3 (Registro2)")
                    findNavController().navigate(com.terpel.app.R.id.action_registerStep2_to_step3)
                } catch (t: Throwable) {
                    Log.e("RegisterStep2", "navigation error", t)
                    Snackbar.make(binding.root, "Error en la transición", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                if (!phoneValid) binding.phoneInputLayout.error = "Celular inválido"
                if (!documentValid) binding.documentInputLayout.error = "Documento inválido"
                if (!passwordValid) binding.passwordInputLayoutStep2.error = "Mínimo 8 caracteres"
                if (!confirmValid) binding.confirmInputLayoutStep2.error = "No coincide"
                if (!termsAccepted) Snackbar.make(binding.root, "Debes aceptar los términos", Snackbar.LENGTH_SHORT).show()
                Snackbar.make(binding.root, "Completa los campos obligatorios", Snackbar.LENGTH_SHORT).show()
            }
        }

        computeInitialValidity()
    }

    private fun computeStrength(pwd: String): Int {
        var score = 0
        if (pwd.length >= 8) score += 25
        if (pwd.any { it.isUpperCase() }) score += 25
        if (pwd.any { it.isDigit() }) score += 25
        if (pwd.any { !it.isLetterOrDigit() }) score += 25
        return score
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
