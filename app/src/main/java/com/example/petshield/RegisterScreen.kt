<<<<<<< HEAD
package com.example.petshield // Verifica que coincida con tu paquete
=======
package com.example.petshield
>>>>>>> origin/main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
<<<<<<< HEAD
import androidx.compose.foundation.shape.CircleShape
=======
>>>>>>> origin/main
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
<<<<<<< HEAD
=======
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
>>>>>>> origin/main
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.graphics.Brush
=======
>>>>>>> origin/main
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
<<<<<<< HEAD
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
=======
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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

@Composable
fun RegisterScreen(
    viewModel: PetShieldViewModel,
>>>>>>> origin/main
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
<<<<<<< HEAD
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val colorDegradadoInicio = Color(0xFF33E4DB)
    val colorDegradadoFin = Color(0xFF00BBD3)
    val colorFondoInput = Color(0xFFE9F6FE)
=======
    var passwordVisible by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }
>>>>>>> origin/main

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
<<<<<<< HEAD
                .background(Brush.horizontalGradient(listOf(colorDegradadoInicio, colorDegradadoFin)))
=======
                .background(PetShieldGradient)
>>>>>>> origin/main
                .padding(top = 30.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
            }
            Text(text = "Nueva Cuenta", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Image(
                painter = painterResource(id = R.drawable.ic_logo_blanco),
                contentDescription = "Logo",
                modifier = Modifier.size(30.dp)
            )
        }

<<<<<<< HEAD
        // --- CONTENIDO DESLIZABLE (SCROLL) ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()), // Activa el scroll al abrir el teclado
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "¡Bienvenido A PetShield!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorDegradadoInicio,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Campo Nombre
            Text(text = "Nombre completo", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("sara maria perez", color = colorDegradadoInicio) },
=======
        // --- FORMULARIO CON SCROLL ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crea tu Cuenta",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = CelesteTexto,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            // 1. Campo Nombre
            Text(text = "Nombre completo", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextoMedio, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; errorMessage = null },
                placeholder = { Text("Ej. Juan Pérez", color = TextoPlaceholder) },
>>>>>>> origin/main
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
<<<<<<< HEAD
                    focusedContainerColor = colorFondoInput,
                    unfocusedContainerColor = colorFondoInput,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorDegradadoInicio
=======
                    focusedContainerColor = FondoClaro,
                    unfocusedContainerColor = FondoClaro,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = CelesteInicio
>>>>>>> origin/main
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Campo Contraseña
<<<<<<< HEAD
            Text(text = "Contraseña", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("*************", color = colorDegradadoInicio) },
                visualTransformation = PasswordVisualTransformation(),
=======
            Text(text = "Contraseña", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextoMedio, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                placeholder = { Text("Mínimo 6 caracteres", color = TextoPlaceholder) },
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
>>>>>>> origin/main
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
<<<<<<< HEAD
                    focusedContainerColor = colorFondoInput,
                    unfocusedContainerColor = colorFondoInput,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorDegradadoInicio
=======
                    focusedContainerColor = FondoClaro,
                    unfocusedContainerColor = FondoClaro,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = CelesteInicio
>>>>>>> origin/main
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Campo Correo
<<<<<<< HEAD
            Text(text = "Correo electrónico", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("example@example.com", color = colorDegradadoInicio) },
=======
            Text(text = "Correo electrónico", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextoMedio, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                placeholder = { Text("example@example.com", color = TextoPlaceholder) },
>>>>>>> origin/main
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
<<<<<<< HEAD
                    focusedContainerColor = colorFondoInput,
                    unfocusedContainerColor = colorFondoInput,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorDegradadoInicio
=======
                    focusedContainerColor = FondoClaro,
                    unfocusedContainerColor = FondoClaro,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = CelesteInicio
>>>>>>> origin/main
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Campo Teléfono
<<<<<<< HEAD
            Text(text = "Número de teléfono", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = { Text("+01 111-1111111", color = colorDegradadoInicio) },
=======
            Text(text = "Número de teléfono", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextoMedio, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it; errorMessage = null },
                placeholder = { Text("+57 300 000 0000", color = TextoPlaceholder) },
>>>>>>> origin/main
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
<<<<<<< HEAD
                    focusedContainerColor = colorFondoInput,
                    unfocusedContainerColor = colorFondoInput,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorDegradadoInicio
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- FOOTER (Términos, Botones y Login) ---
            Text(
                text = "Al continuar, aceptas los\nTérminos de Uso y Política de Privacidad.",
                fontSize = 10.sp,
                color = Color.Gray,
=======
                    focusedContainerColor = FondoClaro,
                    unfocusedContainerColor = FondoClaro,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = CelesteInicio
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Al continuar, aceptas los\nTérminos de Uso y Política de Privacidad.",
                fontSize = 12.sp,
                color = TextoSecundario,
>>>>>>> origin/main
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

<<<<<<< HEAD
            Button(
                onClick = { /* Lógica de registro */ },
=======
            // BOTÓN REGISTRARSE
            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank() || password.isBlank()) {
                        errorMessage = "Por favor completa todos los campos obligatorios"
                        return@Button
                    }
                    if (password.length < 6) {
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"
                        return@Button
                    }
                    cargando = true
                    errorMessage = null

                    viewModel.registrarUsuario(email, password, name, phone) { exito, errorMsg ->
                        cargando = false
                        if (exito) {
                            onNavigateToLogin()
                        } else {
                            errorMessage = errorMsg ?: "Error al registrar el usuario"
                        }
                    }
                },
                enabled = !cargando,
>>>>>>> origin/main
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
<<<<<<< HEAD
                        brush = Brush.horizontalGradient(listOf(colorDegradadoInicio, colorDegradadoFin)),
=======
                        brush = PetShieldGradient,
>>>>>>> origin/main
                        shape = RoundedCornerShape(25.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
<<<<<<< HEAD
                Text("Registrarse", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("o registrate con:", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(colorDegradadoInicio, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("G", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Text("¿Ya tienes cuenta? ", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = "Inicia sesion",
                    fontSize = 12.sp,
                    color = colorDegradadoFin,
=======
                if (cargando) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Registrarse", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text("¿Ya tienes una cuenta? ", fontSize = 14.sp, color = TextoSecundario)
                Text(
                    text = "Inicia Sesión",
                    fontSize = 14.sp,
                    color = CelesteTexto,
>>>>>>> origin/main
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

<<<<<<< HEAD
            Spacer(modifier = Modifier.height(32.dp)) // Espacio final para que el scroll termine limpio
        }
    }
}
=======
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
>>>>>>> origin/main
