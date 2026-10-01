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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petshield.domain.model.Appointment
import com.example.petshield.domain.model.AppointmentStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CancelledAppointmentsScreen(
    appointments: List<Appointment>,
    petNames: Map<Long, String>,
    onSelect: (Long) -> Unit
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Citas canceladas", style = MaterialTheme.typography.headlineMedium)
        val cancelled = appointments.filter { it.status == AppointmentStatus.CANCELLED }
        if (cancelled.isEmpty()) Text("No tienes citas canceladas.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        cancelled.forEach { appointment ->
            AppointmentSummaryCard(appointment, petNames[appointment.petId] ?: "Mascota") {
                onSelect(appointment.id)
            }
        }
    }
}

@Composable
fun AppointmentDetailsScreen(
    appointment: Appointment?,
    petName: String,
    onCancel: () -> Unit,
    onReview: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Detalle de cita", style = MaterialTheme.typography.headlineMedium)
        if (appointment == null) {
            Text("No encontramos esta cita.")
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(appointment.service, style = MaterialTheme.typography.titleLarge)
                    Text(appointment.clinicName)
                    Text("Mascota: $petName")
                    Text(formatDate(appointment.dateTime), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(statusText(appointment.status), color = MaterialTheme.colorScheme.primary)
                }
            }
            when (appointment.status) {
                AppointmentStatus.UPCOMING -> Button(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
                    Text("Cancelar cita")
                }
                AppointmentStatus.COMPLETED -> Button(onClick = onReview, modifier = Modifier.fillMaxWidth()) {
                    Text("Dejar una reseña")
                }
                AppointmentStatus.CANCELLED -> Unit
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver a mis citas") }
    }
}

@Composable
fun CancelAppointmentScreen(appointment: Appointment?, onConfirm: () -> Unit, onKeep: () -> Unit) {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Cancelar cita", style = MaterialTheme.typography.headlineMedium)
        Text(
            appointment?.let { "¿Quieres cancelar ${it.service} en ${it.clinicName}?" }
                ?: "No encontramos esta cita.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (appointment != null) {
            Text(formatDate(appointment.dateTime), style = MaterialTheme.typography.titleMedium)
            Button(onClick = onConfirm, modifier = Modifier.fillMaxWidth()) { Text("Sí, cancelar cita") }
        }
        OutlinedButton(onClick = onKeep, modifier = Modifier.fillMaxWidth()) { Text("Mantener cita") }
    }
}

@Composable
fun AppointmentReviewScreen(appointment: Appointment?, onSubmit: (Int, String) -> Unit) {
    var rating by rememberSaveable { mutableIntStateOf(5) }
    var comment by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Cuéntanos cómo estuvo", style = MaterialTheme.typography.headlineMedium)
        Text(appointment?.clinicName ?: "Tu visita veterinaria", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            (1..5).forEach { value ->
                FilterChip(selected = rating == value, onClick = { rating = value }, label = { Text("$value ★") })
            }
        }
        OutlinedTextField(
            comment, { comment = it; submitted = false },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Comentario") },
            minLines = 4
        )
        Button(
            onClick = { onSubmit(rating, comment.trim()); submitted = true },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Enviar reseña") }
        if (submitted) Text("Gracias por compartir tu experiencia.", color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun AppointmentSummaryCard(appointment: Appointment, petName: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            androidx.compose.material3.Icon(Icons.Default.CalendarMonth, contentDescription = null)
            Column {
                Text(appointment.service, style = MaterialTheme.typography.titleMedium)
                Text("${appointment.clinicName} · $petName")
                Text(formatDate(appointment.dateTime), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

private fun formatDate(value: String): String = runCatching {
    LocalDateTime.parse(value).format(
        DateTimeFormatter.ofPattern("EEEE d 'de' MMMM · HH:mm", Locale.forLanguageTag("es-CL"))
    ).replaceFirstChar { it.uppercase(Locale.forLanguageTag("es-CL")) }
}.getOrDefault(value.replace('T', ' '))

private fun statusText(status: AppointmentStatus): String = when (status) {
    AppointmentStatus.UPCOMING -> "Confirmada"
    AppointmentStatus.COMPLETED -> "Realizada"
    AppointmentStatus.CANCELLED -> "Cancelada"
}