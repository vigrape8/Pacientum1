package com.example.pacientum.ui.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pacientum.R
import com.example.pacientum.data.entities.ConstantesPacienteEntity
import com.example.pacientum.data.entities.PacienteEntity
import com.example.pacientum.databinding.FragmentInfoPacienteBinding
import java.util.Calendar

class InfoPacienteFragment : Fragment() {

    private var _binding: FragmentInfoPacienteBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    // Variables para guardar las fechas
    private var fechaPruebaSeleccionada: String = ""
    private var fechaConstantesLong: Long = 0L // Para ConstantesPacienteEntity (pide Long)

    // Paciente actual cargado
    private var pacienteActual: PacienteEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoPacienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        // Configurar listeners de los RadioGroups (Ocultar/Mostrar campos)
        configurarRadioGroups()

        // Configurar selectores de Fecha y Hora
        binding.tvFechaPrueba.setOnClickListener { abrirSelectorFechaHora { resultadoStr, _ ->
            fechaPruebaSeleccionada = resultadoStr
            binding.tvFechaPrueba.text = resultadoStr
        } }

        binding.tvFechaConstantes.setOnClickListener { abrirSelectorFechaHora { resultadoStr, resultadoLong ->
            fechaConstantesLong = resultadoLong
            binding.tvFechaConstantes.text = resultadoStr
        } }

        // Cargar datos del paciente
        homeViewModel.pacienteSeleccionado.observe(viewLifecycleOwner) { paciente ->
            paciente?.let {
                pacienteActual = it
                cargarDatosPaciente(it)
                homeViewModel.cargarUltimasConstantes(it.id)
            }
        }
        homeViewModel.ultimaConstante.observe(viewLifecycleOwner) { constantes ->
            constantes?.let {
                // Rellenamos los campos de texto solo si tienen datos mayores a 0
                if (it.temperatura > 0f) binding.etTemperatura.setText(it.temperatura.toString())
                if (it.frecuenciaCardiaca > 0) binding.etFrecCardiaca.setText(it.frecuenciaCardiaca.toString())
                if (it.tensionSistolica > 0) binding.etTenSistolica.setText(it.tensionSistolica.toString())
                if (it.tensionDiastolica > 0) binding.etTenDiastolica.setText(it.tensionDiastolica.toString())
                if (it.saturacionO2 > 0) binding.etSaturacion.setText(it.saturacionO2.toString())

                // Rellenamos los RadioButtons de Diuresis
                if (it.diuresisHecha) {
                    binding.rbDiuresisSi.isChecked = true
                    binding.etDiuresisMl.setText(it.diuresis?.toString() ?: "")
                } else {
                    binding.rbDiuresisNo.isChecked = true
                }

                // Rellenamos los RadioButtons de Deposiciones
                if (it.deposicion) binding.rbDeposicionesSi.isChecked = true
                else binding.rbDeposicionesNo.isChecked = true

                // Rellenamos los RadioButtons de Curas
                if (it.requiereCura) {
                    binding.rbCuraSi.isChecked = true
                    binding.etDescripcionCura.setText(it.descripcionCura ?: "")
                } else {
                    binding.rbCuraNo.isChecked = true
                }

                // Pintamos la fecha de la toma de constantes
                val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                binding.tvFechaConstantes.text = sdf.format(java.util.Date(it.fechaHora))
            }
        }
        // Boton de guardar
        binding.btnGuardarCambios.setOnClickListener {
            guardarDatos()
        }
    }

    private fun cargarDatosPaciente(paciente: PacienteEntity) {
        // Datos no modificables
        binding.tvNombreEdad.text = "${paciente.apellidos}, ${paciente.nombre} - ${paciente.edad} años"
        binding.tvMotivoIngreso.text = paciente.motivoIngreso ?: "No especificado"
        binding.tvAlergias.text = paciente.alergias ?: "Sin alergias"
        binding.tvMedicacionCasa.text = paciente.medicacionCasa ?: "Ninguna"

        // Datos modificables
        binding.etPruebasPendientes.setText(paciente.pruebasPendientes)
        binding.etEvolutivo.setText(paciente.evolutivo)

        if (paciente.fechaPrueba.isNotEmpty()) {
            fechaPruebaSeleccionada = paciente.fechaPrueba
            binding.tvFechaPrueba.text = paciente.fechaPrueba
        }

        // Comprobamos si está en el periodo de 8 horas de bloqueo
        checkBloqueoOchoHoras(paciente.id)
    }

    private fun configurarRadioGroups() {
        // Diuresis
        binding.rgDiuresis.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbDiuresisSi) {
                binding.tilDiuresisMl.visibility = View.VISIBLE
            } else {
                binding.tilDiuresisMl.visibility = View.GONE
                binding.etDiuresisMl.text?.clear()
            }
        }

        // Curas
        binding.rgCura.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbCuraSi) {
                binding.tilDescripcionCura.visibility = View.VISIBLE
            } else {
                binding.tilDescripcionCura.visibility = View.GONE
                binding.etDescripcionCura.text?.clear()
            }
        }
    }

    private fun abrirSelectorFechaHora(onFechaSeleccionada: (String, Long) -> Unit) {
        val calendario = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendario.set(Calendar.YEAR, year)
                calendario.set(Calendar.MONTH, month)
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        calendario.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendario.set(Calendar.MINUTE, minute)

                        val fechaFormateada = String.format(
                            "%02d/%02d/%d %02d:%02d",
                            dayOfMonth,
                            month + 1,
                            year,
                            hourOfDay,
                            minute
                        )
                        val fechaLong = calendario.timeInMillis

                        onFechaSeleccionada(fechaFormateada, fechaLong)

                    },
                    calendario.get(Calendar.HOUR_OF_DAY),
                    calendario.get(Calendar.MINUTE),
                    true
                ).show()

            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun guardarDatos() {
        val paciente = pacienteActual
        if (paciente == null) {
            Toast.makeText(requireContext(), "Error: No hay paciente cargado", Toast.LENGTH_SHORT).show()
            return
        }

        // Actualizar paciente
        val pacienteActualizado = paciente.copy(
            pruebasPendientes = binding.etPruebasPendientes.text.toString(),
            fechaPrueba = fechaPruebaSeleccionada,
            evolutivo = binding.etEvolutivo.text.toString()
        )

        // Crear constantes
        val temp = binding.etTemperatura.text.toString().toFloatOrNull() ?: 0f
        val frecCardiaca = binding.etFrecCardiaca.text.toString().toIntOrNull() ?: 0
        val sistolica = binding.etTenSistolica.text.toString().toIntOrNull() ?: 0
        val diastolica = binding.etTenDiastolica.text.toString().toIntOrNull() ?: 0
        val saturacion = binding.etSaturacion.text.toString().toIntOrNull() ?: 0

        val diuresisSi = binding.rbDiuresisSi.isChecked
        val deposicionSi = binding.rbDeposicionesSi.isChecked
        val curaSi = binding.rbCuraSi.isChecked

        val diuresisMl = if (diuresisSi) binding.etDiuresisMl.text.toString().toIntOrNull() else null
        val descCura = if (curaSi) binding.etDescripcionCura.text.toString() else null

        val fechaConstantes = if (fechaConstantesLong == 0L) System.currentTimeMillis() else fechaConstantesLong

        val nuevasConstantes = ConstantesPacienteEntity(
            paciente_id = paciente.id,
            fechaHora = fechaConstantes,
            temperatura = temp,
            tensionSistolica = sistolica,
            tensionDiastolica = diastolica,
            frecuenciaCardiaca = frecCardiaca,
            saturacionO2 = saturacion,
            diuresis = diuresisMl,
            diuresisHecha = diuresisSi,
            deposicion = deposicionSi,
            requiereCura = curaSi,
            descripcionCura = descCura
        )

        // Enviar a ViewModel
        homeViewModel.actualizaryGuardar(pacienteActualizado, nuevasConstantes)
        Toast.makeText(requireContext(), "Historia y constantes guardadas", Toast.LENGTH_SHORT).show()

        // Guardamos la hora actual en SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("BloqueosPaciente", android.content.Context.MODE_PRIVATE)
        sharedPref.edit().putLong("bloqueo_${paciente.id}", System.currentTimeMillis()).apply()

        // Volvemos a comprobar para que se bloquee la pantalla instantáneamente
        checkBloqueoOchoHoras(paciente.id)
    }

    // Comprueba si han pasado 8 horas desde la última vez
    private fun checkBloqueoOchoHoras(pacienteId: Int) {
        val sharedPref = requireActivity().getSharedPreferences("BloqueosPaciente", android.content.Context.MODE_PRIVATE)
        val tiempoGuardado = sharedPref.getLong("bloqueo_$pacienteId", 0L)
        val tiempoActual = System.currentTimeMillis()
        val ochoHorasInMillis = 8 * 60 * 60 * 1000L // 8 horas en milisegundos

        if (tiempoGuardado > 0 && (tiempoActual - tiempoGuardado) < ochoHorasInMillis) {
            // Aún no han pasado 8 horas -> BLOQUEAR
            val tiempoRestanteMilis = ochoHorasInMillis - (tiempoActual - tiempoGuardado)
            val horasRestantes = tiempoRestanteMilis / (1000 * 60 * 60)
            val minutosRestantes = (tiempoRestanteMilis % (1000 * 60 * 60)) / (1000 * 60)

            bloquearFormulario(true, "$horasRestantes h ${minutosRestantes}m")
        } else {
            // Ya pasaron 8 horas o es la primera vez -> DESBLOQUEAR
            bloquearFormulario(false)
        }
    }

    // Activa o desactiva todos los campos de la pantalla
    private fun bloquearFormulario(bloquear: Boolean, tiempoRestante: String = "") {
        val estadoEditable = !bloquear // Si bloqueamos, editable es false

        // Campos de texto y fechas
        binding.etPruebasPendientes.isEnabled = estadoEditable
        binding.tvFechaPrueba.isEnabled = estadoEditable
        binding.etEvolutivo.isEnabled = estadoEditable
        binding.tvFechaConstantes.isEnabled = estadoEditable

        binding.etTemperatura.isEnabled = estadoEditable
        binding.etFrecCardiaca.isEnabled = estadoEditable
        binding.etTenSistolica.isEnabled = estadoEditable
        binding.etTenDiastolica.isEnabled = estadoEditable
        binding.etSaturacion.isEnabled = estadoEditable
        binding.etDiuresisMl.isEnabled = estadoEditable
        binding.etDescripcionCura.isEnabled = estadoEditable

        // RadioGroups
        for (i in 0 until binding.rgDiuresis.childCount) binding.rgDiuresis.getChildAt(i).isEnabled = estadoEditable
        for (i in 0 until binding.rgDeposiciones.childCount) binding.rgDeposiciones.getChildAt(i).isEnabled = estadoEditable
        for (i in 0 until binding.rgCura.childCount) binding.rgCura.getChildAt(i).isEnabled = estadoEditable

        // Boton de guardar
        if (bloquear) {
            binding.btnGuardarCambios.isEnabled = false
            binding.btnGuardarCambios.text = "BLOQUEADO ($tiempoRestante)"
        } else {
            binding.btnGuardarCambios.isEnabled = true
            binding.btnGuardarCambios.text = "Guardar Historia y Constantes"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}