package com.alexsotocepas.coffriend.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexsotocepas.coffriend.R
import com.alexsotocepas.coffriend.ui.theme.CoffriendTheme
import com.alexsotocepas.coffriend.ui.theme.background_light_color
import com.alexsotocepas.coffriend.ui.theme.button_dark_color
import com.alexsotocepas.coffriend.ui.theme.button_light_color

/**
 * Pantalla de menú principal per als usuaris amb rol d'administrador.
 * Aquesta vista proporciona accés a les funcionalitats de gestió global de l'aplicació.
 * Presenta una capçalera amb el nom de l'administrador i opció de logout, un cos
 * central que mostra el logotip de l'empresa i una barra de navegació inferior
 * amb accés a la configuració del sistema i al perfil.
 * @param modifier Modificador per aplicar estils o ajustar el disseny des del NavHost.
 * @param navigate Funció de retorn per gestionar el canvi de pantalla cap al Login en tancar sessió.
 * @param viewModel Instància del ViewModel que gestiona les dades i accions de l'administrador.
 */
@Composable
fun MenuAdminScreen(
    modifier: Modifier = Modifier,
    navigate: ()->Unit={},
    profileNavigate: () -> Unit = {},
    viewModel:MenuAdminViewModel=viewModel()
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(background_light_color)
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(3.dp, alignment = Alignment.CenterVertically)
    ) {
        // Secció de Benvinguda i Control de Sessió
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "  Benvingut, ${viewModel.currentUser?.nom ?:"Usuari"}",
                fontSize = 18.sp,
                color = Color.Black
            )

            Button(
                onClick = {
                    viewModel.logout(){
                        navigate()
                    }
                },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = button_dark_color),
                modifier = Modifier.height(35.dp)
            ) {
                Text("Tancar sessió")
            }
        }

        // Cos principal: Panell d'Administració (actualment amb el logotip central)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo de Coffriend",
                    modifier = Modifier
                        .size(200.dp)
                )
            }
        }

        // Menú inferior de navegació de l'administrador
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(button_dark_color)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {},
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = button_light_color),
                modifier = Modifier.height(60.dp)
            ) {
                Text("⚙\uFE0F", fontSize = 48.sp)
            }
            Button(
                onClick = {profileNavigate()},
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = button_light_color),
                modifier = Modifier.height(60.dp)
            ) {
                Text("👤", fontSize = 48.sp)
            }
        }
    }
}

/**
 * Funció per previsualitzar el disseny de la pantalla d'administrador.
 * Utilitza el tema CoffriendTheme per mostrar la fidelitat visual final.
 */
@Preview
@Composable
fun MenuAdminPreview() {
    CoffriendTheme {
        MenuAdminScreen()
    }
}
