package com.example.pacientum.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pacientum.data.entities.PacienteEntity
import com.example.pacientum.databinding.ViewholderPacienteBinding

class PacienteAdapter(
    private var listaPacientes: List<PacienteEntity> = emptyList()
) : RecyclerView.Adapter<PacienteViewHolder>() {
    //gestionar el click desde el fragment
    var onItemClick: ((PacienteEntity) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        val binding = ViewholderPacienteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PacienteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        holder.bind(listaPacientes[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(listaPacientes[position])
        }
    }

    override fun getItemCount(): Int = listaPacientes.size

    fun actualizarLista(nuevaLista: List<PacienteEntity>) {
        this.listaPacientes = nuevaLista
        notifyDataSetChanged()
    }
}

class PacienteViewHolder(private val binding: ViewholderPacienteBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(paciente: PacienteEntity) {
        //cambiamos los datos para que aparezca los datos de cada paciente
        binding.tvNombrePaciente.text = "${paciente.nombre} ${paciente.apellidos}"
        binding.tvCamaPaciente.text = "Habitación: ${paciente.id}"
        //para que aparezca la letra del paciente dentro del circulo
        binding.tvInicialPaciente.text = paciente.nombre.take(1).uppercase()
        //cambio de color del circulo del viewholder
        val context = binding.root.context
        val sharedPref = context.getSharedPreferences("BloqueosPaciente", android.content.Context.MODE_PRIVATE)
        val tiempoGuardado = sharedPref.getLong("bloqueo_${paciente.id}", 0L)
        val tiempoActual = System.currentTimeMillis()
        val ochoHorasInMillis = 8 * 60 * 60 * 1000L

        if (tiempoGuardado > 0 && (tiempoActual - tiempoGuardado) < ochoHorasInMillis) {
            // ESTÁ AL DÍA (Verde): Han pasado menos de 8 horas
            binding.ivEstadoConstantes.setColorFilter(android.graphics.Color.parseColor("#4CAF50"))
        } else {
            // PENDIENTE (Rojo): Nunca se ha guardado o ya pasaron 8 horas
            binding.ivEstadoConstantes.setColorFilter(android.graphics.Color.parseColor("#F44336"))
        }
    }
}