package com.example.petshield.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petshield.domain.model.AppointmentStatus
import com.example.petshield.ui.navigation.Screen
import com.example.petshield.ui.screens.PetShieldUiState

@Composable
fun NotificationsScreen(state: PetShieldUiState, onNavigate: (String) -> Unit) {
    val upcomingAppointments = if (state.notificationPreferences.appointmentReminders) {
        state.appointments.filter { it.status == AppointmentStatus.UPCOMING }
    } else {
        emptyList()
    }
    val vaccines = if (state.notificationPreferences.healthReminders) state.vaccines else emptyList()
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Notificaciones", style = MaterialTheme.typography.headlineMedium)
        Text("Recordatorios para el cuidado de tus mascotas.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        upcomingAppointments.forEach { appointment ->
            NotificationRow(
                title = "Próxima cita",
                detail = "${appointment.clinicName} · ${appointment.dateTime.replace('T', ' ')}",
                icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                onClick = { onNavigate(Screen.AppointmentDetails.createRoute(appointment.id)) }
            )
        }
        vaccines.forEach { vaccine ->
            NotificationRow(
                title = "Carnet de ${state.pets.firstOrNull { it.id == vaccine.petId }?.name ?: "mascota"}",
                detail = "${vaccine.name} · próxima dosis ${vaccine.dueAt}",
                icon = { Icon(Icons.Default.Vaccines, contentDescription = null) },
                onClick = { onNavigate(Screen.AppliedVaccines.route) }
            )
        }
        if (upcomingAppointments.isEmpty() && vaccines.isEmpty()) {
            Text("Estás al día. Aquí aparecerán tus recordatorios.")
        }
    }
}

@Composable
private fun NotificationRow(
    title: String,
    detail: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            icon()
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(detail, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}