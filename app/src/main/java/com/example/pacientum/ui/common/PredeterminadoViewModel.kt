package com.example.pacientum.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pacientum.data.repository.EnfermeraRepository
import com.example.pacientum.ui.home.HomeViewModel
import com.example.pacientum.ui.login.LoginViewModel

class PredeterminadoViewModel(private val repository: EnfermeraRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // Gestión del LoginViewModel
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            // AGREGAR ESTO: Gestión del HomeViewModel
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("ViewModel no encontrado")
        }
    }
}