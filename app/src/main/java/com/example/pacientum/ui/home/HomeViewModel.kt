package com.example.pacientum.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pacientum.data.entities.ConstantesPacienteEntity
import com.example.pacientum.data.entities.EnfermeraEntity
import com.example.pacientum.data.entities.PacienteEntity
import com.example.pacientum.data.repository.ConstantesRepository
import com.example.pacientum.data.repository.EnfermeraRepository
import com.example.pacientum.data.repository.PacienteRepository
import kotlinx.coroutines.launch

class HomeViewModel (private val repository: EnfermeraRepository,
                     private val pacienteRepository: PacienteRepository,
                     private val constantesRepository: ConstantesRepository): ViewModel(){
    //Enfermera
    val enfermeraLogin= MutableLiveData<EnfermeraEntity?>()
    fun datosEnfermera(nombre:String){
        viewModelScope.launch {
            val resultado = repository.obtenerPorNombre(nombre)
            enfermeraLogin.postValue(resultado)
        }
    }
    //Pacientes
    //lista de pacientes
    val listaPacientes= MutableLiveData<List<PacienteEntity>>()
    fun pacientesPorEnfermera(idEnfermera:Int){
        viewModelScope.launch {
            pacienteRepository.obtenerPorEnfermero(idEnfermera).collect {  lista->
                listaPacientes.postValue(lista)
            }
        }
    }
    //Insertar paciente
    fun insertarPaciente(paciente: PacienteEntity){
        viewModelScope.launch {
            pacienteRepository.insertar(paciente)
        }
    }

    //gestion del paciente
    private val _pacienteSeleccionado = MutableLiveData<PacienteEntity?>()
    val pacienteSeleccionado: LiveData<PacienteEntity?> = this._pacienteSeleccionado
    //seleccionar paciente al clickar en la lista
    fun seleccionarPaciente(paciente: PacienteEntity) {
        _pacienteSeleccionado.value = paciente
        _ultimaConstante.value = null
    }
    //guardar actualacion de constantes y demas
    fun actualizaryGuardar(paciente: PacienteEntity,constantes: ConstantesPacienteEntity){
        viewModelScope.launch {
            //actualizamos los datos del paciente
            pacienteRepository.actualizar(paciente)
            val hayConstantes=constantes.temperatura>0||constantes.frecuenciaCardiaca>0||constantes.diuresisHecha||constantes.deposicion||constantes.requiereCura
            //si hay constantes lo guardamos
            if(hayConstantes){
                constantesRepository.insertar(constantes)
            }
            //actualizamos el paciente seleccionado
            _pacienteSeleccionado.postValue(paciente)
        }

        }
    private val _ultimaConstante = MutableLiveData<ConstantesPacienteEntity?>()
    val ultimaConstante: LiveData<ConstantesPacienteEntity?> get() = _ultimaConstante

    fun cargarUltimasConstantes(pacienteId: Int) {
        viewModelScope.launch {
            // El DAO ya nos devuelve la lista ordenada de más reciente a más antigua
            constantesRepository.obtenerPorPaciente(pacienteId).collect { lista ->
                if (lista.isNotEmpty()) {
                    _ultimaConstante.postValue(lista.first()) // Cogemos la toma más reciente
                } else {
                    _ultimaConstante.postValue(null)
                }
            }
        }
    }

    }




