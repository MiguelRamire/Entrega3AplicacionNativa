package com.example.petshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.petshield.ui.PetShieldApp
import com.example.petshield.ui.theme.PetShieldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetShieldTheme {
                PetShieldApp(container = (application as PetShieldApplication).container)
            }
        }
    }
}