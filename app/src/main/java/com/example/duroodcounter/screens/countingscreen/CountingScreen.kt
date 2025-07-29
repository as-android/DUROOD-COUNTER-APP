
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.duroodcounter.R
import com.example.duroodcounter.screens.countingscreen.CountingScreenViewModel
import com.example.duroodcounter.screens.detailscreen.ArabicFont

val QuranicFont = FontFamily(
    Font(R.font.quranic_font)
)
@Composable
fun CountingScreen(
    counterTitle: String,
    counterId: Int,
    counterTarget: Int,
    onNavigateBack: () -> Unit,
    viewModel: CountingScreenViewModel = hiltViewModel(),
) {
    val count by viewModel.liveCount.collectAsState()
    val showDialog by viewModel.showTargetDialog.collectAsState()

    LaunchedEffect(counterId) {
        viewModel.startCounting(counterId, counterTarget)
    }

    val scrollState = rememberScrollState()


    // 🔎 Get matching durood text from string.xml
    val duroodText = when (counterTitle) {
        stringResource(R.string.durood_ibrahimi_title) -> stringResource(R.string.durood_ibrahimi_text)
        stringResource(R.string.durood_tanjeena_title) -> stringResource(R.string.durood_tanjeena_text)
        stringResource(R.string.durood_short_title) -> stringResource(R.string.durood_short_text)
        stringResource(R.string.durood_sharif_title) -> stringResource(R.string.durood_sharif_text)
        stringResource(R.string.durood_ghausia_title) -> stringResource(R.string.durood_ghausia_text)
        stringResource(R.string.durood_khizri_title) -> stringResource(R.string.durood_khizri_text)
        stringResource(R.string.durood_jumma_title) -> stringResource(R.string.durood_jumma_text)
        stringResource(R.string.daily_durood_title) -> stringResource(R.string.daily_durood_text)
        stringResource(R.string.durood_taj_title) -> stringResource(R.string.durood_taj_text)
        stringResource(R.string.lahawal_title) -> stringResource(R.string.lahawal_text)
        stringResource(R.string.astaghfar_title) -> stringResource(R.string.astaghfar_text)
        stringResource(R.string.kalma_title) -> stringResource(R.string.kalma_text)
        stringResource(R.string.ayatekareema_title) -> stringResource(R.string.ayatekareema_text)
        stringResource(R.string.durood_ghousia1_title) -> stringResource(R.string.durood_ghousia1_text)
        else -> stringResource(R.string.durood_sharif_text) // fallback
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = counterTitle,
                    fontFamily = ArabicFont,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 20.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = duroodText,
                                fontSize = 24.sp,
                                fontFamily = QuranicFont,
                                maxLines = 10,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Right,
                                style = TextStyle(textDirection = TextDirection.Rtl),
                                lineHeight = 34.sp
                            )
                        }
                    }


            Spacer(modifier = Modifier.height(32.dp))

            // 🔢 Count Display
            Text(
                text = "Count: $count",
                fontSize = 48.sp,
                fontFamily = ArabicFont,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(6.dp))

            val currTarget = counterTarget - count
            Text(
                text = if (currTarget > 0) "Remaining: $currTarget" else "Target Completed",
                fontSize = 20.sp,
                fontFamily = ArabicFont,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 👆 Tap Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF5F5F5))
                    .pointerInput(Unit) {
                        detectTapGestures {
                            viewModel.incrementCount()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Tap Anywhere to Count",
                        fontFamily = ArabicFont,
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_touch),
                        contentDescription = "Touch Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // 🎯 Dialog
    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x88000000))
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFF4CAF50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check),
                            contentDescription = "Check",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Congratulations!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Target Completed",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "Leave",
                            fontSize = 16.sp,
                            color = Color.Red,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    viewModel.dismissDialog()
                                    onNavigateBack()
                                }
                        )
                        Text(
                            text = "Continue",
                            fontSize = 16.sp,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    viewModel.dismissDialog()
                                }
                        )
                    }
                }
            }
        }
    }
}

