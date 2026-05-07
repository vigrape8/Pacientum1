package com.example.pacientum.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pacientum.R
import com.example.pacientum.data.entities.PacienteEntity
import com.example.pacientum.databinding.FragmentInicialBinding
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning


class InicialFragment : Fragment() {
    private var _binding: FragmentInicialBinding? = null
    private val binding get() = _binding!!
    private lateinit var pacienteAdapter: PacienteAdapter
    private lateinit var homeViewModel: HomeViewModel

    //variable para desplegar el FAB
    private var FABAbierto = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInicialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pacienteAdapter = PacienteAdapter()
        pacienteAdapter.onItemClick={paciente->
            homeViewModel.seleccionarPaciente(paciente)
            findNavController().navigate(R.id.action_inicialFragment_to_infoPacienteFragment)
        }

        binding.rvPacientes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pacienteAdapter
        }
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        //miramos quien es la enfermera logueada
        homeViewModel.enfermeraLogin.observe(viewLifecycleOwner) { enfermera ->
            enfermera?.let {
                homeViewModel.pacientesPorEnfermera(it.id)
            }
        }
        //sacamos la lista de pacientes asociados a esa enfermera
        homeViewModel.listaPacientes.observe(viewLifecycleOwner) { pacientes ->
            pacientes?.let {
                pacienteAdapter.actualizarLista(pacientes)
            }
        }
        binding.fabMain.setOnClickListener {
            menuFAB()
        }

        binding.fabCamara.setOnClickListener {
            menuFAB()
            camara()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        // Forzamos al adaptador a actualizar la lista para que
        // recalcule los colores de los círculos
        if (::pacienteAdapter.isInitialized) {
            pacienteAdapter.notifyDataSetChanged()
        }
    }
    //funcion para desplegar el FAB
    private fun menuFAB() {
        if (!FABAbierto) {
            //Mostrar las opciones
            binding.fabCamara.visibility = View.VISIBLE
            //se cambia el logo del principal
            binding.fabMain.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
            FABAbierto = true
        } else {
            binding.fabCamara.visibility = View.GONE
            //se cambia el logo del principal
            binding.fabMain.setImageResource(android.R.drawable.ic_input_add)
            FABAbierto = false
        }
    }
    //Funcion para desplegar la camara
    private fun camara() {
        val options =
            GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        val scanner = GmsBarcodeScanning.getClient(requireContext(), options)
        scanner.startScan().addOnSuccessListener { barcode ->
            val qrData: String? = barcode.rawValue
            qrData?.let { data ->
                try {
                    //se introducen todos los datos
                    val partes = data.split(",")
                    if (partes.size >= 11) {
                        val nuevoPaciente = PacienteEntity(
                            enfermeroID = partes[0].toInt(),
                            nombre = partes[1],
                            apellidos = partes[2],
                            edad = partes[3].toInt(),
                            motivoIngreso = partes[4],
                            antecedentes = partes[5],
                            alergias = partes[6],
                            medicacionCasa = partes[7],
                            pruebasPendientes = partes[8],
                            fechaPrueba = partes[9],
                            evolutivo = partes[10]
                        )
                        // Inserción directa en Room mediante el ViewModel
                        homeViewModel.insertarPaciente(nuevoPaciente)
                        Toast.makeText(
                            requireContext(),
                            "Paciente ${nuevoPaciente.nombre} registrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "QR incompleto o formato erróneo",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Error en el formato del QR",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}