package com.example.petshield.ui.screens.profile

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
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petshield.ui.screens.PetShieldUiState
import com.example.petshield.ui.navigation.Screen

@Composable
fun ProfileScreen(state: PetShieldUiState, onNavigate: (String) -> Unit, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text("Mi perfil", style = MaterialTheme.typography.headlineMedium)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium) {
                    Icon(
                        Icons.Default.Pets,
                        contentDescription = null,
                        modifier = Modifier.padding(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Column {
                    Text(state.ownerAccount?.name ?: "Cuenta local", style = MaterialTheme.typography.titleLarge)
                    Text(state.ownerAccount?.email.orEmpty(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Text("Resumen de cuidado", style = MaterialTheme.typography.titleLarge)
        SummaryLine(label = "Mascotas registradas", value = state.pets.size.toString())
        SummaryLine(label = "Vacunas en el carnet", value = state.vaccines.size.toString())
        SummaryLine(label = "Citas próximas", value = state.appointments.count { it.status.name == "UPCOMING" }.toString())
        Text("Cuenta y ayuda", style = MaterialTheme.typography.titleLarge)
        listOf(
            Screen.EditProfile.route to "Editar perfil",
            Screen.Settings.route to "Configuración",
            Screen.NotificationSettings.route to "Notificaciones",
            Screen.PasswordManagement.route to "Gestión de contraseña",
            Screen.PrivacyPolicy.route to "Política de privacidad",
            Screen.HelpFaq.route to "Preguntas frecuentes",
            Screen.HelpContact.route to "Centro de ayuda",
            Screen.FavoriteVeterinarians.route to "Veterinarios favoritos",
            Screen.Notifications.route to "Bandeja de notificaciones"
        ).forEach { (route, label) ->
            TextButton(onClick = { onNavigate(route) }, modifier = Modifier.fillMaxWidth()) {
                Text(label, modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("La información se guarda en este dispositivo.")
            }
        }
        TextButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Cerrar sesión")
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun SummaryLine(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleMedium)
    }
}