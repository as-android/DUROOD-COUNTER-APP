package com.example.duroodcounter.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.duroodcounter.screens.detailscreen.ArabicFont

@Composable
fun WelcomeDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Text(
                "Continue",
                fontFamily = ArabicFont,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onDismiss() },
                color = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = "السَّلَامُ عَلَيْكُمْ",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = ArabicFont
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Welcome to Durood Counter App!\n\nThis app helps you send blessings (Durood) upon Prophet Muhammad ﷺ with ease and beauty.",
                    fontFamily = ArabicFont,
                    fontSize = 18.sp,
                    lineHeight = 22.sp
                )
                Text(
                    "✨ Features:\n• Multiple authentic Durood\n• Tap counting modes\n• Daily reminders\n• Smooth, elegant design", fontFamily = ArabicFont
                )
                Text(
                    "⚠️ Disclaimer: This app is a digital aid and may have minor errors. Always prioritize authentic sources.",
                    fontFamily = ArabicFont,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        },
        shape = RoundedCornerShape(8.dp)
    )
}
