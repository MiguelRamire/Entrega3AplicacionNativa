package com.example.petshield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petshield.data.Cita
import com.example.petshield.ui.theme.CelesteFin
import com.example.petshield.ui.theme.FondoClaro

@Composable
fun TodasCitasScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onCancelarCita: (Cita) -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Próximas, 1: Anteriores

    val filteredCitas = remember(viewModel.citas.toList(), selectedTab) {
        viewModel.citas.filter {
            if (selectedTab == 0) it.esProxima else !it.esProxima
        }
    }

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Todas Las Citas",
                onBack = onBack
            )
        },
        bottomBar = {
            PetShieldBottomBar(
                currentTab = BottomTab.CITAS,
                onNavigateToTab = onNavigateToTab
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // TABS ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = 0 },
                    shape = RoundedCornerShape(20.dp),
                    color = if (selectedTab == 0) CelesteFin else Color.White,
                    border = BorderStroke(1.dp, CelesteFin)
                ) {
                    Text(
                        text = "Proximas",
                        color = if (selectedTab == 0) Color.White else CelesteFin,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = 1 },
                    shape = RoundedCornerShape(20.dp),
                    color = if (selectedTab == 1) CelesteFin else Color.White,
                    border = BorderStroke(1.dp, CelesteFin)
                ) {
                    Text(
                        text = "Anteriores",
                        color = if (selectedTab == 1) Color.White else CelesteFin,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (filteredCitas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedTab == 0) "No tienes citas próximas." else "No tienes citas anteriores.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredCitas, key = { it.id }) { cita ->
                        CitaItemCard(
                            cita = cita,
                            onCancelar = {
                                viewModel.citaACancelar = cita
                                onCancelarCita(cita)
                            }
                        )
                        HorizontalDivider(color = FondoClaro, thickness = 1.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun CitaItemCard(
    cita: Cita,
    onCancelar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(FondoClaro),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "👩‍⚕️", fontSize = 32.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cita.vetNombre,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = CelesteFin
            )
            Text(
                text = cita.servicio,
                fontSize = 12.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, CelesteFin)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = CelesteFin,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = cita.fechaStr, fontSize = 10.sp, color = Color.DarkGray)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, CelesteFin)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = CelesteFin,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = cita.horaStr, fontSize = 10.sp, color = Color.DarkGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, CelesteFin),
                    color = Color.White
                ) {
                    Text(
                        text = "Detalles",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 6.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(1.dp, CelesteFin, CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Confirmar",
                        tint = CelesteFin,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(1.dp, CelesteFin, CircleShape)
                        .clickable { onCancelar() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancelar",
                        tint = CelesteFin,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
