package com.example.pacientum.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicacion_paciente")
data class MedicacionPacienteEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val pacienteID: Int,
    val nombreMedicacion:String,
    val horasPautada:String
)