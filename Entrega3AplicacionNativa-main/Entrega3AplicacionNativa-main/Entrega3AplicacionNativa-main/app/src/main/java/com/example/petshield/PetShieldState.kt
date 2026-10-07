package com.example.petshield

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petshield.data.Cita
import com.example.petshield.data.CitaModel
import com.example.petshield.data.Clinica
import com.example.petshield.data.DatosReserva
import com.example.petshield.data.FirebaseRepository
import com.example.petshield.data.Mascota
import com.example.petshield.data.MascotaModel
import com.example.petshield.data.Veterinario
import kotlinx.coroutines.launch

class PetShieldViewModel(
    private val repository: FirebaseRepository = FirebaseRepository()
) : ViewModel() {

    // Usuario
    var usuarioId by mutableStateOf("usuario_demo")
        private set
    var usuarioNombre by mutableStateOf("Ada")
        private set
    var usuarioTelefono by mutableStateOf("+123 567 89000")
        private set
    var usuarioEmail by mutableStateOf("Janedoe@example.com")
        private set

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

    fun cargarDatosUsuario(uid: String) {
        viewModelScope.launch {
            val perfil = repository.obtenerUsuario(uid)
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
        viewModelScope.launch {
            val result = repository.iniciarSesion(email, pass)
            result.onSuccess {
                val currentUser = repository.obtenerUsuarioActual()
                if (currentUser != null) {
                    usuarioId = currentUser.uid
                    usuarioEmail = currentUser.email ?: ""
                    cargarDatosUsuario(currentUser.uid)
                    cargarMascotasDesdeFirebase()
                    cargarCitasDesdeFirebase()
                }
                onResult(true, null)
            }.onFailure { error ->
                onResult(false, error.localizedMessage ?: "Correo o contraseña incorrectos")
            }
        }
    }

    fun iniciarSesionConGoogle(
        idToken: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.iniciarSesionConGoogleToken(idToken)
            result.onSuccess {
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
            }.onFailure { error ->
                onResult(false, error.localizedMessage ?: "Error al iniciar sesión con Google")
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
        viewModelScope.launch {
            val result = repository.registrarUsuario(email, pass, nombre, telefono)
            result.onSuccess {
                val currentUser = repository.obtenerUsuarioActual()
                if (currentUser != null) {
                    usuarioId = currentUser.uid
                    usuarioNombre = nombre
                    usuarioEmail = email
                    usuarioTelefono = telefono
                    cargarCitasDesdeFirebase()
                }
                onResult(true, null)
            }.onFailure { error ->
                onResult(false, error.localizedMessage ?: "Error al registrar el usuario")
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
        viewModelScope.launch {
            val listaFirebase = repository.obtenerMascotasPorUsuario(usuarioId)
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
        viewModelScope.launch {
            val listaFirebase = repository.obtenerClinicas()
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
        viewModelScope.launch {
            val listaFirebase = repository.obtenerVeterinarios()
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
        viewModelScope.launch {
            val listaFirebase = repository.obtenerCitasPorUsuario(usuarioId)
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
        viewModelScope.launch {
            val modeloFirestore = MascotaModel(
                usuarioId = usuarioId,
                nombre = mascota.nombre,
                especie = mascota.especie,
                raza = mascota.raza,
                peso = mascota.peso,
                fechaNacimiento = mascota.fechaNacimiento,
                sexo = mascota.sexo
            )
            repository.agregarMascota(modeloFirestore)
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

        viewModelScope.launch {
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
            repository.crearCita(modeloCita)
            cargarCitasDesdeFirebase()
        }
    }

    fun cancelarCita(id: String) {
        citas.removeAll { it.id == id }
        viewModelScope.launch {
            repository.cancelarCita(id, "Cancelada por el usuario")
            cargarCitasDesdeFirebase()
        }
    }
}
