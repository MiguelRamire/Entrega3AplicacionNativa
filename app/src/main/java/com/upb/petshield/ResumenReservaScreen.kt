package com.upb.petshield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.petshield.data.Mascota
import com.upb.petshield.data.Veterinario
import com.upb.petshield.ui.theme.CelesteFin
import com.upb.petshield.ui.theme.CelesteTexto
import com.upb.petshield.ui.theme.FondoClaro
import com.upb.petshield.ui.theme.PetShieldGradient
import com.upb.petshield.ui.theme.TextoMedio
import com.upb.petshield.ui.theme.TextoSecundario

@Composable
fun ResumenReservaScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onConfirmarReserva: () -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    val reserva = viewModel.reservaActual

    // 1. Mascota seleccionada
    var mascotaSeleccionada by remember(viewModel.mascotas.toList()) {
        mutableStateOf(viewModel.mascotas.firstOrNull())
    }
    var expandedMascota by remember { mutableStateOf(false) }

    // 2. Veterinario seleccionado
    var vetSeleccionado by remember(viewModel.veterinarios.toList()) {
        mutableStateOf(viewModel.veterinarios.firstOrNull())
    }
    var expandedVet by remember { mutableStateOf(false) }

    // 3. Hora disponible del veterinario
    val opcionesHoras = remember {
        listOf("8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM")
    }
    var horaSeleccionada by remember { mutableStateOf("3:00 PM") }
    var expandedHora by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Resumen de reserva",
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
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = FondoClaro,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TODO LISTO PARA SU MASCOTA",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelesteTexto
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Revisa los datos de tu reserva",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, FondoClaro)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // A. SELECCIÓN DE MASCOTA (DESPLEGABLE)
                    Text(text = "Mascota", fontSize = 12.sp, color = TextoSecundario)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedMascota = true },
                            color = FondoClaro,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Pets, contentDescription = null, tint = CelesteFin, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = mascotaSeleccionada?.let { "${it.nombre} · ${it.raza}" } ?: reserva.mascotaNombre,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = CelesteFin)
                            }
                        }

                        DropdownMenu(
                            expanded = expandedMascota,
                            onDismissRequest = { expandedMascota = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            if (viewModel.mascotas.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("Max · Golden Retriever", color = Color.Black) },
                                    onClick = { expandedMascota = false }
                                )
                            } else {
                                viewModel.mascotas.forEach { m ->
                                    DropdownMenuItem(
                                        text = { Text("${m.nombre} · ${m.especie} (${m.raza})", color = Color.Black) },
                                        onClick = {
                                            mascotaSeleccionada = m
                                            expandedMascota = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ReservaDetailRow(label = "Servicio", value = reserva.servicioNombre)

                    Spacer(modifier = Modifier.height(8.dp))

                    // B. SELECCIÓN DE VETERINARIO (DESPLEGABLE)
                    Text(text = "Médico Veterinario", fontSize = 12.sp, color = TextoSecundario)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedVet = true },
                            color = FondoClaro,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = CelesteFin, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = vetSeleccionado?.nombre ?: "Dra. Sofía Herrera, MV",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = CelesteFin)
                            }
                        }

                        DropdownMenu(
                            expanded = expandedVet,
                            onDismissRequest = { expandedVet = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            if (viewModel.veterinarios.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("Dra. Sofía Herrera, MV", color = Color.Black) },
                                    onClick = { expandedVet = false }
                                )
                            } else {
                                viewModel.veterinarios.forEach { v ->
                                    DropdownMenuItem(
                                        text = { Text("${v.nombre} (${v.especialidad})", color = Color.Black) },
                                        onClick = {
                                            vetSeleccionado = v
                                            expandedVet = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // C. SELECCIÓN DE HORA DISPONIBLE DEL VETERINARIO
                    Text(text = "Hora disponible del veterinario", fontSize = 12.sp, color = TextoSecundario)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedHora = true },
                            color = FondoClaro,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Outlined.Schedule, contentDescription = null, tint = CelesteFin, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = horaSeleccionada,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = CelesteFin)
                            }
                        }

                        DropdownMenu(
                            expanded = expandedHora,
                            onDismissRequest = { expandedHora = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            opcionesHoras.forEach { h ->
                                DropdownMenuItem(
                                    text = { Text(h, color = Color.Black) },
                                    onClick = {
                                        horaSeleccionada = h
                                        expandedHora = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    ReservaDetailRow(label = "Fecha seleccionada", value = reserva.fechaStr)
                    ReservaDetailRow(label = "Modalidad", value = reserva.modalidad)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = FondoClaro)
                    ReservaDetailRow(label = "Lugar", value = reserva.lugar)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = FondoClaro,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Desglose de precio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelesteTexto
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Consulta", fontSize = 14.sp, color = TextoSecundario)
                        Text(text = reserva.precioConsulta, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Tarifa de servicio", fontSize = 14.sp, color = TextoSecundario)
                        Text(text = reserva.tarifaServicio, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.White)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(text = reserva.total, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CelesteTexto)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cancelación gratuita hasta 4 horas antes.",
                fontSize = 12.sp,
                color = TextoSecundario,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val petName = mascotaSeleccionada?.let { "${it.nombre} · ${it.raza}" } ?: reserva.mascotaNombre
                    val petId = mascotaSeleccionada?.id ?: reserva.mascotaId
                    val doctorName = vetSeleccionado?.nombre ?: "Dra. Sofía Herrera, MV"

                    viewModel.reservaActual = viewModel.reservaActual.copy(
                        mascotaId = petId,
                        mascotaNombre = petName,
                        profesionalNombre = doctorName,
                        horaStr = horaSeleccionada
                    )
                    viewModel.confirmarReservaActual()
                    onConfirmarReserva()
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
                    text = "Confirmar reserva",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ReservaDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = TextoSecundario)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}
