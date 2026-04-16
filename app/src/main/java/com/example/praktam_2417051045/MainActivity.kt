package com.example.praktam_2417051045

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.praktam_2417051045.Model.*
import com.example.praktam_2417051045.ui.theme.PraktiktamTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PraktiktamTheme {
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
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        item {
            Text(
                text = "Decision Helper",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        itemsIndexed(decisionList) { index, decision ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
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
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = decision.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )

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

    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = decision.description)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kategori: ${decision.category}",
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        delay(2000)

                        val result = listOf("Lanjutkan", "Tunda Dulu").random()

                        snackbarHostState.showSnackbar(
                            "Keputusan: $result"
                        )

                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Memproses...")
                } else {
                    Text("Ambil Keputusan")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kembali")
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}