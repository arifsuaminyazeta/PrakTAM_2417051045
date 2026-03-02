package com.example.praktam_2417051045.Model

import androidx.annotation.DrawableRes

data class Decision(
    val title: String,
    val description: String,
    val category: String,
    @DrawableRes val imageRes: Int
)