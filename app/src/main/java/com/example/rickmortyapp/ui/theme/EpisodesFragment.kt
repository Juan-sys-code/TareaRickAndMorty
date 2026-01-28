package com.example.rickmortyapp.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickmortyapp.R
import com.example.rickmortyapp.adapter.EpisodeAdapter
import com.example.rickmortyapp.api.RetrofitClient
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EpisodesFragment : Fragment() {

    private lateinit var adapter: EpisodeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el layout del fragmento de episodios
        return inflater.inflate(R.layout.fragment_episodes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvEpisodes)

        // CONFIGURACIÓN DEL CLIC (Guardar en Firebase + Navegar al Detalle)
        adapter = EpisodeAdapter(emptyList()) { episode ->
            // 1. Guardamos el estado de "visto" en Firebase
            marcarComoVisto(episode)

            // 2. Preparamos el Bundle con los datos requeridos
            val bundle = Bundle().apply {
                putString("name", episode.name)
                putString("code", episode.episode)
                putString("date", episode.air_date)
                // Enviamos la lista de URLs de personajes para la cuadrícula del detalle
                putStringArrayList("characters", ArrayList(episode.characters))
            }

            // 3. Navegamos al fragmento de detalle configurado anteriormente
            val detailFrag = EpisodeDetailFragment()
            detailFrag.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFrag)
                .addToBackStack(null) // Esto permite que la flecha atrás de la MainActivity funcione
                .commit()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        fetchEpisodes()
    }

    private fun fetchEpisodes() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getAllEpisodes()
                if (response.isSuccessful && response.body() != null) {
                    val listadoApi = response.body()!!.results

                    // Sincronización con Firebase para marcar los vistos
                    val db = FirebaseFirestore.getInstance()
                    db.collection("watched_episodes")
                        .whereEqualTo("userId", userId)
                        .get()
                        .addOnSuccessListener { documents ->
                            val idsVistos = documents.map { it.getLong("episodeId")?.toInt() }

                            listadoApi.forEach { episodio ->
                                if (idsVistos.contains(episodio.id)) {
                                    episodio.isWatched = true
                                }
                            }
                            adapter.updateEpisodes(listadoApi)
                        }
                        .addOnFailureListener {
                            adapter.updateEpisodes(listadoApi)
                        }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun marcarComoVisto(episode: com.example.rickmortyapp.model.Episode) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val watchedData = hashMapOf(
                "userId" to userId,
                "episodeId" to episode.id,
                "watched" to true
            )

            // Guardado persistente en la colección de Firestore
            db.collection("watched_episodes")
                .document("${userId}_${episode.id}")
                .set(watchedData)
                .addOnSuccessListener {
                    episode.isWatched = true
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error Firebase: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}