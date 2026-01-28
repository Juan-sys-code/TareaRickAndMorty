package com.example.rickmortyapp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.rickmortyapp.ui.theme.EpisodesFragment
import com.example.rickmortyapp.ui.theme.LoginFragment
import com.example.rickmortyapp.ui.theme.StatsFragment
import com.example.rickmortyapp.ui.theme.SettingsFragment
import com.example.rickmortyapp.ui.theme.AboutFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        // Configuración del Toggle para el menú lateral
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // --- LÓGICA DE LA FLECHA DE RETROCESO ---
        // Escucha cambios en la pila de fragmentos para alternar entre el menú y la flecha
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                // Si hay algo en el BackStack (como el detalle), mostramos flecha
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                toolbar.setNavigationOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
            } else {
                // Si volvemos a la raíz, restauramos el menú hamburguesa
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                toggle.syncState()
                toolbar.setNavigationOnClickListener {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }

        // Lógica de inicio de sesión
        if (savedInstanceState == null) {
            if (auth.currentUser == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LoginFragment()).commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, EpisodesFragment()).commit()
                navView.setCheckedItem(R.id.nav_episodes)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_episodes -> {
                // Limpiamos la pila al ir a una sección principal para evitar errores de navegación
                supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, EpisodesFragment()).commit()
            }
            R.id.nav_stats -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, StatsFragment()).commit()
            }
            R.id.nav_settings -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SettingsFragment()).commit()
            }
            R.id.nav_about -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AboutFragment()).commit()
            }
            R.id.nav_logout -> {
                auth.signOut()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LoginFragment()).commit()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // Asegura que si el menú está abierto, el botón atrás lo cierre en lugar de salir de la app
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}