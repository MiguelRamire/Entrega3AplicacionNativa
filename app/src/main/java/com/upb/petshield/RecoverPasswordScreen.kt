package com.upb.petshield

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upb.petshield.ui.theme.CelesteInicio
import com.upb.petshield.ui.theme.FondoClaro
import com.upb.petshield.ui.theme.PetShieldGradient

@Composable
fun RecoverPasswordScreen(
    onBack: () -> Unit,
    onSubmitEmail: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(PetShieldGradient)
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ingrese el correo electrónico de su cuenta y siga las instrucciones del correo que recibirá para restablecer su contraseña.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Start,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Correo Electrónico",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("example@example.com", color = CelesteInicio) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = FondoClaro,
                    unfocusedContainerColor = FondoClaro,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = CelesteInicio
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onSubmitEmail,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        brush = PetShieldGradient,
                        shape = RoundedCornerShape(25.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Text(
                    text = "Enviar correo electrónico",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
