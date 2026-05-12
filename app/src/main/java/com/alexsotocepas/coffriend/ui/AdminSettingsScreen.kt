package com.alexsotocepas.coffriend.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexsotocepas.coffriend.communications.CommController
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.alexsotocepas.coffriend.ui.theme.CoffriendTheme

/**
 * Pantalla de configuració i manteniment del sistema per a perfils d'administrador.
 * Aquesta vista proporciona eines crítiques per a la gestió de la base de dades en entorns
 * de desenvolupament o demostració, permetent la inserció de dades de prova (Seed)
 * i el buidatge complet del sistema (Reset).
 * @param onBack Funció lambda que es dispara per navegar enrere cap a la pantalla anterior.
 */
@Composable
fun AdminSettingsScreen(onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Gestió del Sistema", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        // Botón SEED
        Button(
            onClick = {
                scope.launch {
                    val ok = CommController.doSystemDemoSeed()
                    message = if (ok) "Base de dades poblada!" else "Error al fer seed"
                }
            },
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("Generar Dades (Seed)")
        }

        // Botó PURGE
        Button(
            onClick = {
                scope.launch {
                    val ok = CommController.doSystemResetDemo()
                    message = if (ok) "Dades resetejades!" else "Error al fer reset"
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("Esborrar Base de Dades", color = Color.White)
        }

        if (message.isNotEmpty()) {
            Text(text = message, color = Color.Blue, modifier = Modifier.padding(10.dp))
        }

        Button(onClick = onBack) { Text("Tornar") }
    }
}

/**
 * Funció de previsualització (Preview) per a l'editor de disseny d'Android Studio.
 * Renderitza la interfície de [AdminSettingsScreen] per visualitzar els controls
 * de gestió del sistema sota el tema [CoffriendTheme].
 */
@Preview
@Composable
fun AdminSettingsScreenPreview() {
    CoffriendTheme {
        AdminSettingsScreen(onBack = {})
    }
}
