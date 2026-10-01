package com.example.petshield.ui.screens.clinics

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petshield.domain.model.Clinic

@Composable
fun ClinicFiltersScreen(
    initialQuery: String,
    initialMinimumRating: Double,
    onApply: (String, Double) -> Unit
) {
    var query by rememberSaveable { mutableStateOf(initialQuery) }
    var minimumRating by rememberSaveable { mutableDoubleStateOf(initialMinimumRating) }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Filtrar clínicas", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nombre o dirección") },
            singleLine = true
        )
        Text("Calificación mínima", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(0.0, 4.5, 4.8).forEach { value ->
                FilterChip(
                    selected = minimumRating == value,
                    onClick = { minimumRating = value },
                    label = { Text(if (value == 0.0) "Todas" else "$value+ estrellas") }
                )
            }
        }
        Button(onClick = { onApply(query.trim(), minimumRating) }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.FilterList, contentDescription = null)
            Text("Aplicar filtros")
        }
    }
}

@Composable
fun ClinicDetailsScreen(
    clinic: Clinic?,
    onBack: () -> Unit,
    onBook: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    if (clinic == null) {
        EmptyClinicDetails(onBack)
        return
    }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Detalle de clínica", style = MaterialTheme.typography.headlineMedium)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(clinic.name, style = MaterialTheme.typography.titleLarge)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                    Text("${clinic.rating} · Atención veterinaria")
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Text(clinic.address)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Phone, contentDescription = null)
                    Text(clinic.phone)
                }
            }
        }
        OutlinedButton(onClick = onToggleFavorite, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Favorite, contentDescription = null)
            Text(if (clinic.isFavorite) "Quitar de favoritos" else "Guardar en favoritos")
        }
        Button(onClick = onBook, modifier = Modifier.fillMaxWidth()) { Text("Agendar cita") }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver a clínicas") }
    }
}

@Composable
private fun EmptyClinicDetails(onBack: () -> Unit) {
    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("No encontramos esa clínica.", style = MaterialTheme.typography.titleLarge)
        OutlinedButton(onClick = onBack) { Text("Volver") }
    }
}

@Composable
fun FavoriteVeterinariansScreen(clinics: List<Clinic>, onSelectClinic: (Long) -> Unit) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Veterinarios favoritos", style = MaterialTheme.typography.headlineMedium)
        if (clinics.isEmpty()) {
            Text("Aún no guardaste clínicas favoritas.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        clinics.forEach { clinic ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(clinic.name, style = MaterialTheme.typography.titleMedium)
                        Text(clinic.address, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(onClick = { onSelectClinic(clinic.id) }) {
                        Icon(Icons.Default.Star, contentDescription = "Ver ${clinic.name}")
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}