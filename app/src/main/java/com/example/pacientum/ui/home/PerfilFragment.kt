package com.example.pacientum.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.pacientum.R
import com.example.pacientum.databinding.FragmentPerfilBinding


class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding?=null
    private val binding get()=_binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        homeViewModel.enfermeraLogin.observe(viewLifecycleOwner){enfermera->
            enfermera?.let{
                binding.tvNombrePerfil.text=it.nombre
                binding.tvApellidoPerfil.text=it.apellido
                binding.tvPlantaPerfil.text=it.planta
                binding.tvIdPerfil.text=it.id.toString()
                binding.tvEmailPerfil.text=it.email
            }

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}