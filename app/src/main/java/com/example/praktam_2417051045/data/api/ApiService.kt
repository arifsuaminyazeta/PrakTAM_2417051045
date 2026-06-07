package com.example.praktam_2417051045.data.api

import com.example.praktam_2417051045.data.model.Decision
import retrofit2.http.GET

interface ApiService {
    @GET("decisions.json") // Sesuaikan dengan nama file di Gist Anda
    suspend fun getDecisions(): List<Decision>
}