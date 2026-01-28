package com.example.rickmortyapp.ui.theme

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.rickmortyapp.R
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var auth: FirebaseAuth
    private var isRegisterMode = false // Controla qué vista mostrar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        // Referencias UI
        val etName = view.findViewById<EditText>(R.id.etName)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val btnBack = view.findViewById<TextView>(R.id.btnBackToLogin)

        // Alternar a modo Registro
        btnRegister.setOnClickListener {
            if (!isRegisterMode) {
                showRegisterUI(etName, btnLogin, btnBack, btnRegister)
            } else {
                ejecutarRegistro(etEmail.text.toString(), etPassword.text.toString())
            }
        }

        // Volver a modo Login
        btnBack.setOnClickListener {
            showLoginUI(etName, btnLogin, btnBack, btnRegister)
        }

        // Iniciar sesión
        btnLogin.setOnClickListener {
            ejecutarLogin(etEmail.text.toString(), etPassword.text.toString())
        }
    }

    private fun showRegisterUI(name: View, login: View, back: View, reg: Button) {
        isRegisterMode = true
        name.visibility = View.VISIBLE
        back.visibility = View.VISIBLE
        login.visibility = View.GONE
        reg.text = "Registrarse"
    }

    private fun showLoginUI(name: View, login: View, back: View, reg: Button) {
        isRegisterMode = false
        name.visibility = View.GONE
        back.visibility = View.GONE
        login.visibility = View.VISIBLE
        reg.text = "Registrarse" // En modo login, este botón solo cambia la vista
    }

    private fun ejecutarLogin(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) return
        auth.signInWithEmailAndPassword(email.trim(), pass.trim()).addOnCompleteListener {
            if (it.isSuccessful) irAEpisodios()
            else Toast.makeText(context, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ejecutarRegistro(email: String, pass: String) {
        if (email.isBlank() || pass.length < 6) {
            Toast.makeText(context, "Email válido y 6 caracteres mín.", Toast.LENGTH_SHORT).show()
            return
        }
        auth.createUserWithEmailAndPassword(email.trim(), pass.trim()).addOnCompleteListener {
            if (it.isSuccessful) irAEpisodios()
            else Toast.makeText(context, "Fallo: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun irAEpisodios() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, EpisodesFragment())
            .commit()
    }
}