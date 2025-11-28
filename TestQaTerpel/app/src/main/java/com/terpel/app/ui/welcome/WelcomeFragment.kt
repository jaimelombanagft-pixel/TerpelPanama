package com.terpel.app.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialSharedAxis
import com.terpel.app.R
import com.terpel.app.databinding.FragmentWelcomeBinding
import coil.load

class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerImage.load("https://www.terpelpanama.com/images/slider-vayven-h-0.jpg") {
            crossfade(true)
            placeholder(R.drawable.ic_terpel_placeholder)
        }
        viewModel.pages.observe(viewLifecycleOwner, Observer { pages ->
            val adapter = WelcomePagerAdapter(pages)
            binding.welcomePager.adapter = adapter
            TabLayoutMediator(binding.pagerIndicator, binding.welcomePager) { _, _ -> }.attach()
        })

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_login)
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_register)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
