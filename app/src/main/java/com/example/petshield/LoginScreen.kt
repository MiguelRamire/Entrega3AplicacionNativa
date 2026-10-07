package com.example.petshield

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petshield.ui.theme.CelesteFin
import com.example.petshield.ui.theme.CelesteInicio
import com.example.petshield.ui.theme.CelesteTexto
import com.example.petshield.ui.theme.ErrorRedBg
import com.example.petshield.ui.theme.ErrorRedText
import com.example.petshield.ui.theme.FondoClaro
import com.example.petshield.ui.theme.PetShieldGradient
import com.example.petshield.ui.theme.TextoMedio
import com.example.petshield.ui.theme.TextoPlaceholder
import com.example.petshield.ui.theme.TextoSecundario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToRecover: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                cargando = true
                viewModel.iniciarSesionConGoogle(idToken) { exito, errorMsg ->
                    cargando = false
                    if (exito) {
                        onLoginSuccess()
                    } else {
                        errorMessage = errorMsg ?: "Error al iniciar sesión con Google"
                    }
                }
            } else {
                errorMessage = "No se pudo obtener el token de Google"
            }
        } catch (e: ApiException) {
            errorMessage = "Error en Google Sign-In: ${e.localizedMessage}"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- BARRA SUPERIOR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(PetShieldGradient)
                .padding(top = 30.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
            }
            Text(text = "Iniciar Sesión", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Image(
                painter = painterResource(id = R.drawable.ic_logo_blanco),
                contentDescription = "Logo",
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- CONTENIDO CENTRAL ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "¡Bienvenido De Nuevo!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = CelesteTexto
            )

            Spacer(modifier = Modifier.height(24.dp))

            errorMessage?.let { msg ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = ErrorRedBg),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = msg,
                        color = ErrorRedText,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Campo Correo
            Text(text = "Correo Electrónico", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextoMedio)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                placeholder = { Text("example@example.com", color = TextoPlaceholder) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = FondoClaro,
                    unfocusedContainerColor = FondoClaro,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = CelesteInicio
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            Text(text = "Contraseña", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextoMedio)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                placeholder = { Text("*************", color = TextoPlaceholder) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = TextoMedio
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = FondoClaro,
                    unfocusedContainerColor = FondoClaro,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = CelesteInicio
                )
            )

            // ¿Olvidaste tu contraseña?
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = CelesteTexto,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
                    .clickable { onNavigateToRecover() }
            )

            Spacer(modifier = Modifier.weight(1f))

            // --- BOTÓN INICIAR SESIÓN ---
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Por favor ingresa tu correo y contraseña"
                        return@Button
                    }
                    cargando = true
                    errorMessage = null

                    viewModel.iniciarSesion(email, password) { exito, errorMsg ->
                        cargando = false
                        if (exito) {
                            onLoginSuccess()
                        } else {
                            errorMessage = errorMsg ?: "Error al iniciar sesión"
                        }
                    }
                },
                enabled = !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        brush = PetShieldGradient,
                        shape = RoundedCornerShape(25.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                if (cargando) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Iniciar Sesión", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("o inicia sesión con:", fontSize = 14.sp, color = TextoSecundario)
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(CelesteInicio, CircleShape)
                        .clickable {
                            try {
                                val webClientIdResId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
                                if (webClientIdResId != 0) {
                                    val webClientId = context.getString(webClientIdResId)
                                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(webClientId)
                                        .requestEmail()
                                        .build()
                                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                                    googleSignInClient.signOut().addOnCompleteListener {
                                        googleSignInLauncher.launch(googleSignInClient.signInIntent)
                                    }
                                } else {
                                    errorMessage = "Descargue el nuevo google-services.json desde Firebase Console tras activar Google Sign-In"
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error al iniciar Google Sign-In: ${e.localizedMessage}"
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("G", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    Text("¿No tienes cuenta? ", fontSize = 14.sp, color = TextoSecundario)
                    Text(
                        text = "Crea una",
                        fontSize = 14.sp,
                        color = CelesteTexto,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToRegister() }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
