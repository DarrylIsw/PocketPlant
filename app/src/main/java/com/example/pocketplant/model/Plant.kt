package com.example.pocketplant.model

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class Plant @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: Int,
    val name: String,
    val type: String,
    val lastWatered: LocalDate = LocalDate.now(),
    val wateringStreak: Int = 0,
    val imageUri: Uri? = null,
    val wateringHours: List<String> = emptyList(),
    val sunlight: String = "",
    val notes: String = ""
)

