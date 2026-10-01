package com.example.petshield.domain.model

data class Clinic(
    val id: Long = 0,
    val name: String,
    val address: String,
    val phone: String,
    val rating: Double,
    val isFavorite: Boolean = false
)