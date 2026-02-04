package com.example.pacientum.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pacientum.data.entities.EnfermeraEntity
import com.example.pacientum.data.entities.PacienteEntity
import com.example.pacientum.data.repository.EnfermeraRepository
import com.example.pacientum.data.repository.PacienteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val enfermeraRepository: EnfermeraRepository,
                     private val pacienteRepo: PacienteRepository
): ViewModel() {
    //comunicacion con el repository, encapsulamiento
    private val _loginResult= MutableLiveData<EnfermeraEntity?>()
    val loginResult: LiveData<EnfermeraEntity?> =_loginResult

    //lo mismo pero con el cambio de contraseña
    private val _actualizarPassResult= MutableLiveData<Boolean>()
    val actualizarPassResult: LiveData<Boolean> = _actualizarPassResult
    //Funcion para loguear
    fun login(id:Int,pass:String){
        viewModelScope.launch {
            val user=enfermeraRepository.login(id,pass)
            _loginResult.postValue(user)
        }
    }
    //Funcion para recuperar contraseña
    fun restablecerPass(email:String, nuevaPass: String){
        viewModelScope.launch(Dispatchers.IO) {
            val enfermera=enfermeraRepository.obtenerPorEmail(email)
            if(enfermera!=null){
                //creamos la contraseña nueva
                val enfermeraActual=enfermera.copy(password=nuevaPass)
                enfermeraRepository.insertar(enfermeraActual)
                _actualizarPassResult.postValue(true)
            }else{
                _actualizarPassResult.postValue(false)
            }
        }
    }
    fun crearEnfermeros(){
        viewModelScope.launch {
            // Creamos la lista basada en tu boceto
            val enfermerosIniciales = listOf(
                EnfermeraEntity(1, "Sheila", "Carbonell", "Calle Falsa,123","Hematología","1234","sheila@gmail.com"),
                EnfermeraEntity(2, "Carlos", "Cabeza", "Calle Almendros,12","Laboratorio","1234","carlos@gmail.com"),
                EnfermeraEntity(3, "Sandra", "Pesquero", "Avenida de las Americas,34","Neonatos","1234","sandra@gmail.com"),
                EnfermeraEntity(4, "Ramona", "Sánchez", "Calle Prados,1","Interna","1234","ramona@gmail.com"),
                EnfermeraEntity(5, "Victor", "Gracia", "Evergreen Terrace,742","Suprema","1234","victor@gmail.com")
            )

            enfermerosIniciales.forEach { enfermero ->
                enfermeraRepository.insertar(enfermero)
            }
        }
    }
    fun crearPacientes() {
        viewModelScope.launch {
            val pacientesIniciales = listOf(
                PacienteEntity(
                    enfermeroID = 1, // Asignado a Sheila
                    nombre = "Manuel",
                    apellidos = "Carmena",
                    edad = 72,
                    motivoIngreso = "Insuficiencia cardíaca",
                    antecedentes = "Hipertensión",
                    alergias = "Polen",
                    medicacionCasa = "Enalapril",
                    pruebasPendientes = "ECG",
                    fechaPrueba = "05/02/2026",
                    evolutivo = "Estable"
                ),
                PacienteEntity(
                    enfermeroID = 1,
                    nombre = "María",
                    apellidos = "García",
                    edad = 45,
                    motivoIngreso = "Cirugía programada",
                    antecedentes = "Ninguno",
                    alergias = "Penicilina",
                    medicacionCasa = "Ninguna",
                    pruebasPendientes = "Analítica",
                    fechaPrueba = "06/02/2026",
                    evolutivo = "Buen estado"
                ),
                PacienteEntity(
                    enfermeroID = 2,
                    nombre = "Ricardo",
                    apellidos = "Mesa",
                    edad = 58,
                    motivoIngreso = "Observación",
                    antecedentes = "Diabetes tipo II",
                    alergias = "Ninguna",
                    medicacionCasa = "Metformina",
                    pruebasPendientes = "Radiografía",
                    fechaPrueba = "04/02/2026",
                    evolutivo = "En espera"
                )
            )

            pacientesIniciales.forEach { paciente ->
                pacienteRepo.insertar(paciente) // Insertamos en la tabla pacientes
            }
        }
    }
}