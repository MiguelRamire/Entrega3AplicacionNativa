package com.example.petshield.ui.screens.clinics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petshield.domain.model.Appointment
import com.example.petshield.domain.model.Clinic
import com.example.petshield.domain.model.Pet
import com.example.petshield.ui.navigation.Screen
import com.example.petshield.ui.screens.PetShieldUiState
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun ClinicsScreen(
    state: PetShieldUiState,
    onBookAppointment: (Appointment) -> Unit,
    onNavigate: (String) -> Unit,
    onToggleFavorite: (Long) -> Unit
) {
    val clinics = state.clinics.filter { clinic ->
        val searchText = "${clinic.name} ${clinic.address}"
        searchText.contains(state.clinicQuery, ignoreCase = true) &&
            clinic.rating >= state.clinicMinimumRating
    }
    var selectedClinic by remember { mutableStateOf<Clinic?>(null) }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Clínicas veterinarias", modifier = Modifier.weight(1f), style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = { onNavigate(Screen.FavoriteVeterinarians.route) }) {
                Icon(Icons.Default.Favorite, contentDescription = "Veterinarios favoritos")
            }
            IconButton(onClick = { onNavigate(Screen.ClinicFilters.route) }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtrar clínicas")
            }
        }
        Text("Encuentra atención para tus mascotas.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (clinics.isEmpty()) Text("No hay clínicas que coincidan con los filtros.")
        clinics.forEach { clinic ->
            ClinicCard(
                clinic = clinic,
                onBook = { selectedClinic = clinic },
                onDetails = { onNavigate(Screen.ClinicDetails.createRoute(clinic.id)) },
                onToggleFavorite = { onToggleFavorite(clinic.id) }
            )
        }
        Spacer(Modifier.height(8.dp))
    }
    selectedClinic?.let { clinic ->
        ClinicBookingDialog(
            clinic = clinic,
            pets = state.pets,
            onDismiss = { selectedClinic = null },
            onConfirm = { appointment ->
                onBookAppointment(appointment)
                selectedClinic = null
            }
        )
    }
}

@Composable
private fun ClinicCard(
    clinic: Clinic,
    onBook: () -> Unit,
    onDetails: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(clinic.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                Text(" ${clinic.rating}")
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = if (clinic.isFavorite) "Quitar de favoritos" else "Guardar en favoritos",
                        tint = if (clinic.isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(clinic.address, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(clinic.phone, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDetails) { Text("Ver detalle") }
                Button(onClick = onBook) { Text("Agendar cita") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicBookingDialog(
    clinic: Clinic,
    pets: List<Pet>,
    onDismiss: () -> Unit,
    onConfirm: (Appointment) -> Unit
) {
    val initialDateMillis = remember {
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 7) }.timeInMillis
    }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)
    var selectedPet by remember(pets) { mutableStateOf(pets.firstOrNull()) }
    var petMenuExpanded by remember { mutableStateOf(false) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val selectedDate = datePickerState.selectedDateMillis?.let {
        Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agendar en ${clinic.name}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (pets.isEmpty()) {
                    Text("Agrega una mascota antes de reservar una cita.")
                } else {
                    Box {
                        OutlinedButton(onClick = { petMenuExpanded = true }) {
                            Text(selectedPet?.name ?: "Selecciona una mascota")
                        }
                        DropdownMenu(
                            expanded = petMenuExpanded,
                            onDismissRequest = { petMenuExpanded = false }
                        ) {
                            pets.forEach { pet ->
                                DropdownMenuItem(
                                    text = { Text(pet.name) },
                                    onClick = {
                                        selectedPet = pet
                                        petMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    OutlinedButton(onClick = { showDatePicker = true }) {
                        Text(selectedDate?.format(DateTimeFormatter.ofPattern("d MMM yyyy")) ?: "Elegir fecha")
                    }
                    Text("Atención general · 10:00", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = pets.isNotEmpty() && selectedPet != null && selectedDate != null,
                onClick = {
                    val pet = selectedPet ?: return@TextButton
                    val date = selectedDate ?: return@TextButton
                    onConfirm(
                        Appointment(
                            petId = pet.id,
                            clinicName = clinic.name,
                            service = "Atención general",
                            dateTime = "${date.format(DateTimeFormatter.ISO_DATE)}T10:00:00"
                        )
                    )
                }
            ) { Text("Confirmar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Listo") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}