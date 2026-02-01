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
}