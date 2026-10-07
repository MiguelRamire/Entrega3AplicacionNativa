package com.example.petshield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToRecover: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val colorDegradadoInicio = Color(0xFF33E4DB)
    val colorDegradadoFin = Color(0xFF00BBD3)
    val colorFondoInput = Color(0xFFE9F6FE)

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
                .background(Brush.horizontalGradient(listOf(colorDegradadoInicio, colorDegradadoFin)))
                .padding(top = 30.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
            }
            Text(text = "Iniciar Sesión", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Image(
                painter = painterResource(id = R.drawable.ic_logo_blanco), // Corregido el nombre del recurso
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
                color = colorDegradadoInicio
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo Correo
            Text(text = "Correo o Teléfono", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("example@example.com", color = colorDegradadoInicio) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = colorFondoInput,
                    unfocusedContainerColor = colorFondoInput,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorDegradadoInicio
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            Text(text = "Contraseña", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("*************", color = colorDegradadoInicio) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = colorFondoInput,
                    unfocusedContainerColor = colorFondoInput,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorDegradadoInicio
                )
            )

            // ¿Olvidaste tu contraseña?
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = colorDegradadoFin,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
                    .clickable { onNavigateToRecover() }
            )

            Spacer(modifier = Modifier.weight(1f))

            // --- CONTENIDO INFERIOR ---
            Button(
                onClick = { onLoginSuccess() }, // <--- 2. Lo ejecutas al hacer clic
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        brush = Brush.horizontalGradient(listOf(colorDegradadoInicio, colorDegradadoFin)),
                        shape = RoundedCornerShape(25.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Text("Iniciar Sesión", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("o inicia sesion con:", fontSize = 12.sp, color = Color.Gray)
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
                    Text("¿No tienes cuenta? ", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = "Crea una",
                        fontSize = 12.sp,
                        color = colorDegradadoFin,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToRegister() } // Ahora avisa correctamente al MainActivity
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}