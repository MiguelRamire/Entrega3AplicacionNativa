package com.example.petshield.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.petshield.domain.model.NotificationPreferences
import com.example.petshield.ui.navigation.Screen
import com.example.petshield.ui.screens.PetShieldUiState

@Composable
fun AccountFlowScreen(
    screen: Screen,
    state: PetShieldUiState,
    onNavigate: (String) -> Unit,
    onSaveProfile: (String, String, String) -> Unit,
    onSaveNotificationPreferences: (NotificationPreferences) -> Unit,
    onChangePassword: (String, String, String) -> Unit,
    onSubmitSupport: (String, String) -> Unit,
    onClearMessage: () -> Unit
) {
    LaunchedEffect(screen) { onClearMessage() }
    when (screen) {
        Screen.EditProfile -> EditProfileScreen(state, onSaveProfile)
        Screen.Settings -> SettingsScreen(onNavigate)
        Screen.NotificationSettings -> NotificationSettingsScreen(state, onSaveNotificationPreferences)
        Screen.PasswordManagement -> PasswordManagementScreen(state, onChangePassword)
        Screen.PrivacyPolicy -> PrivacyPolicyScreen()
        Screen.HelpFaq -> FaqScreen()
        Screen.HelpContact -> HelpContactScreen(state, onSubmitSupport)
        else -> Text("Sección no disponible.")
    }
}

@Composable
private fun EditProfileScreen(state: PetShieldUiState, onSave: (String, String, String) -> Unit) {
    val account = state.ownerAccount
    var name by rememberSaveable(account?.email) { mutableStateOf(account?.name.orEmpty()) }
    var email by rememberSaveable(account?.email) { mutableStateOf(account?.email.orEmpty()) }
    var phone by rememberSaveable(account?.email) { mutableStateOf(account?.phone.orEmpty()) }
    AccountPage("Editar perfil", "Mantén tus datos de contacto actualizados.") {
        OutlinedTextField(name, { name = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Nombre") })
        OutlinedTextField(email, { email = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Correo electrónico") })
        OutlinedTextField(phone, { phone = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Teléfono") })
        state.actionMessage?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Button(onClick = { onSave(name, email, phone) }, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar cambios")
        }
    }
}

@Composable
private fun SettingsScreen(onNavigate: (String) -> Unit) {
    AccountPage("Configuración", "Preferencias de tu cuenta.") {
        SettingsAction("Notificaciones", Screen.NotificationSettings.route, onNavigate)
        SettingsAction("Contraseña", Screen.PasswordManagement.route, onNavigate)
        SettingsAction("Privacidad", Screen.PrivacyPolicy.route, onNavigate)
        SettingsAction("Preguntas frecuentes", Screen.HelpFaq.route, onNavigate)
        SettingsAction("Contactar soporte", Screen.HelpContact.route, onNavigate)
    }
}

@Composable
private fun NotificationSettingsScreen(
    state: PetShieldUiState,
    onSave: (NotificationPreferences) -> Unit
) {
    val preferences = state.notificationPreferences
    AccountPage("Configuración de notificaciones", "Elige qué avisos quieres recibir.") {
        PreferenceRow("Recordatorios de citas", preferences.appointmentReminders) {
            onSave(preferences.copy(appointmentReminders = it))
        }
        PreferenceRow("Vacunas y salud", preferences.healthReminders) {
            onSave(preferences.copy(healthReminders = it))
        }
        PreferenceRow("Novedades y promociones", preferences.promotions) {
            onSave(preferences.copy(promotions = it))
        }
    }
}

@Composable
private fun PasswordManagementScreen(
    state: PetShieldUiState,
    onChangePassword: (String, String, String) -> Unit
) {
    var current by rememberSaveable { mutableStateOf("") }
    var next by rememberSaveable { mutableStateOf("") }
    var confirmation by rememberSaveable { mutableStateOf("") }
    AccountPage("Gestión de contraseña", "Actualiza la contraseña de tu cuenta.") {
        OutlinedTextField(current, { current = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Contraseña actual") }, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(next, { next = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Nueva contraseña") }, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(confirmation, { confirmation = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Confirmar contraseña") }, visualTransformation = PasswordVisualTransformation())
        Button(onClick = { onChangePassword(current, next, confirmation) }, modifier = Modifier.fillMaxWidth()) {
            Text("Actualizar contraseña")
        }
        state.actionMessage?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
    }
}

@Composable
private fun PrivacyPolicyScreen() {
    AccountPage("Política de privacidad", "Tus datos y los de tus mascotas.") {
        Text(
            "Pet Shield guarda en este dispositivo la información de perfil, mascotas, citas y salud que agregas. " +
                "Los datos se usan para mostrar tu historial y organizar recordatorios. No se envían a un servidor en esta versión.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text("Control de tus datos", style = MaterialTheme.typography.titleMedium)
        Text("Puedes revisar o quitar la información desde cada sección de la app.")
        Text("Permisos", style = MaterialTheme.typography.titleMedium)
        Text("La versión actual no solicita acceso a ubicación, contactos ni archivos.")
    }
}

@Composable
private fun FaqScreen() {
    val questions = listOf(
        "¿Cómo agrego una mascota?" to "Abre Mascotas, toca Agregar y completa sus datos básicos.",
        "¿Dónde veo las vacunas?" to "Entra al carnet desde Mascotas para consultar o registrar dosis.",
        "¿Cómo reservo una cita?" to "Elige una clínica, selecciona mascota y fecha, y confirma la reserva.",
        "¿Dónde se guardan mis datos?" to "En esta versión se conservan localmente en el dispositivo."
    )
    var expandedQuestion by rememberSaveable { mutableStateOf(-1) }
    AccountPage("Preguntas frecuentes", "Respuestas rápidas sobre Pet Shield.") {
        questions.forEachIndexed { index, (question, answer) ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    TextButton(onClick = { expandedQuestion = if (expandedQuestion == index) -1 else index }) {
                        Text(question, modifier = Modifier.fillMaxWidth())
                    }
                    if (expandedQuestion == index) Text(answer, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun HelpContactScreen(state: PetShieldUiState, onSubmit: (String, String) -> Unit) {
    var subject by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    AccountPage("Centro de ayuda", "La consulta queda guardada localmente para su gestión por soporte.") {
        OutlinedTextField(subject, { subject = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Asunto") })
        OutlinedTextField(
            message, { message = it }, modifier = Modifier.fillMaxWidth(),
            label = { Text("Mensaje") }, minLines = 4
        )
        Button(onClick = { onSubmit(subject, message) }, modifier = Modifier.fillMaxWidth()) {
            Text("Enviar consulta")
        }
        state.actionMessage?.let {
            Text(it, color = if (it.startsWith("No se pudo") || it.startsWith("Completa")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun AccountPage(title: String, subtitle: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(2.dp))
        content()
    }
}

@Composable
private fun SettingsAction(label: String, route: String, onNavigate: (String) -> Unit) {
    TextButton(onClick = { onNavigate(route) }, modifier = Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f))
        Text("›")
    }
}

@Composable
private fun PreferenceRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        Checkbox(checked = checked, onCheckedChange = onChange)
    }
}