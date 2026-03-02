package com.example.praktam_2417051045

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.praktam_2417051045.Model.DecisionSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {

    val decisionList = DecisionSource.dummyDecision

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        decisionList.forEach { decision ->

            Image(
                painter = painterResource(id = decision.imageRes),
                contentDescription = decision.title,
                modifier = Modifier.height(150.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Judul: ${decision.title}")
            Text(text = "Deskripsi: ${decision.description}")
            Text(text = "Kategori: ${decision.category}")

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "-----------------------------")
        }
    }
}