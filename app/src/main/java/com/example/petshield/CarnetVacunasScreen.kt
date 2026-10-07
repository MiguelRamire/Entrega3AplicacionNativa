package com.example.petshield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CarnetVacunasScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onAgregarMascota: () -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    val colorCelesteInicio = Color(0xFF33E4DB)
    val colorCelesteFin = Color(0xFF00BBD3)
    val fondoClaro = Color(0xFFE9F6FE)

    val mascotas = viewModel.mascotas

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Carnet De Vacunas",
                onBack = onBack,
                onNavigateToHome = { onNavigateToTab(BottomTab.HOME) }
            )
        },
        bottomBar = {
            PetShieldBottomBar(
                currentTab = BottomTab.HOME,
                onNavigateToTab = onNavigateToTab
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (mascotas.isEmpty()) Arrangement.Center else Arrangement.Top
        ) {
            if (mascotas.isEmpty()) {
                // ESTADO VACÍO (MOCKUP 10 - A)
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(fondoClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_carnet),
                        contentDescription = "Carnet Vacío",
                        tint = colorCelesteFin,
                        modifier = Modifier.size(70.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Aún No Has\nRegistrado Datos\nDe Tu Mascota",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorCelesteFin,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = onAgregarMascota,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                        .background(
                            brush = Brush.horizontalGradient(listOf(colorCelesteInicio, colorCelesteFin)),
                            shape = RoundedCornerShape(25.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Text(
                        text = "Agregar mascotas",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // ESTADO CON MASCOTAS
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mascotas Registradas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorCelesteFin
                    )

                    Button(
                        onClick = onAgregarMascota,
                        colors = ButtonDefaults.buttonColors(containerColor = fondoClaro),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = "+ Agregar otra", color = colorCelesteFin, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(mascotas) { mascota ->
                        MascotaCarnetCard(mascota = mascota)
                    }
                }
            }
        }
    }
}

@Composable
fun MascotaCarnetCard(mascota: Mascota) {
    val colorCelesteFin = Color(0xFF00BBD3)
    val fondoClaro = Color(0xFFE9F6FE)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, colorCelesteFin)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(fondoClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        tint = colorCelesteFin,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = mascota.nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorCelesteFin
                    )
                    Text(
                        text = "${mascota.especie} • ${mascota.raza} (${mascota.peso})",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = fondoClaro)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Registro de Vacunación",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(6.dp))

            mascota.vacunas.forEach { vacuna ->
                Row(
                    modifier = Modifier.padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = colorCelesteFin,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = vacuna, fontSize = 12.sp, color = Color.DarkGray)
                }
            }
        }
    }
}
