package com.example.praktam_2417051045.Model

import com.example.praktam_2417051045.R

object DecisionSource {

    val dummyDecision = listOf(

        Decision(
            title = "Pilih Karir",
            description = "Menentukan jalur karir setelah lulus",
            category = "Career",
            imageRes = R.drawable.career
        ),

        Decision(
            title = "Lanjut Studi",
            description = "Memilih S2 atau langsung bekerja",
            category = "Education",
            imageRes = R.drawable.study
        ),

        Decision(
            title = "Memulai Bisnis",
            description = "Memutuskan membuka usaha sendiri",
            category = "Business",
            imageRes = R.drawable.business
        ),

        Decision(
            title = "Liburan",
            description = "Memilih tempat untuk traveling",
            category = "Lifestyle",
            imageRes = R.drawable.travel
        )

    )
}