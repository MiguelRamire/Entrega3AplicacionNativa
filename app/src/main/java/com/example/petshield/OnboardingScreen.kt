<<<<<<< HEAD
package com.example.petshield // Asegúrate de que coincida con tu paquete
=======
package com.example.petshield
>>>>>>> origin/main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
<<<<<<< HEAD
=======
import com.example.petshield.ui.theme.CelesteFin
import com.example.petshield.ui.theme.CelesteTexto
import com.example.petshield.ui.theme.FondoClaro
import com.example.petshield.ui.theme.PetShieldGradient
import com.example.petshield.ui.theme.TextoSecundario
>>>>>>> origin/main
import kotlinx.coroutines.launch

data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

<<<<<<< HEAD
    val colorDegradadoInicio = Color(0xFF33E4DB)
    val colorDegradadoFin = Color(0xFF00BBD3)
    val colorPuntoInactivo = Color(0xFFE9F6FE)

    // Definimos las 3 páginas con sus recursos de imagen y textos oficiales
    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.a_onboardin, // Asegúrate de tener los drawables generados
            title = "Gestiona Tus\nMascotas",
            description = "PetShield te ayuda a guardar toda la información medica de tus mascotas en un solo lugar."
=======
    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.a_onboardin,
            title = "Gestiona Tus\nMascotas",
            description = "PetShield te ayuda a guardar toda la información médica de tus mascotas en un solo lugar."
>>>>>>> origin/main
        ),
        OnboardingPage(
            imageRes = R.drawable.b_onboardin,
            title = "Agenda Tus Citas\nVeterinarias",
            description = "PetShield te ayuda a encontrar clínicas para tus mascotas y nunca olvidar tus citas."
        ),
        OnboardingPage(
            imageRes = R.drawable.c_onboardin,
            title = "Consulta El\nCarnet De\nVacunas",
            description = "PetShield te ayuda a mantener al día la salud de tu mascota con un seguimiento completo de los tratamientos de tus mascotas."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 40.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
<<<<<<< HEAD
        // --- BOTÓN OMITIR SUPERIOR ---
=======
>>>>>>> origin/main
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (pagerState.currentPage < 2) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onFinishOnboarding() }
                ) {
                    Text(
                        text = "Omitir",
<<<<<<< HEAD
                        color = colorDegradadoFin,
                        fontSize = 14.sp,
=======
                        color = CelesteTexto,
                        fontSize = 16.sp,
>>>>>>> origin/main
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = ">",
<<<<<<< HEAD
                        color = colorDegradadoFin,
                        fontSize = 14.sp,
=======
                        color = CelesteTexto,
                        fontSize = 16.sp,
>>>>>>> origin/main
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

<<<<<<< HEAD
        // --- PAGER DESLIZABLE ---
=======
>>>>>>> origin/main
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            val item = pages[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
<<<<<<< HEAD
                // Imagen de la ilustración de la pantalla
=======
>>>>>>> origin/main
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = "Ilustración Onboarding",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

<<<<<<< HEAD
                // Título principal
=======
>>>>>>> origin/main
                Text(
                    text = item.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
<<<<<<< HEAD
                    color = colorDegradadoFin,
=======
                    color = CelesteTexto,
>>>>>>> origin/main
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

<<<<<<< HEAD
                // Descripción
                Text(
                    text = item.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
=======
                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
>>>>>>> origin/main
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

<<<<<<< HEAD
        // --- INDICADORES DE PUNTOS ---
=======
>>>>>>> origin/main
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isSelected) 10.dp else 8.dp)
                        .background(
<<<<<<< HEAD
                            brush = if (isSelected) Brush.horizontalGradient(listOf(colorDegradadoInicio, colorDegradadoFin)) else Brush.linearGradient(listOf(colorPuntoInactivo, colorPuntoInactivo)),
=======
                            brush = if (isSelected) PetShieldGradient else Brush.linearGradient(listOf(FondoClaro, FondoClaro)),
>>>>>>> origin/main
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

<<<<<<< HEAD
        // --- BOTÓN INFERIOR (Siguiente / Comenzar) ---
=======
>>>>>>> origin/main
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Button(
                onClick = {
                    if (pagerState.currentPage < 2) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onFinishOnboarding()
                    }
                },
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
                Text(
                    text = if (pagerState.currentPage == 2) "Comenzar" else "Siguiente",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/main
