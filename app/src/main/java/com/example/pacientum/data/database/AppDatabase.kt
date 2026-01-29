package com.example.pacientum.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pacientum.data.dao.ConstantesPacienteDao
import com.example.pacientum.data.dao.EnfermeraDao
import com.example.pacientum.data.dao.MedicacionPacienteDao
import com.example.pacientum.data.dao.PacienteDao
import com.example.pacientum.data.entities.ConstantesPacienteEntity
import com.example.pacientum.data.entities.EnfermeraEntity
import com.example.pacientum.data.entities.MedicacionPacienteEntity
import com.example.pacientum.data.entities.PacienteEntity
//estructura de la base de datos
@Database(
    entities = [
        EnfermeraEntity::class,
        PacienteEntity::class,
        ConstantesPacienteEntity::class,
        MedicacionPacienteEntity::class
    ],
    version = 1
)
abstract  class AppDatabase: RoomDatabase() {
    //funciones para poder utilizar los metodos de los dao

    abstract fun enfermeraDao(): EnfermeraDao
    abstract fun pacienteDao(): PacienteDao
    abstract fun constantesPacienteDao(): ConstantesPacienteDao
    abstract fun medicacionPacienteDao(): MedicacionPacienteDao

    //

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase?=null;
        //funcion para crear la instancia de la base de datos
        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE?: synchronized(this){
                val instance= Room.databaseBuilder<AppDatabase>(context.applicationContext, AppDatabase::class.java,"pacientum_db").build()
                INSTANCE=instance
                instance
            }
        }
    }
}