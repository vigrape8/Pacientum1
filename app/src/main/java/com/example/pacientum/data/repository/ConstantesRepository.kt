package com.example.pacientum.data.repository

import com.example.pacientum.data.dao.ConstantesPacienteDao
import com.example.pacientum.data.entities.ConstantesPacienteEntity
import kotlinx.coroutines.flow.Flow

class ConstantesRepository(private val constantesDao: ConstantesPacienteDao) {
    //muestra las constantes del paciente
    fun obtenerPorPaciente(pacienteId: Int): Flow<List<ConstantesPacienteEntity>> = constantesDao.getConstantesPorPaciente(pacienteId)
    //insertar nuevas constantes
    suspend fun insertar(constantes: ConstantesPacienteEntity) = constantesDao.insertConstantes(constantes)
}