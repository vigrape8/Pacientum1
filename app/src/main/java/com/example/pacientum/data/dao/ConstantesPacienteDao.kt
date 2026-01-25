package com.example.pacientum.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pacientum.data.entities.ConstantesPacienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConstantesPacienteDao{
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertConstantes(constantes: ConstantesPacienteEntity)
    @Update
    suspend fun updateConstantes(constantes: ConstantesPacienteEntity)
    @Delete
    suspend fun deleteConstantes(constantes: ConstantesPacienteEntity)

    @Query("SELECT * FROM constantes_paciente WHERE paciente_id = :pacienteId ORDER BY fechaHora DESC")
    fun getConstantesPorPaciente(pacienteId: Int): Flow<List<ConstantesPacienteEntity>>
    @Query("SELECT * FROM constantes_paciente WHERE id = :id")
    suspend fun getConstantesPorId(id: Int): ConstantesPacienteEntity?
}