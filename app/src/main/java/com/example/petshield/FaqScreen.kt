package com.example.petshield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FaqScreen(
    onBack: () -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

        Pair("¿Necesitas Más Información?", "PetShield te ayuda a mantener al día la salud de tu mascota con un seguimiento completo de vacunas, desparasitaciones y citas veterinarias."),
        Pair("¿Cómo puedo agendar una cita?", "Puedes agendar una cita seleccionando una clínica o veterinario favorito en el inicio y haciendo clic en 'Agendar Cita'."),
        Pair("¿Dónde puedo consultar el carnet de vacunas?", "Accede a la sección 'Carnet' desde la pantalla principal o el menú de tu perfil."),
        Pair("¿Cómo cancelar o reprogramar una cita?", "Ingresa a 'Todas Las Citas', selecciona la cita que deseas modificar y haz clic en el botón 'X' para cancelar o reprogramar."),
        Pair("¿Qué hago si mi mascota tiene una emergencia?", "Te recomendamos contactar directamente a la clínica veterinaria más cercana a través del botón de contacto directo."),
        Pair("¿Puedo agregar varias mascotas?", "Sí, puedes registrar todas las mascotas que desees desde la sección Carnet de Vacunas -> 'Agregar Mascotas'.")
    )

    var expandedIndices by remember { mutableStateOf(setOf(0)) }

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Centro De Ayuda",
                subtitle = "¿Cómo Podemos Ayudarte?",
                onBack = onBack,
                searchQuery = searchQuery,
            )
        },
        bottomBar = {
            PetShieldBottomBar(
                currentTab = BottomTab.PROFILE,
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = 0 },
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Text(
                        text = "Preguntas Frecuentes",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                    )
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = 1 },
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Text(
                        text = "Contáctanos",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (selectedTab == 0) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                        val isExpanded = expandedIndices.contains(index)
                        FaqAccordionItem(
                            question = item.first,
                            answer = item.second,
                            isExpanded = isExpanded,
                            onToggle = {
                                expandedIndices = if (isExpanded) {
                                    expandedIndices - index
                                } else {
                                    expandedIndices + index
                                }
                            }
                        )
                    }
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "¿Tienes alguna duda directa?",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Envíanos un mensaje o llámanos a nuestra línea de atención telefónica:\n\n📞 +1 800 PET SHIELD\n✉️ soporte@petshield.com",
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FaqAccordionItem(
    question: String,
    answer: String,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = answer,
                        color = Color.White.copy(alpha = 0.95f),
                    )
                }
            }
        }
    }
}
