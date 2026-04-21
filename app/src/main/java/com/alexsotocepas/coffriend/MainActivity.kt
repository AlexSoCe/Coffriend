package com.alexsotocepas.coffriend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.alexsotocepas.coffriend.ui.theme.CoffriendTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

/**
 * Activitat principal de l'aplicació que serveix com a punt d'entrada de l'execució.
 * Aquesta classe s'encarrega d'inicialitzar l'entorn de Jetpack Compose, configurar
 * la pantalla de benvinguda (Splash Screen) i establir el tema visual global.
 */
class MainActivity : ComponentActivity() {
    /**
     * Mètode de cicle de vida que s'executa en crear l'activitat.
     * Configura els elements base del sistema abans de renderitzar la interfície.
     * @param savedInstanceState Estat prèviament guardat de l'activitat, si existeix.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()    // Inicialitza la pantalla de càrrega amb el logotip de l'app
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffriendTheme {
                Mainview()
            }
        }
    }
}
