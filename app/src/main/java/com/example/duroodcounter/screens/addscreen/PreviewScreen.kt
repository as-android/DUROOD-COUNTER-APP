package com.example.duroodcounter.screens.addscreen

import QuranicFont
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.duroodcounter.R
import com.example.duroodcounter.screens.detailscreen.ArabicFont
import com.example.duroodcounter.screens.home.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PreviewDuroodScreen(
    titleResId: Int,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val title = stringResource(id = titleResId)
    var showDialog by remember { mutableStateOf(false) }
    var targetText by remember { mutableStateOf("") }

    val duroodText = when (titleResId) {
        R.string.durood_ibrahimi_title -> stringResource(R.string.durood_ibrahimi_text)
        R.string.durood_tanjeena_title -> stringResource(R.string.durood_tanjeena_text)
        R.string.durood_short_title -> stringResource(R.string.durood_short_text)
        R.string.durood_sharif_title -> stringResource(R.string.durood_sharif_text)
        R.string.durood_ghausia_title -> stringResource(R.string.durood_ghausia_text)
        R.string.durood_ghousia1_title -> stringResource(R.string.durood_ghousia1_text)
        R.string.ayatekareema_title -> stringResource(R.string.ayatekareema_text)
        R.string.durood_khizri_title -> stringResource(R.string.durood_khizri_text)
        R.string.durood_jumma_title -> stringResource(R.string.durood_jumma_text)
        R.string.daily_durood_title -> stringResource(R.string.daily_durood_text)
        R.string.durood_taj_title -> stringResource(R.string.durood_taj_text)
        R.string.lahawal_title -> stringResource(R.string.lahawal_text)
        R.string.astaghfar_title -> stringResource(R.string.astaghfar_text)
        R.string.kalma_title -> stringResource(R.string.kalma_text)
        else -> ""
    }

    if (showDialog) {
        var isError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Text(
                    text = "Add",
                    modifier = Modifier
                        .clickable {
                            val target = targetText.toIntOrNull()
                            if (target != null && target > 0) {
                                viewModel.addCounter(title, target)
                                navController.navigate("home")
                                showDialog = false
                            } else {
                                isError = true
                            }
                        }
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = ArabicFont,
                    fontWeight = FontWeight.Bold
                )
            },
            dismissButton = {
                Text(
                    text = "Cancel",
                    modifier = Modifier
                        .clickable { showDialog = false }
                        .padding(8.dp),
                    fontFamily = ArabicFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            },
            title = {
                Text(
                    text = "Set Target",
                    fontFamily = ArabicFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = targetText,
                        onValueChange = {
                            targetText = it
                            isError = !it.all { char -> char.isDigit() } || it.isBlank()
                        },
                        label = { Text("Enter target count", fontFamily = ArabicFont) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        isError = isError,
                        singleLine = true
                    )
                    if (isError) {
                        Text(
                            text = "Please enter a valid number",
                            fontFamily = ArabicFont,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

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
                    text = title,
                    fontFamily = ArabicFont,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        showDialog = true
                    }
                    .padding(vertical = 14.dp, horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Text(
                        text = "Set Target",
                        color = Color.White,
                        fontFamily = ArabicFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.End
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = duroodText,
                fontSize = 22.sp,
                fontFamily = QuranicFont,
                textAlign = TextAlign.Right,
                style = TextStyle(
                    textDirection = TextDirection.Rtl
                )
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

