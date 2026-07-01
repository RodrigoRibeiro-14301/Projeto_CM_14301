package com.example.campo.model

import com.example.campo.R

enum class BookingStatus { CONFIRMED, COMPLETED }

data class Booking(
    val id: String,
    val fieldId: String,
    val fieldName: String,
    val location: String,
    val dateLabel: String,
    val timeLabel: String,
    val durationHours: Int = 1,
    val format: String,
    val price: Int,
    val status: BookingStatus,
    val isUpcoming: Boolean,
    val imageRes: Int
)

fun fieldImageRes(fieldId: String): Int = when (fieldId) {
    "1" -> R.drawable.campo1
    "2" -> R.drawable.campo2
    "3" -> R.drawable.campo3
    "4" -> R.drawable.campo4
    "5" -> R.drawable.campo5
    "6" -> R.drawable.campo6
    else -> R.drawable.campo1
}