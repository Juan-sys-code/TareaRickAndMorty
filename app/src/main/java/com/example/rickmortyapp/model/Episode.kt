package com.example.rickmortyapp.model

data class Episode(
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String, // Este es el código (ej: S01E01)
    val characters: List<String>, // Lista de URLs de los personajes
    val url: String,
    val created: String,
    // Este campo no viene en la API, lo usaremos para Firestore más tarde
    var isWatched: Boolean = false
)

// Esta clase sirve para recibir la lista completa de la API
data class EpisodeResponse(
    val results: List<Episode>
)