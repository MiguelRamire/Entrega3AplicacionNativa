package com.example.petshield.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.petshield.di.AppContainer
import com.example.petshield.ui.navigation.NavGraph
import com.example.petshield.ui.navigation.Screen
import com.example.petshield.ui.screens.PetShieldViewModel

@Composable
fun PetShieldApp(container: AppContainer) {
    val navController = rememberNavController()
    val viewModel: PetShieldViewModel = viewModel(
        factory = PetShieldViewModel.factory(container.petShieldRepository)
    )
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val backStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { snackbarHostState.showSnackbar(it) }
    }
    val destinations = listOf(
        Triple(Screen.Home.route, "Inicio", Icons.Default.Home),
        Triple(Screen.Pets.route, "Mascotas", Icons.Default.Pets),
        Triple(Screen.Appointments.route, "Citas", Icons.Default.CalendarMonth),
        Triple(Screen.Clinics.route, "Clínicas", Icons.Default.LocalHospital),
        Triple(Screen.Profile.route, "Perfil", Icons.Default.Person)
    )
    val showNavigation = destinations.any { it.first == backStackEntry?.destination?.route }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showNavigation) {
                NavigationBar {
                    destinations.forEach { (route, label, icon) ->
                        val selected = backStackEntry?.destination?.hierarchy
                            ?.any { it.route == route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(Screen.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
    ) { contentPadding ->
        NavGraph(
            navController = navController,
            state = state,
            viewModel = viewModel,
            modifier = Modifier.padding(contentPadding)
        )
    }
}