package com.example.petshield

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.petshield.data.CitaModel
import com.example.petshield.data.ClinicaModel
import com.example.petshield.data.FirebaseRepository
import com.example.petshield.data.MascotaModel
import com.example.petshield.data.VeterinarioModel

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

class PetShieldViewModel(
    private val repository: FirebaseRepository = FirebaseRepository()
) {
    // Usuario
    var usuarioId by mutableStateOf("usuario_demo")
    var usuarioNombre by mutableStateOf("Ada")
    var usuarioTelefono by mutableStateOf("+123 567 89000")
    var usuarioEmail by mutableStateOf("Janedoe@example.com")

    // Mascotas
    val mascotas = mutableStateListOf<Mascota>()

    // Clínicas
    val clinicas = mutableStateListOf<Clinica>()

    // Veterinarios Favoritos
    val veterinarios = mutableStateListOf<Veterinario>()

    // Citas
    val citas = mutableStateListOf<Cita>()

    // Datos de la reserva actual
    var reservaActual by mutableStateOf(DatosReserva())

    // Cita seleccionada para cancelar
    var citaACancelar by mutableStateOf<Cita?>(null)

    init {
        val currentUser = repository.obtenerUsuarioActual()
        if (currentUser != null) {
            usuarioId = currentUser.uid
            usuarioEmail = currentUser.email ?: ""
            cargarDatosUsuario(currentUser.uid)
        }
        cargarMascotasDesdeFirebase()
        cargarClinicasDesdeFirebase()
        cargarVeterinariosDesdeFirebase()
        cargarCitasDesdeFirebase()
    }

    fun esUsuarioAutenticado(): Boolean {
        return repository.obtenerUsuarioActual() != null
    }

    fun cargarDatosUsuario(uid: String) {
        repository.obtenerUsuario(uid) { perfil ->
            if (perfil != null) {
                usuarioNombre = perfil.nombre.ifBlank { usuarioNombre }
                usuarioTelefono = perfil.telefono.ifBlank { usuarioTelefono }
                usuarioEmail = perfil.correo.ifBlank { usuarioEmail }
            }
        }
    }

    fun iniciarSesion(
        email: String,
        pass: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (email.isBlank() || pass.isBlank()) {
            onResult(false, "Por favor ingresa tu correo y contraseña")
            return
        }
        repository.iniciarSesion(email, pass) { exito, error ->
            if (exito) {
                val currentUser = repository.obtenerUsuarioActual()
                if (currentUser != null) {
                    usuarioId = currentUser.uid
                    usuarioEmail = currentUser.email ?: ""
                    cargarDatosUsuario(currentUser.uid)
                    cargarMascotasDesdeFirebase()
                    cargarCitasDesdeFirebase()
                }
                onResult(true, null)
            } else {
                onResult(false, error ?: "Correo o contraseña incorrectos")
            }
        }
    }

    fun iniciarSesionConGoogle(
        idToken: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        repository.iniciarSesionConGoogleToken(idToken) { exito, error ->
            if (exito) {
                val currentUser = repository.obtenerUsuarioActual()
                if (currentUser != null) {
                    usuarioId = currentUser.uid
                    usuarioNombre = currentUser.displayName ?: usuarioNombre
                    usuarioEmail = currentUser.email ?: usuarioEmail
                    cargarDatosUsuario(currentUser.uid)
                    cargarMascotasDesdeFirebase()
                    cargarCitasDesdeFirebase()
                }
                onResult(true, null)
            } else {
                onResult(false, error ?: "Error al iniciar sesión con Google")
            }
        }
    }

    fun registrarUsuario(
        email: String,
        pass: String,
        nombre: String,
        telefono: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (email.isBlank() || pass.isBlank() || nombre.isBlank()) {
            onResult(false, "Por favor completa todos los campos obligatorios")
            return
        }
        if (pass.length < 6) {
            onResult(false, "La contraseña debe tener al menos 6 caracteres")
            return
        }
        repository.registrarUsuario(email, pass, nombre, telefono) { exito, error ->
            if (exito) {
                val currentUser = repository.obtenerUsuarioActual()
                if (currentUser != null) {
                    usuarioId = currentUser.uid
                    usuarioNombre = nombre
                    usuarioEmail = email
                    usuarioTelefono = telefono
                    cargarCitasDesdeFirebase()
                }
                onResult(true, null)
            } else {
                onResult(false, error ?: "Error al registrar el usuario")
            }
        }
    }

    fun cerrarSesion() {
        repository.cerrarSesion()
        usuarioId = "anonimo"
        usuarioNombre = ""
        usuarioEmail = ""
        usuarioTelefono = ""
        mascotas.clear()
        citas.clear()
    }

    fun cargarMascotasDesdeFirebase() {
        repository.obtenerMascotasPorUsuario(usuarioId) { listaFirebase ->
            if (listaFirebase.isNotEmpty()) {
                mascotas.clear()
                listaFirebase.forEach { item ->
                    mascotas.add(
                        Mascota(
                            id = item.id,
                            nombre = item.nombre,
                            especie = item.especie,
                            raza = item.raza,
                            peso = item.peso,
                            fechaNacimiento = item.fechaNacimiento,
                            sexo = item.sexo
                        )
                    )
                }
            }
        }
    }

    fun cargarClinicasDesdeFirebase() {
        repository.obtenerClinicas { listaFirebase ->
            if (listaFirebase.isNotEmpty()) {
                clinicas.clear()
                listaFirebase.forEach { item ->
                    clinicas.add(
                        Clinica(
                            id = item.id,
                            nombre = item.nombre,
                            direccion = item.direccion,
                            horario = item.horario,
                            calificacion = item.calificacion.toFloat(),
                            esRecomendada = true,
                            esFavorita = item.esFavorito
                        )
                    )
                }
            }
        }
    }

    fun cargarVeterinariosDesdeFirebase() {
        repository.obtenerVeterinarios { listaFirebase ->
            if (listaFirebase.isNotEmpty()) {
                veterinarios.clear()
                listaFirebase.forEach { item ->
                    veterinarios.add(
                        Veterinario(
                            id = item.id,
                            clinicaId = item.clinicaId,
                            nombre = item.nombre,
                            especialidad = item.especialidad,
                            clinicaTag = item.clinicaTag,
                            esFavorito = item.esFavorito
                        )
                    )
                }
            }
        }
    }

    fun cargarCitasDesdeFirebase() {
        if (usuarioId.isBlank() || usuarioId == "anonimo" || usuarioId == "usuario_demo") {
            citas.clear()
            return
        }
        repository.obtenerCitasPorUsuario(usuarioId) { listaFirebase ->
            citas.clear()
            if (listaFirebase.isNotEmpty()) {
                listaFirebase.forEach { item ->
                    val clinicaEncontrada = clinicas.firstOrNull { it.id == item.clinicaId }
                    val nombreClinicaMostrado = clinicaEncontrada?.nombre ?: item.nombreClinica.ifBlank { "Clínica Veterinaria" }
                    citas.add(
                        Cita(
                            id = item.id,
                            vetNombre = nombreClinicaMostrado,
                            servicio = item.nombreServicio.ifBlank { "Consulta General" },
                            fechaStr = item.fecha,
                            horaStr = item.hora,
                            esProxima = item.estado == "Confirmada" || item.estado == "Pendiente",
                            mascotaNombre = item.nombreMascota,
                            lugar = nombreClinicaMostrado
                        )
                    )
                }
            }
        }
    }

    fun agregarMascota(mascota: Mascota) {
        mascotas.add(mascota)

        // Guardar en Firebase Firestore
        val modeloFirestore = MascotaModel(
            usuarioId = usuarioId,
            nombre = mascota.nombre,
            especie = mascota.especie,
            raza = mascota.raza,
            peso = mascota.peso,
            fechaNacimiento = mascota.fechaNacimiento,
            sexo = mascota.sexo
        )
        repository.agregarMascota(modeloFirestore) { exito, error ->
            // Se guardó en Firestore
        }
    }

    fun toggleFavoritoVet(id: String) {
        val index = veterinarios.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = veterinarios[index]
            veterinarios[index] = item.copy(esFavorito = !item.esFavorito)
        }
    }

    fun toggleFavoritoClinica(id: String) {
        val index = clinicas.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = clinicas[index]
            clinicas[index] = item.copy(esFavorita = !item.esFavorita)
        }
    }

    fun confirmarReservaActual() {
        val clinicaSeleccionada = clinicas.firstOrNull { it.id == reservaActual.clinicaId } ?: clinicas.firstOrNull()
        val mascotaSeleccionada = mascotas.firstOrNull { it.id == reservaActual.mascotaId } ?: mascotas.firstOrNull()

        val idClinicaReal = clinicaSeleccionada?.id ?: reservaActual.clinicaId
        val nombreClinicaReal = clinicaSeleccionada?.nombre ?: reservaActual.lugar
        val idMascotaReal = mascotaSeleccionada?.id ?: reservaActual.mascotaId
        val nombreMascotaReal = mascotaSeleccionada?.nombre ?: reservaActual.mascotaNombre

        val nuevaCita = Cita(
            id = (citas.size + 1).toString(),
            vetNombre = reservaActual.profesionalNombre.ifBlank { nombreClinicaReal },
            servicio = reservaActual.servicioNombre,
            fechaStr = reservaActual.fechaStr,
            horaStr = reservaActual.horaStr,
            esProxima = true,
            mascotaNombre = nombreMascotaReal,
            modalidad = reservaActual.modalidad,
            lugar = nombreClinicaReal
        )
        citas.add(0, nuevaCita)

        // Guardar la cita en Firebase Firestore con IDs reales de clinicas y mascotas
        val modeloCita = CitaModel(
            usuarioId = usuarioId,
            mascotaId = idMascotaReal,
            nombreMascota = nombreMascotaReal,
            clinicaId = idClinicaReal,
            nombreClinica = nombreClinicaReal,
            servicioId = reservaActual.servicioId,
            nombreServicio = reservaActual.servicioNombre,
            fecha = reservaActual.fechaStr,
            hora = reservaActual.horaStr,
            estado = "Confirmada"
        )
        repository.crearCita(modeloCita) { exito, error ->
            cargarCitasDesdeFirebase()
        }
    }

    fun cancelarCita(id: String) {
        citas.removeAll { it.id == id }
        repository.cancelarCita(id, "Cancelada por el usuario") { exito ->
            cargarCitasDesdeFirebase()
        }
    }
}
