package com.example.pacientum.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pacientum.R
import com.example.pacientum.databinding.FragmentInicialBinding
import com.example.pacientum.databinding.FragmentPerfilBinding


class InicialFragment : Fragment() {
    private var _binding: FragmentInicialBinding?=null
    private val binding get()=_binding!!
    private lateinit var pacienteAdapter: PacienteAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentInicialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pacienteAdapter= PacienteAdapter()
        binding.rvPacientes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pacienteAdapter
        }
        homeViewModel= ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        //miramos quien es la enfermera logueada
        homeViewModel.enfermeraLogin.observe(viewLifecycleOwner){ enfermera->
            enfermera?.let{
                homeViewModel.pacientesPorEnfermera(it.id)
            }
        }
        //sacamos la lista de pacientes asociados a esa enfermera
        homeViewModel.listaPacientes.observe(viewLifecycleOwner){pacientes->
            pacientes?.let{
                pacienteAdapter.actualizarLista(pacientes)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}