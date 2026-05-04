package com.example.praktam_2417051045

import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.praktam_2417051045.Model.*
import com.example.praktam_2417051045.ui.theme.PraktiktamTheme
import coil.compose.AsyncImage
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    var favoriteIndex by remember { mutableStateOf(-1) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text("DecisionApp", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Bantu kamu menentukan pilihan hidup")
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            val decision = decisionList[0]

            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box {

                    val context = LocalContext.current

                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(decision.imageUrl)
                            .placeholder(R.drawable.career)
                            .error(R.drawable.business)
                            .crossfade(true)
                            .build(),
                        contentDescription = decision.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color.Black.copy(0.7f))
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(decision.title, color = Color.White)
                        Text(decision.description, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Text("Pilihan terbaik untukmu")
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            LazyRow {
                itemsIndexed(decisionList) { index, decision ->

                    Card(
                        modifier = Modifier
                            .width(160.dp)
                            .padding(end = 12.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column {

                            val context = LocalContext.current

                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(decision.imageUrl)
                                    .placeholder(R.drawable.career)
                                    .error(R.drawable.business)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = decision.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                            )

                            Column(modifier = Modifier.padding(8.dp)) {

                                Text(decision.title, maxLines = 1)

                                Button(
                                    onClick = {
                                        navController.navigate("detail/$index")
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Detail")
                                }

                                OutlinedButton(
                                    onClick = { favoriteIndex = index },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(if (favoriteIndex == index) "✓ Favorit" else "Favorit")
                                }
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
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(Modifier.fillMaxSize()) {

        Column(Modifier.padding(16.dp)) {

            val context = LocalContext.current

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(decision.imageUrl)
                    .placeholder(R.drawable.career)
                    .error(R.drawable.business)
                    .crossfade(true)
                    .build(),
                contentDescription = decision.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(decision.title, fontWeight = FontWeight.Bold)
            Text(decision.description)

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        delay(2000)
                        val result = listOf("Lanjutkan", "Tunda Dulu").random()
                        snackbarHostState.showSnackbar("Keputusan: $result")
                        isLoading = false
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Ambil Keputusan")
                }
            }

            Button(
                onClick = { navController.popBackStack() },
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