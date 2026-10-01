package com.example.petshield.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object OnboardingVeterinarian : Screen("onboarding_veterinarian")
    object OnboardingAppointments : Screen("onboarding_appointments")
    object OnboardingHealthRecord : Screen("onboarding_health_record")
    object Login : Screen("login")
    object Register : Screen("register")
    object RecoverPassword : Screen("recover_password")
    object Home : Screen("home")
    object Pets : Screen("pets")
    object Appointments : Screen("appointments")
    object Clinics : Screen("clinics")
    object Profile : Screen("profile")
    object ClinicFilters : Screen("clinic_filters")
    object ClinicDetails : Screen("clinic_details/{clinicId}") {
        fun createRoute(clinicId: Long) = "clinic_details/$clinicId"
    }
    object FavoriteVeterinarians : Screen("favorite_veterinarians")
    object EditProfile : Screen("edit_profile")
    object Settings : Screen("settings")
    object NotificationSettings : Screen("notification_settings")
    object PasswordManagement : Screen("password_management")
    object PrivacyPolicy : Screen("privacy_policy")
    object HelpFaq : Screen("help_faq")
    object HelpContact : Screen("help_contact")
    object Notifications : Screen("notifications")
    object UpcomingAppointments : Screen("upcoming_appointments")
    object CancelledAppointments : Screen("cancelled_appointments")
    object AppointmentDetails : Screen("appointment_details/{appointmentId}") {
        fun createRoute(appointmentId: Long) = "appointment_details/$appointmentId"
    }
    object CancelAppointment : Screen("cancel_appointment/{appointmentId}") {
        fun createRoute(appointmentId: Long) = "cancel_appointment/$appointmentId"
    }
    object AppointmentReview : Screen("appointment_review/{appointmentId}") {
        fun createRoute(appointmentId: Long) = "appointment_review/$appointmentId"
    }
    object VaccineCardEmpty : Screen("vaccine_card_empty")
    object VaccineCardEmptyAlternate : Screen("vaccine_card_empty_alternate")
    object AddPet : Screen("add_pet")
    object HealthRecordMenu : Screen("health_record_menu")
    object Allergies : Screen("allergies")
    object ClinicalTests : Screen("clinical_tests")
    object ClinicalTestDetails : Screen("clinical_test_details/{recordId}") {
        fun createRoute(recordId: Long) = "clinical_test_details/$recordId"
    }
    object AppliedVaccines : Screen("applied_vaccines")
    object VeterinaryHistory : Screen("veterinary_history")
}