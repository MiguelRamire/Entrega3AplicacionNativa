package com.upb.petshield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.petshield.ui.theme.CelesteFin
import com.upb.petshield.ui.theme.CelesteTexto
import com.upb.petshield.ui.theme.FondoClaro
import com.upb.petshield.ui.theme.PetShieldGradient

@Composable
fun PerfilScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onNavigateToPets: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    val iniciales = viewModel.usuarioNombre
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "U" }

    Scaffold(
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
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PetShieldGradient)
                    .padding(top = 28.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "Mi Perfil",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_logo_blanco),
                        contentDescription = "Logo",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(90.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .size(86.dp)
                                .clip(CircleShape)
                                .background(CelesteTexto),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = iniciales,
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, CelesteFin, CircleShape)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = CelesteFin,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Column {
                        Text(
                            text = viewModel.usuarioNombre,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = viewModel.usuarioTelefono,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = viewModel.usuarioEmail,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PerfilMenuItem(
                    icon = Icons.Default.Person,
                    title = "Perfil",
                    onClick = { }
                )
                PerfilMenuItem(
                    icon = Icons.Default.Lock,
                    title = "Política De Privacidad",
                    onClick = { }
                )
                PerfilMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Configuración",
                    onClick = { }
                )
                PerfilMenuItem(
                    icon = Icons.Default.Pets,
                    title = "Mis Mascotas",
                    onClick = onNavigateToPets
                )
                PerfilMenuItem(
                    icon = Icons.Default.HelpOutline,
                    title = "Ayuda",
                    onClick = onNavigateToHelp
                )
                PerfilMenuItem(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    title = "Cerrar Sesión",
                    showChevron = false,
                    onClick = onLogout
                )
            }
        }
    }
}

@Composable
fun PerfilMenuItem(
    icon: ImageVector,
    title: String,
    showChevron: Boolean = true,
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
                .size(46.dp)
                .clip(CircleShape)
                .background(CelesteFin),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        if (showChevron) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = CelesteFin
            )
        }
    }
}
