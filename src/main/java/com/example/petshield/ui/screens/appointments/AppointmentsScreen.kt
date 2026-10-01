package com.example.petshield.ui.screens.appointments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petshield.domain.model.Appointment
import com.example.petshield.domain.model.AppointmentStatus
import com.example.petshield.ui.navigation.Screen
import com.example.petshield.ui.screens.PetShieldUiState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AppointmentsScreen(
    state: PetShieldUiState,
    onNavigate: (String) -> Unit
) {
    val appointments = state.appointments.filter { it.status == AppointmentStatus.UPCOMING }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Mis citas", style = MaterialTheme.typography.headlineMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { onNavigate(Screen.UpcomingAppointments.route) }) { Text("Próximas") }
            OutlinedButton(onClick = { onNavigate(Screen.CancelledAppointments.route) }) { Text("Canceladas") }
        }
        if (appointments.isEmpty()) {
            Text("No tienes citas próximas.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        appointments.forEach { appointment ->
            AppointmentCard(
                appointment = appointment,
                petName = state.pets.firstOrNull { it.id == appointment.petId }?.name ?: "Mascota",
                onOpen = { onNavigate(Screen.AppointmentDetails.createRoute(appointment.id)) }
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun AppointmentCard(appointment: Appointment, petName: String, onOpen: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onOpen
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(appointment.service, style = MaterialTheme.typography.titleMedium)
            }
            Text("${appointment.clinicName} · $petName")
            Text(formatAppointmentDate(appointment.dateTime), color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(statusLabel(appointment.status), color = MaterialTheme.colorScheme.primary)
            Text("Ver detalle", color = MaterialTheme.colorScheme.primary)
        }
    }
}

private fun formatAppointmentDate(value: String): String = runCatching {
    LocalDateTime.parse(value).format(
        DateTimeFormatter.ofPattern("EEEE d 'de' MMMM · HH:mm", Locale.forLanguageTag("es-CL"))
    ).replaceFirstChar { it.uppercase(Locale.forLanguageTag("es-CL")) }
}.getOrDefault(value.replace('T', ' '))

private fun statusLabel(status: AppointmentStatus): String = when (status) {
    AppointmentStatus.UPCOMING -> "Confirmada"
    AppointmentStatus.COMPLETED -> "Realizada"
    AppointmentStatus.CANCELLED -> "Cancelada"
}