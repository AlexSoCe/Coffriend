package com.alexsotocepas.coffriend.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexsotocepas.coffriend.data.User
import com.alexsotocepas.coffriend.ui.theme.CoffriendTheme
import com.alexsotocepas.coffriend.ui.theme.background_dark_color
import com.alexsotocepas.coffriend.ui.theme.background_light_color
import com.alexsotocepas.coffriend.ui.theme.button_dark_color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * Pantalla de visualització i gestió del perfil de l'usuari.
 * Aquesta interfície actua com el centre de gestió personal de l'usuari. Permet consultar
 * dades de perfil (nom, email, rol) i elements de gamificació (nivell, punts i col·lecció
 * d'insígnies). A més, inclou formularis per a l'actualització de dades sensibles com el
 * correu electrònic i la baixa definitiva del sistema.
 * La pantalla adapta els seus components segons el rol de l'usuari (Client, Staff o Admin),
 * amagant els elements de fidelització per als perfils de gestió.
 * @param onDeleted Callback que s'executa per navegar al Login després d'una eliminació de compte satisfactòria.
 * @param onBack Callback per retornar a la pantalla de menú anterior.
 * @param modifier Modificador de [Modifier] per ajustar el disseny de la pantalla.
 * @param viewModel Instància de [ProfileUserViewModel] per gestionar la lògica de dades i estats d'error.
 */
@Composable
fun ProfileUserScreen(
    onDeleted: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileUserViewModel = viewModel()
) {
    val user = User.current
    var email by remember { mutableStateOf(user?.email ?: "") }
    var newEmail by remember { mutableStateOf(user?.email ?: "") }
    var password by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var errorText = viewModel.errorMessage
    val successState = viewModel.isSuccess

    Column(
        modifier = modifier
        .fillMaxSize()
        .background(background_light_color)
        .safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(3.dp, alignment = Alignment.CenterVertically)
    ) {
        // Perfil i botó enrere
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "  Perfil",
                fontSize = 18.sp,
                color = Color.Black
            )

            Button(
                onClick = { onBack() },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = button_dark_color),
                modifier = Modifier.height(35.dp)
            ) {
                Text("⬅\uFE0F Tornar")
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().background(background_dark_color,
                shape = RoundedCornerShape(12.dp)).padding(16.dp).verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Nivell client o no mostra res en cas de ser admin o staff
            val esClient = user?.rol?.lowercase() != "admin" && user?.rol?.lowercase() != "staff"
            if (esClient) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(40.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Fons estrella
                    Text(
                        text = "🌟",
                        fontSize = 60.sp
                    )

                    // Nivell d'usuari
                    Text(
                        text = "${user?.nivell ?: 0}",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // Nom i punts de l'client o nom i rol en cas de treballadors o administradors
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(" ${user?.nom ?: " Demo  "}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(background_dark_color, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Lògica condicional segons el rol
                    val textAMostrar = when (user?.rol?.lowercase()) {
                        "admin" -> "ADMIN 🛠️"
                        "staf", "staff" -> "STAFF ☕"
                        else -> "${user?.punts ?: 0} 🎖️" // Usuari normal o sense rol
                    }
                    Text(textAMostrar, fontSize = 24.sp)
                }
            }

            // Insignies de l'usuari
            println("DEBUG: Usuario ${user?.nom} tiene ${user?.llistaInsignies?.size ?: 0} insignias")
            if (esClient && !user?.llistaInsignies.isNullOrEmpty()) {
                Text(
                    text = "⚜\uFE0F Les meves Insígnies ⚜\uFE0F",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 18.sp
                )

                // Llistat d'insignies
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    user?.llistaInsignies?.forEach { insignia ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(Color(0xFFFFD700), shape = CircleShape), // Color or
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🏅")
                            }
                            Text(text = insignia.nom ?: "Insignia", fontSize = 10.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
                Text("⚜\uFE0F", color = Color.White, fontSize = 20.sp)
            } else if (esClient) {
                Text("Encara no tens cap insígnia. ¡Segueix comprant! ☕", fontSize = 12.sp, color = Color.White)
            }

            // Email i canvi d'email
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .background(
                                        Color(0xFFF5F5F5),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("\uD83D\uDC64", fontSize = 50.sp)
                            }
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Email",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                            )
                            Text(
                                text = "${user?.email ?: "exemple@exemple.com"}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Canviar l'email de l'usuari
                    Text(
                        text = "  Canviar email  ✏\uFE0F",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp) // Espai entre la columna i el botó
                    ) {
                        // Columna de l'esquerra amb els inputs
                        Column(
                            modifier = Modifier.weight(1f) // Això fa que la columna ocupi tot l'espai restant
                        ) {
                            // TextField per l'Email
                            TextField(
                                value = newEmail,
                                onValueChange = { newEmail = it },
                                label = { Text("Correu electrònic nou") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // TextField per la Contrasenya
                            TextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Introdueix la contrasenya") },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Botó de l'edició a la dreta
                        Button(
                            onClick = {
                                if (newEmail.isNotEmpty() && password.isNotEmpty()) {
                                    viewModel.updateEmail(newEmail)
                                }
                            },
                            modifier = Modifier.height(120.dp),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = button_dark_color),
                            enabled = newEmail.isNotEmpty() && password.isNotEmpty()
                        ){
                            Text("✏\uFE0F", fontSize = 30.sp)
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).heightIn(min = 20.dp)) {
                        // Missatge d'error
                        if (errorText != null) {
                            Text(
                                text = errorText,
                                color = Color.Red,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }

                        // Missatge d'èxit
                        if (successState) {
                            Text(
                                text = "Email actualitzat correctament!",
                                color = Color(0xFF4CAF50),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Canvi de contrasenya
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Canviar contrasenya  \uD83D\uDD11",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Nova contrasenya
                    TextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Contrasenya nova") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(), // Ocultar el text
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    )

                    // Repetir contrasenya
                    TextField(
                        value = repeatPassword,
                        onValueChange = { repeatPassword = it },
                        label = { Text("Repeteix la contrasenya") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botó modificar contrasenya
                Button(
                    onClick = {
                        if (password == repeatPassword && password.isNotEmpty()) {
                            // viewModel.updatePassword(password) en desenvolupament pel servidor
                        }
                    },
                    modifier = Modifier.height(150.dp),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = button_dark_color
                    ),
                    enabled = password.isNotEmpty() && password == repeatPassword
                ) {
                    Text("✏\uFE0F", fontSize = 30.sp)
                }
            }

            // Botó d'eliminar compte
            Button(
                onClick = { viewModel.deleteAccount(onDeleted) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar el meu compte", color = Color.White)
            }
        }
    }
}

/**
 * Previsualització (Preview) de la pantalla de perfil [ProfileUserScreen].
 * Renderitza la interfície utilitzant el tema global [CoffriendTheme] per validar
 * la disposició dels elements i el contrast de colors entre les seccions de dades i formularis.
 */
@Preview
@Composable
fun ProfileUserPreview() {
    CoffriendTheme {
        ProfileUserScreen(onDeleted = {}, onBack = {})
    }
}