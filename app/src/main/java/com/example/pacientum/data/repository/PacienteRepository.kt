package com.example.pacientum.data.repository

import com.example.pacientum.data.dao.PacienteDao
import com.example.pacientum.data.entities.PacienteEntity
import kotlinx.coroutines.flow.Flow

class PacienteRepository(private val pacienteDao: PacienteDao) {
    //lista de todos los pacientes
    val todosLosPacientes: Flow<List<PacienteEntity>> = pacienteDao.getTodosPacientes()
    //te da los pacientes que tiene asignado el enfermero
    fun obtenerPorEnfermero(enfermeroId:Int) = pacienteDao.getPacientesPorEnfermero(enfermeroId)
    //añadir paciente
    suspend fun insertar(paciente: PacienteEntity) = pacienteDao.insertPaciente(paciente)
    //obtener paciente por su id
    suspend fun obtenerPorId(id: Int) = pacienteDao.getPacienteById(id)
    // actualizar paciente
    suspend fun actualizar(paciente: PacienteEntity) = pacienteDao.updatePaciente(paciente)
}