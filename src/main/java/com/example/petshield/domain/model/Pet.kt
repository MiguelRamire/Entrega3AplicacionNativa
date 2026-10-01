package com.example.petshield.domain.model

data class Pet(
    val id: Long = 0,
    val name: String,
    val species: String,
    val breed: String,
    val ageYears: Int,
    val weightKg: Double,
    val photoUrl: String? = null
)