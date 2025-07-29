package com.example.duroodcounter.screens.addscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.duroodcounter.R
import com.example.duroodcounter.screens.detailscreen.ArabicFont

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DuroodListScreen(navController: NavController) {
    val duroodList = listOf(
        R.string.kalma_title,
        R.string.ayatekareema_title,
        R.string.durood_short_title,
        R.string.durood_sharif_title,
        R.string.durood_jumma_title,
        R.string.durood_ibrahimi_title,
        R.string.daily_durood_title,
        R.string.durood_taj_title,
        R.string.durood_tanjeena_title,
        R.string.lahawal_title,
        R.string.astaghfar_title,
        R.string.durood_ghousia1_title,
        R.string.durood_ghausia_title,
        R.string.durood_khizri_title,

    )
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Select Durood",
                    fontFamily = ArabicFont,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            duroodList.forEach { titleRes ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            navController.navigate("preview_screen/${titleRes}")
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.madina_png),
                            contentDescription = "Madina",
                            modifier = Modifier
                                .size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = stringResource(id = titleRes),
                            fontSize = 18.sp,
                            fontFamily = ArabicFont
                        )
                    }
                }

            }
        }
    }
}
