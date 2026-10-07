package com.example.petshield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import com.example.petshield.data.Mascota
import com.example.petshield.ui.theme.CelesteFin
import com.example.petshield.ui.theme.FondoClaro
import com.example.petshield.ui.theme.PetShieldGradient

@Composable
fun AgregarMascotaScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onNavigateToCarnet: () -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("Perro") }
    var raza by remember { mutableStateOf("Golden Retriever") }
    var peso by remember { mutableStateOf("12 kg") }
    var fechaNacimiento by remember { mutableStateOf("12/03/2023") }
    var sexo by remember { mutableStateOf("Macho") }

    var estaGuardada by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PetShieldTopBar(
                title = "Agregar Mascota",
                onBack = onBack
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
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(FondoClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🐾", fontSize = 36.sp)
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(CelesteFin),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Foto",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            FormFieldLabel(label = "Nombre de tu mascota")
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = { Text("Ej. Max", color = CelesteFin.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = customTextFieldColors(FondoClaro, CelesteFin)
            )

            Spacer(modifier = Modifier.height(12.dp))

            FormFieldLabel(label = "Especie")
            OutlinedTextField(
                value = especie,
                onValueChange = { especie = it },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Black) },
                shape = RoundedCornerShape(12.dp),
                colors = customTextFieldColors(FondoClaro, CelesteFin)
            )

            Spacer(modifier = Modifier.height(12.dp))

            FormFieldLabel(label = "Raza")
            OutlinedTextField(
                value = raza,
                onValueChange = { raza = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = customTextFieldColors(FondoClaro, CelesteFin)
            )

            Spacer(modifier = Modifier.height(12.dp))

            FormFieldLabel(label = "Peso")
            OutlinedTextField(
                value = peso,
                onValueChange = { peso = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = customTextFieldColors(FondoClaro, CelesteFin)
            )

            Spacer(modifier = Modifier.height(12.dp))

            FormFieldLabel(label = "Fecha de nacimiento")
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.DarkGray) },
                shape = RoundedCornerShape(12.dp),
                colors = customTextFieldColors(FondoClaro, CelesteFin)
            )

            Spacer(modifier = Modifier.height(12.dp))

            FormFieldLabel(label = "Sexo")
            OutlinedTextField(
                value = sexo,
                onValueChange = { sexo = it },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Black) },
                shape = RoundedCornerShape(12.dp),
                colors = customTextFieldColors(FondoClaro, CelesteFin)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (nombre.isNotBlank()) {
                        val nuevaMascota = Mascota(
                            id = (viewModel.mascotas.size + 1).toString(),
                            nombre = nombre,
                            especie = especie,
                            raza = raza,
                            peso = peso,
                            fechaNacimiento = fechaNacimiento,
                            sexo = sexo
                        )
                        viewModel.agregarMascota(nuevaMascota)
                        estaGuardada = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.Start)
                    .height(42.dp)
                    .background(
                        brush = PetShieldGradient,
                        shape = RoundedCornerShape(21.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Text(text = "Guardar", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(visible = estaGuardada) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = FondoClaro
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Mascota guardada",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$nombre ya está registrado.\nPuedes completar su carnet.",
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onNavigateToCarnet,
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
                                text = "Ir al carnet",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormFieldLabel(label: String) {
    Text(
        text = label,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color.DarkGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    )
}

@Composable
fun customTextFieldColors(containerColor: Color, cursorColor: Color) = TextFieldDefaults.colors(
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    focusedContainerColor = containerColor,
    unfocusedContainerColor = containerColor,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    cursorColor = cursorColor
)
