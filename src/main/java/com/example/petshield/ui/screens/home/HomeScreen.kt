package com.example.petshield.ui.screens.home

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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.petshield.ui.navigation.Screen
import com.example.petshield.ui.screens.PetShieldUiState

@Composable
fun HomeScreen(
    state: PetShieldUiState,
    onNavigate: (String) -> Unit
) {
    val ownerName = state.ownerAccount?.name?.substringBefore(' ') ?: "bienvenido"
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column {
            Text("Hola, $ownerName", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Todo bien con tus compañeros?",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        val pet = state.pets.firstOrNull()
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Pets, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pet?.name ?: "Agrega a tu primera mascota",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = pet?.let { "${it.breed} · ${it.ageYears} años" }
                            ?: "Guarda su información y cuidados",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                OutlinedButton(onClick = { onNavigate(Screen.Pets.route) }) {
                    Text("Ver")
                }
            }
        }

        Text("Próxima cita", style = MaterialTheme.typography.titleLarge)
        val appointment = state.appointments.firstOrNull { it.status.name == "UPCOMING" }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                Column(modifier = Modifier.weight(1f)) {
                    Text(appointment?.service ?: "Sin citas próximas", style = MaterialTheme.typography.titleMedium)
                    Text(
                        appointment?.let { "${it.clinicName} · ${it.dateTime.replace('T', ' ')}" }
                            ?: "Encuentra una clínica y agenda una visita",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        }

        Text("Al día con su cuidado", style = MaterialTheme.typography.titleLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                icon = { Icon(Icons.Default.Pets, contentDescription = null) },
                value = state.pets.size.toString(),
                label = "Mascotas"
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                icon = { Icon(Icons.Default.Vaccines, contentDescription = null) },
                value = state.vaccines.size.toString(),
                label = "Vacunas"
            )
        }

        Button(onClick = { onNavigate(Screen.Clinics.route) }, modifier = Modifier.fillMaxWidth()) {
            Text("Buscar clínica veterinaria")
        }
        OutlinedButton(onClick = { onNavigate(Screen.HealthRecordMenu.route) }, modifier = Modifier.fillMaxWidth()) {
            Text("Abrir carnet de salud")
        }
        OutlinedButton(onClick = { onNavigate(Screen.Notifications.route) }, modifier = Modifier.fillMaxWidth()) {
            Text("Ver notificaciones")
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    com.example.petshield.ui.theme.PetShieldTheme {
        HomeScreen(state = PetShieldUiState(), onNavigate = {})
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier,
    icon: @Composable () -> Unit,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            icon()
            Text(value, style = MaterialTheme.typography.headlineSmall)
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}