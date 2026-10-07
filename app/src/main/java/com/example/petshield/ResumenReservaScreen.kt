package com.example.petshield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petshield.ui.theme.CelesteFin
import com.example.petshield.ui.theme.CelesteTexto
import com.example.petshield.ui.theme.FondoClaro
import com.example.petshield.ui.theme.PetShieldGradient
import com.example.petshield.ui.theme.TextoSecundario

@Composable
fun ResumenReservaScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onConfirmarReserva: () -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    val reserva = viewModel.reservaActual

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Resumen de reserva",
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
                    ReservaDetailRow(label = "Mascota", value = reserva.mascotaNombre)
                    ReservaDetailRow(label = "Servicio", value = reserva.servicioNombre)
                    ReservaDetailRow(label = "Profesional", value = reserva.profesionalNombre)
                    ReservaDetailRow(label = "Fecha", value = reserva.fechaStr)
                    ReservaDetailRow(label = "Hora", value = reserva.horaStr)
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
