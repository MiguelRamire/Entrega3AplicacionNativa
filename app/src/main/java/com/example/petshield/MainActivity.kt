package com.example.petshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.petshield.ui.theme.PetShieldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetShieldTheme {
                val pantallaActual = remember { mutableStateOf("splash") }

                if (pantallaActual.value == "splash") {
                    SplashScreen(
                        onNavigateToWelcome = {
                            pantallaActual.value = "welcome"
                        }
                    )
                } else if (pantallaActual.value == "welcome") {
                    WelcomeScreen(
                        onNavigateToLogin = { pantallaActual.value = "login" },
                        onNavigateToRegister = { pantallaActual.value = "register" }
                    )
                } else if (pantallaActual.value == "login") {
                    LoginScreen(
                        onBack = { pantallaActual.value = "welcome" },
                        onNavigateToRegister = { pantallaActual.value = "register_from_login" },
                        onNavigateToRecover = { pantallaActual.value = "recover" },
                        onLoginSuccess = {
                            pantallaActual.value = "onboarding"
                        }
                    )
                } else if (pantallaActual.value == "onboarding") {
                    OnboardingScreen(
                        onFinishOnboarding = {
                            // Al terminar el onboarding, salta al home sin errores
                            pantallaActual.value = "home"
                        }
                    )
                } else if (pantallaActual.value == "home") {
                    // --- HOME LIMPIO SIN PARÁMETROS RAROS ---
                    HomeScreen()
                } else if (pantallaActual.value == "recover") {
                    RecoverPasswordScreen(
                        onBack = { pantallaActual.value = "login" },
                        onSubmitEmail = {
                            pantallaActual.value = "login"
                        }
                    )
                } else if (pantallaActual.value == "register" || pantallaActual.value == "register_from_login") {
                    val origenLogin = pantallaActual.value == "register_from_login"

                    RegisterScreen(
                        onBack = {
                            pantallaActual.value = if (origenLogin) "login" else "welcome"
                        },
                        onNavigateToLogin = { pantallaActual.value = "login" }
                    )
                }
            }
        }
    }
}