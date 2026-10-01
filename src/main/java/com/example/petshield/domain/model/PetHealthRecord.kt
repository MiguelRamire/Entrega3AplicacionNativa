package com.example.petshield.domain.model

data class PetHealthRecord(
    val id: Long = 0,
    val petId: Long,
    val category: String,
    val title: String,
    val date: String,
    val details: String
)