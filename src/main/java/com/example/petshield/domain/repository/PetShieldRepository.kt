package com.example.petshield.domain.repository

import com.example.petshield.domain.model.Appointment
import com.example.petshield.domain.model.AppointmentStatus
import com.example.petshield.domain.model.Clinic
import com.example.petshield.domain.model.Pet
import com.example.petshield.domain.model.PetHealthRecord
import com.example.petshield.domain.model.VaccineRecord
import com.example.petshield.domain.model.NotificationPreferences
import com.example.petshield.domain.model.OwnerAccount

interface PetShieldRepository {
    suspend fun getPets(): List<Pet>
    suspend fun savePet(pet: Pet): Long
    suspend fun deletePet(petId: Long)
    suspend fun getVaccines(petId: Long): List<VaccineRecord>
    suspend fun saveVaccine(vaccine: VaccineRecord): Long
    suspend fun getAppointments(): List<Appointment>
    suspend fun saveAppointment(appointment: Appointment): Long
    suspend fun updateAppointmentStatus(appointmentId: Long, status: AppointmentStatus)
    suspend fun getClinics(): List<Clinic>
    suspend fun toggleClinicFavorite(clinicId: Long)
    suspend fun getHealthRecords(category: String? = null): List<PetHealthRecord>
    suspend fun saveHealthRecord(record: PetHealthRecord): Long
    suspend fun deleteHealthRecord(recordId: Long)
    suspend fun saveAppointmentFeedback(appointmentId: Long, rating: Int, comment: String)
    suspend fun getOwnerAccount(): OwnerAccount?
    suspend fun registerOwnerAccount(account: OwnerAccount, password: String)
    suspend fun authenticate(email: String, password: String): Boolean
    suspend fun updateOwnerAccount(account: OwnerAccount)
    suspend fun changePassword(currentPassword: String, newPassword: String): Boolean
    suspend fun getNotificationPreferences(): NotificationPreferences
    suspend fun saveNotificationPreferences(preferences: NotificationPreferences)
    suspend fun saveSupportRequest(subject: String, message: String, email: String)
    suspend fun requestPasswordRecovery(email: String)
}