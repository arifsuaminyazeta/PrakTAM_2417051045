package com.example.praktam_2417051045

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.praktam_2417051045.Model.Decision
import com.example.praktam_2417051045.Model.DecisionSource
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape

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

    var isFavorite by remember { mutableStateOf(false) }

    Column {

        Box {

            Image(
                painter = painterResource(id = decision.imageRes),
                contentDescription = decision.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite)
                            Icons.Filled.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }
        }

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