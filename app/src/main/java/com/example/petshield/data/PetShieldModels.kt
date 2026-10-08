package com.example.petshield.data

import com.google.firebase.firestore.DocumentId

// ==========================================
// MODELOS DE INTERFAZ DE USUARIO (UI)
// ==========================================

data class Mascota(
    val id: String,
    val nombre: String,
    val especie: String,
    val raza: String,
    val peso: String,
    val fechaNacimiento: String,
    val sexo: String,
    val vacunas: List<String> = listOf("Rabia (Completada)", "Triple Viral (Completada)", "Refuerzo Anual (Pendiente)")
)

data class Clinica(
    val id: String,
    val nombre: String,
    val direccion: String,
    val horario: String,
    val calificacion: Float = 4.9f,
    val esRecomendada: Boolean = true,
    var esFavorita: Boolean = true
)

data class Veterinario(
    val id: String,
    val clinicaId: String = "",
    val nombre: String,
    val especialidad: String,
    val clinicaTag: String,
    var esFavorito: Boolean = true
)

data class Cita(
    val id: String,
    val vetNombre: String,
    val servicio: String,
    val fechaStr: String,
    val horaStr: String,
    val esProxima: Boolean = true,
    val mascotaNombre: String = "Max",
    val modalidad: String = "En clínica",
    val lugar: String = "Clínica PetCare · Poblado"
)

data class DatosReserva(
    val clinicaId: String = "",
    val servicioId: String = "",
    val mascotaId: String = "",
    val mascotaNombre: String = "Max · Golden Retriever",
    val servicioNombre: String = "Consulta veterinaria general",
    val profesionalNombre: String = "Dra. Ana Jiménez, MV",
    val fechaStr: String = "Miércoles, 23 de septiembre",
    val horaStr: String = "3:00 PM",
    val modalidad: String = "En clínica",
    val lugar: String = "PetCare Clínica Veterinaria",
    val precioConsulta: String = "$65.000",
    val tarifaServicio: String = "$3.500",
    val total: String = "$68.500"
)

// ==========================================
// MODELOS FIRESTORE
// ==========================================

/**
 * Modelo para la colección "usuarios" en Firestore.
 */
data class UsuarioModel(
    @DocumentId val uid: String = "",
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val fotoUrl: String = "",
    val fechaRegistro: Long = System.currentTimeMillis()
)

/**
 * Modelo para la colección "mascotas" en Firestore.
 */
data class MascotaModel(
    @DocumentId val id: String = "",
    val usuarioId: String = "",
    val nombre: String = "",
    val especie: String = "",
    val raza: String = "",
    val peso: String = "",
    val fechaNacimiento: String = "",
    val sexo: String = "",
    val fotoUrl: String = ""
)

/**
 * Modelo para la colección "vacunas" en Firestore (Carnet de Vacunación).
 */
data class VacunaModel(
    @DocumentId val id: String = "",
    val mascotaId: String = "",
    val nombreVacuna: String = "",
    val fechaAplicacion: String = "",
    val proximaDosis: String = "",
    val veterinario: String = "",
    val lote: String = ""
)

/**
 * Modelo para la colección "clinicas" en Firestore.
 */
data class ClinicaModel(
    @DocumentId val id: String = "",
    val nombre: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val calificacion: Double = 5.0,
    val imagenUrl: String = "",
    val horario: String = "8:00 AM - 8:00 PM",
    val esFavorito: Boolean = false
)

/**
 * Modelo para la colección "servicios" en Firestore.
 */
data class ServicioModel(
    @DocumentId val id: String = "",
    val clinicaId: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val duracionMinutos: Int = 30,
    val imagenUrl: String = ""
)

/**
 * Modelo para la colección "veterinarios" en Firestore.
 */
data class VeterinarioModel(
    @DocumentId val id: String = "",
    val clinicaId: String = "",
    val nombre: String = "",
    val especialidad: String = "",
    val clinicaTag: String = "",
    val esFavorito: Boolean = false
)

/**
 * Modelo para la colección "citas" en Firestore.
 */
data class CitaModel(
    @DocumentId val id: String = "",
    val usuarioId: String = "",
    val mascotaId: String = "",
    val nombreMascota: String = "",
    val clinicaId: String = "",
    val nombreClinica: String = "",
    val vetNombre: String = "",
    val servicioId: String = "",
    val nombreServicio: String = "",
    val fecha: String = "",
    val hora: String = "",
    val precio: Double = 0.0,
    val estado: String = "Pendiente",
    val motivoCancelacion: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)

/**
 * Modelo para la colección "favoritos" en Firestore.
 */
data class FavoritoModel(
    @DocumentId val id: String = "",
    val usuarioId: String = "",
    val clinicaId: String = "",
    val vetId: String = "",
    val tipo: String = "clinica",
    val fechaAgregado: Long = System.currentTimeMillis()
)

/**
 * Modelo para la colección "faqs" en Firestore.
 */
data class FaqModel(
    @DocumentId val id: String = "",
    val pregunta: String = "",
    val respuesta: String = "",
    val categoria: String = "General"
)
