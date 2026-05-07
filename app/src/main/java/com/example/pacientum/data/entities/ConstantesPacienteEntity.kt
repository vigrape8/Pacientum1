package com.example.pacientum.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "constantes_paciente")
data class ConstantesPacienteEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val paciente_id:Int,
    val fechaHora: Long,
    //datos de constantes
    val temperatura: Float,
    val tensionSistolica: Int,
    val tensionDiastolica: Int,
    val frecuenciaCardiaca: Int,
    val saturacionO2: Int,
    //deposiciones
    val diuresis: Int?,
    val diuresisHecha: Boolean,
    val deposicion: Boolean,
    //curas
    val requiereCura: Boolean,
    val descripcionCura: String?
)
