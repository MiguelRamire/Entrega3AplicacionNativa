package com.upb.petshield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import com.upb.petshield.data.Clinica
import com.upb.petshield.ui.theme.CelesteFin
import com.upb.petshield.ui.theme.CelesteTexto
import com.upb.petshield.ui.theme.FondoClaro
import com.upb.petshield.ui.theme.TextoMedio

@Composable
fun ClinicasScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onSelectClinica: (Clinica) -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.cargarClinicasDesdeFirebase()
    }

    val filteredClinicas = viewModel.clinicas.filter {
        it.nombre.contains(searchQuery, ignoreCase = true) ||
                it.direccion.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Clínicas\nVeterinarias",
                subtitle = "Busca tu Clínica\nVeterinaria",
                onBack = onBack,
                onLogoClick = { onNavigateToTab(BottomTab.HOME) },
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it }
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
                .padding(horizontal = 16.dp)
        ) {
            // Botón Filtrar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, CelesteFin),
                    color = Color.White,
                    modifier = Modifier.clickable { }
                ) {
                    Text(
                        text = "Filtrar",
                        color = CelesteTexto,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredClinicas, key = { it.id }) { clinica ->
                    ClinicaCard(
                        clinica = clinica,
                        onToggleFavorite = { viewModel.toggleFavoritoClinica(clinica.id) },
                        onClick = { onSelectClinica(clinica) }
                    )
                    HorizontalDivider(color = FondoClaro, thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun ClinicaCard(
    clinica: Clinica,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(FondoClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clinics),
                contentDescription = null,
                tint = CelesteFin,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = clinica.nombre,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CelesteTexto
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Dirección: ${clinica.direccion}",
                fontSize = 12.sp,
                color = TextoMedio
            )
            Text(
                text = "Horario: ${clinica.horario}",
                fontSize = 12.sp,
                color = TextoMedio
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (clinica.esRecomendada) {
                    Text(
                        text = "Recomendada",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CelesteTexto
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                repeat(5) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = CelesteFin,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (clinica.esFavorita) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorito",
                tint = CelesteFin
            )
        }
    }
}
