package com.example.duroodcounter.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.duroodcounter.screens.detailscreen.ArabicFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController,
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "About",
                    fontFamily = ArabicFont,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 📱 App Intro Card
            Card(
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "About This App",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = ArabicFont,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "In celebration of 1500 years since the blessed birth of our beloved Prophet Muhammad ﷺ, this app was created as a humble gift to the Ummah. It provides an easy and beautiful way to send Durood upon the Prophet ﷺ through various authentic invocations.",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = ArabicFont,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // 🕌 Sponsored Card
            Card(
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Sponsored By",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = ArabicFont,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "This application is proudly sponsored by the Mohammed Islamic Status YouTube Channel. We aim to spread love for the Prophet ﷺ through modern tools and platforms.",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = ArabicFont,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // 📩 Feedback Card
            Card(
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Feedback & Suggestions",
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = ArabicFont,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "We would love to hear your feedback, suggestions, or reports of any issues you encounter. Please feel free to contact us via email.",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = ArabicFont,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "📧 Email us: duroodapp@gmail.com",
                        fontFamily = ArabicFont,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .clickable {
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:asifkhan78six@gmail.com")
                                    putExtra(Intent.EXTRA_SUBJECT, "Feedback from CounterApp")
                                }
                                context.startActivity(emailIntent)
                            }
                    )
                }
            }
        }
    }
}

