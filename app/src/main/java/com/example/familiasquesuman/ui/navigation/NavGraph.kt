
package com.example.familiasquesuman.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.familiasquesuman.ui.screens.chatbot.ChatbotScreen
import com.example.familiasquesuman.ui.screens.comun.PantallaProximamente
import com.example.familiasquesuman.ui.screens.comunidad.ComunidadScreen
import com.example.familiasquesuman.ui.screens.comunidad.NuevaPublicacionScreen
import com.example.familiasquesuman.ui.screens.comunidad.ResultadoPublicacionScreen
import com.example.familiasquesuman.ui.screens.directorio.DirectorioScreen
import com.example.familiasquesuman.ui.screens.donar.DonacionDetalleScreen
import com.example.familiasquesuman.ui.screens.donar.DonacionScreen
import com.example.familiasquesuman.ui.screens.inicio.InicioScreen
import com.example.familiasquesuman.ui.screens.login.LoginScreen
import com.example.familiasquesuman.ui.screens.notificaciones.NotificacionesScreen
import com.example.familiasquesuman.ui.screens.onboarding.OnboardingScreen

@Composable
fun NavGraphFamilias(navController: NavHostController = rememberNavController(), mostrarOnboarding: Boolean, marcarOnboardingVisto: () -> Unit
) {
    NavHost(navController = navController, startDestination = if (mostrarOnboarding) { Rutas.Onboarding.ruta
        } else {
            Rutas.Inicio.ruta
        }
    ) {

        composable(Rutas.Onboarding.ruta) {
            OnboardingScreen(
                onComenzar = {
                    marcarOnboardingVisto()
                    navController.navigate(Rutas.Inicio.ruta) {
                        popUpTo(Rutas.Onboarding.ruta) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Rutas.Inicio.ruta) { InicioScreen(navController = navController) }
        composable(Rutas.Actividades.ruta) { PantallaProximamente("Actividades") }
        composable(Rutas.Donar.ruta) { DonacionScreen(navController = navController) }
        composable(
            route = "detalle_donacion/{donacionId}",
            arguments = listOf(
                navArgument("donacionId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments
                ?.getString("donacionId") ?: ""

            DonacionDetalleScreen(
                navController = navController,
                donacionId = id
            )
        }

        composable(Rutas.Proyectos.ruta) { PantallaProximamente("Proyectos") }
        composable(Rutas.Directorio.ruta) { DirectorioScreen(navController = navController) }
        composable(Rutas.Chatbot.ruta) { ChatbotScreen(navController) }
        composable(Rutas.Login.ruta) { LoginScreen(navController = navController) }
        composable(Rutas.Comunidad.ruta) { ComunidadScreen(navController = navController) }
        composable(Rutas.NuevaPublicacion.ruta) { NuevaPublicacionScreen(navController = navController) }
        composable(
            route = Rutas.ResultadoPublicacion.ruta,
            arguments = listOf(
                navArgument("fueExitoso") {
                    type = NavType.BoolType
                }
            )
        ) { backStackEntry ->
            val fueExitoso = backStackEntry.arguments
                ?.getBoolean("fueExitoso") ?: false

            ResultadoPublicacionScreen(
                navController = navController,
                fueExitoso = fueExitoso
            )
        }
        composable(Rutas.Notificaciones.ruta) {
            NotificacionesScreen(navController = navController)
        }
    }
}