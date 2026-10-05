package com.example.petshield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
    val colorCelesteFin = Color(0xFF00BBD3)
    val fondoClaro = Color(0xFFE9F6FE)

    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) } // 0: Preguntas Frecuentes, 1: Contáctanos

    val faqItems = listOf(
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
                onSearchChange = { searchQuery = it }
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

            // TABS ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tab 1
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = 0 },
                    shape = RoundedCornerShape(20.dp),
                    color = if (selectedTab == 0) colorCelesteFin else Color.White,
                    border = BorderStroke(1.dp, colorCelesteFin)
                ) {
                    Text(
                        text = "Preguntas Frecuentes",
                        color = if (selectedTab == 0) Color.White else colorCelesteFin,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                // Tab 2
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = 1 },
                    shape = RoundedCornerShape(20.dp),
                    color = if (selectedTab == 1) colorCelesteFin else Color.White,
                    border = BorderStroke(1.dp, colorCelesteFin)
                ) {
                    Text(
                        text = "Contáctanos",
                        color = if (selectedTab == 1) Color.White else colorCelesteFin,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (selectedTab == 0) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    itemsIndexed(faqItems) { index, item ->
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
                // Sección Contáctanos
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = fondoClaro
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "¿Tienes alguna duda directa?",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorCelesteFin
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Envíanos un mensaje o llámanos a nuestra línea de atención telefónica:\n\n📞 +1 800 PET SHIELD\n✉️ soporte@petshield.com",
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
    val colorCelesteFin = Color(0xFF00BBD3)
    val fondoClaro = Color(0xFFE9F6FE)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(20.dp),
        color = if (isExpanded) colorCelesteFin else Color.White,
        border = if (isExpanded) null else BorderStroke(1.dp, fondoClaro)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isExpanded) Color.White else colorCelesteFin,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = if (isExpanded) Color.White else colorCelesteFin
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = answer,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.95f),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
