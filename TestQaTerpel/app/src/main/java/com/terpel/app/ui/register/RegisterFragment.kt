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
import com.terpel.app.databinding.FragmentRegisterBinding
import android.util.Log
import coil.load

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerImage.load("https://www.terpelpanama.com/images/vayven-amenidad-2.jpg") {
            crossfade(true)
            placeholder(com.terpel.app.R.drawable.ic_terpel_placeholder)
        }

        binding.documentEditText.doOnTextChanged { text, _, _, _ ->
            val v = text?.toString().orEmpty()
            viewModel.onDocumentChanged(v)
            binding.documentInputLayout.error = if (v.length >= 6) null else "Número inválido"
            updateStep1Button()
        }
        binding.phoneEditText.doOnTextChanged { text, _, _, _ ->
            val v = text?.toString().orEmpty()
            viewModel.onPhoneChanged(v)
            binding.phoneInputLayout.error = if (v.length >= 7) null else "Celular inválido"
            updateStep1Button()
        }
        setupDocTypeDropdown()

        viewModel.state.observe(viewLifecycleOwner) { state ->
            updateStep1Button()
            binding.progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        }

        binding.btnSubmit.setOnClickListener {
            val s = viewModel.state.value
            val valid = s != null && s.document.length >= 6 && s.phone.length >= 7
            Log.d("RegistroStep1", "Click Siguiente: doc='${s?.document}' phone='${s?.phone}' valid=$valid")
            if (valid) {
                try {
                    Log.d("RegistroStep1", "Navegando a Registro2 (action_register_to_step2)")
                    findNavController().navigate(com.terpel.app.R.id.action_register_to_step2)
                } catch (t: Throwable) {
                    Log.e("RegistroStep1", "Error en navegación a Registro2", t)
                    Snackbar.make(binding.root, "Error al navegar", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                val docOk = (s?.document?.length ?: 0) >= 6
                val phoneOk = (s?.phone?.length ?: 0) >= 7
                Log.w("RegistroStep1", "Bloqueado: docOk=$docOk phoneOk=$phoneOk")
            }
        }

    }

    private fun setupDocTypeDropdown() {
        val items = resources.getStringArray(com.terpel.app.R.array.doc_types)
        val adapter = android.widget.ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        binding.docTypeAuto.setAdapter(adapter)
        binding.docTypeAuto.setText(items.first(), false)
    }

    private fun updateStep1Button() {
        val s = viewModel.state.value
        val valid = s != null && s.document.length >= 6 && s.phone.length >= 7
        binding.btnSubmit.isEnabled = valid && !(s?.isLoading ?: false)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
