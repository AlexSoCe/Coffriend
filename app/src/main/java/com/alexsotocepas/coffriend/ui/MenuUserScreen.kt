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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexsotocepas.coffriend.communications.ServerRequests
import com.alexsotocepas.coffriend.data.User
import com.alexsotocepas.coffriend.ui.theme.CoffriendTheme
import com.alexsotocepas.coffriend.ui.theme.background_light_color
import com.alexsotocepas.coffriend.ui.theme.button_dark_color
import com.alexsotocepas.coffriend.ui.theme.button_light_color

/**
 * Pantalla de menú principal per als usuaris amb rol de client.
 * Aquesta vista permet als clients explorar el catàleg de productes (cafès),
 * afegir-los a la cistella i gestionar el seu perfil. Inclou una capçalera de
 * benvinguda, un llistat de productes amb scroll vertical i una barra de
 * navegació inferior amb icones per a les diferents seccions de l'app.
 * @param modifier Modificador per aplicar estils o dimensions des del contenidor pare.
 * @param navigate Funció lambda que s'executa per navegar enrere (normalment al Login) després de fer logout.
 * @param viewModel Instància del ViewModel que conté l'estat de l'usuari i la lògica de tancament de sessió.
 */
@Composable
fun MenuUserScreen(
    modifier: Modifier = Modifier,
    navigate: ()->Unit={},
    viewModel:MenuUserViewModel=viewModel()
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(background_light_color)
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(3.dp, alignment = Alignment.CenterVertically)
    ) {
        // Secció de Benvinguda i Tancament de Sessió
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "  Benvingut, ${viewModel.currentUser?.username ?:"Usuari"}",
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

        // Cos principal: Catàleg de productes
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(15) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("☕", fontSize = 30.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Cafè ${index + 1}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Descripció del cafè número ${index + 1} llest per afegir a la cistella.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            maxLines = 2
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {},
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = button_dark_color)
                    ) {
                        Text("🛒", fontSize = 18.sp)
                    }
                }
            }
        }

        // Menú de navegació inferior
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
                colors = ButtonDefaults.buttonColors(containerColor = button_dark_color),
                modifier = Modifier.height(60.dp)
            ) {
                Text("☕", fontSize = 48.sp)
            }
            Button(
                onClick = {},
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = button_light_color),
                modifier = Modifier.height(60.dp)
            ) {
                Text("\uD83E\uDD47", fontSize = 48.sp)
            }
            Button(
                onClick = {},
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = button_light_color),
                modifier = Modifier.height(60.dp)
            ) {
                Text("\uD83D\uDED2", fontSize = 40.sp)
            }
            Button(
                onClick = {},
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
 * Funció de previsualització per a l'editor de disseny.
 * Mostra la interfície del menú d'usuari amb el tema de l'aplicació.
 */
@Preview
@Composable
fun MenuUserPreview() {
    CoffriendTheme {
        MenuUserScreen()
    }
}
