package com.example.petshield

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

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
    val nombre: String,
    val especialidad: String,
    val clinicaTag: String,
    var esFavorito: Boolean = true,
    val emojiAvatar: String = "👩‍⚕️"
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
    val mascotaNombre: String = "Max · Golden Retriever",
    val servicioNombre: String = "Consulta veterinaria general",
    val profesionalNombre: String = "Dra. Ana Jiménez, MV",
    val fechaStr: String = "Miércoles, 23 de septiembre",
    val horaStr: String = "3:00 PM",
    val modalidad: String = "En clínica",
    val lugar: String = "Clínica PetCare · Poblado",
    val precioConsulta: String = "$65.000",
    val tarifaServicio: String = "$3.500",
    val total: String = "$68.500"
)

class PetShieldViewModel {
    // Usuario
    var usuarioNombre by mutableStateOf("Ada")
    var usuarioTelefono by mutableStateOf("+123 567 89000")
    var usuarioEmail by mutableStateOf("Janedoe@example.com")

    // Mascotas
    val mascotas = mutableStateListOf<Mascota>()

    // Clínicas
    val clinicas = mutableStateListOf(
        Clinica("1", "PetCare Clínica Veterinaria", "Av. Insurgentes 778 Ciudad De México", "7:15 AM - 6:30 PM", 4.9f, true, true),
        Clinica("2", "VitalPet Clínica Veterinaria", "Av. Insurgentes 778 Ciudad De México", "7:15 AM - 6:30 PM", 4.8f, true, false),
        Clinica("3", "VetSalud Clínica Veterinaria", "778 Locust View Drive Oakland, CA", "7:15 AM - 6:30 PM", 4.9f, true, false),
        Clinica("4", "HappyPaws Veterinaria", "778 Locust View Drive Oakland, CA", "7:15 AM - 6:30 PM", 4.7f, true, false)
    )

    // Veterinarios Favoritos
    val veterinarios = mutableStateListOf(
        Veterinario("1", "Dra. Ana Jiménez, MV", "Medicina Reproductiva", "Veterinaria A", true, "👩‍⚕️"),
        Veterinario("2", "Dr. Mateo Navarro, MV", "Cardiólogo Veterinario", "Veterinaria B", true, "👨‍⚕️"),
        Veterinario("3", "Dra. Laura Gómez, MV", "Veterinario General", "Veterinaria C", true, "👩‍⚕️"),
        Veterinario("4", "Dr. Martín Espinoza, MV", "Veterinario General", "Veterinaria D", true, "👨‍⚕️")
    )

    // Citas
    val citas = mutableStateListOf(
        Cita("1", "Veterinaria A", "Veterinario General", "Domingo, 12 Junio", "9:30 AM - 10:00 AM", true),
        Cita("2", "Veterinaria B", "Desparasitación", "Viernes, 20 Junio", "2:30 PM - 3:00 PM", true),
        Cita("3", "Veterinaria C", "Chequeo General", "Martes, 15 Junio", "9:30 AM - 10:00 AM", true),
        Cita("4", "Veterinaria D", "Vacunación", "Viernes, 20 Junio", "2:30 PM - 3:00 PM", true)
    )

    // Datos de la reserva actual
    var reservaActual by mutableStateOf(DatosReserva())

    // Cita seleccionada para cancelar
    var citaACancelar by mutableStateOf<Cita?>(null)

    fun agregarMascota(mascota: Mascota) {
        mascotas.add(mascota)
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
        val nuevaCita = Cita(
            id = (citas.size + 1).toString(),
            vetNombre = reservaActual.profesionalNombre,
            servicio = reservaActual.servicioNombre,
            fechaStr = reservaActual.fechaStr,
            horaStr = reservaActual.horaStr,
            esProxima = true,
            mascotaNombre = reservaActual.mascotaNombre,
            modalidad = reservaActual.modalidad,
            lugar = reservaActual.lugar
        )
        citas.add(0, nuevaCita)
    }

    fun cancelarCita(id: String) {
        citas.removeAll { it.id == id }
    }
}
