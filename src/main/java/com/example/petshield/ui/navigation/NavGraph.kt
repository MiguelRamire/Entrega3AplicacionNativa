package com.example.petshield.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.petshield.ui.screens.appointments.AppointmentDetailsScreen
import com.example.petshield.ui.screens.appointments.AppointmentReviewScreen
import com.example.petshield.ui.screens.appointments.AppointmentsScreen
import com.example.petshield.ui.screens.appointments.CancelAppointmentScreen
import com.example.petshield.ui.screens.appointments.CancelledAppointmentsScreen
import com.example.petshield.ui.screens.auth.OnboardingScreen
import com.example.petshield.ui.screens.auth.RecoverPasswordScreen
import com.example.petshield.ui.screens.auth.RegisterScreen
import com.example.petshield.ui.screens.auth.SplashScreen
import com.example.petshield.ui.screens.auth.WelcomeScreen
import com.example.petshield.ui.screens.clinics.ClinicBookingDialog
import com.example.petshield.ui.screens.clinics.ClinicDetailsScreen
import com.example.petshield.ui.screens.clinics.ClinicFiltersScreen
import com.example.petshield.ui.screens.clinics.FavoriteVeterinariansScreen
import com.example.petshield.ui.screens.PetShieldUiState
import com.example.petshield.ui.screens.PetShieldViewModel
import com.example.petshield.ui.screens.home.NotificationsScreen
import com.example.petshield.ui.screens.auth.LoginScreen
import com.example.petshield.ui.screens.clinics.ClinicsScreen
import com.example.petshield.ui.screens.home.HomeScreen
import com.example.petshield.ui.screens.pets.AddPetScreen
import com.example.petshield.ui.screens.pets.AppliedVaccinesScreen
import com.example.petshield.ui.screens.pets.ClinicalTestDetailsScreen
import com.example.petshield.ui.screens.pets.HealthRecordMenuScreen
import com.example.petshield.ui.screens.pets.HealthRecordsScreen
import com.example.petshield.ui.screens.pets.PetsScreen
import com.example.petshield.ui.screens.pets.VaccineCardScreen
import com.example.petshield.ui.screens.pets.VeterinaryHistoryScreen
import com.example.petshield.ui.screens.profile.AccountFlowScreen
import com.example.petshield.ui.screens.profile.ProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    state: PetShieldUiState,
    viewModel: PetShieldViewModel,
    modifier: Modifier = Modifier
) {
    fun openHome() {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) openHome()
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen { navController.navigate(Screen.Welcome.route) }
        }
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onStart = { navController.navigate(Screen.OnboardingVeterinarian.route) },
                onLogin = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.OnboardingVeterinarian.route) {
            OnboardingScreen(
                page = 0,
                onContinue = { navController.navigate(Screen.OnboardingAppointments.route) },
                onSkip = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.OnboardingAppointments.route) {
            OnboardingScreen(
                page = 1,
                onContinue = { navController.navigate(Screen.OnboardingHealthRecord.route) },
                onSkip = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.OnboardingHealthRecord.route) {
            OnboardingScreen(
                page = 2,
                onContinue = { navController.navigate(Screen.Login.route) },
                onSkip = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = viewModel::login,
                onRegister = { navController.navigate(Screen.Register.route) },
                onRecoverPassword = { navController.navigate(Screen.RecoverPassword.route) },
                message = state.authMessage,
                isBusy = state.isBusy,
                onClearMessage = viewModel::clearAuthMessage
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegister = viewModel::registerAccount,
                onLogin = { navController.popBackStack() },
                message = state.authMessage,
                isBusy = state.isBusy,
                onClearMessage = viewModel::clearAuthMessage
            )
        }
        composable(Screen.RecoverPassword.route) {
            LaunchedEffect(Unit) { viewModel.clearActionMessage() }
            RecoverPasswordScreen(
                onRequest = viewModel::requestPasswordRecovery,
                onBackToLogin = { navController.popBackStack() },
                message = state.actionMessage
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(state = state, onNavigate = navController::navigate)
        }
        composable(Screen.Pets.route) {
            PetsScreen(
                state = state,
                onAddPet = viewModel::addPet,
                onDeletePet = viewModel::deletePet,
                onAddVaccine = viewModel::addVaccine,
                onNavigate = navController::navigate
            )
        }
        composable(Screen.Appointments.route) {
            AppointmentsScreen(state = state, onNavigate = navController::navigate)
        }
        composable(Screen.UpcomingAppointments.route) {
            AppointmentsScreen(state = state, onNavigate = navController::navigate)
        }
        composable(Screen.CancelledAppointments.route) {
            CancelledAppointmentsScreen(
                appointments = state.appointments,
                petNames = state.pets.associate { it.id to it.name },
                onSelect = { navController.navigate(Screen.AppointmentDetails.createRoute(it)) }
            )
        }
        composable(Screen.Clinics.route) {
            ClinicsScreen(
                state = state,
                onBookAppointment = viewModel::addAppointment,
                onNavigate = navController::navigate,
                onToggleFavorite = viewModel::toggleClinicFavorite
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                state = state,
                onNavigate = navController::navigate,
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.ClinicFilters.route) {
            ClinicFiltersScreen(
                initialQuery = state.clinicQuery,
                initialMinimumRating = state.clinicMinimumRating,
                onApply = { query, rating ->
                    viewModel.setClinicFilters(query, rating)
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.ClinicDetails.route, arguments = listOf(navArgument("clinicId") { type = NavType.LongType })) { entry ->
            val clinicId = entry.arguments?.getLong("clinicId")
            val clinic = state.clinics.firstOrNull { it.id == clinicId }
            var showBooking by rememberSaveable(clinicId) { mutableStateOf(false) }
            ClinicDetailsScreen(
                clinic = clinic,
                onBack = { navController.popBackStack() },
                onBook = { showBooking = true },
                onToggleFavorite = { clinic?.let { viewModel.toggleClinicFavorite(it.id) } }
            )
            if (showBooking && clinic != null) {
                ClinicBookingDialog(
                    clinic = clinic,
                    pets = state.pets,
                    onDismiss = { showBooking = false },
                    onConfirm = { appointment ->
                        viewModel.addAppointment(appointment)
                        showBooking = false
                    }
                )
            }
        }
        composable(Screen.FavoriteVeterinarians.route) {
            FavoriteVeterinariansScreen(state.clinics.filter { it.isFavorite }) {
                navController.navigate(Screen.ClinicDetails.createRoute(it))
            }
        }
        composable(Screen.EditProfile.route) {
            AccountFlowDestination(Screen.EditProfile, state, viewModel, navController::navigate)
        }
        composable(Screen.Settings.route) {
            AccountFlowDestination(Screen.Settings, state, viewModel, navController::navigate)
        }
        composable(Screen.NotificationSettings.route) {
            AccountFlowDestination(Screen.NotificationSettings, state, viewModel, navController::navigate)
        }
        composable(Screen.PasswordManagement.route) {
            AccountFlowDestination(Screen.PasswordManagement, state, viewModel, navController::navigate)
        }
        composable(Screen.PrivacyPolicy.route) {
            AccountFlowDestination(Screen.PrivacyPolicy, state, viewModel, navController::navigate)
        }
        composable(Screen.HelpFaq.route) {
            AccountFlowDestination(Screen.HelpFaq, state, viewModel, navController::navigate)
        }
        composable(Screen.HelpContact.route) {
            AccountFlowDestination(Screen.HelpContact, state, viewModel, navController::navigate)
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(state = state, onNavigate = navController::navigate)
        }
        composable(
            Screen.AppointmentDetails.route,
            arguments = listOf(navArgument("appointmentId") { type = NavType.LongType })
        ) { entry ->
            val appointmentId = entry.arguments?.getLong("appointmentId")
            val appointment = state.appointments.firstOrNull { it.id == appointmentId }
            AppointmentDetailsScreen(
                appointment = appointment,
                petName = state.pets.firstOrNull { it.id == appointment?.petId }?.name ?: "Mascota",
                onCancel = { appointment?.let { navController.navigate(Screen.CancelAppointment.createRoute(it.id)) } },
                onReview = { appointment?.let { navController.navigate(Screen.AppointmentReview.createRoute(it.id)) } },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            Screen.CancelAppointment.route,
            arguments = listOf(navArgument("appointmentId") { type = NavType.LongType })
        ) { entry ->
            val appointmentId = entry.arguments?.getLong("appointmentId")
            val appointment = state.appointments.firstOrNull { it.id == appointmentId }
            CancelAppointmentScreen(
                appointment = appointment,
                onConfirm = {
                    appointmentId?.let(viewModel::cancelAppointment)
                    navController.navigate(Screen.CancelledAppointments.route) {
                        popUpTo(Screen.Appointments.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onKeep = { navController.popBackStack() }
            )
        }
        composable(
            Screen.AppointmentReview.route,
            arguments = listOf(navArgument("appointmentId") { type = NavType.LongType })
        ) { entry ->
            val appointmentId = entry.arguments?.getLong("appointmentId")
            AppointmentReviewScreen(
                appointment = state.appointments.firstOrNull { it.id == appointmentId },
                onSubmit = { rating, comment ->
                    appointmentId?.let { viewModel.submitAppointmentFeedback(it, rating, comment) }
                }
            )
        }
        composable(Screen.VaccineCardEmpty.route) {
            VaccineCardScreen(state, alternate = false, onNavigate = navController::navigate)
        }
        composable(Screen.VaccineCardEmptyAlternate.route) {
            VaccineCardScreen(state, alternate = true, onNavigate = navController::navigate)
        }
        composable(Screen.AddPet.route) {
            AddPetScreen(
                onSave = viewModel::addPet,
                onDone = {
                    navController.navigate(Screen.Pets.route) { launchSingleTop = true }
                }
            )
        }
        composable(Screen.HealthRecordMenu.route) { HealthRecordMenuScreen(navController::navigate) }
        composable(Screen.Allergies.route) {
            HealthRecordsScreen(
                state,
                "ALLERGY",
                "Alergias y reacciones",
                viewModel::addHealthRecord,
                { navController.navigate(Screen.ClinicalTestDetails.createRoute(it)) },
                navController::navigate
            )
        }
        composable(Screen.ClinicalTests.route) {
            HealthRecordsScreen(
                state,
                "LAB",
                "Análisis clínicos",
                viewModel::addHealthRecord,
                { navController.navigate(Screen.ClinicalTestDetails.createRoute(it)) },
                navController::navigate
            )
        }
        composable(
            Screen.ClinicalTestDetails.route,
            arguments = listOf(navArgument("recordId") { type = NavType.LongType })
        ) { entry ->
            val recordId = entry.arguments?.getLong("recordId")
            ClinicalTestDetailsScreen(
                record = state.healthRecords.firstOrNull { it.id == recordId },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.AppliedVaccines.route) {
            AppliedVaccinesScreen(state, navController::navigate)
        }
        composable(Screen.VeterinaryHistory.route) {
            VeterinaryHistoryScreen(state) {
                navController.navigate(Screen.AppointmentDetails.createRoute(it))
            }
        }
    }
}

@Composable
private fun AccountFlowDestination(
    screen: Screen,
    state: PetShieldUiState,
    viewModel: PetShieldViewModel,
    onNavigate: (String) -> Unit
) {
    AccountFlowScreen(
        screen = screen,
        state = state,
        onNavigate = onNavigate,
        onSaveProfile = viewModel::updateOwnerAccount,
        onSaveNotificationPreferences = viewModel::saveNotificationPreferences,
        onChangePassword = viewModel::changePassword,
        onSubmitSupport = viewModel::submitSupportRequest,
        onClearMessage = viewModel::clearActionMessage
    )
}