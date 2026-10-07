package com.example.petshield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
// Si te marca error en la R, importa tu paquete: import com.example.petshield.R

@Composable
fun SplashScreen(
    isUserLoggedIn: Boolean,
    onNavigateNext: (Boolean) -> Unit
) {
    val colorInicio = Color(0xFF33E4DB)
    val colorFin = Color(0xFF00BBD3)

    LaunchedEffect(key1 = true) {
        delay(2500L)
        onNavigateNext(isUserLoggedIn)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(colorInicio, colorFin))),
        contentAlignment = Alignment.Center
    ) {
        // Column agrupa elementos verticalmente (uno debajo de otro)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. El ícono (Asegúrate de que la imagen en drawable se llame ic_logo_blanco)
            Image(
                painter = painterResource(id = R.drawable.ic_logo_blanco),
                contentDescription = "Icono PetShield",
                modifier = Modifier.size(120.dp)
            )

            // Espacio entre el ícono y las letras
            Spacer(modifier = Modifier.height(16.dp))

            // 2. El texto nativo
            Text(
                text = "PetShield",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}