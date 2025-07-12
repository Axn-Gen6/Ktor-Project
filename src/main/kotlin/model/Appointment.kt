package com.example.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Appointment(
    val id: Int,
    val clientName: String,
    val clientEmail: String,
    val appointmentTime: String, // LocalDateTime in ISO format
    val serviceId: Int
)
