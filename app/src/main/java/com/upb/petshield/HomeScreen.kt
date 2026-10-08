package com.upb.petshield

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
import com.upb.petshield.ui.theme.CelesteFin
import com.upb.petshield.ui.theme.CelesteTexto
import com.upb.petshield.ui.theme.FondoClaro
import com.upb.petshield.ui.theme.PetShieldGradient
import com.upb.petshield.ui.theme.TextoMedio
import com.upb.petshield.ui.theme.TextoOscuro
import com.upb.petshield.ui.theme.TextoSecundario
import java.util.Calendar

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
    LaunchedEffect(Unit) {
        viewModel.cargarCitasDesdeFirebase()
    }

    // --- FECHAS DINÁMICAS EN TIEMPO REAL ---
    val calendarHoy = Calendar.getInstance()
    val hoyDiaDelMes = calendarHoy.get(Calendar.DAY_OF_MONTH)

    // Cálculo de los días de la semana actual (Lunes a Sábado)
    val calLunes = Calendar.getInstance().apply {
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    val diasSemanaDinamicos = remember {
        val nombresDias = arrayOf("LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB")
        (0..5).map { idx ->
            val cal = (calLunes.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, idx) }
            val numDia = cal.get(Calendar.DAY_OF_MONTH).toString()
            val esHoy = cal.get(Calendar.DAY_OF_MONTH) == hoyDiaDelMes &&
                        cal.get(Calendar.MONTH) == calendarHoy.get(Calendar.MONTH)
            Triple(numDia, nombresDias[idx], esHoy)
        }
    }

    var diaSeleccionadoCitas by remember {
        mutableStateOf(diasSemanaDinamicos.firstOrNull { it.third }?.first ?: diasSemanaDinamicos.first().first)
    }

    // Cálculo del mes actual y su matriz de días para el calendario
    val calMesActual = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val totalDiasEnMes = calMesActual.getActualMaximum(Calendar.DAY_OF_MONTH)
    val primerDiaSemana = calMesActual.get(Calendar.DAY_OF_WEEK)
    val offsetInic = if (primerDiaSemana == Calendar.SUNDAY) 6 else primerDiaSemana - Calendar.MONDAY

    val nombreMesActual = remember {
        val meses = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
        "${meses[calMesActual.get(Calendar.MONTH)]} ${calMesActual.get(Calendar.YEAR)}"
    }

    val daysGrid = remember {
        val lista = mutableListOf<String>()
        repeat(offsetInic) { lista.add("") }
        for (d in 1..totalDiasEnMes) {
            lista.add(d.toString())
        }
        while (lista.size % 7 != 0) {
            lista.add("")
        }
        lista.chunked(7)
    }

    var showNotifDialog by remember { mutableStateOf(false) }
    var diaSeleccionadoCalendar by remember { mutableStateOf<String?>(null) }

    if (showNotifDialog) {
        AlertDialog(
            onDismissRequest = { showNotifDialog = false },
            title = { Text("Notificaciones") },
            text = { Text("¡Tienes recordatorios de vacunación y citas esta semana!") },
            confirmButton = {
                TextButton(onClick = { showNotifDialog = false }) {
                    Text("Entendido", color = CelesteTexto)
                }
            },
            containerColor = Color.White
        )
    }

    // DIÁLOGO DE DETALLES DE CITAS AL PRESIONAR UNA FECHA DEL CALENDARIO
    diaSeleccionadoCalendar?.let { day ->
        val citasDelDia = viewModel.citas.filter { cita ->
            val regex = Regex("""\b([1-9]|[12][0-9]|3[01])\b""")
            val diaEnCita = regex.find(cita.fechaStr)?.value
            diaEnCita == day
        }

        AlertDialog(
            onDismissRequest = { diaSeleccionadoCalendar = null },
            title = {
                Text(
                    text = "Citas para el día $day de $nombreMesActual",
                    fontWeight = FontWeight.Bold,
                    color = CelesteTexto
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (citasDelDia.isEmpty()) {
                        Text(
                            text = "No tienes citas programadas para el día $day.",
                            fontSize = 14.sp,
                            color = TextoSecundario
                        )
                    } else {
                        citasDelDia.forEach { cita ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = FondoClaro,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = cita.vetNombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = CelesteTexto
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Servicio: ${cita.servicio}",
                                        fontSize = 13.sp,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "Lugar: ${cita.lugar}",
                                        fontSize = 13.sp,
                                        color = TextoMedio
                                    )
                                    Text(
                                        text = "Mascota: ${cita.mascotaNombre}",
                                        fontSize = 13.sp,
                                        color = TextoMedio
                                    )
                                    Text(
                                        text = "Fecha/Hora: ${cita.fechaStr} a las ${cita.horaStr}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CelesteFin
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (citasDelDia.isEmpty()) {
                        Button(
                            onClick = {
                                diaSeleccionadoCalendar = null
                                onNavigateToClinicas()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CelesteFin),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Agendar cita", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    TextButton(onClick = { diaSeleccionadoCalendar = null }) {
                        Text("Cerrar", color = CelesteTexto)
                    }
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    val diasConCitas = remember(viewModel.citas.toList()) {
        viewModel.citas.mapNotNull { cita ->
            val regex = Regex("""\b([1-9]|[12][0-9]|3[01])\b""")
            regex.find(cita.fechaStr)?.value
        }.toSet()
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        diasSemanaDinamicos.forEach { (num, name, esHoy) ->
                            val isSelected = diaSeleccionadoCitas == num
                            DayBadge(
                                dayNum = num,
                                dayName = name,
                                isSelected = isSelected || esHoy,
                                onClick = { diaSeleccionadoCitas = num }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val primeraCita = viewModel.citas.firstOrNull()
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToCitas() },
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                    ) {
                        if (primeraCita == null) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No tienes citas programadas para esta semana.",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = onNavigateToClinicas,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(16.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Agendar una cita",
                                        color = CelesteTexto,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = primeraCita.fechaStr,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = primeraCita.horaStr,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = primeraCita.vetNombre,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 4. SECCIÓN INFERIOR: SERVICIOS VETERINARIOS PRÓXIMOS Y CALENDARIO REAL ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Servicios próximos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelesteTexto
                    )
                    Text(
                        text = nombreMesActual,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CelesteFin
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB", "DOM").forEach { day ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(FondoClaro)
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = day,
                                color = TextoSecundario,
                                fontSize = 11.sp,
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
                        daysGrid.forEach { week ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                week.forEach { day ->
                                    if (day.isNotEmpty()) {
                                        val tieneCita = day in diasConCitas
                                        val esHoy = day == hoyDiaDelMes.toString()

                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    when {
                                                        tieneCita -> CelesteFin
                                                        esHoy -> CelesteTexto.copy(alpha = 0.25f)
                                                        else -> Color.Transparent
                                                    }
                                                )
                                                .border(
                                                    width = if (esHoy && !tieneCita) 1.5.dp else 0.dp,
                                                    color = if (esHoy && !tieneCita) CelesteTexto else Color.Transparent,
                                                    shape = CircleShape
                                                )
                                                .clickable {
                                                    diaSeleccionadoCalendar = day
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = day,
                                                fontSize = 12.sp,
                                                fontWeight = if (tieneCita || esHoy) FontWeight.Bold else FontWeight.Normal,
                                                color = when {
                                                    tieneCita -> Color.White
                                                    esHoy -> CelesteTexto
                                                    else -> TextoOscuro
                                                }
                                            )
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.size(28.dp))
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
