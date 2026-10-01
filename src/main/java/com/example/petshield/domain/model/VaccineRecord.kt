package com.example.petshield.domain.model

data class VaccineRecord(
    val id: Long = 0,
    val petId: Long,
    val name: String,
    val administeredAt: String,
    val dueAt: String,
    val veterinarian: String
)