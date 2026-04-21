package com.alexsotocepas.coffriend.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexsotocepas.coffriend.R
import com.alexsotocepas.coffriend.ui.theme.CoffriendTheme
import com.alexsotocepas.coffriend.ui.theme.button_dark_color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Pantalla de registre de nous usuaris.
 * Aquesta pantalla recull les dades d'email, nom d'usuari i contrasenya, realitzant
 * validacions locals de concordança de contrasenyes i camps obligatoris abans
 * d'enviar la petició al [RegistrationViewModel].
 * @param modifier Modificador per ajustar el disseny i el posicionament.
 * @param onBack Callback que s'executa per navegar enrere cap a la pantalla de Login.
 * @param viewModel Instància del ViewModel que gestiona l'estat del registre.
 */
@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    viewModel: RegistrationViewModel = viewModel()
) {
    // Estats per als camps de text
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var registrationTried by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    // Subscripció als estats del ViewModel
    val regState by viewModel.registrationUiState.collectAsState()
    val ioState by viewModel.uiIOState.collectAsState()

    // Efecte per tornar al login si el registre és correcte
    LaunchedEffect(regState.isSuccess) {
        if (regState.isSuccess) {
            onBack() // O podríem navegar directament al menú
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de Coffriend",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
        )

        Text("Registrar-se", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))

        // Camp: Email
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email (per iniciar sessió)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(0.8f),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Camp: Nom
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nom d'usuari") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(0.8f),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Camp: Contrasenya
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contrasenya") },
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Camp: Repetir Contrasenya
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Repetir contrasenya") },
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))


        if (regState.registrationTried) {
            when {
                email.isEmpty() || username.isEmpty() || password.isEmpty() -> {
                    Text("Tots els camps són obligatoris", color = Color.Red)
                }
                password != confirmPassword -> {
                    Text("Les contrasenyes no coincideixen", color = Color.Red)
                }
                regState.userAlreadyExists -> {
                    Text("Aquest email ja està registrat", color = Color.Red)
                }
                !ioState.goodResult -> {
                    Text("Error de connexió amb el servidor", color = Color.Red)
                }
            }
        }

        // Botó per registrar
        Button(
            onClick = {
                if (email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                    viewModel.doRegistration(email, username, password)
                    println("Registre correcte per a l'usuari: $username")
                }
            },
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(containerColor = button_dark_color)
        ) {
            Text("Registrar")
        }

        // Botó per tornar enrere
        Button(onClick = onBack) {
            Text("Tornar al Login")
        }
    }
}

/**
 * Previsualització de la pantalla de Registre per a l'entorn de desenvolupament.
 */
@Preview
@Composable
fun RegistrationPreview() {
    CoffriendTheme {
        RegistrationScreen()
    }
}