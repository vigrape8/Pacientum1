package com.example.pacientum.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pacientum.R
import com.example.pacientum.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nombreLogueado = intent.getStringExtra("nombreEnfermera") ?: "Enfermero"
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //cambiar cuando tengamos la conexion a room
        binding.toolbarHome.title="Planta Hematología"

        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        appBarConfiguration= AppBarConfiguration(navController.graph,binding.drawerLayout)
        setupActionBarWithNavController(navController,appBarConfiguration)

        binding.navView.setupWithNavController(navController)

        //cuando tenga el fragment de usuario
        binding.btnPerfil.setOnClickListener {

        }
        val headerView = binding.navView.getHeaderView(0)
        val tvNombre = headerView.findViewById<TextView>(R.id.tvNombreEnfermeroMenu)
        tvNombre.text = nombreLogueado
    }
    //estrella
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