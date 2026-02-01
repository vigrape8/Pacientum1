package com.example.pacientum.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pacientum.R
import com.example.pacientum.data.database.AppDatabase
import com.example.pacientum.data.repository.EnfermeraRepository
import com.example.pacientum.databinding.FragmentLoginBinding
import com.example.pacientum.ui.common.PredeterminadoViewModel
import com.example.pacientum.ui.home.HomeActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding?=null
    private val binding get()=_binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //base de datos
        val database= AppDatabase.getDatabase(requireContext())
        val repository= EnfermeraRepository(database.enfermeraDao())
        val determinado= PredeterminadoViewModel(repository)

        //iniciamos el viewmodel
        viewModel= ViewModelProvider(this,determinado).get(LoginViewModel::class.java)

        viewModel.loginResult.observe(viewLifecycleOwner){ enfermera ->
            //si concuerda los datos del login pasa al home activity
            if(enfermera!=null){
                val intent= Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }else{
                Toast.makeText(requireContext(),"Numero de enfermera o contraseña no valido", Toast.LENGTH_SHORT).show()
            }
        }
        //accion al presionar boton de conectar
        binding.btnConectar.setOnClickListener {
            val id=binding.tietnEnfermero.text.toString().toIntOrNull()
            val pass=binding.tietpassword.text.toString()
            //si los dos campos estan rellenos intenta el login
            if(id!= null&&pass.isNotEmpty()){
                viewModel.login(id,pass)
            }else{
                Toast.makeText(requireContext(),"Rellena todos los campos",Toast.LENGTH_SHORT).show()
            }
        }
        //pasar al fragment de recuperar contraseña
        binding.tvOlvidar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_olvidarFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}