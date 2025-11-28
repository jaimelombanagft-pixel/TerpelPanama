package com.terpel.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.terpel.app.databinding.FragmentHomeBinding
import coil.load
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeLogo.load("https://www.terpelpanama.com/images/logo-terpel.png")
        binding.homeBanner.load("https://www.terpelpanama.com/images/slider-vayven-h-0.jpg")

        binding.btnFriendCard.setOnClickListener {
            Snackbar.make(binding.root, "Tarjeta Friend", Snackbar.LENGTH_SHORT).show()
        }
        binding.btnVerTodo.setOnClickListener {
            Snackbar.make(binding.root, "Ver todo cupones", Snackbar.LENGTH_SHORT).show()
        }
        binding.actionCatalogo.setOnClickListener { Snackbar.make(binding.root, "Catálogo", Snackbar.LENGTH_SHORT).show() }
        binding.actionBeneficios.setOnClickListener { Snackbar.make(binding.root, "Beneficios", Snackbar.LENGTH_SHORT).show() }
        binding.actionMisiones.setOnClickListener { Snackbar.make(binding.root, "Misiones", Snackbar.LENGTH_SHORT).show() }
        binding.actionConsumos.setOnClickListener { Snackbar.make(binding.root, "Mis consumos", Snackbar.LENGTH_SHORT).show() }
        binding.actionAliados.setOnClickListener { Snackbar.make(binding.root, "Tus aliados", Snackbar.LENGTH_SHORT).show() }
        binding.cuponImg1.load("https://www.terpelpanama.com/images/linea-lubricantes-h.png")
        binding.greetingTitle.text = getString(com.terpel.app.R.string.hello_name, "Miguel")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
