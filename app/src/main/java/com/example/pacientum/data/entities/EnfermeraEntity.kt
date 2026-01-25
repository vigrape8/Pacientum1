package com.example.pacientum.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "enfermera")
class EnfermeraEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,

    val nombre: String,
    val apellido: String,
    val direccion: String,
    val planta: String,
    val password: String,
    val email: String
)