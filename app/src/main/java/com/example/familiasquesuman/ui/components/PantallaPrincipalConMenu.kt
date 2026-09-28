package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.navigation.Rutas
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipalConMenu(
    navController: NavHostController,
    pantallaActual: PantallaPrincipal,
    titulo: String = "Familias que Suman+",
    acciones: @Composable RowScope.() -> Unit = {},
    contenido: @Composable (paddingInterno: PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuLateral(
                onInicioClick = { scope.launch { drawerState.close() }; navController.navigate(Rutas.Inicio.ruta) },
                onIniciarSesionClick = { scope.launch { drawerState.close() }; navController.navigate(Rutas.Login.ruta) },
                onCrearCuentaClick = { scope.launch { drawerState.close() }; navController.navigate(Rutas.Login.ruta) },
                onComunidadClick = { scope.launch { drawerState.close() }; navController.navigate(Rutas.Comunidad.ruta) }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    title = { Text(titulo) },
                    actions = acciones
                )
            },
            bottomBar = {
                BarraNavegacionInferior(
                    pantallaActual = pantallaActual,
                    onPantallaSeleccionada = { pantalla ->
                        val ruta = when (pantalla) {
                            PantallaPrincipal.INICIO -> Rutas.Inicio.ruta
                            PantallaPrincipal.ACTIVIDADES -> Rutas.Actividades.ruta
                            PantallaPrincipal.PROYECTOS -> Rutas.Proyectos.ruta
                            PantallaPrincipal.DONAR -> Rutas.Donar.ruta
                            PantallaPrincipal.DIRECTORIO -> Rutas.Directorio.ruta
                        }
                        navController.navigate(ruta) { launchSingleTop = true }
                    }
                )
            }
        ) { paddingInterno ->
            contenido(paddingInterno)
        }
    }
}