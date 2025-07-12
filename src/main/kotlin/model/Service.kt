package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val id: Int,
    val name: String,
    val description: String,
    val defaultDurationInMinutes: Int
)
