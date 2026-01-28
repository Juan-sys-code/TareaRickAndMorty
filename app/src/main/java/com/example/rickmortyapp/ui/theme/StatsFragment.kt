package com.example.rickmortyapp.ui.theme

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.rickmortyapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StatsFragment : Fragment(R.layout.fragment_stats) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias a la UI
        val tvInfo = view.findViewById<TextView>(R.id.tvStatsInfo)
        val tvPercent = view.findViewById<TextView>(R.id.tvStatsPercent)
        val progressBar = view.findViewById<ProgressBar>(R.id.statsProgressBar)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val totalEpisodios = 51 // Total de la API

        if (userId != null) {
            val db = FirebaseFirestore.getInstance()

            // Consultamos los episodios marcados por este usuario
            db.collection("watched_episodes")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    val vistos = documents.size()
                    val porcentaje = if (totalEpisodios > 0) (vistos * 100) / totalEpisodios else 0

                    // Actualizamos los textos y la barra
                    tvInfo.text = "Has visto $vistos de $totalEpisodios episodios"
                    tvPercent.text = "$porcentaje % completado"
                    progressBar.progress = porcentaje
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
        }
    }
}