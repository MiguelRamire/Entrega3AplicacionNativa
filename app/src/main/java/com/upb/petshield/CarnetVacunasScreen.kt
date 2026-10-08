package com.upb.petshield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.petshield.data.Mascota
import com.upb.petshield.ui.theme.CelesteFin
import com.upb.petshield.ui.theme.CelesteTexto
import com.upb.petshield.ui.theme.FondoClaro
import com.upb.petshield.ui.theme.PetShieldGradient
import com.upb.petshield.ui.theme.TextoMedio

@Composable
fun CarnetVacunasScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onAgregarMascota: () -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    val mascotas = viewModel.mascotas

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Carnet De Vacunas",
                onBack = onBack,
                onLogoClick = { onNavigateToTab(BottomTab.HOME) }
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
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(FondoClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_carnet),
                        contentDescription = "Carnet Vacío",
                        tint = CelesteFin,
                        modifier = Modifier.size(70.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Aún No Has\nRegistrado Datos\nDe Tu Mascota",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CelesteTexto,
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
                            brush = PetShieldGradient,
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
                        color = CelesteTexto
                    )

                    Button(
                        onClick = onAgregarMascota,
                        colors = ButtonDefaults.buttonColors(containerColor = FondoClaro),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = "+ Agregar otra", color = CelesteTexto, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(mascotas, key = { it.id }) { mascota ->
                        MascotaCarnetCard(mascota = mascota)
                    }
                }
            }
        }
    }
}

@Composable
fun MascotaCarnetCard(mascota: Mascota) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CelesteFin)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(FondoClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        tint = CelesteFin,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = mascota.nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelesteTexto
                    )
                    Text(
                        text = "${mascota.especie} • ${mascota.raza} (${mascota.peso})",
                        fontSize = 14.sp,
                        color = TextoMedio
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = FondoClaro)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Registro de Vacunación",
                fontSize = 14.sp,
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
                        tint = CelesteFin,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = vacuna, fontSize = 14.sp, color = TextoMedio)
                }
            }
        }
    }
}
