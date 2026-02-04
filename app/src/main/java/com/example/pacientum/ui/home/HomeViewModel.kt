package com.example.pacientum.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pacientum.data.entities.EnfermeraEntity
import com.example.pacientum.data.repository.EnfermeraRepository
import kotlinx.coroutines.launch

class HomeViewModel (private val repository: EnfermeraRepository): ViewModel(){
    val enfermeraLogin= MutableLiveData<EnfermeraEntity?>()
    fun datosEnfermera(nombre:String){
        viewModelScope.launch {
            val resultado = repository.obtenerPorNombre(nombre)
            enfermeraLogin.postValue(resultado)
        }
    }
}