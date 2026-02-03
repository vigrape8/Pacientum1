package com.example.pacientum.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pacientum.data.entities.EnfermeraEntity
import com.example.pacientum.data.repository.EnfermeraRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: EnfermeraRepository): ViewModel() {
    //comunicacion con el repository, encapsulamiento
    private val _loginResult= MutableLiveData<EnfermeraEntity?>()
    val loginResult: LiveData<EnfermeraEntity?> =_loginResult

    //lo mismo pero con el cambio de contraseña
    private val _actualizarPassResult= MutableLiveData<Boolean>()
    val actualizarPassResult: LiveData<Boolean> = _actualizarPassResult
    //Funcion para loguear
    fun login(id:Int,pass:String){
        viewModelScope.launch {
            val user=repository.login(id,pass)
            _loginResult.postValue(user)
        }
    }
    //Funcion para recuperar contraseña
    fun restablecerPass(email:String, nuevaPass: String){
        viewModelScope.launch(Dispatchers.IO) {
            val enfermera=repository.obtenerPorEmail(email)
            if(enfermera!=null){
                //creamos la contraseña nueva
                val enfermeraActual=enfermera.copy(password=nuevaPass)
                repository.insertar(enfermeraActual)
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
                repository.insertar(enfermero)
            }
        }
    }
}