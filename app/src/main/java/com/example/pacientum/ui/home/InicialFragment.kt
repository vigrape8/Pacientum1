package com.example.pacientum.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pacientum.databinding.FragmentInicialBinding
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning


class InicialFragment : Fragment() {
    private var _binding: FragmentInicialBinding?=null
    private val binding get()=_binding!!
    private lateinit var pacienteAdapter: PacienteAdapter
    private lateinit var homeViewModel: HomeViewModel
    //variable para desplegar el FAB
    private var FABAbierto=false

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
        binding.fabMain.setOnClickListener {
            menuFAB()
        }
        binding.fabNuevoPaciente.setOnClickListener {
            menuFAB()
        }
        binding.fabCamara.setOnClickListener {
            menuFAB()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //funcion para desplegar el FAB
    private fun menuFAB(){
        if(!FABAbierto){
            //Mostrar las opciones
            binding.fabNuevoPaciente.visibility= View.VISIBLE
            binding.fabCamara.visibility=View.VISIBLE
            //se cambia el logo del principal
            binding.fabMain.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
            FABAbierto=true
        }else{
            binding.fabNuevoPaciente.visibility= View.GONE
            binding.fabCamara.visibility=View.GONE
            //se cambia el logo del principal
            binding.fabMain.setImageResource(android.R.drawable.ic_input_add)
            FABAbierto=false
        }
    }
    private fun camara(){
        val options= GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        val scanner= GmsBarcodeScanning.getClient(requireContext(),options)
        scanner.startScan().addOnSuccessListener { barcode->
            val rawValue:String?=barcode.rawValue

        }
    }
}