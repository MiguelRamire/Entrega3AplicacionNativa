package com.example.petshield.domain.model

data class NotificationPreferences(
    val appointmentReminders: Boolean = true,
    val healthReminders: Boolean = true,
    val promotions: Boolean = false
)