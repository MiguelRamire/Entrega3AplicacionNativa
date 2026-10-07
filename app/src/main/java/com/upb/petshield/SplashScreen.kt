package com.upb.petshield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.petshield.ui.theme.PetShieldGradient
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToWelcome: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(2000L)
        onNavigateToWelcome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PetShieldGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_blanco),
                contentDescription = "Icono PetShield",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "PetShield",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
