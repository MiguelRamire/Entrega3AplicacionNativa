package com.example.petshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.petshield.ui.theme.PetShieldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetShieldTheme {
                PetShieldApp()
            }
        }
    }
}

@Composable
fun PetShieldApp() {
    var pantallaActual by remember { mutableStateOf("splash") }
    val viewModel = remember { PetShieldViewModel() }

    fun navigateToTab(tab: BottomTab) {
        pantallaActual = when (tab) {
            BottomTab.HOME -> "home"
            BottomTab.PROFILE -> "perfil"
            BottomTab.CITAS -> "todas_citas"
        }
    }

    when (pantallaActual) {
        "splash" -> {
            SplashScreen(
                onNavigateToWelcome = {
                    pantallaActual = "welcome"
                }
            )
        }

        "welcome" -> {
            WelcomeScreen(
                onNavigateToLogin = { pantallaActual = "login" },
                onNavigateToRegister = { pantallaActual = "register" }
            )
        }

        "login" -> {
            LoginScreen(
                onBack = { pantallaActual = "welcome" },
                onNavigateToRegister = { pantallaActual = "register_from_login" },
                onNavigateToRecover = { pantallaActual = "recover" },
                onLoginSuccess = { pantallaActual = "onboarding" }
            )
        }

        "recover" -> {
            RecoverPasswordScreen(
                onBack = { pantallaActual = "login" },
                onSubmitEmail = { pantallaActual = "login" }
            )
        }

        "register", "register_from_login" -> {
            val origenLogin = pantallaActual == "register_from_login"
            RegisterScreen(
                onBack = {
                    pantallaActual = if (origenLogin) "login" else "welcome"
                },
                onNavigateToLogin = { pantallaActual = "login" }
            )
        }

        "onboarding" -> {
            OnboardingScreen(
                onFinishOnboarding = { pantallaActual = "home" }
            )
        }

        "home" -> {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToFavoritos = { pantallaActual = "favoritos" },
                onNavigateToClinicas = { pantallaActual = "clinicas" },
                onNavigateToCarnet = { pantallaActual = "carnet" },
                onNavigateToPerfil = { pantallaActual = "perfil" },
                onNavigateToCitas = { pantallaActual = "todas_citas" },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        "clinicas" -> {
            ClinicasScreen(
                viewModel = viewModel,
                onBack = { pantallaActual = "home" },
                onSelectClinica = { pantallaActual = "detalle_servicio" },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        "detalle_servicio" -> {
            DetalleServicioScreen(
                viewModel = viewModel,
                onBack = { pantallaActual = "clinicas" },
                onReservarServicio = { pantallaActual = "resumen_reserva" },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        "resumen_reserva" -> {
            ResumenReservaScreen(
                viewModel = viewModel,
                onBack = { pantallaActual = "detalle_servicio" },
                onConfirmarReserva = { pantallaActual = "todas_citas" },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        "favoritos" -> {
            FavoritosScreen(
                viewModel = viewModel,
                onBack = { pantallaActual = "home" },
                onAgendarCita = { pantallaActual = "detalle_servicio" },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        "perfil" -> {
            PerfilScreen(
                viewModel = viewModel,
                onBack = { pantallaActual = "home" },
                onNavigateToPets = { pantallaActual = "carnet" },
                onNavigateToHelp = { pantallaActual = "faq" },
                onLogout = { pantallaActual = "welcome" },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        "faq" -> {
            FaqScreen(
                onBack = { pantallaActual = "perfil" },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        "todas_citas" -> {
            TodasCitasScreen(
                viewModel = viewModel,
                onBack = { pantallaActual = "home" },
                onCancelarCita = { pantallaActual = "cancelar_cita" },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        "cancelar_cita" -> {
            CancelarCitaScreen(
                viewModel = viewModel,
                onBack = { pantallaActual = "todas_citas" },
                onCitaCanceladaSuccess = { pantallaActual = "todas_citas" },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        "carnet" -> {
            CarnetVacunasScreen(
                viewModel = viewModel,
                onBack = { pantallaActual = "home" },
                onAgregarMascota = { pantallaActual = "agregar_mascota" },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        "agregar_mascota" -> {
            AgregarMascotaScreen(
                viewModel = viewModel,
                onBack = { pantallaActual = "carnet" },
                onNavigateToCarnet = { pantallaActual = "carnet" },
                onNavigateToTab = { navigateToTab(it) }
            )
        }
    }
}
