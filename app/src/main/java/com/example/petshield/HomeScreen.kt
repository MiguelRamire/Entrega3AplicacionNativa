package com.example.petshield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    val colorCelesteInicio = Color(0xFF33E4DB)
    val colorCelesteFin = Color(0xFF00BBD3)
    val fondoClaro = Color(0xFFE9F6FE)
    val textoOscuro = Color(0xFF252525)

    // Estado para el día seleccionado en la tarjeta principal (ej. "11")
    var diaSeleccionadoCitas by remember { mutableStateOf("11") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. HEADER SUPERIOR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Notificaciones y Ajustes con espacio separado y tamaños correctos
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(fondoClaro, CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_notification),
                        contentDescription = "Notificaciones",
                        tint = colorCelesteFin,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(fondoClaro, CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = "Configuración",
                        tint = colorCelesteFin,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Título / Logo PetShield
            Text(
                text = "PetShield",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorCelesteFin
            )

            // Perfil de Usuario (Ada) con su foto real y botón de editar superpuesto
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Ada",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textoOscuro
                )
                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier.size(44.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    // Foto de perfil del usuario (Placeholder temporal / Imagen de Ada)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(fondoClaro),
                        contentAlignment = Alignment.Center
                    ) {
                        // Puedes cambiar el icono o usar Image() si tienes la foto real en drawable
                        Text(text = "👩", fontSize = 18.sp)
                    }

                    // Icono de editar superpuesto (ic_editar) en la esquina inferior derecha
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(0.5.dp, colorCelesteFin, CircleShape)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_editar),
                            contentDescription = "Editar perfil",
                            tint = colorCelesteFin,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- 2. SECCIÓN DE SERVICIOS (Favoritos, Clínicas, Carnet) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Servicios",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colorCelesteFin
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = fondoClaro, thickness = 1.dp)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TopServiceItem(iconRes = R.drawable.ic_heart_outline, label = "Favoritos", color = colorCelesteFin)
                TopServiceItem(iconRes = R.drawable.ic_clinics, label = "Clínicas", color = colorCelesteFin)
                TopServiceItem(iconRes = R.drawable.ic_carnet, label = "Carnet", color = colorCelesteFin)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- 3. TARJETA GRANDE: CITAS ESTA SEMANA ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(listOf(colorCelesteInicio, colorCelesteFin))
                )
                .padding(horizontal = 24.dp, vertical = 14.dp)
        ) {
            Column {
                Text(
                    text = "Citas Esta Semana",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.5f), thickness = 1.dp)

                Spacer(modifier = Modifier.height(10.dp))

                // Selector horizontal de días con identificación única por índice/posición
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

                // Tarjeta interior translúcida con detalles de citas
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        // Cita 1
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "11 Mes - Miércoles - Hoy", color = Color.White, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "10:00 am", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "Dra. Sofía Herrera, MV", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(8.dp))

                        // Cita 2
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "16 Mes - Lunes", color = Color.White, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "08:00 am", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "Dr. Andrés Molina, MV", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // --- 4. SECCIÓN INFERIOR: SERVICIOS VETERINARIOS PRÓXIMOS ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 10.dp)
        ) {
            Text(
                text = "Servicios Veterinarios proximos",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorCelesteFin
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEachIndexed { index, day ->
                    val active = index == 0 || index == 1
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (active) colorCelesteFin else fondoClaro)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = day,
                            color = if (active) Color.White else Color.Gray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Calendario estático base
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = fondoClaro,
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
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(if (isHighlighted) colorCelesteFin else Color.Transparent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = day,
                                            fontSize = 11.sp,
                                            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isHighlighted) Color.White else textoOscuro
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(24.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 5. BARRA DE NAVEGACIÓN INFERIOR (Separada holgadamente de la base) ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(60.dp),
            color = fondoClaro,
            shape = RoundedCornerShape(28.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_profile_user),
                        contentDescription = "Perfil",
                        tint = colorCelesteFin,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(colorCelesteFin),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home_active),
                        contentDescription = "Home",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar_nav),
                        contentDescription = "Calendario",
                        tint = colorCelesteFin,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Margen inferior de respiro para que el navbar no toque el borde del dispositivo
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun TopServiceItem(iconRes: Int, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .border(1.dp, Color(0xFFE2F3FC), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 11.sp, color = color, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun DayBadge(dayNum: String, dayName: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 9.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dayNum,
            color = if (isSelected) Color(0xFF00BBD3) else Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Text(
            text = dayName,
            color = if (isSelected) Color(0xFF00BBD3) else Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 8.sp
        )
    }
}