package com.alexsotocepas.coffriend.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexsotocepas.coffriend.ui.theme.CoffriendTheme
import com.alexsotocepas.coffriend.ui.theme.background_green_color
import com.alexsotocepas.coffriend.ui.theme.background_light_color
import com.alexsotocepas.coffriend.ui.theme.background_red_color
import com.alexsotocepas.coffriend.ui.theme.button_dark_color
import com.alexsotocepas.coffriend.ui.theme.button_light_color

/**
 * Pantalla principal per als usuaris amb el rol de treballador.
 * Aquesta vista permet gestionar el llistat de comandes actives, pendents i finalitzades.
 * Inclou una capçalera amb el nom de l'usuari, un botó de tancament de sessió,
 * un cos central amb scroll per veure les comandes i un menú inferior de navegació.
 * @param modifier Modificador per ajustar el disseny o dimensions de la pantalla.
 * @param navigate Funció de retorn per gestionar la navegació cap al Login en tancar sessió.
 * @param viewModel Instància del ViewModel que gestiona la lògica i dades del treballador.
 */
@Composable
fun MenuWorkerScreen(
    modifier: Modifier = Modifier,
    navigate: ()->Unit={},
    viewModel:MenuWorkerViewModel=viewModel()
) {
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
                text = "  Benvingut, ${viewModel.currentUser?.username ?: "Usuari"}",
                fontSize = 18.sp,
                color = Color.Black
            )

            Button(
                onClick = {
                    viewModel.logout() {
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

        // Cos principal amb el llistat de comandes
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(3) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Comanda número ${index + 6841} (19/03/2026 - 10:37)" +
                                    "\nProductes: cafè 1 (x2); cafè 4 (x1).",
                            fontSize = 12.sp,
                            color = Color.Black,
                            maxLines = 2
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {},
                        modifier = Modifier.size(47.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = button_light_color)
                    ) {
                        Text("\uD83D\uDD0D", fontSize = 26.sp)
                    }
                }
            }

            repeat(1) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = background_red_color, shape = RoundedCornerShape(16.dp))
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Comanda número ${index + 6841} (19/03/2026 - 10:37)" +
                                    "\nProductes: cafè 1 (x2); cafè 4 (x1).",
                            fontSize = 12.sp,
                            color = Color.Black,
                            maxLines = 2
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {},
                        modifier = Modifier.size(47.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = button_light_color)
                    ) {
                        Text("❌", fontSize = 26.sp)
                    }
                }
            }
            repeat(10) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = background_green_color, shape = RoundedCornerShape(16.dp))
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Comanda número ${index + 6841} (19/03/2026 - 10:37)" +
                                    "\nProductes: cafè 1 (x2); cafè 4 (x1).",
                            fontSize = 12.sp,
                            color = Color.Black,
                            maxLines = 2
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {},
                        modifier = Modifier.size(47.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = button_light_color)
                    ) {
                        Text("✔\uFE0F", fontSize = 26.sp)
                    }
                }
            }
        }

        // Menú inferior de navegació de l'usuari
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
                Text("\uD83D\uDCCB", fontSize = 48.sp)
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
 * Previsualització de la pantalla MenuWorkerScreen per a l'editor de disseny.
 * Aplica el tema CoffriendTheme per mostrar els colors i formes reals.
 */
@Preview
@Composable
fun MenuWorkerPreview() {
    CoffriendTheme {
        MenuWorkerScreen()
    }
}
