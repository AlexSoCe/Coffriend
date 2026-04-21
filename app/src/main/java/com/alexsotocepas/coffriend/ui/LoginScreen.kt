package com.alexsotocepas.coffriend.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexsotocepas.coffriend.R
import com.alexsotocepas.coffriend.ui.theme.CoffriendTheme
import com.alexsotocepas.coffriend.ui.theme.button_dark_color

/**
 * Pantalla d'inici de sessió de l'aplicació.
 * És la primera interfície que veu l'usuari i permet l'entrada de credencials
 * (usuari i contrasenya) per accedir al sistema. Gestiona visualment els estats
 * d'error i coordina la navegació cap al menú principal un cop l'autenticació és vàlida.
 * @param modifier Modificador per ajustar el disseny o el posicionament de la pantalla.
 * @param navigate Funció de retorn per executar la navegació cap a la següent pantalla.
 * @param registrationNavigate Callback per navegar cap a la pantalla de registre d'usuaris.
 * @param viewModel Instància del ViewModel que gestiona la lògica de login i els estats de xarxa.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigate: ()->Unit={},
    registrationNavigate: () -> Unit = {},
    viewModel:LoginViewModel = viewModel()
){
    val focusManager = LocalFocusManager.current
    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    val loginUiState by viewModel.LoginUiState.collectAsState()
    val ioState by viewModel.uiIOState.collectAsState()

    /**
     * Efecte secundari que monitoritza el resultat de l'operació d'entrada/sortida.
     * Si el resultat és positiu i s'ha confirmat l'intent, s'executa la navegació.
     */
    LaunchedEffect(ioState.goodResult) {
        if (ioState.goodResult && loginUiState.loginTried) {
            navigate()
        }
    }

    Column(
        modifier = modifier.fillMaxSize().safeDrawingPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Logotip de l'aplicació
            Image(
                painter = painterResource(id = R.drawable.logo), // Cambia 'logo_cafeteria' por tu nombre de archivo
                contentDescription = "Logo de Coffriend",
                modifier = Modifier
                    .size(150.dp) // Ajusta el tamaño que quieras
                    .padding(bottom = 24.dp) // Espacio entre el logo y el texto
            )

            // Títol de la secció
            Text("Iniciar sessió:",fontSize=25.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Email
            TextField(
                label= { Text("Email")},
                value= email,
                onValueChange = {email=it},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) } // Mou el focus al camp de sota
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Constrasenya
            TextField(
                label= { Text("Contrasenya")},
                value= password,
                onValueChange = {password=it},
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus() // Tanca el teclat
                        viewModel.doLogin(email, password) { navigate() }
                    }
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            /**
             * Missatge d'error reactiu.
             * Es mostra només quan s'ha intentat fer login i el servidor ha retornat un error.
             */
            if (loginUiState.loginTried && !ioState.goodResult) {
                Text("Usuari i/o contrasenya incorrectes", color = Color.Red)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally), // Espai de 16dp i centrats
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { registrationNavigate() }, // Cridem a la nova funció de navegació
                    colors = ButtonDefaults.buttonColors(containerColor = button_dark_color)
                ) {
                    Text("Registrar")
                }

                Button(
                    onClick = { viewModel.doLogin(email, password) { navigate() } },
                    colors = ButtonDefaults.buttonColors(containerColor = button_dark_color)
                ) {
                    Text("Entrar")
                }
            }
        }
    }
}

/**
 * Previsualització de la pantalla de Login per a l'entorn de desenvolupament.
 */
@Preview
@Composable
fun LoginPreview() {
    CoffriendTheme {
        LoginScreen()
    }
}