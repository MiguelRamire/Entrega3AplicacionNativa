package com.example.petshield.ui.screens.pets

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petshield.domain.model.Pet
import com.example.petshield.domain.model.PetHealthRecord
import com.example.petshield.ui.navigation.Screen
import com.example.petshield.ui.screens.PetShieldUiState
import java.time.LocalDate

@Composable
fun HealthRecordMenuScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Carnet de salud", style = MaterialTheme.typography.headlineMedium)
        Text("Información médica de tus mascotas.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        HealthMenuRow("Vacunas", "Dosis aplicadas y próximas", Icons.Default.Vaccines) {
            onNavigate(Screen.AppliedVaccines.route)
        }
        HealthMenuRow("Alergias y reacciones", "Sensibilidades conocidas", Icons.Default.MedicalInformation) {
            onNavigate(Screen.Allergies.route)
        }
        HealthMenuRow("Análisis clínicos", "Exámenes y resultados", Icons.Default.Biotech) {
            onNavigate(Screen.ClinicalTests.route)
        }
        HealthMenuRow("Historial veterinario", "Visitas y controles", Icons.Default.History) {
            onNavigate(Screen.VeterinaryHistory.route)
        }
        HealthMenuRow("Datos de la mascota", "Agregar o actualizar información", Icons.Default.Pets) {
            onNavigate(Screen.AddPet.route)
        }
        HealthMenuRow("Carnet sin registros", "Vista vacía del carnet", Icons.Default.Assignment) {
            onNavigate(Screen.VaccineCardEmpty.route)
        }
    }
}

@Composable
private fun HealthMenuRow(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
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
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
fun VaccineCardScreen(state: PetShieldUiState, alternate: Boolean, onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(if (alternate) "Carnet de vacunas" else "Carnet de vacunas", style = MaterialTheme.typography.headlineMedium)
        if (state.pets.isEmpty()) {
            Text("Agrega una mascota para comenzar su carnet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = { onNavigate(Screen.AddPet.route) }) { Text("Agregar mascota") }
        } else if (state.vaccines.isEmpty()) {
            Text("Todavía no hay vacunas registradas.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedButton(onClick = { onNavigate(Screen.Pets.route) }) { Text("Registrar una vacuna") }
        } else {
            state.pets.forEach { pet ->
                val vaccines = state.vaccines.filter { it.petId == pet.id }
                if (vaccines.isNotEmpty()) {
                    Text(pet.name, style = MaterialTheme.typography.titleLarge)
                    vaccines.forEach { vaccine ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(vaccine.name, style = MaterialTheme.typography.titleMedium)
                                Text("Aplicada ${vaccine.administeredAt} · Próxima ${vaccine.dueAt}")
                                Text(vaccine.veterinarian, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
        OutlinedButton(
            onClick = {
                onNavigate(if (alternate) Screen.VaccineCardEmpty.route else Screen.VaccineCardEmptyAlternate.route)
            }
        ) {
            Text(if (alternate) "Volver al carnet" else "Vista alternativa del carnet")
        }
    }
}

@Composable
fun AddPetScreen(onSave: (String, String, String, Int, Double) -> Unit, onDone: () -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var species by rememberSaveable { mutableStateOf("Perro") }
    var breed by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Agregar mascota", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(name, { name = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Nombre") })
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Perro", "Gato", "Otro").forEach { value ->
                FilterChip(selected = species == value, onClick = { species = value }, label = { Text(value) })
            }
        }
        OutlinedTextField(breed, { breed = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Raza") })
        OutlinedTextField(age, { age = it.filter(Char::isDigit) }, modifier = Modifier.fillMaxWidth(), label = { Text("Edad (años)") })
        OutlinedTextField(weight, { weight = it.filter { char -> char.isDigit() || char == '.' || char == ',' } }, modifier = Modifier.fillMaxWidth(), label = { Text("Peso (kg)") })
        if (error) Text("Completa los datos requeridos.", color = MaterialTheme.colorScheme.error)
        Button(onClick = {
            val parsedAge = age.toIntOrNull()
            val parsedWeight = weight.replace(',', '.').toDoubleOrNull()
            if (name.isBlank() || parsedAge == null || parsedWeight == null) error = true
            else {
                onSave(name, species, breed, parsedAge, parsedWeight)
                onDone()
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Guardar mascota") }
    }
}

@Composable
fun HealthRecordsScreen(
    state: PetShieldUiState,
    category: String,
    title: String,
    onAddRecord: (PetHealthRecord) -> Unit,
    onSelectRecord: (Long) -> Unit,
    onNavigate: (String) -> Unit
) {
    val records = state.healthRecords.filter { it.category == category }
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        if (state.pets.isEmpty()) {
            Text("Agrega una mascota para registrar información médica.")
            Button(onClick = { onNavigate(Screen.AddPet.route) }) { Text("Agregar mascota") }
        } else {
            OutlinedButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Agregar registro")
            }
        }
        if (records.isEmpty()) Text("No hay registros todavía.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        records.forEach { record ->
            Card(
                onClick = { onSelectRecord(record.id) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(record.title, style = MaterialTheme.typography.titleMedium)
                    Text("${state.pets.firstOrNull { it.id == record.petId }?.name ?: "Mascota"} · ${record.date}")
                    Text(record.details, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
    if (showAddDialog && state.pets.isNotEmpty()) {
        AddHealthRecordDialog(
            pet = state.pets.first(),
            category = category,
            onDismiss = { showAddDialog = false },
            onSave = { record -> onAddRecord(record); showAddDialog = false }
        )
    }
}

@Composable
private fun AddHealthRecordDialog(
    pet: Pet,
    category: String,
    onDismiss: () -> Unit,
    onSave: (PetHealthRecord) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var details by rememberSaveable { mutableStateOf("") }
    var invalid by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar registro") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(pet.name, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(title, { title = it }, label = { Text("Nombre") }, singleLine = true)
                OutlinedTextField(date, { date = it }, label = { Text("Fecha (AAAA-MM-DD)") }, singleLine = true)
                OutlinedTextField(details, { details = it }, label = { Text("Detalle") }, minLines = 3)
                if (invalid) Text("Revisa el nombre y la fecha.", color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val validDate = runCatching { LocalDate.parse(date) }.isSuccess
                if (title.isBlank() || !validDate) invalid = true
                else onSave(PetHealthRecord(petId = pet.id, category = category, title = title, date = date, details = details))
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun ClinicalTestDetailsScreen(record: PetHealthRecord?, onBack: () -> Unit) {
    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        val title = when (record?.category) {
            "ALLERGY" -> "Detalle de alergia"
            "VISIT" -> "Detalle de visita"
            else -> "Detalle de análisis"
        }
        Text(title, style = MaterialTheme.typography.headlineMedium)
        if (record == null) Text("No encontramos este análisis.")
        else {
            Text(record.title, style = MaterialTheme.typography.titleLarge)
            Text("Fecha: ${record.date}")
            Text(record.details, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        OutlinedButton(onClick = onBack) { Text("Volver a análisis") }
    }
}

@Composable
fun AppliedVaccinesScreen(state: PetShieldUiState, onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Vacunas aplicadas", style = MaterialTheme.typography.headlineMedium)
        if (state.vaccines.isEmpty()) Text("Todavía no hay dosis registradas.")
        state.vaccines.forEach { vaccine ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(vaccine.name, style = MaterialTheme.typography.titleMedium)
                    Text("${state.pets.firstOrNull { it.id == vaccine.petId }?.name ?: "Mascota"} · ${vaccine.administeredAt}")
                    Text("Próxima dosis: ${vaccine.dueAt}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        OutlinedButton(onClick = { onNavigate(Screen.Pets.route) }) { Text("Gestionar carnet") }
    }
}

@Composable
fun VeterinaryHistoryScreen(state: PetShieldUiState, onSelectAppointment: (Long) -> Unit) {
    val visits = state.healthRecords.filter { it.category == "VISIT" }
    val completedAppointments = state.appointments.filter { it.status.name == "COMPLETED" }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Historial veterinario", style = MaterialTheme.typography.headlineMedium)
        visits.forEach { visit ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(visit.title, style = MaterialTheme.typography.titleMedium)
                    Text("${state.pets.firstOrNull { it.id == visit.petId }?.name ?: "Mascota"} · ${visit.date}")
                    Text(visit.details, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        completedAppointments.forEach { appointment ->
            Card(
                onClick = { onSelectAppointment(appointment.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(appointment.service, style = MaterialTheme.typography.titleMedium)
                    Text("${appointment.clinicName} · ${appointment.dateTime.replace('T', ' ')}")
                }
            }
        }
        if (visits.isEmpty() && completedAppointments.isEmpty()) Text("Aún no hay visitas en el historial.")
        Spacer(Modifier.height(8.dp))
    }
}