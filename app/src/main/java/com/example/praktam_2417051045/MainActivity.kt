package com.example.praktam_2417051045

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.praktam_2417051045.data.model.Decision
import com.example.praktam_2417051045.data.repository.DecisionRepository
import com.example.praktam_2417051045.ui.theme.PraktiktamTheme
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
    val repository = remember { DecisionRepository() }
    var decisions by remember { mutableStateOf<List<Decision>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        decisions = repository.getDecisions()
        isLoading = false
        isError = decisions.isEmpty()
    }

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            DecisionListScreen(navController, decisions, isLoading, isError)
        }

        composable("detail/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            if (decisions.isNotEmpty() && index < decisions.size) {
                DecisionDetailScreen(decisions[index], navController)
            } else if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Data tidak ditemukan")
                }
            }
        }
    }
}

@Composable
fun DecisionListScreen(
    navController: NavController,
    decisions: List<Decision>,
    isLoading: Boolean,
    isError: Boolean
) {
    var favoriteIndex by remember { mutableStateOf(-1) }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (isError) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Gagal memuat data", style = MaterialTheme.typography.titleMedium)
                Text("Periksa koneksi internet Anda", style = MaterialTheme.typography.bodySmall)
            }
        }
    } else {
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

            if (decisions.isNotEmpty()) {
                item {
                    val decision = decisions[0]
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
                                    .matchParentSize()
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
                                Text(decision.title, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(decision.description, color = Color.White, maxLines = 1)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            item {
                Text("Pilihan terbaik untukmu", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                LazyRow {
                    itemsIndexed(decisions) { index, decision ->
                        Card(
                            modifier = Modifier
                                .width(180.dp)
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
                                        .height(120.dp)
                                )

                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(decision.title, maxLines = 1, fontWeight = FontWeight.Bold)
                                    Text(decision.category, style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            navController.navigate("detail/$index")
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text("Detail", style = MaterialTheme.typography.labelLarge)
                                    }

                                    OutlinedButton(
                                        onClick = { favoriteIndex = index },
                                        modifier = Modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(if (favoriteIndex == index) "✓ Favorit" else "Favorit", style = MaterialTheme.typography.labelLarge)
                                    }
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
    var isThinking by remember { mutableStateOf(false) }
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
                    .height(250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(decision.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
            Text(decision.category, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(12.dp))
            Text(decision.description)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    scope.launch {
                        isThinking = true
                        delay(2000)
                        val result = listOf("Lanjutkan", "Tunda Dulu", "Coba Lagi Nanti").random()
                        snackbarHostState.showSnackbar("Keputusan: $result")
                        isThinking = false
                    }
                },
                enabled = !isThinking,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isThinking) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Ambil Keputusan")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
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
