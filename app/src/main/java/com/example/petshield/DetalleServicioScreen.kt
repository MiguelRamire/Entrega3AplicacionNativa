package com.example.petshield

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petshield.ui.theme.CelesteFin
import com.example.petshield.ui.theme.CelesteTexto
import com.example.petshield.ui.theme.FondoClaro
import com.example.petshield.ui.theme.PetShieldGradient
import com.example.petshield.ui.theme.TextoMedio
import com.example.petshield.ui.theme.TextoSecundario
import java.util.Calendar

@Composable
fun DetalleServicioScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onReservarServicio: () -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    var fechaSeleccionada by remember { mutableStateOf("Miércoles, 23 De Septiembre") }
    var horaSeleccionada by remember { mutableStateOf("3:00 PM") }

    var expandedFecha by remember { mutableStateOf(false) }
    var expandedHora by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val meses = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
            val diasSemana = arrayOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
            val cal = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
            val dayName = diasSemana[cal.get(Calendar.DAY_OF_WEEK) - 1]
            val monthName = meses[month]
            fechaSeleccionada = "$dayName, $dayOfMonth De $monthName"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val opcionesFechas = listOf(
        "Miércoles, 23 De Septiembre",
        "Jueves, 24 De Septiembre",
        "Viernes, 25 De Septiembre",
        "Sábado, 26 De Septiembre",
        "Lunes, 28 De Septiembre",
        "Seleccionar en calendario..."
    )

    val opcionesHoras = listOf(
        "8:00 AM",
        "9:00 AM",
        "10:00 AM",
        "11:00 AM",
        "2:00 PM",
        "3:00 PM",
        "4:00 PM",
        "5:00 PM"
    )

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Consulta general",
                onBack = onBack
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(FondoClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clinics),
                        contentDescription = null,
                        tint = CelesteFin,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "CONSULTA GENERAL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelesteTexto
                    )
                    Text(
                        text = "Consulta veterinaria\ngeneral",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        lineHeight = 22.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = CelesteFin,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "4,9 · 128 reseñas",
                            fontSize = 14.sp,
                            color = CelesteTexto,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Evaluación clínica completa para conocer el estado de salud de tu mascota, resolver inquietudes y definir un plan de cuidado personalizado.",
                fontSize = 14.sp,
                color = TextoMedio,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = FondoClaro)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Qué incluye",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CelesteTexto
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "✓ Examen físico de nariz a cola", fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "✓ Revisión de antecedentes y signos vitales", fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "✓ Recomendaciones y fórmula digital", fontSize = 14.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Preparación requerida",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CelesteTexto
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Trae el carnet de vacunas y exámenes recientes. No requiere ayuno.",
                fontSize = 14.sp,
                color = TextoMedio
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Duración", fontSize = 12.sp, color = TextoSecundario)
                    Text(text = "30 minutos", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
                Column {
                    Text(text = "Modalidad", fontSize = 12.sp, color = TextoSecundario)
                    Text(text = "En clínica", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
                Column {
                    Text(text = "Precio", fontSize = 12.sp, color = TextoSecundario)
                    Text(text = "Desde $65.000", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = FondoClaro,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Fecha y hora",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelesteTexto
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedFecha = true },
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null,
                                        tint = CelesteFin,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = fechaSeleccionada,
                                        color = CelesteTexto,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = CelesteFin
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = expandedFecha,
                            onDismissRequest = { expandedFecha = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            opcionesFechas.forEach { opcion ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = opcion,
                                            color = if (opcion.startsWith("Seleccionar")) CelesteFin else Color.Black,
                                            fontWeight = if (opcion.startsWith("Seleccionar")) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        expandedFecha = false
                                        if (opcion.startsWith("Seleccionar")) {
                                            datePickerDialog.show()
                                        } else {
                                            fechaSeleccionada = opcion
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedHora = true },
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.Schedule,
                                        contentDescription = null,
                                        tint = CelesteFin,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = horaSeleccionada,
                                        color = CelesteTexto,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = CelesteFin
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = expandedHora,
                            onDismissRequest = { expandedHora = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            opcionesHoras.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion, color = Color.Black) },
                                    onClick = {
                                        horaSeleccionada = opcion
                                        expandedHora = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.reservaActual = viewModel.reservaActual.copy(
                        fechaStr = fechaSeleccionada,
                        horaStr = horaSeleccionada
                    )
                    onReservarServicio()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        brush = PetShieldGradient,
                        shape = RoundedCornerShape(25.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Text(
                    text = "Reservar servicio",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ServiceMiniCard(category = "PROCEDIMIENTO", title = "Vacunacion", rating = "4,9 · 128 reseñas")
            Spacer(modifier = Modifier.height(12.dp))
            ServiceMiniCard(category = "TRATAMIENTO", title = "Desparasitacion", rating = "4,9 · 128 reseñas")
        }
    }
}

@Composable
fun ServiceMiniCard(category: String, title: String, rating: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(FondoClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clinics),
                contentDescription = null,
                tint = CelesteFin,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = category, fontSize = 12.sp, color = CelesteTexto, fontWeight = FontWeight.Bold)
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = CelesteFin,
                        modifier = Modifier.size(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = rating, fontSize = 12.sp, color = CelesteTexto)
            }
        }
    }
}
