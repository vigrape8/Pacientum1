package com.example.pacientum.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pacientum.data.entities.PacienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PacienteDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaciente(paciente: PacienteEntity)
    @Update
    suspend fun updatePaciente(paciente: PacienteEntity)
    @Delete
    suspend fun deletePaciente(paciente: PacienteEntity)

    @Query("SELECT * FROM pacientes WHERE id= :id")
    suspend fun getPacienteById(id:Int): PacienteEntity?
    @Query("SELECT * FROM pacientes WHERE enfermeroID= :enfermeroId")
    fun getPacientesPorEnfermero(enfermeroId:Int): Flow<List<PacienteEntity>>
    @Query("SELECT * FROM pacientes")
    fun getTodosPacientes(): Flow<List<PacienteEntity>>
}