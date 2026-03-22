package com.alexsotocepas.coffriend.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Definició de l'esquema de colors personalitzat per a l'aplicació Coffriend.
 * Utilitza una paleta basada en [lightColorScheme] però adaptada amb colors
 * corporatius foscos i clars (com `button_dark_color` i `background_dark_color`).
 * Aquest esquema ignora la configuració del sistema (mode fosc/clar) per mantenir
 * una experiència visual consistent en tot moment.
 */
private val CoffriendColorScheme = lightColorScheme(
    primary = button_dark_color,
    onPrimary = text_light_color,

    background = background_dark_color,
    surface = background_light_color,
    onBackground = text_black_color,
    onSurface = text_black_color,

)

/**
 * Composable que defineix el tema visual global de l'aplicació.
 * Aquest component envolta la jerarquia de la UI i aplica l'estil de Material Design 3.
 * També gestiona efectes secundaris ([SideEffect]) per configurar l'aparença de la
 * barra d'estat del sistema operatiu (la zona de l'hora i bateria).
 * @param content El contingut de la interfície d'usuari que s'ha de renderitzar amb aquest tema.
 */
@Composable
fun CoffriendTheme(
    content: @Composable () -> Unit
) {
    // Forcem sempre el mateix esquema de colors independentment de la configuració del sistema
    val colorScheme = CoffriendColorScheme
    val view = LocalView.current

    // Configuració de la barra d'estat si no estem en mode de previsualització (EditMode)
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            // Ajustem el color de la barra d'estat per a que coincideixi amb el fons de l'app
            window.statusBarColor = colorScheme.background.toArgb()
            // Determina si les icones de la barra d'estat han de ser fosques o clares
            insetsController.isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,    // Utilitza la configuració de tipografia definida a Typography.kt
        content = content
    )
}