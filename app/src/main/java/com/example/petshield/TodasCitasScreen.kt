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
import com.example.petshield.ui.theme.CelesteTexto
import com.example.petshield.ui.theme.FondoClaro
import com.example.petshield.ui.theme.TextoMedio
import com.example.petshield.ui.theme.TextoSecundario

fun obtenerInicialesVet(nombreCompleto: String): String {
    val limpio = nombreCompleto
        .replace("Dra.", "", ignoreCase = true)
        .replace("Dr.", "", ignoreCase = true)
        .replace("MV", "", ignoreCase = true)
        .replace(",", "")
        .trim()
    val partes = limpio.split(" ").filter { it.isNotBlank() }
    return when {
        partes.size >= 2 -> "${partes[0].first().uppercase()}${partes[1].first().uppercase()}"
        partes.size == 1 -> partes[0].first().uppercase()
        else -> "V"
    }
}

@Composable
fun TodasCitasScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onCancelarCita: (Cita) -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Próximas, 1: Anteriores
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "¡Cita Agendada!",
                    fontWeight = FontWeight.Bold,
                    color = CelesteTexto
                )
            },
            text = {
                Text(
                    text = "Tu cita ha sido agendada con éxito.",
                    fontSize = 15.sp,
                    color = TextoMedio
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        onNavigateToTab(BottomTab.HOME)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CelesteFin),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Entendido", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    val filteredCitas = viewModel.citas.filter {
        if (selectedTab == 0) it.esProxima else !it.esProxima
    }

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Todas Las Citas",
                onBack = onBack,
                onLogoClick = { onNavigateToTab(BottomTab.HOME) }
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
                        color = if (selectedTab == 0) Color.White else CelesteTexto,
                        fontSize = 14.sp,
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
                        color = if (selectedTab == 1) Color.White else CelesteTexto,
                        fontSize = 14.sp,
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
                        color = TextoSecundario,
                        fontSize = 16.sp
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
                            onConfirmar = {
                                showConfirmDialog = true
                            },
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
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    val iniciales = remember(cita.vetNombre) { obtenerInicialesVet(cita.vetNombre) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(CelesteTexto),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iniciales,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cita.vetNombre,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CelesteTexto
            )
            Text(
                text = "${cita.servicio} · ${cita.lugar}",
                fontSize = 13.sp,
                color = TextoMedio
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
                        Text(text = cita.fechaStr, fontSize = 12.sp, color = TextoMedio)
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
                        Text(text = cita.horaStr, fontSize = 12.sp, color = TextoMedio)
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
                        fontSize = 14.sp,
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
                        .clickable { onConfirmar() },
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
