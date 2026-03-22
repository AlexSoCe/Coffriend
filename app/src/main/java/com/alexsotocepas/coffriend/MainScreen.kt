@file:OptIn(ExperimentalMaterial3Api::class)
package com.alexsotocepas.coffriend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexsotocepas.coffriend.data.User
import com.alexsotocepas.coffriend.ui.LoginScreen
import com.alexsotocepas.coffriend.ui.MenuAdminScreen
import com.alexsotocepas.coffriend.ui.MenuUserScreen
import com.alexsotocepas.coffriend.ui.MenuWorkerScreen
import com.alexsotocepas.coffriend.ui.theme.background_dark_color
import com.alexsotocepas.coffriend.ui.theme.background_light_color

/**
 * Enumeració que defineix les rutes de navegació principals de l'aplicació.
 * @property title El títol descriptiu de la pantalla (útil per a capçaleres o depuració).
 */
enum class MainScreen(val title: String) {
    /** Pantalla d'accés i autenticació. */
    Login(title = "Login"),
    /** Pantalla principal del menú (dinàmica segons el rol). */
    Menu(title = "Menu")
}

/**
 * Composable principal que gestiona el graf de navegació (NavHost) de l'aplicació.
 * Aquesta funció coordina el canvi entre pantalles i aplica la lògica de seguretat
 * per mostrar el menú corresponent segons el rol de l'usuari ([User.role]).
 * @param navController El controlador de navegació. Es crea un per defecte si no se'n passa cap.
 */
@Preview
@Composable
fun Mainview(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = MainScreen.Login.name,
        modifier = Modifier.fillMaxSize().background(background_dark_color)
    ) {
        // Definició de la ruta de Login
        composable(route = MainScreen.Login.name){
            LoginScreen(
                navigate = {navController.navigate(MainScreen.Menu.name){   // Netegem la pila per evitar que l'usuari torni enrere al Login amb el botó físic
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp).clip(MaterialTheme.shapes.medium).background(background_light_color)
            )
        }
        // Definició de la ruta del Menú amb lògica de rols
        composable(route = MainScreen.Menu.name) {
            val context = LocalContext.current
            val user = User.current
            // Decidim quina pantalla mostrar basant-nos en el rol de l'usuari autenticat
            when (user?.role) {
                "admin" -> MenuAdminScreen(
                    modifier = Modifier.fillMaxHeight(),
                    navigate = {
                        navController.navigate(MainScreen.Login.name) {
                            popUpTo(MainScreen.Menu.name) { inclusive = true }
                        }
                    }
                )
                "worker" -> MenuWorkerScreen(
                    modifier = Modifier.fillMaxHeight(),
                    navigate = {
                        navController.navigate(MainScreen.Login.name) {
                            popUpTo(MainScreen.Menu.name) { inclusive = true }
                        }
                    }
                )
                // Rol per defecte ("user") o qualsevol altre cas no especificat
                else -> MenuUserScreen(
                    modifier = Modifier.fillMaxHeight(),
                    navigate = {
                        navController.navigate(MainScreen.Login.name) {
                            popUpTo(MainScreen.Menu.name) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
