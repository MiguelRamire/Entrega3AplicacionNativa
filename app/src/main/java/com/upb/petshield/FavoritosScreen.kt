package com.upb.petshield

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.petshield.data.Clinica
import com.upb.petshield.data.Veterinario
import com.upb.petshield.ui.theme.CelesteFin
import com.upb.petshield.ui.theme.CelesteTexto
import com.upb.petshield.ui.theme.FondoClaro
import com.upb.petshield.ui.theme.PetShieldGradient
import com.upb.petshield.ui.theme.TextoMedio
import com.upb.petshield.ui.theme.TextoSecundario

@Composable
fun FavoritosScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onAgendarCita: (Veterinario) -> Unit = {},
    onSelectClinica: (Clinica) -> Unit = {},
    onNavigateToClinicas: () -> Unit = {},
    onNavigateToTab: (BottomTab) -> Unit
) {
    val favoritosClinicas = viewModel.clinicas.filter { it.esFavorita }
    val totalFavoritos = favoritosClinicas.size

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Clínicas Favoritas",
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
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (totalFavoritos == 0) Arrangement.Center else Arrangement.Top
        ) {
            if (totalFavoritos == 0) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(FondoClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Sin favoritos",
                        tint = CelesteFin,
                        modifier = Modifier.size(70.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Aún no tienes clínicas favoritas",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CelesteTexto,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Explora las clínicas registradas para agregarlas a tu lista de favoritos.",
                    fontSize = 14.sp,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onNavigateToClinicas,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(50.dp)
                        .background(
                            brush = PetShieldGradient,
                            shape = RoundedCornerShape(25.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Text(
                        text = "Ver clínicas",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(12.dp))

                // Encabezado con filtro y botón para añadir más clínicas
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Ordenar Por", fontSize = 14.sp, color = TextoMedio)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CelesteFin
                        ) {
                            Text(
                                text = "A → Z",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onNavigateToClinicas,
                        colors = ButtonDefaults.buttonColors(containerColor = FondoClaro),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = "+ Añadir clínicas", color = CelesteTexto, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        Text(
                            text = "Clínicas Favoritas",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CelesteTexto,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )
                    }

                    items(favoritosClinicas, key = { "clinica_${it.id}" }) { clinica ->
                        ClinicaCard(
                            clinica = clinica,
                            onToggleFavorite = { viewModel.toggleFavoritoClinica(clinica.id) },
                            onClick = { onSelectClinica(clinica) }
                        )
                        HorizontalDivider(color = FondoClaro, thickness = 1.dp)
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onNavigateToClinicas,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .background(
                                    brush = PetShieldGradient,
                                    shape = RoundedCornerShape(23.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues()
                        ) {
                            Text(
                                text = "+ Explorar y añadir más clínicas a favoritos",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
