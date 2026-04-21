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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.alexsotocepas.coffriend.data.User
import com.alexsotocepas.coffriend.ui.theme.CoffriendTheme
import com.alexsotocepas.coffriend.ui.theme.background_dark_color
import com.alexsotocepas.coffriend.ui.theme.background_light_color
import com.alexsotocepas.coffriend.ui.theme.button_dark_color

/**
 * Pantalla de visualització i gestió del perfil de l'usuari.
 * Aquesta interfície permet a l'usuari consultar les seves dades personals (nom, email, rol i punts)
 * extretes de l'estat global de l'aplicació. També proporciona funcionalitats per navegar
 * enrere al menú principal o sol·licitar l'eliminació definitiva del compte mitjançant
 * el [ProfileUserViewModel].
 * @param onDeleted Funció callback que s'executa després d'eliminar el compte amb èxit.
 * @param onBack Funció callback per tancar la pantalla i tornar a la vista anterior.
 * @param viewModel Instància del ViewModel que gestiona la lògica de baixa de l'usuari.
 */
@Composable
fun ProfileUserScreen(
    onDeleted: () -> Unit,
    onBack: () -> Unit,
    viewModel: ProfileUserViewModel = viewModel()
) {
    val user = User.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Botó enrere
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onBack() },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = button_dark_color),
                modifier = Modifier.height(35.dp)
            ) {
                Text("⬅ Tornar")
            }

        }

        Column(
            modifier = Modifier.fillMaxSize().background(background_light_color,
                shape = RoundedCornerShape(12.dp)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(text = "El meu perfil", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            // Info de l'usuari
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                Text("Nom: ${user?.nom ?: "Desconegut"}")
                Text("Email: ${user?.email ?: ""}")
                Text("Rol: ${user?.rol ?: ""}")
                Text("Punts: ${user?.punts ?: 0}")
            }

            Spacer(modifier = Modifier.weight(1f))

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
 * Previsualització de la pantalla del perfil d'usuari per a l'entorn de desenvolupament.
 */
@Preview
@Composable
fun ProfileUserPreview() {
    CoffriendTheme {
        ProfileUserScreen(onDeleted = {}, onBack = {})
    }
}