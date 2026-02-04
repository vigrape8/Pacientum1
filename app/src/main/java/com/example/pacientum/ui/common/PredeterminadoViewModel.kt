package com.example.pacientum.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pacientum.data.repository.EnfermeraRepository
import com.example.pacientum.data.repository.PacienteRepository
import com.example.pacientum.ui.home.HomeViewModel
import com.example.pacientum.ui.login.LoginViewModel

class PredeterminadoViewModel(
    private val enfermeraRepo: EnfermeraRepository? = null,
    private val pacienteRepo: PacienteRepository? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                val eRepo = enfermeraRepo ?: throw IllegalArgumentException("LoginViewModel necesita EnfermeraRepository")
                val pRepo = pacienteRepo ?: throw IllegalArgumentException("LoginViewModel ahora necesita PacienteRepository para crear datos de prueba")

                LoginViewModel(eRepo, pRepo) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                val eRepo = enfermeraRepo ?: throw IllegalArgumentException("HomeViewModel necesita EnfermeraRepository")
                val pRepo = pacienteRepo ?: throw IllegalArgumentException("HomeViewModel necesita PacienteRepository")

                HomeViewModel(eRepo, pRepo) as T
            }
            else -> throw IllegalArgumentException("ViewModel no encontrado")
        }
    }
}