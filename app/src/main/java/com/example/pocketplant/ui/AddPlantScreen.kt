package com.example.pocketplant.ui

import android.app.TimePickerDialog
import android.net.Uri
import android.os.Build
import android.widget.TimePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.flowlayout.FlowRow
import com.example.pocketplant.model.Plant
import com.example.pocketplant.viewmodel.PlantViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddPlantScreen(
    navController: NavController,
    viewModel: PlantViewModel,
    onAddPlant: (Plant) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var sunlight by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val wateringHours = remember { mutableStateListOf<String>() }

    var typeExpanded by remember { mutableStateOf(false) }
    var sunlightExpanded by remember { mutableStateOf(false) }

    val plantTypes = listOf("Succulent", "Cactus", "Fern", "Herb", "Tree", "Other")
    val sunlightOptions = listOf("Full Sun", "Partial Shade", "Low Light")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> if (uri != null) imageUri = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Plant", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // === Plant Image Card ===
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable { launcher.launch("image/*") },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "Plant Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text("Tap to upload image", color = Color.Gray)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // === Info Card ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {

                    Text("Plant Info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Plant Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    // --- Type Dropdown ---
                    ExposedDropdownMenuBox(
                        expanded = typeExpanded,
                        onExpandedChange = { typeExpanded = !typeExpanded }
                    ) {
                        OutlinedTextField(
                            value = type,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Plant Type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(typeExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false }
                        ) {
                            plantTypes.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        type = option
                                        typeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // === Watering Card ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Watering Schedule", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val now = LocalTime.now()
                            TimePickerDialog(
                                context,
                                { _: TimePicker, hourOfDay: Int, minute: Int ->
                                    val selected = LocalTime.of(hourOfDay, minute)
                                    val formatted = selected.format(timeFormatter)
                                    if (!wateringHours.contains(formatted)) wateringHours.add(formatted)
                                },
                                now.hour,
                                now.minute,
                                true
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Watering Time")
                    }

                    if (wateringHours.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 4.dp) {
                            wateringHours.forEach { hour ->
                                AssistChip(
                                    onClick = {},
                                    label = { Text(hour) },
                                    trailingIcon = {
                                        IconButton(onClick = { wateringHours.remove(hour) }) {
                                            Icon(Icons.Default.Close, contentDescription = "Remove")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // === Sunlight & Notes Card ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Care Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = sunlightExpanded,
                        onExpandedChange = { sunlightExpanded = !sunlightExpanded }
                    ) {
                        OutlinedTextField(
                            value = sunlight,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Sunlight Requirement") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(sunlightExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = sunlightExpanded,
                            onDismissRequest = { sunlightExpanded = false }
                        ) {
                            sunlightOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        sunlight = option
                                        sunlightExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // === Save Button ===
            Button(
                onClick = {
                    if (name.isBlank() || type.isBlank()) return@Button
                    val newPlant = Plant(
                        id = (viewModel.plants.value?.size ?: 0) + 1,
                        name = name,
                        type = type,
                        lastWatered = LocalDate.now(),
                        wateringStreak = 0,
                        imageUri = imageUri,
                        wateringHours = wateringHours.toList(),
                        sunlight = sunlight,
                        notes = notes
                    )
                    viewModel.addPlant(newPlant)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Plant", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
