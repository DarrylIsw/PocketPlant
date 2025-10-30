package com.example.pocketplant.model

import android.net.Uri
import java.time.LocalDate

data class Plant(
    val id: Int,
    val name: String,
    val type: String,
    val lastWatered: LocalDate,
    val wateringStreak: Int,
    val imageUri: Uri? = null,
    val wateringHours: List<String> = emptyList(),
    val sunlight: String = "",
    val notes: String = ""
)

