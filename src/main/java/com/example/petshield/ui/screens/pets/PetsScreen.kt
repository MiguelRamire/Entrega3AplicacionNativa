package com.example.petshield.ui.screens.pets

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.petshield.domain.model.VaccineRecord
import com.example.petshield.ui.navigation.Screen
import com.example.petshield.ui.screens.PetShieldUiState
import java.time.LocalDate

@Composable
fun PetsScreen(
    state: PetShieldUiState,
    onAddPet: (String, String, String, Int, Double) -> Unit,
    onDeletePet: (Long) -> Unit,
    onAddVaccine: (VaccineRecord) -> Unit,
    onNavigate: (String) -> Unit
) {
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var showVaccineDialog by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Mis mascotas", style = MaterialTheme.typography.headlineMedium)
                Text("Información y cuidado", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            OutlinedButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Agregar")
            }
        }
        if (state.pets.isEmpty()) {
            Text("Todavía no agregas mascotas.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        state.pets.forEach { pet ->
            PetCard(pet = pet, onDelete = { onDeletePet(pet.id) })
        }
        OutlinedButton(onClick = { onNavigate(Screen.AddPet.route) }, modifier = Modifier.fillMaxWidth()) {
            Text("Agregar mascota")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Carnet de vacunas", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleLarge)
            OutlinedButton(onClick = { showVaccineDialog = true }) { Text("Registrar") }
        }
        if (state.vaccines.isEmpty()) {
            Text("Aún no hay vacunas registradas.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            state.vaccines.forEach { vaccine ->
                VaccineCard(
                    vaccine = vaccine,
                    petName = state.pets.firstOrNull { it.id == vaccine.petId }?.name.orEmpty()
                )
            }
        }
        OutlinedButton(onClick = { onNavigate(Screen.HealthRecordMenu.route) }, modifier = Modifier.fillMaxWidth()) {
            Text("Ver todo el carnet de salud")
        }
        Spacer(Modifier.height(8.dp))
    }
    if (showAddDialog) {
        AddPetDialog(
            onDismiss = { showAddDialog = false },
            onSave = { name, species, breed, age, weight ->
                onAddPet(name, species, breed, age, weight)
                showAddDialog = false
            }
        )
    }
    if (showVaccineDialog) {
        AddVaccineDialog(
            pets = state.pets,
            onDismiss = { showVaccineDialog = false },
            onSave = { vaccine ->
                onAddVaccine(vaccine)
                showVaccineDialog = false
            }
        )
    }
}

@Composable
private fun PetCard(pet: Pet, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(Icons.Default.Pets, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text(pet.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${pet.species} · ${pet.breed} · ${pet.ageYears} años · ${pet.weightKg} kg",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar ${pet.name}")
            }
        }
    }
}

@Composable
private fun VaccineCard(vaccine: VaccineRecord, petName: String) {
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
            Icon(Icons.Default.Vaccines, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column {
                Text(vaccine.name, style = MaterialTheme.typography.titleMedium)
                Text("$petName · Próxima dosis: ${vaccine.dueAt}")
                Text("${vaccine.veterinarian} · Aplicada ${vaccine.administeredAt}")
            }
        }
    }
}

@Composable
private fun AddPetDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String, Int, Double) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var species by rememberSaveable { mutableStateOf("Perro") }
    var breed by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }
    var invalid by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar mascota") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Perro", "Gato", "Otro").forEach { option ->
                        FilterChip(
                            selected = species == option,
                            onClick = { species = option },
                            label = { Text(option) }
                        )
                    }
                }
                OutlinedTextField(breed, { breed = it }, label = { Text("Raza") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        age,
                        { age = it.filter(Char::isDigit) },
                        modifier = Modifier.weight(1f),
                        label = { Text("Edad (años)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        weight,
                        { weight = it.filter { char -> char.isDigit() || char == '.' || char == ',' } },
                        modifier = Modifier.weight(1f),
                        label = { Text("Peso (kg)") },
                        singleLine = true
                    )
                }
                if (invalid) Text("Completa el nombre, la edad y el peso.", color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val parsedAge = age.toIntOrNull()
                val parsedWeight = weight.replace(',', '.').toDoubleOrNull()
                if (name.isBlank() || parsedAge == null || parsedWeight == null) invalid = true
                else onSave(name, species, breed, parsedAge, parsedWeight)
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
private fun AddVaccineDialog(
    pets: List<Pet>,
    onDismiss: () -> Unit,
    onSave: (VaccineRecord) -> Unit
) {
    var selectedPet by remember(pets) { mutableStateOf(pets.firstOrNull()) }
    var menuExpanded by remember { mutableStateOf(false) }
    var name by rememberSaveable { mutableStateOf("") }
    var administeredAt by rememberSaveable { mutableStateOf("") }
    var dueAt by rememberSaveable { mutableStateOf("") }
    var veterinarian by rememberSaveable { mutableStateOf("") }
    var invalid by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar vacuna") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (pets.isEmpty()) {
                    Text("Agrega una mascota antes de registrar una vacuna.")
                } else {
                    Box {
                        OutlinedButton(onClick = { menuExpanded = true }) {
                            Text(selectedPet?.name ?: "Selecciona una mascota")
                        }
                        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                            pets.forEach { pet ->
                                DropdownMenuItem(
                                    text = { Text(pet.name) },
                                    onClick = { selectedPet = pet; menuExpanded = false }
                                )
                            }
                        }
                    }
                    OutlinedTextField(name, { name = it }, label = { Text("Vacuna") }, singleLine = true)
                    OutlinedTextField(
                        administeredAt,
                        { administeredAt = it },
                        label = { Text("Fecha de aplicación (AAAA-MM-DD)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        dueAt,
                        { dueAt = it },
                        label = { Text("Próxima dosis (AAAA-MM-DD)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        veterinarian,
                        { veterinarian = it },
                        label = { Text("Veterinario/a") },
                        singleLine = true
                    )
                    if (invalid) Text("Revisa los datos y las fechas.", color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = pets.isNotEmpty(),
                onClick = {
                    val pet = selectedPet ?: return@TextButton
                    val validDates = runCatching {
                        LocalDate.parse(administeredAt)
                        LocalDate.parse(dueAt)
                    }.isSuccess
                    if (name.isBlank() || veterinarian.isBlank() || !validDates) {
                        invalid = true
                    } else {
                        onSave(
                            VaccineRecord(
                                petId = pet.id,
                                name = name.trim(),
                                administeredAt = administeredAt,
                                dueAt = dueAt,
                                veterinarian = veterinarian.trim()
                            )
                        )
                    }
                }
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}