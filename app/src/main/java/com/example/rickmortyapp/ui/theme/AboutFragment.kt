package com.example.rickmortyapp.ui.theme

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.rickmortyapp.R

class AboutFragment : Fragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón cerrar: nos devuelve a la lista de episodios
        view.findViewById<Button>(R.id.btnAboutClose).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EpisodesFragment())
                .commit()
        }
    }
}