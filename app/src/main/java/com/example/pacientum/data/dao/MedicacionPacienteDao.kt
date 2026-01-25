package com.example.pacientum.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pacientum.data.entities.MedicacionPacienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicacionPacienteDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicacion(medicacion: MedicacionPacienteEntity)
    @Update
    suspend fun updateMedicacion(medicacion: MedicacionPacienteEntity)
    @Delete
    suspend fun deleteMedicacion(medicacion: MedicacionPacienteEntity)

    @Query("SELECT * FROM medicacion_paciente WHERE pacienteID = :pacienteId")
    fun getMedicacionPorPaciente(pacienteId:Int): Flow<List<MedicacionPacienteEntity>>
    @Query("SELECT * FROM medicacion_paciente WHERE id = :id")
    suspend fun getMedicacionPorId(id:Int): MedicacionPacienteEntity?

}