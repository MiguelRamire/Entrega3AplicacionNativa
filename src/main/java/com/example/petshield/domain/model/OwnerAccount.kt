package com.example.petshield.domain.model

data class OwnerAccount(
    val name: String,
    val email: String,
    val phone: String = ""
)