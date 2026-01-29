package com.example.pacientum.data.repository

import com.example.pacientum.data.dao.MedicacionPacienteDao
import com.example.pacientum.data.entities.MedicacionPacienteEntity
import kotlinx.coroutines.flow.Flow

class MedicacionRepository(private val medicacionDao: MedicacionPacienteDao) {

    //obtener medicacion por paciente
    fun obtenerPorPaciente(pacienteId :Int): Flow<List<MedicacionPacienteEntity>> = medicacionDao.getMedicacionPorPaciente(pacienteId)
    //añadir nueva medicacion
    suspend fun insertar(medicacion: MedicacionPacienteEntity) = medicacionDao.insertMedicacion(medicacion)
    //eliminar medicacion
    suspend fun eliminar(medicacion: MedicacionPacienteEntity)=medicacionDao.deleteMedicacion(medicacion)
}