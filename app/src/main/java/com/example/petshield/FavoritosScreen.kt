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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FavoritosScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onAgendarCita: (Veterinario) -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Favoritos",
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
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Ordenar por
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        text = "A → Z",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                    VetCard(
                        vet = vet,
                        onToggleFavorite = { viewModel.toggleFavoritoVet(vet.id) },
                        onAgendar = {
                            viewModel.reservaActual = viewModel.reservaActual.copy(
                                profesionalNombre = vet.nombre
                            )
                            onAgendarCita(vet)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun VetCard(
    vet: Veterinario,
    onToggleFavorite: () -> Unit,
    onAgendar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
                imageVector = Icons.Default.Person,
                contentDescription = "Veterinario",
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                    contentAlignment = Alignment.Center
                ) {
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = vet.clinicaTag,
                )
            }

            Text(
                text = vet.nombre,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = vet.especialidad,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onAgendar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(
                        shape = RoundedCornerShape(18.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
            }
        }

        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (vet.esFavorito) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorito",
            )
        }
    }
}
