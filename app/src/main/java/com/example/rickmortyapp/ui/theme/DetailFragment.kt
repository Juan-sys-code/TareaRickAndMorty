package com.example.rickmortyapp.ui.theme

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.rickmortyapp.R

class DetailFragment : Fragment(R.layout.fragment_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias a los textos del diseño XML
        val tvName = view.findViewById<TextView>(R.id.tvDetailName)
        val tvCode = view.findViewById<TextView>(R.id.tvDetailCode)
        val tvDate = view.findViewById<TextView>(R.id.tvDetailAirDate)

        // Recuperamos los datos que enviamos desde el EpisodesFragment
        val name = arguments?.getString("name")
        val code = arguments?.getString("code")
        val date = arguments?.getString("date")

        // Asignamos los datos a la vista
        tvName.text = name ?: "Sin nombre"
        tvCode.text = code ?: "Sin código"
        tvDate.text = "Fecha de estreno: ${date ?: "Desconocida"}"
    }
}