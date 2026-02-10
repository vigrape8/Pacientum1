package com.example.pacientum.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pacientum.R
import com.example.pacientum.data.database.AppDatabase
import com.example.pacientum.data.repository.EnfermeraRepository
import com.example.pacientum.data.repository.PacienteRepository
import com.example.pacientum.databinding.ActivityHomeBinding
import com.example.pacientum.ui.common.PredeterminadoViewModel
import com.example.pacientum.ui.login.LoginActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //cogemos el nombre del login
        val nombreLogueado = intent.getStringExtra("nombreEnfermera") ?: "Enfermero"
        //inflamos la vista
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //base de datos
        val database= AppDatabase.getDatabase(this)
        val enfermeraRepo = EnfermeraRepository(database.enfermeraDao())
        val pacienteRepo = PacienteRepository(database.pacienteDao())


        val predeterminado= PredeterminadoViewModel(enfermeraRepo,pacienteRepo)
        viewModel= ViewModelProvider(this,predeterminado).get(HomeViewModel::class.java)

        viewModel.datosEnfermera(nombreLogueado)
        //para que aparezca el nombre y la planta de la enfermera
        viewModel.enfermeraLogin.observe(this){enfermera->
            enfermera?.let{
                val headerView = binding.navView.getHeaderView(0)
                val tvNombre = headerView.findViewById<TextView>(R.id.tvNombreEnfermeroMenu)
                val ivPerfil=headerView.findViewById<ImageView>(R.id.ivPerfilMenu)
                tvNombre.text = it.nombre
                binding.toolbarHome.title="Planta ${it.planta}"
                ivPerfil.setOnClickListener {
                    navController.navigate(R.id.perfilFragment)
                    binding.drawerLayout.closeDrawers()
                }
            }
        }
        //toolbar
        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)
        //menu lateral

        binding.navView.setNavigationItemSelectedListener { item->
            when(item.itemId){
                //opcion Inicio
                R.id.nav_inicio->{
                    navController.popBackStack(R.id.inicialFragment,false)
                    binding.drawerLayout.closeDrawers()
                }
                R.id.nav_cerrar_sesion->{
                    cerrarSesion()
                }
                else -> {

                    NavigationUI.onNavDestinationSelected(item, navController)
                    binding.drawerLayout.closeDrawers()
                }
            }
            true
        }
        //vamos al fragment de perfil
        binding.btnPerfil.setOnClickListener {
            navController.navigate(R.id.perfilFragment)
        }

    }
    private fun cerrarSesion() {
        // 1. Preparamos el salto al Login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_enfermero, menu)
        return true
    }

    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item)
    }

    //boton de devolver
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}