package com.upb.petshield

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upb.petshield.data.Cita
import com.upb.petshield.data.CitaModel
import com.upb.petshield.data.Clinica
import com.upb.petshield.data.ClinicaModel
import com.upb.petshield.data.DatosReserva
import com.upb.petshield.data.FavoritoModel
import com.upb.petshield.data.FirebaseRepository
import com.upb.petshield.data.Mascota
import com.upb.petshield.data.MascotaModel
import com.upb.petshield.data.ServicioModel
import com.upb.petshield.data.Veterinario
import com.upb.petshield.data.VeterinarioModel
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

    // Servicios
    val servicios = mutableStateListOf<ServicioModel>()

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
        cargarServiciosDesdeFirebase()
        cargarCitasDesdeFirebase()
        cargarFavoritosDesdeFirebase()
    }

    fun obtenerUsuarioActual() = repository.obtenerUsuarioActual()

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
                    cargarFavoritosDesdeFirebase()
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
                    cargarFavoritosDesdeFirebase()
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
                    cargarFavoritosDesdeFirebase()
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
            } else {
                sembrarClinicasEjemplo()
            }
            cargarFavoritosDesdeFirebase()
        }
    }

    private fun sembrarClinicasEjemplo() {
        viewModelScope.launch {
            val ejemplos = listOf(
                ClinicaModel(
                    nombre = "Hospital Veterinario San Francisco",
                    direccion = "Calle 100 #15-32, Chapinero",
                    telefono = "+57 601 555 1234",
                    horario = "24 Horas (Urgencias)",
                    calificacion = 4.8
                ),
                ClinicaModel(
                    nombre = "Centro Médico Veterinario Mascotas & Co.",
                    direccion = "Carrera 43A #1-50, El Poblado",
                    telefono = "+57 604 444 8899",
                    horario = "8:00 AM - 8:00 PM",
                    calificacion = 5.0
                ),
                ClinicaModel(
                    nombre = "Clínica Veterinaria Movivet",
                    direccion = "Av. Las Américas #68-12",
                    telefono = "+57 310 987 6543",
                    horario = "8:00 AM - 6:00 PM",
                    calificacion = 4.7
                ),
                ClinicaModel(
                    nombre = "Veterinaria Especializada VetLife",
                    direccion = "Calle 127 #19-45",
                    telefono = "+57 601 789 0123",
                    horario = "7:00 AM - 9:00 PM",
                    calificacion = 4.9
                )
            )
            ejemplos.forEach { model ->
                repository.agregarClinica(model)
            }
            val listaActualizada = repository.obtenerClinicas()
            if (listaActualizada.isNotEmpty()) {
                clinicas.clear()
                listaActualizada.forEach { item ->
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
                cargarFavoritosDesdeFirebase()
            }
        }
    }

    fun cargarVeterinariosDesdeFirebase() {
        viewModelScope.launch {
            val listaFirebase = repository.obtenerVeterinarios()
            if (listaFirebase.size >= 5) {
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
                cargarFavoritosDesdeFirebase()
            } else {
                sembrarVeterinariosEjemplo()
            }
        }
    }

    private fun sembrarVeterinariosEjemplo() {
        viewModelScope.launch {
            val ejemplos = listOf(
                VeterinarioModel(
                    nombre = "Dra. Ana María Jiménez, MV",
                    especialidad = "Medicina Interna y Cardiología",
                    clinicaTag = "Hospital Vet San Francisco",
                    esFavorito = true
                ),
                VeterinarioModel(
                    nombre = "Dr. Carlos Eduardo Mendoza, MVZ",
                    especialidad = "Cirugía General y Traumatología",
                    clinicaTag = "Centro Médico Mascotas & Co.",
                    esFavorito = true
                ),
                VeterinarioModel(
                    nombre = "Dra. Sofía Herrera, MV",
                    especialidad = "Dermatología y Alergias",
                    clinicaTag = "Movivet Clínica Veterinaria",
                    esFavorito = true
                ),
                VeterinarioModel(
                    nombre = "Dr. Alejandro Torres, MV",
                    especialidad = "Odontología y Profilaxis",
                    clinicaTag = "VetLife Especializada",
                    esFavorito = false
                ),
                VeterinarioModel(
                    nombre = "Dra. Valentina Ríos, MV",
                    especialidad = "Pediatría y Neonatología",
                    clinicaTag = "Hospital Vet San Francisco",
                    esFavorito = true
                ),
                VeterinarioModel(
                    nombre = "Dr. Mateo Restrepo, MVZ",
                    especialidad = "Neurología y Fisioterapia",
                    clinicaTag = "Centro Médico Mascotas & Co.",
                    esFavorito = false
                ),
                VeterinarioModel(
                    nombre = "Dra. Camila Gómez, MV",
                    especialidad = "Medicina Felina y Etología",
                    clinicaTag = "Movivet Clínica Veterinaria",
                    esFavorito = true
                ),
                VeterinarioModel(
                    nombre = "Dr. Esteban Morales, MV",
                    especialidad = "Urgencias y Cuidado Crítico 24/7",
                    clinicaTag = "VetLife Especializada",
                    esFavorito = false
                )
            )

            // Cargar localmente de inmediato para actualizar la interfaz
            veterinarios.clear()
            ejemplos.forEachIndexed { index, model ->
                veterinarios.add(
                    Veterinario(
                        id = "vet_seeded_$index",
                        nombre = model.nombre,
                        especialidad = model.especialidad,
                        clinicaTag = model.clinicaTag,
                        esFavorito = model.esFavorito
                    )
                )
            }

            // Guardar en Firestore
            ejemplos.forEach { model ->
                repository.agregarVeterinario(model)
            }

            val listaActualizada = repository.obtenerVeterinarios()
            if (listaActualizada.isNotEmpty()) {
                veterinarios.clear()
                listaActualizada.forEach { item ->
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
                cargarFavoritosDesdeFirebase()
            }
        }
    }

    fun cargarServiciosDesdeFirebase() {
        viewModelScope.launch {
            val lista = repository.obtenerServicios()
            if (lista.isNotEmpty()) {
                servicios.clear()
                servicios.addAll(lista.distinctBy { it.nombre.trim().lowercase() })
            } else {
                sembrarServiciosEjemplo()
            }
        }
    }

    private fun sembrarServiciosEjemplo() {
        viewModelScope.launch {
            val ejemplos = listOf(
                ServicioModel(
                    nombre = "Consulta veterinaria general",
                    descripcion = "Evaluación clínica completa para conocer el estado de salud de tu mascota, resolver inquietudes y definir un plan de cuidado.",
                    precio = 65000.0,
                    duracionMinutos = 30
                ),
                ServicioModel(
                    nombre = "Vacunación y desparasitación",
                    descripcion = "Aplicación de esquema completo de vacunas anuales y tratamiento antiparasitario preventivo.",
                    precio = 45000.0,
                    duracionMinutos = 20
                ),
                ServicioModel(
                    nombre = "Examen de laboratorio y profilaxis",
                    descripcion = "Toma de muestras de sangre, perfil hepático, renal y limpieza dental preventiva con ultrasonido.",
                    precio = 85000.0,
                    duracionMinutos = 45
                ),
                ServicioModel(
                    nombre = "Urgencias y atención prioritaria",
                    descripcion = "Atención médica inmediata de urgencia con monitoreo de signos vitales e hidratación fluida.",
                    precio = 120000.0,
                    duracionMinutos = 60
                ),
                ServicioModel(
                    nombre = "Cirugía y esterilización",
                    descripcion = "Procedimiento quirúrgico ambulatorio con anestesia inhalada, monitoreo continuo y medicamentos postoperatorios.",
                    precio = 150000.0,
                    duracionMinutos = 90
                )
            )
            ejemplos.forEach { s -> repository.agregarServicio(s) }
            val listaActualizada = repository.obtenerServicios()
            servicios.clear()
            servicios.addAll(listaActualizada.distinctBy { it.nombre.trim().lowercase() }.ifEmpty { ejemplos })
        }
    }

    fun cargarFavoritosDesdeFirebase() {
        if (usuarioId.isBlank()) return
        viewModelScope.launch {
            val listaFavoritos = repository.obtenerFavoritosPorUsuario(usuarioId)
            if (listaFavoritos.isNotEmpty()) {
                val setClinicasFav = listaFavoritos.map { it.clinicaId }.filter { it.isNotBlank() }.toSet()
                val setVetsFav = listaFavoritos.map { it.vetId }.filter { it.isNotBlank() }.toSet()

                for (i in clinicas.indices) {
                    if (clinicas[i].id in setClinicasFav) {
                        clinicas[i] = clinicas[i].copy(esFavorita = true)
                    }
                }
                for (i in veterinarios.indices) {
                    if (veterinarios[i].id in setVetsFav) {
                        veterinarios[i] = veterinarios[i].copy(esFavorito = true)
                    }
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
                    val nombreClinicaMostrado = clinicaEncontrada?.nombre ?: item.nombreClinica.ifBlank { "Clínica PetCare" }
                    val vetEncontrado = veterinarios.firstOrNull { it.clinicaId == item.clinicaId }
                    val nombreVetMostrado = if (item.vetNombre.isNotBlank()) item.vetNombre else (vetEncontrado?.nombre ?: "Dra. Sofía Herrera, MV")

                    citas.add(
                        Cita(
                            id = item.id,
                            vetNombre = nombreVetMostrado,
                            servicio = item.nombreServicio.ifBlank { "Consulta veterinaria general" },
                            fechaStr = item.fecha.ifBlank { "Miércoles, 23 De Septiembre" },
                            horaStr = item.hora.ifBlank { "3:00 PM" },
                            esProxima = item.estado == "Confirmada" || item.estado == "Pendiente",
                            mascotaNombre = item.nombreMascota.ifBlank { "Max" },
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
            val nuevoEstado = !item.esFavorito
            veterinarios[index] = item.copy(esFavorito = nuevoEstado)

            viewModelScope.launch {
                if (nuevoEstado) {
                    val fav = FavoritoModel(
                        usuarioId = usuarioId,
                        vetId = id,
                        tipo = "veterinario"
                    )
                    repository.agregarFavorito(fav)
                } else {
                    repository.eliminarFavoritoVet(usuarioId, id)
                }
            }
        }
    }

    fun toggleFavoritoClinica(id: String) {
        val index = clinicas.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = clinicas[index]
            val nuevoEstado = !item.esFavorita
            clinicas[index] = item.copy(esFavorita = !item.esFavorita)

            viewModelScope.launch {
                if (nuevoEstado) {
                    val fav = FavoritoModel(
                        usuarioId = usuarioId,
                        clinicaId = id,
                        tipo = "clinica"
                    )
                    repository.agregarFavorito(fav)
                } else {
                    repository.eliminarFavoritoClinica(usuarioId, id)
                }
            }
        }
    }

    fun confirmarReservaActual() {
        val clinicaSeleccionada = clinicas.firstOrNull { it.id == reservaActual.clinicaId } ?: clinicas.firstOrNull()
        val mascotaSeleccionada = mascotas.firstOrNull { it.id == reservaActual.mascotaId } ?: mascotas.firstOrNull()

        val idClinicaReal = clinicaSeleccionada?.id ?: reservaActual.clinicaId
        val nombreClinicaReal = clinicaSeleccionada?.nombre ?: reservaActual.lugar
        val idMascotaReal = mascotaSeleccionada?.id ?: reservaActual.mascotaId
        val nombreMascotaReal = mascotaSeleccionada?.nombre ?: reservaActual.mascotaNombre

        val vetNombreMostrado = if (reservaActual.profesionalNombre.isNotBlank() && !reservaActual.profesionalNombre.contains("Clínica")) {
            reservaActual.profesionalNombre
        } else {
            val vetEncontrado = veterinarios.firstOrNull { it.clinicaId == idClinicaReal }
            vetEncontrado?.nombre ?: "Dra. Sofía Herrera, MV"
        }

        val nuevaCita = Cita(
            id = (citas.size + 1).toString(),
            vetNombre = vetNombreMostrado,
            servicio = reservaActual.servicioNombre.ifBlank { "Consulta veterinaria general" },
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
                vetNombre = vetNombreMostrado,
                servicioId = reservaActual.servicioId,
                nombreServicio = reservaActual.servicioNombre.ifBlank { "Consulta veterinaria general" },
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
