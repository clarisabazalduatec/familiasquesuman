package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.R
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.CremaFondo
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipalConMenu(
    navController: NavHostController,
    pantallaActual: PantallaPrincipal,
    titulo: String = "Familias que Suman+",
    navbar: Boolean = true,
    chatbot: Boolean = false,
    acciones: @Composable RowScope.() -> Unit = {},
    contenido: @Composable (
        paddingInterno: PaddingValues
    ) -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuLateral(
                onChatbotClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Rutas.Chatbot.ruta)
                },
                onInicioClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Rutas.Inicio.ruta)
                },
                onIniciarSesionClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Rutas.Login.ruta)
                },
                onCrearCuentaClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Rutas.Login.ruta)
                },
                onComunidadClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Rutas.Comunidad.ruta)
                },

                onActividadesClick = {
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(Rutas.Actividades.ruta)
                },

                onProyectosClick = {
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(Rutas.Proyectos.ruta)
                },

                onDonarClick = {
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(Rutas.Donar.ruta)
                },

                onDirectorioClick = {
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(Rutas.Directorio.ruta)
                },
                onAdminClick = {
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(Rutas.Admin.ruta)
                },
                onPerfilClick = {
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(Rutas.Perfil.ruta)
                }
            )
        }

    ) {

        Scaffold(containerColor = CremaFondo,
            topBar = { TopAppBar(
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = CremaFondo,
                            navigationIconContentColor =AzulMarino,
                            actionIconContentColor =AzulMarino
                        ),

                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch {drawerState.open()
                                }
                            }
                        ) {
                            Icon( imageVector = Icons.Default.Menu, contentDescription ="Menú")
                        }
                    },
                    title = {
                        Image(
                            painter =painterResource(id = R.drawable.logo2_onb),
                            contentDescription =titulo,
                            modifier = Modifier
                                .width(300.dp)
                                .height(62.dp),
                            contentScale =ContentScale.Fit
                        )
                    },
                    actions = acciones
                )
            },

//chatbot
            floatingActionButton = {
                if (chatbot) {

                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Rutas.Chatbot.ruta) {
                                launchSingleTop = true
                            }
                        },
                        containerColor = AzulMarino,
                        contentColor = Color.White,
                        modifier = Modifier.size(68.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SmartToy,
                            contentDescription = "Abrir chatbot",
                            modifier = Modifier.size(34.dp),
                            tint = Color.White
                        )
                    }
                }
            },
            bottomBar = {
                if(navbar) {
                    BarraNavegacionInferior(
                        pantallaActual =
                            pantallaActual,
                        onPantallaSeleccionada =
                            { pantalla ->
                                val ruta =
                                    when (pantalla) {
                                        PantallaPrincipal.INICIO -> Rutas.Inicio.ruta
                                        PantallaPrincipal.ACTIVIDADES -> Rutas.Actividades.ruta
                                        PantallaPrincipal.PROYECTOS -> Rutas.Proyectos.ruta
                                        PantallaPrincipal.DONAR -> Rutas.Donar.ruta
                                        PantallaPrincipal.DIRECTORIO -> Rutas.Directorio.ruta
                                        PantallaPrincipal.CHATBOT -> Rutas.Chatbot.ruta
                                    }
                                navController.navigate(ruta) { launchSingleTop = true }
                            }
                    )
                }
            }
        ) { paddingInterno ->contenido( paddingInterno )
        }
    }
}