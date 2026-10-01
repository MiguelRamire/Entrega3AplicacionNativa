package com.example.petshield.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.petshield.domain.model.Appointment
import com.example.petshield.domain.model.AppointmentStatus
import com.example.petshield.domain.model.Clinic
import com.example.petshield.domain.model.NotificationPreferences
import com.example.petshield.domain.model.OwnerAccount
import com.example.petshield.domain.model.Pet
import com.example.petshield.domain.model.PetHealthRecord
import com.example.petshield.domain.model.VaccineRecord
import com.example.petshield.domain.repository.PetShieldRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PetShieldUiState(
    val isLoading: Boolean = true,
    val pets: List<Pet> = emptyList(),
    val vaccines: List<VaccineRecord> = emptyList(),
    val appointments: List<Appointment> = emptyList(),
    val clinics: List<Clinic> = emptyList(),
    val healthRecords: List<PetHealthRecord> = emptyList(),
    val ownerAccount: OwnerAccount? = null,
    val notificationPreferences: NotificationPreferences = NotificationPreferences(),
    val isAuthenticated: Boolean = false,
    val isBusy: Boolean = false,
    val authMessage: String? = null,
    val actionMessage: String? = null,
    val clinicQuery: String = "",
    val clinicMinimumRating: Double = 0.0,
    val errorMessage: String? = null
)

class PetShieldViewModel(
    private val repository: PetShieldRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PetShieldUiState())
    val uiState: StateFlow<PetShieldUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            runCatching {
                val pets = repository.getPets()
                val vaccines = pets.flatMap { repository.getVaccines(it.id) }
                val appointments = repository.getAppointments()
                val clinics = repository.getClinics()
                val healthRecords = repository.getHealthRecords()
                val ownerAccount = repository.getOwnerAccount()
                val notificationPreferences = repository.getNotificationPreferences()
                val currentState = _uiState.value
                PetShieldUiState(
                    isLoading = false,
                    pets = pets,
                    vaccines = vaccines,
                    appointments = appointments,
                    clinics = clinics,
                    healthRecords = healthRecords,
                    ownerAccount = ownerAccount,
                    notificationPreferences = notificationPreferences,
                    isAuthenticated = currentState.isAuthenticated,
                    isBusy = false,
                    authMessage = currentState.authMessage,
                    actionMessage = currentState.actionMessage,
                    clinicQuery = currentState.clinicQuery,
                    clinicMinimumRating = currentState.clinicMinimumRating
                )
            }.onSuccess { _uiState.value = it }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudieron cargar tus datos. Inténtalo de nuevo."
                    )
                }
        }
    }

    fun addPet(name: String, species: String, breed: String, ageYears: Int, weightKg: Double) {
        if (name.isBlank() || ageYears < 0 || weightKg < 0) return
        mutate {
            repository.savePet(
                Pet(
                    name = name.trim(),
                    species = species,
                    breed = breed.trim().ifBlank { "Sin especificar" },
                    ageYears = ageYears,
                    weightKg = weightKg
                )
            )
        }
    }

    fun registerAccount(name: String, email: String, password: String) {
        if (name.isBlank() || !email.contains('@') || password.length < 8) {
            _uiState.value = _uiState.value.copy(
                authMessage = "Ingresa un nombre, correo válido y contraseña de 8 caracteres o más."
            )
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isBusy = true, authMessage = null)
            runCatching {
                repository.registerOwnerAccount(OwnerAccount(name = name, email = email), password)
                repository.getOwnerAccount()
            }.onSuccess { account ->
                _uiState.value = _uiState.value.copy(
                    ownerAccount = account,
                    isAuthenticated = true,
                    isBusy = false,
                    authMessage = null
                )
                refresh()
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isBusy = false,
                    authMessage = "No se pudo crear la cuenta. Revisa el correo o intenta iniciar sesión."
                )
            }
        }
    }

    fun login(email: String, password: String) {
        if (!email.contains('@') || password.isBlank()) {
            _uiState.value = _uiState.value.copy(authMessage = "Ingresa tu correo y contraseña.")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isBusy = true, authMessage = null)
            runCatching { repository.authenticate(email, password) }
                .onSuccess { authenticated ->
                    if (authenticated) {
                        _uiState.value = _uiState.value.copy(
                            ownerAccount = repository.getOwnerAccount(),
                            isAuthenticated = true,
                            isBusy = false,
                            authMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isBusy = false,
                            authMessage = "Correo o contraseña incorrectos."
                        )
                    }
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isBusy = false,
                        authMessage = "No se pudo validar la cuenta. Inténtalo de nuevo."
                    )
                }
        }
    }

    fun logout() {
        _uiState.value = _uiState.value.copy(isAuthenticated = false, authMessage = null)
    }

    fun updateOwnerAccount(name: String, email: String, phone: String) {
        if (name.isBlank() || !email.contains('@')) {
            _uiState.value = _uiState.value.copy(actionMessage = "Completa un nombre y correo válidos.")
            return
        }
        mutate(
            successMessage = "Perfil guardado en este dispositivo."
        ) {
            repository.updateOwnerAccount(OwnerAccount(name = name, email = email, phone = phone))
        }
    }

    fun changePassword(current: String, next: String, confirmation: String) {
        if (next.length < 8 || next != confirmation) {
            _uiState.value = _uiState.value.copy(
                actionMessage = "La nueva contraseña debe tener 8 caracteres y coincidir con la confirmación."
            )
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isBusy = true, actionMessage = null)
            runCatching { repository.changePassword(current, next) }
                .onSuccess { changed ->
                    _uiState.value = _uiState.value.copy(
                        isBusy = false,
                        actionMessage = if (changed) "Contraseña actualizada." else "La contraseña actual no es correcta."
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isBusy = false, actionMessage = "No se pudo cambiar la contraseña.")
                }
        }
    }

    fun saveNotificationPreferences(preferences: NotificationPreferences) {
        mutate { repository.saveNotificationPreferences(preferences) }
    }

    fun submitSupportRequest(subject: String, message: String) {
        if (subject.isBlank() || message.isBlank()) {
            _uiState.value = _uiState.value.copy(actionMessage = "Completa el asunto y el mensaje.")
            return
        }
        viewModelScope.launch {
            runCatching { repository.saveSupportRequest(subject, message, _uiState.value.ownerAccount?.email.orEmpty()) }
                .onSuccess {
                    _uiState.value = _uiState.value.copy(actionMessage = "Consulta guardada para soporte en este dispositivo.")
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(actionMessage = "No se pudo guardar la consulta.")
                }
        }
    }

    fun requestPasswordRecovery(email: String) {
        if (!email.contains('@')) {
            _uiState.value = _uiState.value.copy(actionMessage = "Ingresa un correo válido.")
            return
        }
        viewModelScope.launch {
            runCatching { repository.requestPasswordRecovery(email) }
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        actionMessage = "Solicitud guardada localmente. El envío de correo requiere un servicio remoto."
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(actionMessage = "No se pudo guardar la solicitud.")
                }
        }
    }

    fun clearActionMessage() {
        _uiState.value = _uiState.value.copy(actionMessage = null)
    }

    fun clearAuthMessage() {
        _uiState.value = _uiState.value.copy(authMessage = null)
    }

    fun deletePet(petId: Long) {
        mutate { repository.deletePet(petId) }
    }

    fun addAppointment(appointment: Appointment) {
        mutate { repository.saveAppointment(appointment) }
    }

    fun toggleClinicFavorite(clinicId: Long) {
        mutate { repository.toggleClinicFavorite(clinicId) }
    }

    fun addHealthRecord(record: PetHealthRecord) {
        mutate { repository.saveHealthRecord(record) }
    }

    fun deleteHealthRecord(recordId: Long) {
        mutate { repository.deleteHealthRecord(recordId) }
    }

    fun submitAppointmentFeedback(appointmentId: Long, rating: Int, comment: String) {
        if (rating !in 1..5) return
        mutate { repository.saveAppointmentFeedback(appointmentId, rating, comment.trim()) }
    }

    fun setClinicFilters(query: String, minimumRating: Double) {
        _uiState.value = _uiState.value.copy(
            clinicQuery = query,
            clinicMinimumRating = minimumRating
        )
    }

    fun cancelAppointment(appointmentId: Long) {
        mutate {
            repository.updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELLED)
        }
    }

    fun addVaccine(vaccine: VaccineRecord) {
        mutate { repository.saveVaccine(vaccine) }
    }

    private fun mutate(
        successMessage: String? = null,
        action: suspend () -> Any?
    ) {
        viewModelScope.launch {
            runCatching { action() }
                .onSuccess {
                    if (successMessage != null) {
                        _uiState.value = _uiState.value.copy(actionMessage = successMessage)
                    }
                    refresh()
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isBusy = false,
                        actionMessage = "No se pudo guardar el cambio.",
                        errorMessage = "No se pudo guardar el cambio. Inténtalo de nuevo."
                    )
                }
        }
    }

    companion object {
        fun factory(repository: PetShieldRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    PetShieldViewModel(repository) as T
            }
    }
}