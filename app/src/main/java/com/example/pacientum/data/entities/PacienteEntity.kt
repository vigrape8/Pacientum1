package com.example.pacientum.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pacientes")
data class PacienteEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    //id del enfermero que tiene
    val enfermeroID:Int,

    //datos personales
    val nombre: String,
    val apellidos: String,
    val edad: Int,

    //datos de ingreso
    val motivoIngreso: String?,
    val antecedentes: String?,
    val alergias: String?,
    val medicacionCasa: String?,

    //pruebas pendientes
    val pruebasPendientes:String?,
    val fechaPrueba:String,

    //evolutivo
    val evolutivo:String?
)
