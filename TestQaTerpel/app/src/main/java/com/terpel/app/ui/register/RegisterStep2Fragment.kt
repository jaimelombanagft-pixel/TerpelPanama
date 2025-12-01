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
import androidx.core.util.PatternsCompat

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

        // Pre-fill documento and celular from step 1
        val state = viewModel.state.value
        binding.documentEditText.setText(state?.document.orEmpty())
        binding.phoneEditText.setText(state?.phone.orEmpty())

        var nameValid = false
        var lastNameValid = false
        var emailValid = false
        var termsAccepted = false
        var vehicleTypeSelected = false

        fun refreshButton() {
            binding.btnRegisterFinal.isEnabled = nameValid && lastNameValid && emailValid && termsAccepted && vehicleTypeSelected && !(viewModel.state.value?.isLoading ?: false)
        }

        binding.nameEditText.doOnTextChanged { text, _, _, _ ->
            val value = text?.toString().orEmpty().trim()
            viewModel.onNameChanged(value)
            nameValid = value.isNotEmpty() && value.length >= 3
            binding.nameInputLayout.error = if (nameValid) null else "Mínimo 3 caracteres"
            refreshButton()
        }

        binding.lastNameEditText.doOnTextChanged { text, _, _, _ ->
            val value = text?.toString().orEmpty().trim()
            viewModel.onLastNameChanged(value)
            lastNameValid = value.isNotEmpty() && value.length >= 3
            binding.lastNameInputLayout.error = if (lastNameValid) null else "Mínimo 3 caracteres"
            refreshButton()
        }

        binding.emailEditText.doOnTextChanged { text, _, _, _ ->
            val value = text?.toString().orEmpty().trim()
            viewModel.onEmailChanged(value)
            emailValid = value.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(value).matches()
            binding.emailInputLayout.error = if (emailValid) null else "Email inválido"
            refreshButton()
        }

        binding.termsCheck.setOnCheckedChangeListener { _, checked ->
            Log.d("RegisterStep2", "terms checked=$checked")
            termsAccepted = checked
            refreshButton()
        }

        // Setup vehicle checkboxes - solo se puede seleccionar uno
        val checkboxes = listOf(
            binding.checkMoto,
            binding.checkTaxi,
            binding.checkBus,
            binding.checkParticular,
            binding.checkCamion
        )

        checkboxes.forEach { checkbox ->
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Desmarcar los otros
                    checkboxes.forEach { other ->
                        if (other != checkbox) {
                            other.isChecked = false
                        }
                    }
                    // Guardar el vehículo seleccionado
                    val vehicleType = when (checkbox.id) {
                        binding.checkMoto.id -> "Moto"
                        binding.checkTaxi.id -> "Taxi"
                        binding.checkBus.id -> "Bus"
                        binding.checkParticular.id -> "Particular"
                        binding.checkCamion.id -> "Camión"
                        else -> ""
                    }
                    viewModel.onVehicleTypeChanged(vehicleType)
                    vehicleTypeSelected = true
                } else {
                    vehicleTypeSelected = checkboxes.any { it.isChecked }
                    if (!vehicleTypeSelected) {
                        viewModel.onVehicleTypeChanged("")
                    }
                }
                refreshButton()
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { s ->
            binding.progressStep2.visibility = if (s.isLoading) View.VISIBLE else View.GONE
        }

        binding.btnValidateReferral.setOnClickListener {
            val referralCode = binding.referralEditText.text?.toString().orEmpty()
            if (referralCode.isNotEmpty()) {
                Snackbar.make(binding.root, "Código referido validado", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnRegisterFinal.setOnClickListener {
            Log.d("RegisterStep2", "btnRegisterFinal clicked enabled=${binding.btnRegisterFinal.isEnabled}")
            if (nameValid && lastNameValid && emailValid && termsAccepted && vehicleTypeSelected) {
                try {
                    binding.progressStep2.visibility = View.VISIBLE
                    android.util.Log.d("Navigation", "step2 -> step3 (Registro2)")
                    findNavController().navigate(com.terpel.app.R.id.action_registerStep2_to_step3)
                } catch (t: Throwable) {
                    Log.e("RegisterStep2", "navigation error", t)
                    Snackbar.make(binding.root, "Error en la transición", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                if (!nameValid) binding.nameInputLayout.error = "Nombre requerido"
                if (!lastNameValid) binding.lastNameInputLayout.error = "Apellido requerido"
                if (!emailValid) binding.emailInputLayout.error = "Email inválido"
                if (!vehicleTypeSelected) Snackbar.make(binding.root, "Debes seleccionar tipo de vehículo", Snackbar.LENGTH_SHORT).show()
                if (!termsAccepted) Snackbar.make(binding.root, "Debes aceptar los términos", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
