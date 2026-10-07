package com.example.petshield

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CancelarCitaScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onCitaCanceladaSuccess: () -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    val cita = viewModel.citaACancelar

        "Reprogramación",
        "Condiciones Climáticas",
        "Imprevisto Laboral",
        "Otros"
    )
    var motivoSeleccionado by remember { mutableStateOf(opcionesMotivo[0]) }
    var detalleTexto by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Cancelar Cita",
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
            Text(
                text = "Consulta de vacunación programada. Se realizará la evaluación general de la mascota y se aplicarán las vacunas correspondientes según el calendario de inmunización.",
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = opcion,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = detalleTexto,
                onValueChange = { detalleTexto = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (cita != null) {
                        viewModel.cancelarCita(cita.id)
                    }
                    onCitaCanceladaSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
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
