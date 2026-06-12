package com.example.praktam_2417051045

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
    
    var takenDecisions by rememberSaveable { mutableStateOf(setOf<Int>()) }
    var userStatus by rememberSaveable { mutableStateOf("Umum") }

    LaunchedEffect(Unit) {
        isLoading = true
        decisions = repository.getDecisions()
        isLoading = false
        isError = decisions.isEmpty()
    }

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            DecisionListScreen(
                navController = navController, 
                decisions = decisions, 
                isLoading = isLoading, 
                isError = isError, 
                takenDecisions = takenDecisions,
                userStatus = userStatus,
                onStatusChange = { userStatus = it }
            )
        }

        composable("detail/{index}") { backStackEntry ->
            val indexString = backStackEntry.arguments?.getString("index")
            val index = indexString?.toIntOrNull() ?: 0
            if (decisions.isNotEmpty() && index < decisions.size) {
                DecisionDetailScreen(
                    decision = decisions[index], 
                    navController = navController,
                    isAlreadyTaken = takenDecisions.contains(index),
                    onDecisionMade = { takenDecisions = takenDecisions + index },
                    onRemoveDecision = { takenDecisions = takenDecisions - index }
                )
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
    isError: Boolean,
    takenDecisions: Set<Int>,
    userStatus: String,
    onStatusChange: (String) -> Unit
) {
    val statuses = listOf("Umum", "Mahasiswa", "Lulusan SMA", "Pekerja", "Wirausaha")
    var showDialog by remember { mutableStateOf(false) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("DecisionApp", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Pilihan bijak untuk masa depan")
                }
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Profil")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info Status User & Rekomendasi
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().clickable { showDialog = true }
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, tint = MaterialTheme.colorScheme.primary, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Status Anda: $userStatus", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        Text("Sistem merekomendasikan pilihan yang sesuai", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Banner Rekomendasi (Disesuaikan dengan status)
            val recommendedDecision = decisions.find { it.suitableFor.any { role -> role.contains(userStatus, ignoreCase = true) } } ?: decisions.getOrNull(0)
            
            if (recommendedDecision != null) {
                val index = decisions.indexOf(recommendedDecision)
                Text("Rekomendasi Untukmu", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().clickable { navController.navigate("detail/$index") }
                ) {
                    Box {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(recommendedDecision.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(180.dp)
                        )
                        Box(modifier = Modifier.matchParentSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.7f)))))
                        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Bottom) {
                            Text("Cocok untuk $userStatus", color = Color.Yellow, style = MaterialTheme.typography.labelSmall)
                            Text(recommendedDecision.title, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Semua Pilihan", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            decisions.forEachIndexed { index, decision ->
                val isRecommended = decision.suitableFor.any { it.contains(userStatus, ignoreCase = true) }
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clickable { navController.navigate("detail/$index") },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isRecommended) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) 
                                         else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Row(modifier = Modifier.padding(8.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = decision.imageRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(70.dp).clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(decision.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                if (isRecommended) {
                                    Spacer(Modifier.width(4.dp))
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                                }
                            }
                            Text(decision.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        }
                        if (takenDecisions.contains(index)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                        }
                    }
                }
            }
        }
    }

    // Dialog Input Status
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Siapa Anda Saat Ini?") },
            text = {
                Column {
                    statuses.forEach { status ->
                        Row(
                            Modifier.fillMaxWidth().clickable { onStatusChange(status); showDialog = false }.padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = userStatus == status, onClick = { onStatusChange(status); showDialog = false })
                            Spacer(Modifier.width(8.dp))
                            Text(status)
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showDialog = false }) { Text("Tutup") } }
        )
    }
}

@Composable
fun DecisionDetailScreen(
    decision: Decision, 
    navController: NavController, 
    isAlreadyTaken: Boolean,
    onDecisionMade: () -> Unit,
    onRemoveDecision: () -> Unit
) {
    var isAnalyzing by remember { mutableStateOf(false) }
    var showAnalysis by remember { mutableStateOf(isAlreadyTaken) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(decision.imageUrl).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(decision.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                    Text(decision.category, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                }
                if (isAlreadyTaken) {
                    Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(8.dp)) {
                        Text("Terpilih", color = Color(0xFF2E7D32), modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(decision.description)

            // Info Kesesuaian
            Spacer(modifier = Modifier.height(12.dp))
            Text("Cocok untuk: ${decision.suitableFor.joinToString(", ")}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Analisis SWOT", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))

            Text("Pros (Keuntungan)", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            decision.pros.forEach { pro ->
                InfoCard(text = pro, icon = Icons.Default.Check, iconColor = Color(0xFF2E7D32), bgColor = Color(0xFFE8F5E9))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Cons (Kerugian)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
            decision.cons.forEach { con ->
                InfoCard(text = con, icon = Icons.Default.Clear, iconColor = Color.Red, bgColor = Color(0xFFFFEBEE))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Tips Strategis", fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
            decision.tips.forEach { tip ->
                InfoCard(text = tip, icon = Icons.Default.Star, iconColor = Color(0xFF1976D2), bgColor = Color(0xFFE3F2FD))
            }

            if (showAnalysis) {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Kesimpulan", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(decision.analysis, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (!showAnalysis) {
                Button(
                    onClick = {
                        scope.launch { isAnalyzing = true; delay(1000); isAnalyzing = false; showAnalysis = true }
                    },
                    enabled = !isAnalyzing,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isAnalyzing) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    else Text("Mulai Analisis")
                }
            } else if (!isAlreadyTaken) {
                Button(
                    onClick = {
                        onDecisionMade()
                        scope.launch { snackbarHostState.showSnackbar("Keputusan ditetapkan!") }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Tetapkan Sebagai Pilihan")
                }
            } else {
                OutlinedButton(
                    onClick = { onRemoveDecision(); showAnalysis = false; scope.launch { snackbarHostState.showSnackbar("Pilihan dihapus.") } },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Hapus dari Pilihan Saya")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text("Kembali") }
            Spacer(modifier = Modifier.height(16.dp))
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun InfoCard(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconColor: Color, bgColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
