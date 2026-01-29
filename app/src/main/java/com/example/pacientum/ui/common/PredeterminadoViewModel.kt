package com.example.pacientum.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pacientum.data.repository.EnfermeraRepository
import com.example.pacientum.ui.login.LoginViewModel

class PredeterminadoViewModel(private val repository: EnfermeraRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
            }
        throw IllegalArgumentException("ViewModel no encontrado")
    }
}