package com.example.pacientum.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pacientum.data.entities.EnfermeraEntity

@Dao
interface EnfermeraDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnfermera(enfermera: EnfermeraEntity)
    @Update
    suspend fun updateEnfermera(enfermera: EnfermeraEntity)
    @Delete
    suspend fun deleteEnfermera(enfermera: EnfermeraEntity)


    @Query("SELECT * FROM enfermera WHERE id = :id")
    suspend fun getEnfermeraPorId(id: Int): EnfermeraEntity?
    @Query("SELECT * FROM enfermera WHERE id = :numeroId AND password = :pass")
    suspend fun login(numeroId: Int, pass: String): EnfermeraEntity?
    @Query("SELECT * FROM enfermera WHERE email = :email LIMIT 1")
    suspend fun getEnfermeraPorEmail(email: String): EnfermeraEntity?
}