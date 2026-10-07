package com.example.petshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

    fun navigateToTab(tab: BottomTab) {
        }
    }

            SplashScreen(
                }
            )
        }

            WelcomeScreen(
            )
        }

            LoginScreen(
                viewModel = viewModel,
            )
        }

            RecoverPasswordScreen(
            )
        }

            RegisterScreen(
                viewModel = viewModel,
            )
        }

            OnboardingScreen(
            )
        }

            HomeScreen(
                viewModel = viewModel,
                onNavigateToTab = { navigateToTab(it) }
            )
        }

            ClinicasScreen(
                viewModel = viewModel,
                    viewModel.reservaActual = viewModel.reservaActual.copy(
                        clinicaId = clinica.id,
                        lugar = clinica.nombre
                    )
                },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

            DetalleServicioScreen(
                viewModel = viewModel,
                onNavigateToTab = { navigateToTab(it) }
            )
        }

            ResumenReservaScreen(
                viewModel = viewModel,
                onNavigateToTab = { navigateToTab(it) }
            )
        }

            FavoritosScreen(
                viewModel = viewModel,
                onNavigateToTab = { navigateToTab(it) }
            )
        }

            PerfilScreen(
                viewModel = viewModel,
                onLogout = {
                    viewModel.cerrarSesion()
                },
                onNavigateToTab = { navigateToTab(it) }
            )
        }

            FaqScreen(
                onNavigateToTab = { navigateToTab(it) }
            )
        }

            TodasCitasScreen(
                viewModel = viewModel,
                onNavigateToTab = { navigateToTab(it) }
            )
        }

            CancelarCitaScreen(
                viewModel = viewModel,
                onNavigateToTab = { navigateToTab(it) }
            )
        }

            CarnetVacunasScreen(
                viewModel = viewModel,
                onNavigateToTab = { navigateToTab(it) }
            )
        }

            AgregarMascotaScreen(
                viewModel = viewModel,
                onNavigateToTab = { navigateToTab(it) }
            )
        }
    }
}
