package com.example.pocketplant.model

import java.time.LocalDate

data class Plant(
    val id: Int,
    val name: String,
    val type: String,
    val lastWatered: LocalDate,
    val wateringStreak: Int
)
