package com.example.duroodcounter.screens.home
import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.duroodcounter.R
import com.example.duroodcounter.data.counterlistdata.CounterEntity
import com.example.duroodcounter.screens.detailscreen.ArabicFont
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class SortOption {
    TIME, NAME, TARGET
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var showDeleteIcon by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf(SortOption.TIME) }
    var sortMenuExpanded by remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateListOf<Int>() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<CounterEntity?>(null) }

    val countersWithProgress by viewModel.counterWithProgress.collectAsState()

    val filteredCounters = countersWithProgress
        .filter { it.counter.title.contains(searchQuery, ignoreCase = true) }
        .let {
            when (sortOption) {
                SortOption.TIME -> it.sortedByDescending { it.counter.createdAt }
                SortOption.NAME -> it.sortedBy { it.counter.title.lowercase() }
                SortOption.TARGET -> it.sortedBy { it.counter.target }
            }
        }

    // 🧼 Clean screen layout
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // 📜 Whole scrollable screen
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // 🔝 Top Bar
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Durood Counter",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = ArabicFont,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )
                        //about icon
                        IconButton(onClick = { navController.navigate("about_screen") }) {
                            Icon(Icons.Default.Info, contentDescription = "About", modifier = Modifier.size(22.dp))
                        }
                        Box {
                            IconButton(onClick = { sortMenuExpanded = true }) {
                                Icon(
                                    painter = painterResource(R.drawable.sort),
                                    contentDescription = "Sort",
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = sortMenuExpanded,
                                onDismissRequest = { sortMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Sort by Time", fontFamily = ArabicFont) },

                                    onClick = {
                                        sortOption = SortOption.TIME
                                        sortMenuExpanded = false
                                        if(countersWithProgress.isEmpty()) {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "No counters available",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }else if(countersWithProgress.size == 1){
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Only One Item Available",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                        else {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    "Sorted by latest Created Item",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Sort by Name", fontFamily = ArabicFont) },
                                    onClick = {
                                        sortOption = SortOption.NAME
                                        sortMenuExpanded = false
                                        if(countersWithProgress.isEmpty()) {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "No counters available",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }else if(countersWithProgress.size == 1){
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Only One Item Available",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                        else {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    "Sorted by Alphabetical Order",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }

                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Sort by Target", fontFamily = ArabicFont) },
                                    onClick = {
                                        sortOption = SortOption.TARGET
                                        sortMenuExpanded = false
                                        if(countersWithProgress.isEmpty()) {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "No counters available",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }else if(countersWithProgress.size == 1){
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Only One Item Available",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                        else{
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Sorted by Min-Max format",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                        if (showDeleteIcon) {
                            IconButton(onClick = { showDeleteDialog = true }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                        IconButton(onClick = { navController.navigate("addscreen") }) {
                            Icon(Icons.Default.AddCircle, contentDescription = "Add")
                        }
                    }
                }
                // 🔍 Search Box
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search for counters", fontFamily = ArabicFont) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Gray,
                            focusedBorderColor = Color.Black,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        )
                    )
                }


                // 🌙 Durood Ayah Section
                item {
                    DuroodAyahSection()
                }
                item{
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFBBDEFB))
                            .clickable{
                                navController.navigate("durood_list_screen")
                            }
                            .padding(vertical = 14.dp, horizontal = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Text(
                                text = "Select From List",
                                fontFamily = ArabicFont,
                                color = Color(0xFF0D47A1),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Durood Screen",
                            )
                        }
                    }
                }


                // 📦 Empty State
                if (filteredCounters.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 100.dp), // Adds breathing room
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Optional: Add a soft illustrative icon
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_empty_state),
                                    contentDescription = "Empty State",
                                    tint = Color(0xFFBDBDBD),
                                    modifier = Modifier.size(80.dp)
                                )

                                Text(
                                    text = "No counters available",
                                    color = Color.Gray,
                                    fontSize = 18.sp,
                                    fontFamily = ArabicFont
                                )
                            }
                        }
                    }

                } else {
                    items(filteredCounters) { item ->
                        val counter = item.counter
                        val isSelected = selectedItems.contains(counter.id)

                        CounterItem(
                            data = counter,
                            recited = item.totalRecited,
                            isSelected = isSelected,
                            onClick = {
                                if (showDeleteIcon) {
                                    if (isSelected) selectedItems.remove(counter.id)
                                    else selectedItems.add(counter.id)
                                    if (selectedItems.isEmpty()) showDeleteIcon = false
                                } else {
                                    navController.navigate("counter/${counter.id}")
                                }
                            },
                            onLongClick = {
                                showDeleteIcon = true
                                if (!selectedItems.contains(counter.id)) selectedItems.add(counter.id)
                            },
                            onView = { navController.navigate("counter/${counter.id}") },
                            onDelete = { itemToDelete = counter }
                        )
                    }
                }
            }

            // 🗑️ Delete dialogs
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Delete selected counters?", fontFamily = ArabicFont) },
                    text = { Text("Are you sure you want to delete ${selectedItems.size} counter(s)?", fontFamily = ArabicFont) },
                    confirmButton = {
                        TextButton(onClick = {
                            selectedItems.forEach { id ->
                                countersWithProgress.find { it.counter.id == id }
                                    ?.let { viewModel.deleteCounter(it.counter) }
                            }
                            selectedItems.clear()
                            showDeleteIcon = false
                            showDeleteDialog = false
                            scope.launch {
                                snackbarHostState.showSnackbar(message = "Counter(s) deleted", duration = SnackbarDuration.Short)
                            }
                        }) {
                            Text("Delete", color = Color.Red)
                        }

                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (itemToDelete != null) {
                AlertDialog(
                    onDismissRequest = { itemToDelete = null },
                    title = { Text("Delete counter?", fontFamily = ArabicFont) },
                    text = { Text("Are you sure you want to delete this counter?", fontFamily = ArabicFont) },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteCounter(itemToDelete!!)
                            itemToDelete = null
                            scope.launch {
                                snackbarHostState.showSnackbar("Counter deleted")
                            }

                        }) {
                            Text("Delete", color = Color.Red, fontFamily = ArabicFont)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { itemToDelete = null }) {
                            Text("Cancel", color = MaterialTheme.colorScheme.primary, fontFamily = ArabicFont)
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DuroodAyahSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // 🕌 Madina Image (Left)
                Image(
                    painter = painterResource(R.drawable.madina_png),
                    contentDescription = "Masjid an-Nabawi",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                // 📜 Durood Text (Center)
                Text(
                    text = stringResource(R.string.durood_sharif_text),
                    fontSize = 20.sp,
                    fontFamily = ArabicFont,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 30.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                )

                // 🕋 Mecca Image (Right)
                Image(
                    painter = painterResource(R.drawable.mecca_png),
                    contentDescription = "Masjid al-Haram",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
            }
        }
    }
}











@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CounterItem(
    data: CounterEntity,
    recited: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onView: () -> Unit,
    onDelete: () -> Unit
) {
    val remaining = (data.target - recited).coerceAtLeast(0)
    val isComplete = remaining == 0

    val haptic = LocalHapticFeedback.current
    var expanded by remember { mutableStateOf(false) }

    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        onClick()
                    },
                    onLongClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onLongClick()
                    }
                ),
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 0.dp,
            color = if (isSelected) Color.Red.copy(alpha = 0.5f) else Color.Transparent
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = data.title,
                        fontFamily = ArabicFont,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options")
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(text = { Text("View", fontFamily = ArabicFont) }, onClick = {
                                expanded = false
                                onView()
                            })
                            DropdownMenuItem(text = { Text("Delete", fontFamily = ArabicFont) }, onClick = {
                                expanded = false
                                onDelete()
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (isComplete) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("✅ Completed", color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium, fontFamily = ArabicFont)
                    }
                } else {
                    Column {
                        Text(" Target: ${data.target}", color = Color.Gray, fontSize = 14.sp, fontFamily = ArabicFont)
                        Text(" Remaining: $remaining", color = Color.Gray, fontSize = 14.sp, fontFamily = ArabicFont)
                    }
                }

                Text(
                    text = " Created at " + SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                        .format(Date(data.createdAt)),
                    fontSize = 12.sp,
                    fontFamily = ArabicFont,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // 🌙 Separator line
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            color = Color.Gray.copy(alpha = 0.15f),
            thickness = 2.dp
        )
    }
}



