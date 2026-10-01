package com.example.petshield.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun SplashScreen(onContinue: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Pets, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            Spacer(Modifier.height(18.dp))
            Text("Pet Shield", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimary)
            Text("Cuidamos de quienes te acompañan.", color = MaterialTheme.colorScheme.onPrimary)
            Spacer(Modifier.height(36.dp))
            Button(onClick = onContinue) { Text("Comenzar") }
        }
    }
}

@Composable
fun WelcomeScreen(onStart: () -> Unit, onLogin: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(20.dp))
            Text("Bienvenido a Pet Shield", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                "La información veterinaria de tus mascotas, organizada en un solo lugar.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(28.dp))
            Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) { Text("Conocer la app") }
            TextButton(onClick = onLogin, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Ya tengo una cuenta")
            }
        }
    }
}

@Composable
fun OnboardingScreen(
    page: Int,
    onContinue: () -> Unit,
    onSkip: () -> Unit
) {
    val content = when (page) {
        0 -> "Encuentra atención veterinaria cerca de ti."
        1 -> "Organiza y sigue las citas de cada mascota."
        else -> "Consulta vacunas, alergias y su historial de salud."
    }
    val title = when (page) {
        0 -> "Veterinarios de confianza"
        1 -> "Citas sin perder el hilo"
        else -> "Su salud, bien cuidada"
    }
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(28.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Pets, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(28.dp))
        Text(title, style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(10.dp))
        Text(content, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(28.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(3) { index ->
                Surface(
                    color = if (index == page) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                ) {
                    Spacer(Modifier.padding(horizontal = if (index == page) 14.dp else 6.dp, vertical = 3.dp))
                }
            }
        }
        Spacer(Modifier.height(28.dp))
        Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
            Text(if (page == 2) "Ir al acceso" else "Siguiente")
        }
        TextButton(onClick = onSkip, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Omitir")
        }
    }
}

@Composable
fun RegisterScreen(
    onRegister: (String, String, String) -> Unit,
    onLogin: () -> Unit,
    message: String?,
    isBusy: Boolean,
    onClearMessage: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf(false) }
    EntryFormPage(title = "Crear cuenta", subtitle = "Registra tu perfil para empezar.") {
        OutlinedTextField(name, { name = it; onClearMessage() }, modifier = Modifier.fillMaxWidth(), label = { Text("Nombre") }, singleLine = true)
        OutlinedTextField(email, { email = it; onClearMessage() }, modifier = Modifier.fillMaxWidth(), label = { Text("Correo electrónico") }, singleLine = true)
        OutlinedTextField(
            password, { password = it; onClearMessage() }, modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        if (error) Text("Completa los datos y usa una contraseña de 8 caracteres o más.", color = MaterialTheme.colorScheme.error)
        message?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(
            onClick = {
                if (name.isNotBlank() && email.contains('@') && password.length >= 8) onRegister(name, email, password)
                else error = true
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isBusy
        ) { Text(if (isBusy) "Creando cuenta..." else "Crear cuenta") }
        TextButton(onClick = onLogin, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Ya tengo una cuenta") }
    }
}

@Composable
fun RecoverPasswordScreen(
    onRequest: (String) -> Unit,
    onBackToLogin: () -> Unit,
    message: String?
) {
    var email by rememberSaveable { mutableStateOf("") }
    var invalid by rememberSaveable { mutableStateOf(false) }
    EntryFormPage(title = "Recuperar contraseña", subtitle = "Registra una solicitud local de recuperación.") {
        OutlinedTextField(email, { email = it; invalid = false }, modifier = Modifier.fillMaxWidth(), label = { Text("Correo electrónico") }, singleLine = true)
        if (invalid) Text("Ingresa un correo válido.", color = MaterialTheme.colorScheme.error)
        message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Button(
            onClick = { if (email.contains('@')) onRequest(email) else invalid = true },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Registrar solicitud") }
        TextButton(onClick = onBackToLogin, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Volver al acceso") }
    }
}

@Composable
private fun EntryFormPage(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(6.dp))
        Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(20.dp))
        content()
    }
}