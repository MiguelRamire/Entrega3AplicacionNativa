package com.example.petshield

import android.app.DatePickerDialog
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
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Star
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
import com.example.petshield.data.ServicioModel
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
    LaunchedEffect(Unit) {
        viewModel.cargarServiciosDesdeFirebase()
    }

    var servicioSeleccionado by remember(viewModel.servicios.toList()) {
        mutableStateOf(viewModel.servicios.firstOrNull() ?: ServicioModel())
    }

    var fechaSeleccionada by remember { mutableStateOf("Miércoles, 23 De Septiembre") }

    var expandedServicio by remember { mutableStateOf(false) }
    var expandedFecha by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
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

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = servicioSeleccionado.nombre,
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
                        text = "SERVICIO VETERINARIO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelesteTexto
                    )
                    Text(
                        text = servicioSeleccionado.nombre,
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
                text = servicioSeleccionado.descripcion,
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Duración", fontSize = 12.sp, color = TextoSecundario)
                    Text(text = "${servicioSeleccionado.duracionMinutos} minutos", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
                Column {
                    Text(text = "Modalidad", fontSize = 12.sp, color = TextoSecundario)
                    Text(text = "En clínica", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
                Column {
                    Text(text = "Precio", fontSize = 12.sp, color = TextoSecundario)
                    Text(text = "$${servicioSeleccionado.precio.toInt()}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- SELECCIÓN DE SERVICIO Y FECHA ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = FondoClaro,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Seleccionar servicio y fecha",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelesteTexto
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // 1. DESPLEGABLE DE SERVICIOS
                    Text(text = "Servicio registrado", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextoMedio)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedServicio = true },
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Icon(
                                        imageVector = Icons.Default.MedicalServices,
                                        contentDescription = null,
                                        tint = CelesteFin,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = servicioSeleccionado.nombre,
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
                            expanded = expandedServicio,
                            onDismissRequest = { expandedServicio = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            viewModel.servicios.forEach { servicio ->
                                DropdownMenuItem(
                                    text = { Text(text = "${servicio.nombre} ($${servicio.precio.toInt()})", color = Color.Black) },
                                    onClick = {
                                        servicioSeleccionado = servicio
                                        expandedServicio = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2. DESPLEGABLE DE FECHA
                    Text(text = "Fecha de la cita", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextoMedio)
                    Spacer(modifier = Modifier.height(4.dp))
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
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
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
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val s = servicioSeleccionado
                    viewModel.reservaActual = viewModel.reservaActual.copy(
                        servicioId = s.id,
                        servicioNombre = s.nombre,
                        precioConsulta = "$${s.precio.toInt()}",
                        tarifaServicio = "$3.500",
                        total = "$${(s.precio + 3500.0).toInt()}",
                        fechaStr = fechaSeleccionada
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
        }
    }
}
