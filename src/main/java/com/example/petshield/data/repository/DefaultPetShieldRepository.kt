package com.example.petshield.data.repository

import com.example.petshield.data.local.PetShieldDatabase
import com.example.petshield.data.local.PasswordHasher
import com.example.petshield.domain.model.Appointment
import com.example.petshield.domain.model.AppointmentStatus
import com.example.petshield.domain.model.Clinic
import com.example.petshield.domain.model.NotificationPreferences
import com.example.petshield.domain.model.OwnerAccount
import com.example.petshield.domain.model.Pet
import com.example.petshield.domain.model.PetHealthRecord
import com.example.petshield.domain.model.VaccineRecord
import com.example.petshield.domain.repository.PetShieldRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultPetShieldRepository(
    private val database: PetShieldDatabase
) : PetShieldRepository {
    override suspend fun getPets(): List<Pet> = withContext(Dispatchers.IO) {
        database.getPets()
    }

    override suspend fun savePet(pet: Pet): Long = withContext(Dispatchers.IO) {
        database.savePet(pet)
    }

    override suspend fun deletePet(petId: Long) = withContext(Dispatchers.IO) {
        database.deletePet(petId)
    }

    override suspend fun getVaccines(petId: Long): List<VaccineRecord> = withContext(Dispatchers.IO) {
        database.getVaccines(petId)
    }

    override suspend fun saveVaccine(vaccine: VaccineRecord): Long = withContext(Dispatchers.IO) {
        database.saveVaccine(vaccine)
    }

    override suspend fun getAppointments(): List<Appointment> = withContext(Dispatchers.IO) {
        database.getAppointments()
    }

    override suspend fun saveAppointment(appointment: Appointment): Long = withContext(Dispatchers.IO) {
        database.saveAppointment(appointment)
    }

    override suspend fun updateAppointmentStatus(
        appointmentId: Long,
        status: AppointmentStatus
    ) = withContext(Dispatchers.IO) {
        database.updateAppointmentStatus(appointmentId, status.name)
    }

    override suspend fun getClinics(): List<Clinic> = withContext(Dispatchers.IO) {
        database.getClinics()
    }

    override suspend fun toggleClinicFavorite(clinicId: Long) = withContext(Dispatchers.IO) {
        database.toggleClinicFavorite(clinicId)
    }

    override suspend fun getHealthRecords(category: String?): List<PetHealthRecord> = withContext(Dispatchers.IO) {
        database.getHealthRecords(category)
    }

    override suspend fun saveHealthRecord(record: PetHealthRecord): Long = withContext(Dispatchers.IO) {
        database.saveHealthRecord(record)
    }

    override suspend fun deleteHealthRecord(recordId: Long) = withContext(Dispatchers.IO) {
        database.deleteHealthRecord(recordId)
    }

    override suspend fun saveAppointmentFeedback(appointmentId: Long, rating: Int, comment: String) =
        withContext(Dispatchers.IO) {
            database.saveAppointmentFeedback(appointmentId, rating, comment)
        }

    override suspend fun getOwnerAccount(): OwnerAccount? = withContext(Dispatchers.IO) {
        database.getOwnerAccount()
    }

    override suspend fun registerOwnerAccount(account: OwnerAccount, password: String) =
        withContext(Dispatchers.IO) {
            val digest = PasswordHasher.create(password)
            database.createOwnerAccount(account, digest.salt, digest.hash)
        }

    override suspend fun authenticate(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val credentials = database.getAccountCredentials(email) ?: return@withContext false
        PasswordHasher.verify(password, credentials.first, credentials.second)
    }

    override suspend fun updateOwnerAccount(account: OwnerAccount) = withContext(Dispatchers.IO) {
        database.updateOwnerAccount(account)
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Boolean =
        withContext(Dispatchers.IO) {
            val account = database.getOwnerAccount() ?: return@withContext false
            val credentials = database.getAccountCredentials(account.email) ?: return@withContext false
            if (!PasswordHasher.verify(currentPassword, credentials.first, credentials.second)) {
                return@withContext false
            }
            val updated = PasswordHasher.create(newPassword)
            database.updateAccountPassword(updated.salt, updated.hash)
            true
        }

    override suspend fun getNotificationPreferences(): NotificationPreferences = withContext(Dispatchers.IO) {
        database.getNotificationPreferences()
    }

    override suspend fun saveNotificationPreferences(preferences: NotificationPreferences) =
        withContext(Dispatchers.IO) {
            database.saveNotificationPreferences(preferences)
        }

    override suspend fun saveSupportRequest(subject: String, message: String, email: String) = withContext(Dispatchers.IO) {
        database.saveSupportRequest(subject, message, email)
        Unit
    }

    override suspend fun requestPasswordRecovery(email: String) = withContext(Dispatchers.IO) {
        database.savePasswordRecoveryRequest(email)
        Unit
    }
}