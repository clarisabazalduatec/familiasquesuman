package com.example.familiasquesuman.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.ui.screens.comun.PantallaProximamente
import com.example.familiasquesuman.ui.screens.inicio.InicioScreen
import com.example.familiasquesuman.ui.screens.login.LoginScreen

@Composable
fun NavGraphFamilias(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Rutas.Inicio.ruta) {
        composable(Rutas.Inicio.ruta) {
            InicioScreen(navController = navController)
        }
        composable(Rutas.Actividades.ruta) {
            PantallaProximamente("Actividades")
        }
        composable(Rutas.Donar.ruta) {
            PantallaProximamente("Donar")
        }
        composable(Rutas.Proyectos.ruta) {
            PantallaProximamente("Proyectos")
        }
        composable(Rutas.Directorio.ruta) {
            PantallaProximamente("Directorio")
        }
        composable(Rutas.Chatbot.ruta) {
            PantallaProximamente("Chatbot")
        }
        composable (Rutas.Login.ruta){
            LoginScreen(navController = navController)
        }
    }
}