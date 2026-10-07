<<<<<<< HEAD
package com.example.petshield // Asegúrate de que coincida con tu paquete
=======
package com.example.petshield
>>>>>>> origin/main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.graphics.Brush
=======
>>>>>>> origin/main
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
<<<<<<< HEAD

@OptIn(ExperimentalMaterial3Api::class)
=======
import com.example.petshield.ui.theme.CelesteInicio
import com.example.petshield.ui.theme.FondoClaro
import com.example.petshield.ui.theme.PetShieldGradient

>>>>>>> origin/main
@Composable
fun RecoverPasswordScreen(
    onBack: () -> Unit,
    onSubmitEmail: () -> Unit
) {
    var email by remember { mutableStateOf("") }

<<<<<<< HEAD
    val colorDegradadoInicio = Color(0xFF33E4DB)
    val colorDegradadoFin = Color(0xFF00BBD3)
    val colorFondoInput = Color(0xFFE9F6FE)

=======
>>>>>>> origin/main
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
<<<<<<< HEAD
        // --- BARRA SUPERIOR ---
=======
>>>>>>> origin/main
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
<<<<<<< HEAD
                .background(Brush.horizontalGradient(listOf(colorDegradadoInicio, colorDegradadoFin)))
=======
                .background(PetShieldGradient)
>>>>>>> origin/main
                .padding(top = 30.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
            }
            Text(text = "Recuperar Contraseña", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Image(
                painter = painterResource(id = R.drawable.ic_logo_blanco),
                contentDescription = "Logo",
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

<<<<<<< HEAD
        // --- CONTENIDO ---
=======
>>>>>>> origin/main
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
<<<<<<< HEAD
            // Párrafo descriptivo
            Text(
                text = "Ingrese el correo electronico de su cuenta y escriba su nueva contraseña en el correo que recibira",
=======
            Text(
                text = "Ingrese el correo electrónico de su cuenta y siga las instrucciones del correo que recibirá para restablecer su contraseña.",
>>>>>>> origin/main
                fontSize = 14.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Start,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

<<<<<<< HEAD
            // Campo Correo Electrónico
            Text(
                text = "Correo Electronico",
=======
            Text(
                text = "Correo Electrónico",
>>>>>>> origin/main
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
<<<<<<< HEAD
                placeholder = { Text("example@example.com", color = colorDegradadoInicio) },
=======
                placeholder = { Text("example@example.com", color = CelesteInicio) },
>>>>>>> origin/main
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
<<<<<<< HEAD
                    focusedContainerColor = colorFondoInput,
                    unfocusedContainerColor = colorFondoInput,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorDegradadoInicio
=======
                    focusedContainerColor = FondoClaro,
                    unfocusedContainerColor = FondoClaro,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = CelesteInicio
>>>>>>> origin/main
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

<<<<<<< HEAD
            // Botón Enviar correo electrónico
=======
>>>>>>> origin/main
            Button(
                onClick = onSubmitEmail,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
<<<<<<< HEAD
                        brush = Brush.horizontalGradient(listOf(colorDegradadoInicio, colorDegradadoFin)),
=======
                        brush = PetShieldGradient,
>>>>>>> origin/main
                        shape = RoundedCornerShape(25.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Text(
<<<<<<< HEAD
                    text = "Enviar correo electronico",
=======
                    text = "Enviar correo electrónico",
>>>>>>> origin/main
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/main
