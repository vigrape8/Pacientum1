package com.example.pacientum.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pacientum.data.entities.EnfermeraEntity
import com.example.pacientum.data.entities.PacienteEntity
import com.example.pacientum.data.repository.EnfermeraRepository
import com.example.pacientum.data.repository.PacienteRepository
import kotlinx.coroutines.launch

class HomeViewModel (private val repository: EnfermeraRepository,
                     private val pacienteRepository: PacienteRepository): ViewModel(){
    val enfermeraLogin= MutableLiveData<EnfermeraEntity?>()
    fun datosEnfermera(nombre:String){
        viewModelScope.launch {
            val resultado = repository.obtenerPorNombre(nombre)
            enfermeraLogin.postValue(resultado)
        }
    }

    val listaPacientes= MutableLiveData<List<PacienteEntity>>()
    fun pacientesPorEnfermera(idEnfermera:Int){
        viewModelScope.launch {
            pacienteRepository.obtenerPorEnfermero(idEnfermera).collect {  lista->
                listaPacientes.postValue(lista)
            }
        }
    }

    fun insertarPaciente(paciente: PacienteEntity){
        viewModelScope.launch {
            pacienteRepository.insertar(paciente)
        }
    }
}