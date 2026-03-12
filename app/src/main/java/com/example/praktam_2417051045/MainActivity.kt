package com.example.praktam_2417051045

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.praktam_2417051045.Model.Decision
import com.example.praktam_2417051045.Model.DecisionSource

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                DecisionListScreen()
            }
        }
    }
}

@Composable
fun DecisionListScreen() {

    val decisionList = DecisionSource.dummyDecision

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        decisionList.forEach { decision ->

            DecisionDetailScreen(decision)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DecisionDetailScreen(decision: Decision) {

    Column {

        Image(
            painter = painterResource(id = decision.imageRes),
            contentDescription = decision.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = decision.title,
            fontWeight = FontWeight.Bold
        )

        Text(text = decision.description)

        Text(text = "Kategori: ${decision.category}")

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { }) {
            Text("Bantu Putuskan")
        }
    }
}