package com.example.duroodcounter.screens.detailscreen

import QuranicFont
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.duroodcounter.R
import java.text.SimpleDateFormat
import java.util.Date

val ArabicFont = FontFamily(
    Font(R.font.arabic_font) // Make sure the filename is exactly "arabic_font.otf"
)

@Composable
fun CounterScreen(
    navController: NavController,
    counterId: Int,
    viewModel: CounterDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var newAmount by remember { mutableStateOf("") }

    val recitations by viewModel.recitations.collectAsState()
    val counter by viewModel.counter.collectAsState()

    LaunchedEffect(counterId) {
        viewModel.loadCounterWithHistory(counterId)
    }

    counter?.let { counter ->
        val totalRecited = recitations.sumOf { it.amount }
        val remaining = (counter.target - totalRecited).coerceAtLeast(0)

        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = counter.title,
                        fontFamily = ArabicFont,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            },
            bottomBar = {
                if (remaining == 0) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "✅ Completed",
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                fontFamily = ArabicFont
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFBBDEFB))
                                .clickable { showDialog = true }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Add Recitation", color = Color(0xFF0D47A1), fontWeight = FontWeight.Bold, fontFamily = ArabicFont)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFC8E6C9))
                                .clickable {
                                    navController.navigate("counting_screen/${counter.id}/${counter.title}/${remaining}")
                                }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Start Counting", color = Color(0xFF1B5E20), fontWeight = FontWeight.Bold, fontFamily = ArabicFont)
                        }
                    }
                }
            }
        ) { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                item {
                    // Bismillah
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "﷽",
                            fontSize = 36.sp,
                            fontFamily = QuranicFont,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Progress Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ProgressItem("Target", counter.target.toString())
                            Divider(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(1.dp),
                                color = Color.LightGray
                            )
                            ProgressItem("Recited", totalRecited.toString())
                            Divider(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(1.dp),
                                color = Color.LightGray
                            )
                            ProgressItem("Remaining", remaining.toString())
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "📜 Recitation History",
                        fontFamily = ArabicFont,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(recitations) { entry ->
                    val time = SimpleDateFormat("dd MMM yyyy - hh:mm a").format(Date(entry.timestamp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "${entry.amount} recited",
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = ArabicFont
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = time,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = ArabicFont
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            // Delete Dialog
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Delete Counter", fontFamily = ArabicFont) },
                    text = { Text("Are you sure you want to delete this counter?", fontFamily = ArabicFont) },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteCounter(counter.id)
                            showDeleteDialog = false
                            onNavigateBack()
                        }) {
                            Text("Yes", color = Color.Red, fontFamily = ArabicFont)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Cancel", fontFamily = ArabicFont)
                        }
                    }
                )
            }

            // Add Recitation Dialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    shape = RoundedCornerShape(6.dp),
                    confirmButton = {
                        TextButton(onClick = {
                            newAmount.toIntOrNull()?.takeIf { it > 0 }?.let {
                                viewModel.addRecitation(counter.id, it)
                                newAmount = ""
                                showDialog = false
                            }
                        }) {
                            Text("Add", color = MaterialTheme.colorScheme.primary)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel", color = Color.Red)
                        }
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = " Durood Input ﷺ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                fontFamily = ArabicFont,
                                color = MaterialTheme.colorScheme.primary
                            )
                            OutlinedTextField(
                                value = newAmount,
                                onValueChange = { newAmount = it },
                                label = { Text("Enter Amount", fontFamily = ArabicFont) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp
                )
            }
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}



@Composable
fun ProgressItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 14.sp, color = Color.DarkGray, fontFamily = ArabicFont)
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = ArabicFont)
    }
}

