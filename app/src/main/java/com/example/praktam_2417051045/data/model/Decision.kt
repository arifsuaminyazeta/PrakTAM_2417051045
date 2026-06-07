package com.example.praktam_2417051045.data.model

import com.google.gson.annotations.SerializedName

data class Decision(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("image_url") val imageUrl: String
)