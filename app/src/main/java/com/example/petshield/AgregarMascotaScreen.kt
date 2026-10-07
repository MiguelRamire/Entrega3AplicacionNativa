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
import com.example.petshield.ui.theme.TextoMedio
import com.example.petshield.ui.theme.TextoPlaceholder

@Composable
fun AgregarMascotaScreen(
    viewModel: PetShieldViewModel,
    onBack: () -> Unit,
    onNavigateToCarnet: () -> Unit,
    onNavigateToTab: (BottomTab) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }

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
                .imePadding()
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
                placeholder = { Text("Ej. Max", color = TextoPlaceholder) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = customTextFieldColors(FondoClaro, CelesteFin)
            )

            Spacer(modifier = Modifier.height(12.dp))

            FormFieldLabel(label = "Especie")
            OutlinedTextField(
                value = especie,
                onValueChange = { especie = it },
                placeholder = { Text("Ej. Perro", color = TextoPlaceholder) },
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
                placeholder = { Text("Ej. Golden Retriever", color = TextoPlaceholder) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = customTextFieldColors(FondoClaro, CelesteFin)
            )

            Spacer(modifier = Modifier.height(12.dp))

            FormFieldLabel(label = "Peso")
            OutlinedTextField(
                value = peso,
                onValueChange = { peso = it },
                placeholder = { Text("Ej. 12 kg", color = TextoPlaceholder) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = customTextFieldColors(FondoClaro, CelesteFin)
            )

            Spacer(modifier = Modifier.height(12.dp))

            FormFieldLabel(label = "Fecha de nacimiento")
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                placeholder = { Text("DD/MM/AAAA", color = TextoPlaceholder) },
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
                placeholder = { Text("Ej. Macho", color = TextoPlaceholder) },
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
                Text(text = "Guardar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                            fontSize = 14.sp,
                            color = TextoMedio,
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
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = TextoMedio,
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
