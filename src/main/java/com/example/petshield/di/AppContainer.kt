package com.example.petshield.di

import android.content.Context
import com.example.petshield.data.local.PetShieldDatabase
import com.example.petshield.data.repository.DefaultPetShieldRepository
import com.example.petshield.domain.repository.PetShieldRepository

class AppContainer(context: Context) {
    private val database = PetShieldDatabase(context.applicationContext)
    val petShieldRepository: PetShieldRepository = DefaultPetShieldRepository(database)
}