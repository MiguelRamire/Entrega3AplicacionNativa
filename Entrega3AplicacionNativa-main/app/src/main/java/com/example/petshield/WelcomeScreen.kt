package com.example.petshield // Asegúrate de que coincida con tu paquete

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // Colores extraídos de tu Figma
    val colorDegradadoInicio = Color(0xFF33E4DB)
    val colorDegradadoFin = Color(0xFF00BBD3)
    val colorBotonRegistro = Color(0xFFE9F6FE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp), // Márgenes laterales
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Logo (Requiere que exportes solo el logo en color desde Figma)
        Image(
            painter = painterResource(id = R.drawable.ic_icono_color),
            contentDescription = "Logo PetShield Color",
            modifier = Modifier.size(140.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Título principal
        Text(
            text = "PetShield",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = colorDegradadoFin
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 3. Texto descriptivo
        Text(
            text = "PetShield te ayuda a mantener al día la salud de tu mascota con un seguimiento completo de vacunas, desparasitaciones y citas veterinarias.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // 4. Botón Iniciar Sesión (Con Degradado)
        Button(
            onClick = onNavigateToLogin,
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
            Text(text = "Iniciar Sesión", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Botón Registrarse
        Button(
            onClick = onNavigateToRegister,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorBotonRegistro),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(text = "Registrarse", color = colorDegradadoFin, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}