package com.example.petshield

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

@Composable
fun ClinicasScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onSelectClinica: (Clinica) -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

        it.nombre.contains(searchQuery, ignoreCase = true) ||
                it.direccion.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Clínicas\nVeterinarias",
                subtitle = "Busca tu Clínica\nVeterinaria",
                onBack = onBack,
                searchQuery = searchQuery,
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
                    color = Color.White,
                    modifier = Modifier.clickable { }
                ) {
                    Text(
                        text = "Filtrar",
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                    ClinicaCard(
                        clinica = clinica,
                        onToggleFavorite = { viewModel.toggleFavoritoClinica(clinica.id) },
                        onClick = { onSelectClinica(clinica) }
                    )
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
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clinics),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = clinica.nombre,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Dirección: ${clinica.direccion}",
            )
            Text(
                text = "Horario: ${clinica.horario}",
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (clinica.esRecomendada) {
                    Text(
                        text = "Recomendada",
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                repeat(5) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (clinica.esFavorita) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorito",
            )
        }
    }
}
