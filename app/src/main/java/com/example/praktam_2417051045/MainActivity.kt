package com.example.praktam_2417051045

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.praktam_2417051045.Model.Decision
import com.example.praktam_2417051045.Model.DecisionSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {

        composable("list") {
            DecisionListScreen(navController)
        }

        composable("detail/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            val decision = DecisionSource.dummyDecision[index]

            DecisionDetailScreen(decision, navController)
        }
    }
}

@Composable
fun DecisionListScreen(navController: NavController) {

    val decisionList = DecisionSource.dummyDecision

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text(
                text = "Decision Helper",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        itemsIndexed(decisionList) { index, decision ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {

                    Image(
                        painter = painterResource(id = decision.imageRes),
                        contentDescription = decision.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = decision.title,
                        fontWeight = FontWeight.Bold
                    )

                    Text(text = decision.category)

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow {
                        item {
                            Button(
                                onClick = {
                                    navController.navigate("detail/$index")
                                },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Detail")
                            }
                        }

                        item {
                            Button(onClick = { }) {
                                Text("Pilih")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DecisionDetailScreen(decision: Decision, navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Image(
            painter = painterResource(id = decision.imageRes),
            contentDescription = decision.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = decision.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = decision.description)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Kategori: ${decision.category}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kembali")
        }
    }
}