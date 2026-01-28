package com.example.rickmortyapp.ui.theme

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.rickmortyapp.R
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configuración del Modo Oscuro
        val switchDark = view.findViewById<Switch>(R.id.switchDarkMode)

        // Sincronizamos el switch con el estado real del tema actual
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        switchDark.isChecked = currentMode == AppCompatDelegate.MODE_NIGHT_YES

        switchDark.setOnCheckedChangeListener { _, isChecked ->
            val targetMode = if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

            // Usamos postDelayed para dar tiempo a la animación del switch
            // y evitar el crash durante la recreación de la actividad
            view.postDelayed({
                if (isAdded) { // Verificamos que el fragmento siga activo
                    AppCompatDelegate.setDefaultNightMode(targetMode)
                }
            }, 100)
        }

        // 2. Configuración del Botón Cerrar Sesión
        val btnLogout = view.findViewById<Button>(R.id.btnSettingsLogout)
        btnLogout.setOnClickListener {
            // Cerramos sesión en Firebase
            FirebaseAuth.getInstance().signOut()

            // Volvemos al fragmento de Login (Asegúrate de que LoginFragment esté en ui.theme)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }
    }
}