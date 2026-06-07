package com.example.praktam_2417051045.data.repository

import com.example.praktam_2417051045.data.api.RetrofitClient
import com.example.praktam_2417051045.data.model.Decision
import com.example.praktam_2417051045.data.model.DecisionSource
import android.util.Log

class DecisionRepository {
    suspend fun getDecisions(): List<Decision> {
        return try {
            val response = RetrofitClient.instance.getDecisions()
            if (response.isEmpty()) {
                Log.d("DecisionRepository", "API response empty, using dummy data")
                DecisionSource.dummyDecision
            } else {
                response
            }
        } catch (e: Exception) {
            Log.e("DecisionRepository", "Error fetching data: ${e.message}")
            // Jika internet mati atau URL salah, kembalikan data dummy
            DecisionSource.dummyDecision
        }
    }
}
