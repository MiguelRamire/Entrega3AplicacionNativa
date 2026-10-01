package com.example.petshield.domain.model

enum class AppointmentStatus {
    UPCOMING,
    COMPLETED,
    CANCELLED
}

data class Appointment(
    val id: Long = 0,
    val petId: Long,
    val clinicName: String,
    val service: String,
    val dateTime: String,
    val status: AppointmentStatus = AppointmentStatus.UPCOMING
)