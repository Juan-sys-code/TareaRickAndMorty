package com.example.rickmortyapp.ui.theme

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickmortyapp.R

class EpisodeDetailFragment : Fragment(R.layout.fragment_episode_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperar datos (puedes pasarlos por Bundle o un ViewModel compartid)
        val name = arguments?.getString("name") ?: "Desconocido"
        val code = arguments?.getString("code") ?: "S00E00"
        val date = arguments?.getString("date") ?: "Sin fecha"
        val characterUrls = arguments?.getStringArrayList("characters") ?: arrayListOf()

        view.findViewById<TextView>(R.id.tvDetailName).text = name
        view.findViewById<TextView>(R.id.tvDetailCode).text = code
        view.findViewById<TextView>(R.id.tvDetailDate).text = date

        // Configurar la rejilla de 3 columnas como tu boceto image_7a48d7.png
        val rv = view.findViewById<RecyclerView>(R.id.rvCharacters)
        rv.layoutManager = GridLayoutManager(requireContext(), 3)
        // rv.adapter = CharacterAdapter(characterUrls)
    }
}