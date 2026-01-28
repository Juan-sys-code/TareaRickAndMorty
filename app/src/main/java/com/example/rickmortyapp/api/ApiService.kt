package com.example.rickmortyapp.api

import com.example.rickmortyapp.model.EpisodeResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("episode")
    suspend fun getAllEpisodes(): Response<EpisodeResponse>
}