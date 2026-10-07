package com.example.petshield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petshield.ui.theme.CelesteFin
import com.example.petshield.ui.theme.CelesteTexto
import com.example.petshield.ui.theme.FondoClaro
import com.example.petshield.ui.theme.PetShieldGradient
import com.example.petshield.ui.theme.TextoOscuro
import com.example.petshield.ui.theme.TextoSecundario

@Composable
fun HomeScreen(
    viewModel: PetShieldViewModel,
    onNavigateToFavoritos: () -> Unit,
    onNavigateToClinicas: () -> Unit,
    onNavigateToCarnet: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToCitas: () -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    var diaSeleccionadoCitas by remember { mutableStateOf("2") }
    var showNotifDialog by remember { mutableStateOf(false) }

    if (showNotifDialog) {
        AlertDialog(
            onDismissRequest = { showNotifDialog = false },
            title = { Text("Notificaciones") },
            text = { Text("¡Tienes 2 recordatorios de vacunación y citas esta semana!") },
            confirmButton = {
                TextButton(onClick = { showNotifDialog = false }) {
                    Text("Entendido", color = CelesteTexto)
                }
            }
        )
    }

    Scaffold(
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // --- 1. HEADER SUPERIOR ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(FondoClaro, CircleShape)
                            .clickable { showNotifDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_notification),
                            contentDescription = "Notificaciones",
                            tint = CelesteFin,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(FondoClaro, CircleShape)
                            .clickable { onNavigateToPerfil() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = "Configuración",
                            tint = CelesteFin,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = "PetShield",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CelesteTexto
                )

                val iniciales = viewModel.usuarioNombre
                    .split(" ")
                    .filter { it.isNotBlank() }
                    .take(2)
                    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                    .joinToString("")
                    .ifEmpty { "U" }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CelesteTexto)
                        .clickable { onNavigateToPerfil() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iniciales,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- 2. SECCIÓN DE SERVICIOS ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Servicios",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CelesteTexto
                )
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(color = FondoClaro, thickness = 1.dp)

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TopServiceItem(
                        iconRes = R.drawable.ic_heart_outline,
                        label = "Favoritos",
                        color = CelesteFin,
                        onClick = onNavigateToFavoritos
                    )
                    TopServiceItem(
                        iconRes = R.drawable.ic_clinics,
                        label = "Clínicas",
                        color = CelesteFin,
                        onClick = onNavigateToClinicas
                    )
                    TopServiceItem(
                        iconRes = R.drawable.ic_carnet,
                        label = "Carnet",
                        color = CelesteFin,
                        onClick = onNavigateToCarnet
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 3. TARJETA GRANDE: CITAS ESTA SEMANA ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PetShieldGradient)
                    .padding(horizontal = 24.dp, vertical = 14.dp)
            ) {
                Column {
                    Text(
                        text = "Citas Esta Semana",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.5f), thickness = 1.dp)

                    Spacer(modifier = Modifier.height(10.dp))

                    val diasSemanaCitas = listOf(
                        Triple("9", "MON", "0"),
                        Triple("10", "TUE", "1"),
                        Triple("11", "WED", "2"),
                        Triple("12", "THU", "3"),
                        Triple("13", "FRI", "4"),
                        Triple("12", "SAT", "5")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        diasSemanaCitas.forEach { (num, name, idUnique) ->
                            val isSelected = diaSeleccionadoCitas == idUnique
                            DayBadge(
                                dayNum = num,
                                dayName = name,
                                isSelected = isSelected,
                                onClick = { diaSeleccionadoCitas = idUnique }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToCitas() },
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Cita 1
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "11 Mes - Miércoles - Hoy", color = Color.White, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "10:00 am", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = "Dra. Sofía Herrera, MV", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
                            Spacer(modifier = Modifier.height(8.dp))

                            // Cita 2
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "16 Mes - Lunes", color = Color.White, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "08:00 am", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = "Dr. Andrés Molina, MV", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 4. SECCIÓN INFERIOR: SERVICIOS VETERINARIOS PRÓXIMOS ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Servicios Veterinarios proximos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CelesteTexto
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEachIndexed { index, day ->
                        val active = index == 0 || index == 1
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (active) CelesteFin else FondoClaro)
                                .clickable { onNavigateToClinicas() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = day,
                                color = if (active) Color.White else TextoSecundario,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = FondoClaro,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        val daysGrid = listOf(
                            listOf("1", "2", "3", "4", "5", "6", "7"),
                            listOf("8", "9", "10", "11", "12", "13", "14"),
                            listOf("15", "16", "17", "18", "19", "20", "21"),
                            listOf("22", "23", "24", "25", "26", "27", "28"),
                            listOf("29", "30", "31", "", "", "", "")
                        )

                        daysGrid.forEach { week ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                week.forEach { day ->
                                    if (day.isNotEmpty()) {
                                        val isHighlighted = day == "11" || day == "16"
                                        Box(
                                            modifier = Modifier
                                                .size(26.dp)
                                                .clip(CircleShape)
                                                .background(if (isHighlighted) CelesteFin else Color.Transparent)
                                                .clickable {
                                                    if (isHighlighted) onNavigateToCitas() else onNavigateToClinicas()
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = day,
                                                fontSize = 12.sp,
                                                fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isHighlighted) Color.White else TextoOscuro
                                            )
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.size(26.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TopServiceItem(iconRes: Int, label: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .border(1.dp, Color(0xFFE2F3FC), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = CelesteTexto, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun DayBadge(
    dayNum: String,
    dayName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(42.dp)
            .clickable { onClick() },
        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.2f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            Text(
                text = dayNum,
                color = if (isSelected) CelesteTexto else Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = dayName,
                color = if (isSelected) CelesteTexto else Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp
            )
        }
    }
}
