package com.upb.petshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upb.petshield.data.Clinica
import com.upb.petshield.data.Veterinario
import com.upb.petshield.ui.theme.PetShieldTheme

sealed class Screen {
    object Splash : Screen()
    object Welcome : Screen()
    object Login : Screen()
    object Recover : Screen()
    data class Register(val fromLogin: Boolean = false) : Screen()
    object Onboarding : Screen()
    object Home : Screen()
    object Clinicas : Screen()
    object DetalleServicio : Screen()
    object ResumenReserva : Screen()
    object Favoritos : Screen()
    object Perfil : Screen()
    object Faq : Screen()
    object TodasCitas : Screen()
    object CancelarCita : Screen()
    object Carnet : Screen()
    object AgregarMascota : Screen()
}

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
fun PetShieldApp(
    viewModel: PetShieldViewModel = viewModel()
) {
    val backStack = remember { mutableStateListOf<Screen>(Screen.Splash) }
    val currentScreen = backStack.lastOrNull() ?: Screen.Splash

    fun navigateTo(screen: Screen) {
        backStack.add(screen)
    }

    fun popBack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    fun navigateAndClearTo(screen: Screen) {
        backStack.clear()
        backStack.add(screen)
    }

    fun navigateToTab(tab: BottomTab) {
        val targetScreen = when (tab) {
            BottomTab.HOME -> Screen.Home
            BottomTab.PROFILE -> Screen.Perfil
            BottomTab.CITAS -> Screen.TodasCitas
        }
        if (currentScreen != targetScreen) {
            backStack.removeAll { it !is Screen.Home }
            if (targetScreen !is Screen.Home) {
                backStack.add(targetScreen)
            }
        }
    }

    BackHandler(enabled = backStack.size > 1) {
        popBack()
    }

    when (val screen = currentScreen) {
        is Screen.Splash -> {
            SplashScreen(
                onNavigateToWelcome = {
                    navigateAndClearTo(Screen.Welcome)
                }
            )
        }

        is Screen.Welcome -> {
            WelcomeScreen(
                onNavigateToLogin = { navigateTo(Screen.Login) },
                onNavigateToRegister = { navigateTo(Screen.Register(fromLogin = false)) }
            )
        }

        is Screen.Login -> {
            LoginScreen(
                viewModel = viewModel,
                onBack = { popBack() },
                onNavigateToRegister = { navigateTo(Screen.Register(fromLogin = true)) },
                onNavigateToRecover = { navigateTo(Screen.Recover) },
                onLoginSuccess = { navigateAndClearTo(Screen.Onboarding) }
            )
        }

        is Screen.Recover -> {
            RecoverPasswordScreen(
                onBack = { popBack() },
                onSubmitEmail = { popBack() }
            )
        }

        is Screen.Register -> {
            RegisterScreen(
                viewModel = viewModel,
                onBack = { popBack() },
                onNavigateToLogin = { navigateTo(Screen.Login) }
            )
        }

        is Screen.Onboarding -> {
            OnboardingScreen(
                onFinishOnboarding = { navigateAndClearTo(Screen.Home) }
            )
        }

        is Screen.Home -> {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToFavoritos = { navigateTo(Screen.Favoritos) },
                onNavigateToClinicas = { navigateTo(Screen.Clinicas) },
                onNavigateToCarnet = { navigateTo(Screen.Carnet) },
                onNavigateToPerfil = { navigateTo(Screen.Perfil) },
                onNavigateToCitas = { navigateTo(Screen.TodasCitas) },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        is Screen.Clinicas -> {
            ClinicasScreen(
                viewModel = viewModel,
                onBack = { popBack() },
                onSelectClinica = { clinica: Clinica ->
                    viewModel.reservaActual = viewModel.reservaActual.copy(
                        clinicaId = clinica.id,
                        lugar = clinica.nombre
                    )
                    navigateTo(Screen.DetalleServicio)
                },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        is Screen.DetalleServicio -> {
            DetalleServicioScreen(
                viewModel = viewModel,
                onBack = { popBack() },
                onReservarServicio = { navigateTo(Screen.ResumenReserva) },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        is Screen.ResumenReserva -> {
            ResumenReservaScreen(
                viewModel = viewModel,
                onBack = { popBack() },
                onConfirmarReserva = { navigateTo(Screen.TodasCitas) },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        is Screen.Favoritos -> {
            FavoritosScreen(
                viewModel = viewModel,
                onBack = { popBack() },
                onAgendarCita = { vet: Veterinario ->
                    viewModel.reservaActual = viewModel.reservaActual.copy(
                        profesionalNombre = vet.nombre
                    )
                    navigateTo(Screen.DetalleServicio)
                },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        is Screen.Perfil -> {
            PerfilScreen(
                viewModel = viewModel,
                onBack = { popBack() },
                onNavigateToPets = { navigateTo(Screen.Carnet) },
                onNavigateToHelp = { navigateTo(Screen.Faq) },
                onLogout = {
                    viewModel.cerrarSesion()
                    navigateAndClearTo(Screen.Welcome)
                },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        is Screen.Faq -> {
            FaqScreen(
                onBack = { popBack() },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        is Screen.TodasCitas -> {
            TodasCitasScreen(
                viewModel = viewModel,
                onBack = { popBack() },
                onCancelarCita = { cita ->
                    viewModel.citaACancelar = cita
                    navigateTo(Screen.CancelarCita)
                },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        is Screen.CancelarCita -> {
            CancelarCitaScreen(
                viewModel = viewModel,
                onBack = { popBack() },
                onCitaCanceladaSuccess = {
                    popBack()
                },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        is Screen.Carnet -> {
            CarnetVacunasScreen(
                viewModel = viewModel,
                onBack = { popBack() },
                onAgregarMascota = { navigateTo(Screen.AgregarMascota) },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

        is Screen.AgregarMascota -> {
            AgregarMascotaScreen(
                viewModel = viewModel,
                onBack = { popBack() },
                onNavigateToCarnet = {
                    popBack()
                },
                onNavigateToTab = { navigateToTab(it) }
            )
        }
    }
}
