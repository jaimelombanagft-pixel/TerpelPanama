package com.terpel.app.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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

    private fun validateButtonState() {
        val state = viewModel.state.value
        val allFieldsFilled = !state?.fullName.isNullOrBlank() &&
                !state?.lastName.isNullOrBlank() &&
                !state?.email.isNullOrBlank() &&
                !state?.vehicleType.isNullOrBlank() &&
                binding.authorizationCheck.isChecked
        binding.btnNextOtp.isEnabled = allFieldsFilled
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.headerImage.load("https://www.terpelpanama.com/images/linea-lubricantes-h.png") {
            crossfade(true)
            placeholder(com.terpel.app.R.drawable.ic_terpel_placeholder)
        }

        binding.authorizationCheck.setOnCheckedChangeListener { _, _ ->
            validateButtonState()
        }

        // Observar el estado del registro (cambios en los datos)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            // Actualizar el estado del botón cuando cambien los datos
            validateButtonState()
            
            if (state.isLoading) {
                binding.btnNextOtp.visibility = View.GONE
                // Mostrar un progress indicator si existe
            } else {
                binding.btnNextOtp.visibility = View.VISIBLE
            }

            if (state.isSuccess && state.registrationResponse != null) {
                Log.d("RegisterStep3", "Registro exitoso: ${state.registrationResponse.message}")
                // Navegar al OTP
                try {
                    findNavController().navigate(com.terpel.app.R.id.action_registerStep3_to_otp)
                } catch (e: Exception) {
                    Log.e("RegisterStep3", "Error navegando a OTP", e)
                    Snackbar.make(binding.root, "Error en la transición", Snackbar.LENGTH_SHORT).show()
                }
            }

            if (state.errorMessage != null) {
                Log.e("RegisterStep3", "Error: ${state.errorMessage}")
                Snackbar.make(binding.root, "Error: ${state.errorMessage}", Snackbar.LENGTH_LONG).show()
            }
        }

        validateButtonState()

        binding.btnNextOtp.setOnClickListener {
            if (binding.authorizationCheck.isChecked) {
                Log.d("RegisterStep3", "Iniciando registro de usuario")
                viewModel.registerUser()
            } else {
                Snackbar.make(binding.root, "Debes aceptar los términos", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
