package com.upb.petshield

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.petshield.ui.theme.CelesteFin
import com.upb.petshield.ui.theme.CelesteTexto
import com.upb.petshield.ui.theme.ErrorRedText
import com.upb.petshield.ui.theme.FondoClaro
import com.upb.petshield.ui.theme.PetShieldGradient
import com.upb.petshield.ui.theme.TextoMedio
import com.upb.petshield.ui.theme.TextoSecundario

@Composable
fun CancelarCitaScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onCitaCanceladaSuccess: () -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    val cita = viewModel.citaACancelar

    val opcionesMotivo = remember {
        listOf(
            "Reprogramación",
            "Condiciones Climáticas",
            "Imprevisto Laboral",
            "Otros"
        )
    }
    var motivoSeleccionado by rememberSaveable { mutableStateOf(opcionesMotivo[0]) }
    var detalleTexto by rememberSaveable { mutableStateOf("") }
    var mostrarConfirmacionCancelacion by rememberSaveable { mutableStateOf(false) }

    if (mostrarConfirmacionCancelacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionCancelacion = false },
            title = {
                Text(
                    text = "Confirmar Cancelación",
                    fontWeight = FontWeight.Bold,
                    color = ErrorRedText
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas cancelar esta cita con ${cita?.vetNombre ?: "el veterinario"}? Esta acción no se puede deshacer.",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarConfirmacionCancelacion = false
                        if (cita != null) {
                            val motivoFinal = if (detalleTexto.isNotBlank()) {
                                "$motivoSeleccionado - $detalleTexto"
                            } else {
                                motivoSeleccionado
                            }
                            viewModel.cancelarCita(cita.id, motivoFinal)
                        }
                        onCitaCanceladaSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRedText),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Sí, cancelar cita", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacionCancelacion = false }) {
                    Text("Mantener cita", color = CelesteTexto)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Cancelar Cita",
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            if (cita == null) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No has seleccionado ninguna cita para cancelar.",
                            fontSize = 16.sp,
                            color = TextoSecundario,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onBack,
                            colors = ButtonDefaults.buttonColors(containerColor = CelesteFin),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Volver a mis citas", color = Color.White)
                        }
                    }
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = FondoClaro
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Cita a Cancelar",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CelesteTexto
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${cita.servicio} con ${cita.vetNombre}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "${cita.fechaStr} a las ${cita.horaStr} · ${cita.lugar}",
                            fontSize = 13.sp,
                            color = TextoMedio
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Selecciona el motivo de la cancelación:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                opcionesMotivo.forEach { opcion ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { motivoSeleccionado = opcion }
                            .padding(vertical = 6.dp)
                    ) {
                        RadioButton(
                            selected = (motivoSeleccionado == opcion),
                            onClick = { motivoSeleccionado = opcion },
                            colors = RadioButtonDefaults.colors(selectedColor = CelesteFin)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = opcion,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Detalles adicionales (opcional):",
                    fontSize = 14.sp,
                    color = CelesteTexto,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = detalleTexto,
                    onValueChange = { detalleTexto = it },
                    placeholder = { Text("Escribe tu motivo aquí...", color = TextoSecundario, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = FondoClaro,
                        unfocusedContainerColor = FondoClaro,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        mostrarConfirmacionCancelacion = true
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
                        text = "Cancelar Cita",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
