package com.example.pacientum.data.repository

import com.example.pacientum.data.dao.EnfermeraDao
import com.example.pacientum.data.entities.EnfermeraEntity

class EnfermeraRepository(private val enfermeraDao: EnfermeraDao) {
    //para el login
    suspend fun login(id:Int,pass:String)=enfermeraDao.login(id,pass)
    //para añadir nuevas enfermeras
    suspend fun insertar(enfermera: EnfermeraEntity)=enfermeraDao.insertEnfermera(enfermera)
    //para recuperar contraseña
    suspend fun obtenerPorEmail(email:String)=enfermeraDao.getEnfermeraPorEmail(email)
    //para los datos de la enfermera
    suspend fun obtenerPorNombre(nombre: String) = enfermeraDao.getEnfermeraPorNombre(nombre)
}