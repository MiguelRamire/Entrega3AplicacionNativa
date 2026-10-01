package com.example.petshield.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.petshield.domain.model.Appointment
import com.example.petshield.domain.model.AppointmentStatus
import com.example.petshield.domain.model.Clinic
import com.example.petshield.domain.model.Pet
import com.example.petshield.domain.model.PetHealthRecord
import com.example.petshield.domain.model.NotificationPreferences
import com.example.petshield.domain.model.OwnerAccount
import com.example.petshield.domain.model.VaccineRecord

class PetShieldDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE pets (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                species TEXT NOT NULL,
                breed TEXT NOT NULL,
                age_years INTEGER NOT NULL,
                weight_kg REAL NOT NULL,
                photo_url TEXT
            )"""
        )
        db.execSQL(
            """CREATE TABLE vaccines (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                pet_id INTEGER NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
                name TEXT NOT NULL,
                administered_at TEXT NOT NULL,
                due_at TEXT NOT NULL,
                veterinarian TEXT NOT NULL
            )"""
        )
        db.execSQL(
            """CREATE TABLE appointments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                pet_id INTEGER NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
                clinic_name TEXT NOT NULL,
                service TEXT NOT NULL,
                date_time TEXT NOT NULL,
                status TEXT NOT NULL
            )"""
        )
        db.execSQL(
            """CREATE TABLE clinics (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                address TEXT NOT NULL,
                phone TEXT NOT NULL,
                rating REAL NOT NULL,
                is_favorite INTEGER NOT NULL DEFAULT 0
            )"""
        )
        createHealthRecordsTable(db)
        createAppointmentReviewsTable(db)
        createAccountTables(db)
        seedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE clinics ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0")
        }
        if (oldVersion < 3) createHealthRecordsTable(db)
        if (oldVersion < 4) createAppointmentReviewsTable(db)
        if (oldVersion < 5) createAccountTables(db)
        if (oldVersion == 5) {
            db.execSQL("ALTER TABLE support_requests ADD COLUMN email TEXT NOT NULL DEFAULT ''")
        }
    }

    fun getPets(): List<Pet> = readableDatabase.query(
        "pets", null, null, null, null, null, "name COLLATE NOCASE"
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(
                    Pet(
                        id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        species = cursor.getString(cursor.getColumnIndexOrThrow("species")),
                        breed = cursor.getString(cursor.getColumnIndexOrThrow("breed")),
                        ageYears = cursor.getInt(cursor.getColumnIndexOrThrow("age_years")),
                        weightKg = cursor.getDouble(cursor.getColumnIndexOrThrow("weight_kg")),
                        photoUrl = cursor.getString(cursor.getColumnIndexOrThrow("photo_url"))
                    )
                )
            }
        }
    }

    fun savePet(pet: Pet): Long {
        val values = ContentValues().apply {
            put("name", pet.name)
            put("species", pet.species)
            put("breed", pet.breed)
            put("age_years", pet.ageYears)
            put("weight_kg", pet.weightKg)
            put("photo_url", pet.photoUrl)
        }
        return if (pet.id == 0L) {
            writableDatabase.insertOrThrow("pets", null, values)
        } else {
            writableDatabase.update("pets", values, "id = ?", arrayOf(pet.id.toString()))
                .let { pet.id }
        }
    }

    fun deletePet(petId: Long) {
        writableDatabase.delete("pets", "id = ?", arrayOf(petId.toString()))
    }

    fun getVaccines(petId: Long): List<VaccineRecord> = readableDatabase.query(
        "vaccines", null, "pet_id = ?", arrayOf(petId.toString()), null, null, "due_at"
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(
                    VaccineRecord(
                        id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                        petId = cursor.getLong(cursor.getColumnIndexOrThrow("pet_id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        administeredAt = cursor.getString(cursor.getColumnIndexOrThrow("administered_at")),
                        dueAt = cursor.getString(cursor.getColumnIndexOrThrow("due_at")),
                        veterinarian = cursor.getString(cursor.getColumnIndexOrThrow("veterinarian"))
                    )
                )
            }
        }
    }

    fun saveVaccine(vaccine: VaccineRecord): Long {
        val values = ContentValues().apply {
            put("pet_id", vaccine.petId)
            put("name", vaccine.name)
            put("administered_at", vaccine.administeredAt)
            put("due_at", vaccine.dueAt)
            put("veterinarian", vaccine.veterinarian)
        }
        return writableDatabase.insertOrThrow("vaccines", null, values)
    }

    fun getAppointments(): List<Appointment> = readableDatabase.query(
        "appointments", null, null, null, null, null, "date_time"
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                val status = runCatching {
                    AppointmentStatus.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("status")))
                }.getOrDefault(AppointmentStatus.UPCOMING)
                add(
                    Appointment(
                        id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                        petId = cursor.getLong(cursor.getColumnIndexOrThrow("pet_id")),
                        clinicName = cursor.getString(cursor.getColumnIndexOrThrow("clinic_name")),
                        service = cursor.getString(cursor.getColumnIndexOrThrow("service")),
                        dateTime = cursor.getString(cursor.getColumnIndexOrThrow("date_time")),
                        status = status
                    )
                )
            }
        }
    }

    fun saveAppointment(appointment: Appointment): Long {
        val values = ContentValues().apply {
            put("pet_id", appointment.petId)
            put("clinic_name", appointment.clinicName)
            put("service", appointment.service)
            put("date_time", appointment.dateTime)
            put("status", appointment.status.name)
        }
        return writableDatabase.insertOrThrow("appointments", null, values)
    }

    fun updateAppointmentStatus(appointmentId: Long, status: String) {
        writableDatabase.update(
            "appointments",
            ContentValues().apply { put("status", status) },
            "id = ?",
            arrayOf(appointmentId.toString())
        )
    }

    fun getClinics(): List<Clinic> = readableDatabase.query(
        "clinics", null, null, null, null, null, "rating DESC"
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(
                    Clinic(
                        id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        address = cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        rating = cursor.getDouble(cursor.getColumnIndexOrThrow("rating")),
                        isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("is_favorite")) != 0
                    )
                )
            }
        }
    }

    fun toggleClinicFavorite(clinicId: Long) {
        val current = readableDatabase.query(
            "clinics", arrayOf("is_favorite"), "id = ?", arrayOf(clinicId.toString()), null, null, null
        ).use { cursor -> cursor.moveToFirst() && cursor.getInt(0) != 0 }
        writableDatabase.update(
            "clinics",
            ContentValues().apply { put("is_favorite", if (current) 0 else 1) },
            "id = ?",
            arrayOf(clinicId.toString())
        )
    }

    fun getHealthRecords(category: String? = null): List<PetHealthRecord> {
        val selection = category?.let { "category = ?" }
        val args = category?.let { arrayOf(it) }
        return readableDatabase.query(
            "health_records", null, selection, args, null, null, "date DESC"
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(
                        PetHealthRecord(
                            id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                            petId = cursor.getLong(cursor.getColumnIndexOrThrow("pet_id")),
                            category = cursor.getString(cursor.getColumnIndexOrThrow("category")),
                            title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                            date = cursor.getString(cursor.getColumnIndexOrThrow("date")),
                            details = cursor.getString(cursor.getColumnIndexOrThrow("details"))
                        )
                    )
                }
            }
        }
    }

    fun saveHealthRecord(record: PetHealthRecord): Long = writableDatabase.insertOrThrow(
        "health_records",
        null,
        ContentValues().apply {
            put("pet_id", record.petId)
            put("category", record.category)
            put("title", record.title)
            put("date", record.date)
            put("details", record.details)
        }
    )

    fun deleteHealthRecord(recordId: Long) {
        writableDatabase.delete("health_records", "id = ?", arrayOf(recordId.toString()))
    }

    fun saveAppointmentFeedback(appointmentId: Long, rating: Int, comment: String) {
        writableDatabase.insertOrThrow(
            "appointment_reviews",
            null,
            ContentValues().apply {
                put("appointment_id", appointmentId)
                put("rating", rating)
                put("comment", comment)
                put("created_at", System.currentTimeMillis())
            }
        )
    }

    fun getOwnerAccount(): OwnerAccount? = readableDatabase.query(
        "owner_account", arrayOf("name", "email", "phone"), "id = 1", null, null, null, null
    ).use { cursor ->
        if (!cursor.moveToFirst()) null else OwnerAccount(
            name = cursor.getString(0),
            email = cursor.getString(1),
            phone = cursor.getString(2)
        )
    }

    fun getAccountCredentials(email: String): Pair<String, String>? = readableDatabase.query(
        "owner_account",
        arrayOf("password_salt", "password_hash"),
        "email = ? COLLATE NOCASE",
        arrayOf(email.trim()),
        null,
        null,
        null
    ).use { cursor ->
        if (!cursor.moveToFirst()) null else cursor.getString(0) to cursor.getString(1)
    }

    fun createOwnerAccount(account: OwnerAccount, salt: String, passwordHash: String) {
        writableDatabase.insertOrThrow(
            "owner_account",
            null,
            ContentValues().apply {
                put("id", 1)
                put("name", account.name.trim())
                put("email", account.email.trim().lowercase())
                put("phone", account.phone.trim())
                put("password_salt", salt)
                put("password_hash", passwordHash)
            }
        )
    }

    fun updateOwnerAccount(account: OwnerAccount) {
        val rows = writableDatabase.update(
            "owner_account",
            ContentValues().apply {
                put("name", account.name.trim())
                put("email", account.email.trim().lowercase())
                put("phone", account.phone.trim())
            },
            "id = 1",
            null
        )
        check(rows == 1) { "No existe una cuenta local para actualizar" }
    }

    fun updateAccountPassword(salt: String, passwordHash: String) {
        val rows = writableDatabase.update(
            "owner_account",
            ContentValues().apply {
                put("password_salt", salt)
                put("password_hash", passwordHash)
            },
            "id = 1",
            null
        )
        check(rows == 1) { "No existe una cuenta local para cambiar la contraseña" }
    }

    fun getNotificationPreferences(): NotificationPreferences = readableDatabase.query(
        "notification_preferences",
        arrayOf("appointment_reminders", "health_reminders", "promotions"),
        "id = 1",
        null,
        null,
        null,
        null
    ).use { cursor ->
        if (!cursor.moveToFirst()) NotificationPreferences()
        else NotificationPreferences(
            appointmentReminders = cursor.getInt(0) != 0,
            healthReminders = cursor.getInt(1) != 0,
            promotions = cursor.getInt(2) != 0
        )
    }

    fun saveNotificationPreferences(preferences: NotificationPreferences) {
        writableDatabase.update(
            "notification_preferences",
            ContentValues().apply {
                put("appointment_reminders", preferences.appointmentReminders)
                put("health_reminders", preferences.healthReminders)
                put("promotions", preferences.promotions)
            },
            "id = 1",
            null
        )
    }

    fun saveSupportRequest(subject: String, message: String, email: String): Long = writableDatabase.insertOrThrow(
        "support_requests",
        null,
        ContentValues().apply {
            put("subject", subject.trim())
            put("message", message.trim())
            put("email", email.trim().lowercase())
            put("created_at", System.currentTimeMillis())
            put("status", "PENDING_LOCAL")
        }
    )

    fun savePasswordRecoveryRequest(email: String): Long = writableDatabase.insertOrThrow(
        "password_recovery_requests",
        null,
        ContentValues().apply {
            put("email", email.trim().lowercase())
            put("created_at", System.currentTimeMillis())
            put("status", "PENDING_REMOTE_DELIVERY")
        }
    )

    private fun createHealthRecordsTable(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE health_records (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                pet_id INTEGER NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
                category TEXT NOT NULL,
                title TEXT NOT NULL,
                date TEXT NOT NULL,
                details TEXT NOT NULL
            )"""
        )
    }

    private fun createAppointmentReviewsTable(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE appointment_reviews (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                appointment_id INTEGER NOT NULL REFERENCES appointments(id) ON DELETE CASCADE,
                rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
                comment TEXT NOT NULL,
                created_at INTEGER NOT NULL
            )"""
        )
    }

    private fun createAccountTables(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE owner_account (
                id INTEGER PRIMARY KEY CHECK (id = 1),
                name TEXT NOT NULL,
                email TEXT NOT NULL COLLATE NOCASE UNIQUE,
                phone TEXT NOT NULL DEFAULT '',
                password_salt TEXT NOT NULL,
                password_hash TEXT NOT NULL
            )"""
        )
        db.execSQL(
            """CREATE TABLE notification_preferences (
                id INTEGER PRIMARY KEY CHECK (id = 1),
                appointment_reminders INTEGER NOT NULL DEFAULT 1,
                health_reminders INTEGER NOT NULL DEFAULT 1,
                promotions INTEGER NOT NULL DEFAULT 0
            )"""
        )
        db.execSQL(
            """CREATE TABLE support_requests (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL DEFAULT '',
                subject TEXT NOT NULL,
                message TEXT NOT NULL,
                created_at INTEGER NOT NULL,
                status TEXT NOT NULL
            )"""
        )
        db.execSQL(
            """CREATE TABLE password_recovery_requests (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL,
                created_at INTEGER NOT NULL,
                status TEXT NOT NULL
            )"""
        )
        db.insertOrThrow(
            "notification_preferences",
            null,
            ContentValues().apply { put("id", 1) }
        )
    }

    private fun seedData(db: SQLiteDatabase) {
        val petId = db.insertOrThrow(
            "pets",
            null,
            ContentValues().apply {
                put("name", "Milo")
                put("species", "Perro")
                put("breed", "Golden Retriever")
                put("age_years", 3)
                put("weight_kg", 18.5)
            }
        )
        db.insertOrThrow(
            "vaccines",
            null,
            ContentValues().apply {
                put("pet_id", petId)
                put("name", "Rabia")
                put("administered_at", "2026-04-12")
                put("due_at", "2027-04-12")
                put("veterinarian", "Dra. Valeria Ruiz")
            }
        )
        db.insertOrThrow(
            "appointments",
            null,
            ContentValues().apply {
                put("pet_id", petId)
                put("clinic_name", "Clínica Huellitas")
                put("service", "Control anual")
                put("date_time", "2026-10-08T10:30:00")
                put("status", AppointmentStatus.UPCOMING.name)
            }
        )
        db.insertOrThrow(
            "appointments",
            null,
            ContentValues().apply {
                put("pet_id", petId)
                put("clinic_name", "Centro Veterinario Parque")
                put("service", "Vacunación anual")
                put("date_time", "2026-09-18T09:00:00")
                put("status", AppointmentStatus.COMPLETED.name)
            }
        )
        db.insertOrThrow(
            "clinics",
            null,
            ContentValues().apply {
                put("name", "Clínica Huellitas")
                put("address", "Av. Providencia 1234")
                put("phone", "+56 2 2345 6789")
                put("rating", 4.9)
            }
        )
        db.insertOrThrow(
            "health_records",
            null,
            ContentValues().apply {
                put("pet_id", petId)
                put("category", "ALLERGY")
                put("title", "Sensibilidad a pollo")
                put("date", "2026-05-14")
                put("details", "Reacción cutánea leve; evitar alimento con pollo.")
            }
        )
        db.insertOrThrow(
            "health_records",
            null,
            ContentValues().apply {
                put("pet_id", petId)
                put("category", "LAB")
                put("title", "Hemograma")
                put("date", "2026-06-03")
                put("details", "Resultados dentro de los rangos esperados.")
            }
        )
        db.insertOrThrow(
            "health_records",
            null,
            ContentValues().apply {
                put("pet_id", petId)
                put("category", "VISIT")
                put("title", "Control general")
                put("date", "2026-06-03")
                put("details", "Clínica Huellitas · Dra. Valeria Ruiz")
            }
        )
        db.insertOrThrow(
            "clinics",
            null,
            ContentValues().apply {
                put("name", "Centro Veterinario Parque")
                put("address", "Los Leones 456")
                put("phone", "+56 2 2987 6543")
                put("rating", 4.7)
            }
        )
    }

    companion object {
        private const val DATABASE_NAME = "pet_shield.db"
        private const val DATABASE_VERSION = 6
    }
}