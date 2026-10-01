package com.example.petshield

import android.app.Application
import com.example.petshield.di.AppContainer

class PetShieldApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}