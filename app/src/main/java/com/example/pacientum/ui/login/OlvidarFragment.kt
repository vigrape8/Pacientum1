package com.example.pacientum.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pacientum.data.database.AppDatabase
import com.example.pacientum.data.repository.EnfermeraRepository
import com.example.pacientum.databinding.FragmentOlvidarBinding
import com.example.pacientum.ui.common.PredeterminadoViewModel

class OlvidarFragment : Fragment() {
    private var _binding: FragmentOlvidarBinding?=null
    private val binding get()=_binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentOlvidarBinding.inflate(inflater,container,false)
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
        viewModel.actualizarPassResult.observe(viewLifecycleOwner){ correcto->
            if(correcto){
                Toast.makeText(requireContext(),"Contraseña actualizada", Toast.LENGTH_SHORT).show()
                //vuelve a la pantalla anterior si es correcto
                findNavController().popBackStack()
            }else{
                Toast.makeText(requireContext(),"Email introducido no es correcto", Toast.LENGTH_SHORT).show()
            }
        }
        //cambiar contraseña
        binding.btnConectar.setOnClickListener {
            val email=binding.tietnEnfermeroOlvidar.text.toString()
            val pass1=binding.tietpasswordOlvidar.text.toString()
            val pass2=binding.tietpasswordrepeat.text.toString()
            if(email.isNotEmpty()&&pass1.isNotEmpty() && pass2.isNotEmpty()){
                //si coincide las dos contraseñas
                if(pass1==pass2){
                    viewModel.restablecerPass(email,pass1)
                }else{
                    Toast.makeText(requireContext(),"Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(),"Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}