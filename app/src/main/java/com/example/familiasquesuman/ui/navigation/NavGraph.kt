
package com.example.familiasquesuman.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.familiasquesuman.ui.screens.informacion.SobreNosotrosScreen
import com.example.familiasquesuman.ui.screens.informacion.ContactoAyudaScreen
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
import com.example.familiasquesuman.ui.screens.actividad.ActividadScreen
import com.example.familiasquesuman.ui.screens.admin.AdminScreen
import com.example.familiasquesuman.ui.screens.notificaciones.NotificacionesScreen
import com.example.familiasquesuman.ui.screens.onboarding.OnboardingScreen
import com.example.familiasquesuman.ui.screens.perfil.PerfilScreen
import com.example.familiasquesuman.ui.screens.proyectos.ProyectoDetalleScreen
import com.example.familiasquesuman.ui.screens.proyectos.ProyectosScreen
import com.example.familiasquesuman.ui.screens.comunidad.MisPublicacionesScreen
import com.example.familiasquesuman.ui.screens.comunidad.PublicacionDetalleScreen

@Composable
fun NavGraphFamilias(navController: NavHostController = rememberNavController(), mostrarOnboarding: Boolean, marcarOnboardingVisto: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = if (mostrarOnboarding) {
            Rutas.Onboarding.ruta
        } else {
            Rutas.Inicio.ruta
        },
        enterTransition = {
            fadeIn(animationSpec = tween(250))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(250))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(250))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(250))
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
        composable(Rutas.Actividades.ruta) {
            ActividadScreen(navController = navController)
        }
        composable(Rutas.Donar.ruta) {
            DonacionScreen(navController = navController)
        }
        composable(
            route = "detalle_donacion/{donacionId}",
            arguments = listOf(navArgument("donacionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("donacionId") ?: ""
            DonacionDetalleScreen(navController = navController, donacionId = id)
        }

        composable(Rutas.Proyectos.ruta) {
            ProyectosScreen(navController = navController)
        }
        composable(Rutas.Directorio.ruta) {
            DirectorioScreen(navController = navController)
        }
        composable(
            route = Rutas.DirectorioDetalle.ruta,
            arguments = listOf(navArgument("centroId") { type = NavType.StringType })
        ) { backStackEntry ->
            val centroId = backStackEntry.arguments?.getString("centroId") ?: "1"
            com.example.familiasquesuman.ui.screens.directorio.DirectorioDetalleScreen(
                navController = navController,
                centroId = centroId
            )
        }
        composable(Rutas.Chatbot.ruta) {
            ChatbotScreen(navController)
        }
        composable (Rutas.Login.ruta){
            LoginScreen(navController = navController)
        }
        composable(Rutas.Admin.ruta) {
            AdminScreen(navController = navController)
        }
        composable(Rutas.AdminModeracionComunidad.ruta) {
            com.example.familiasquesuman.ui.screens.admin.AdminModeracionComunidadScreen(navController = navController)
        }
        composable(Rutas.AdminModeracionActividades.ruta) {
            com.example.familiasquesuman.ui.screens.admin.AdminModeracionActividadesScreen(navController = navController)
        }
        composable(Rutas.AdminActualizacionDonaciones.ruta) {
            com.example.familiasquesuman.ui.screens.admin.AdminActualizacionDonacionesScreen(navController = navController)
        }
        composable(Rutas.AdminCrearContenido.ruta) {
            com.example.familiasquesuman.ui.screens.admin.AdminCrearContenidoScreen(navController = navController)
        }
        composable(Rutas.AdminModeracionProyectos.ruta) {
            com.example.familiasquesuman.ui.screens.admin.AdminModeracionProyectosScreen(navController = navController)
        }
        composable(
            route = Rutas.AdminEditarContenido.ruta,
            arguments = listOf(
                navArgument("tipo") { type = NavType.StringType },
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val tipo = backStackEntry.arguments?.getString("tipo") ?: "actividad"
            val id = backStackEntry.arguments?.getString("id") ?: ""
            com.example.familiasquesuman.ui.screens.admin.AdminEditarContenidoScreen(
                navController = navController,
                tipo = tipo,
                id = id
            )
        }
        composable(Rutas.Comunidad.ruta) {
            ComunidadScreen(navController = navController)
        }
        composable(Rutas.NuevaPublicacion.ruta) {
            NuevaPublicacionScreen(navController = navController)
        }
        composable(Rutas.Perfil.ruta) {
            PerfilScreen(navController = navController)
        }
        composable(Rutas.SobreNosotros.ruta) {
            SobreNosotrosScreen(
                navController = navController
            )
        }

        composable(Rutas.ContactoAyuda.ruta) {
            ContactoAyudaScreen(
                navController = navController
            )
        }
        composable(
            route = Rutas.ActividadDetalle.ruta,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 1
            com.example.familiasquesuman.ui.screens.actividad.ActividadDetalleScreen(
                navController = navController, 
                actividadId = id
            )
        }
        composable(
            route = Rutas.ActividadParticipar.ruta,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 1
            com.example.familiasquesuman.ui.screens.actividad.ActividadParticiparScreen(
                navController = navController, 
                actividadId = id
            )
        }
        composable(
            route = Rutas.ActividadSeleccionParticipantes.ruta,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 1
            com.example.familiasquesuman.ui.screens.actividad.ActividadSeleccionParticipantesScreen(
                navController = navController, 
                actividadId = id
            )
        }
        composable(
            route = Rutas.ActividadConfirmacion.ruta,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 1
            com.example.familiasquesuman.ui.screens.actividad.ActividadConfirmacionScreen(
                navController = navController, 
                actividadId = id
            )
        }
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
        composable(
            route = Rutas.ProyectoDetalle.ruta,
            arguments = listOf(navArgument("proyectoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val proyectoId = backStackEntry.arguments?.getString("proyectoId") ?: return@composable
            ProyectoDetalleScreen(navController = navController, proyectoId = proyectoId)
        }
        composable(Rutas.NuevaPublicacionOficial.ruta) {
            NuevaPublicacionScreen(
                navController = navController,
                esOficial = true
            )
        }

        composable(Rutas.MisPublicaciones.ruta) {
            MisPublicacionesScreen(
                navController = navController
            )
        }

        composable(
            route = Rutas.DetallePublicacion.ruta,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments
                ?.getString("id")
                ?: ""

            PublicacionDetalleScreen(
                navController = navController,
                publicacionId = id
            )
        }

    }
}