package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
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
import com.example.familiasquesuman.ui.model.SesionDemoState
import com.example.familiasquesuman.ui.model.TipoSesion
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
    backButton: Boolean = false,
    onBackClick: () -> Unit = {},

    contenido: @Composable (paddingInterno: PaddingValues) -> Unit) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val tipoSesion = SesionDemoState.tipoSesion

    val navigateTo: (String) -> Unit = { route ->
        scope.launch { drawerState.close() }
        if (navController.currentDestination?.route != route) {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            MenuLateral(
                tipoSesion = tipoSesion,
                nombreUsuario = "Usuario Generico",
                onChatbotClick = { navigateTo(Rutas.Chatbot.ruta) },
                onTipoSesionChange = { nuevoTipo -> SesionDemoState.tipoSesion = nuevoTipo },
                onInicioClick = { navigateTo(Rutas.Inicio.ruta) },
                onActividadesClick = { navigateTo(Rutas.Actividades.ruta) },
                onProyectosClick = { navigateTo(Rutas.Proyectos.ruta) },
                onDonarClick = { navigateTo(Rutas.Donar.ruta) },
                onComunidadClick = { navigateTo(Rutas.Comunidad.ruta) },
                onDirectorioClick = { navigateTo(Rutas.Directorio.ruta) },
                onSobreNosotrosClick = { navigateTo(Rutas.SobreNosotros.ruta) },
                onContactoClick = { navigateTo(Rutas.ContactoAyuda.ruta) },
                onPerfilClick = { navigateTo(Rutas.Perfil.ruta) },
                onAdminClick = { navigateTo(Rutas.Admin.ruta) },
                onIniciarSesionClick = {
                    SesionDemoState.tipoSesion = TipoSesion.USUARIO
                    navigateTo(Rutas.Login.ruta)
                },
                onCrearCuentaClick = { navigateTo(Rutas.Login.ruta) },
                onCerrarSesionClick = {
                    SesionDemoState.tipoSesion = TipoSesion.INVITADO
                    navigateTo(Rutas.Inicio.ruta)
                },
                onBackClick = { scope.launch { drawerState.close() } }
            )
        }
    ) {

        Scaffold(
            containerColor = CremaFondo,
            topBar = { CenterAlignedTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = CremaFondo,
                                navigationIconContentColor = AzulMarino,
                                actionIconContentColor = AzulMarino),

                    navigationIcon = {
                        if (backButton) {
                            IconButton(onClick = onBackClick) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Regresar",
                                    tint = AzulMarino
                                )
                            }
                        } else { IconButton(onClick = { scope.launch { drawerState.open() } }
                            ) {
                                Icon( imageVector = Icons.Default.Menu,
                                    contentDescription = "Menú",
                                    tint = AzulMarino
                                )
                            }
                        }
                    },
                    title = {
                        Image(painter = painterResource(id = R.drawable.logo2_onb),
                            contentDescription = titulo,
                            modifier = Modifier.width(180.dp).height(62.dp),
                            contentScale = ContentScale.Fit
                        )
                    },
                    actions = {
                        if (tipoSesion != TipoSesion.INVITADO) {
                            IconButton(
                                onClick = {
                                    navController.navigate(
                                        Rutas.Notificaciones.ruta
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notificaciones",
                                    tint = AzulMarino
                                )
                            }
                        }
                    }
                )
            },
            floatingActionButton = {if (chatbot) {
                FloatingActionButton(
                    onClick = { navController.navigate(Rutas.Chatbot.ruta) { launchSingleTop = true } },
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
            } },
            bottomBar = {
                if (navbar) {
                    BarraNavegacionInferior(
                        pantallaActual = pantallaActual,
                        onPantallaSeleccionada = { pantalla ->
                            val ruta =
                                when (pantalla) {
                                    PantallaPrincipal.INICIO -> Rutas.Inicio.ruta
                                    PantallaPrincipal.ACTIVIDADES -> Rutas.Actividades.ruta
                                    PantallaPrincipal.PROYECTOS -> Rutas.Proyectos.ruta
                                    PantallaPrincipal.DONAR -> Rutas.Donar.ruta
                                    PantallaPrincipal.DIRECTORIO -> Rutas.Directorio.ruta
                                    PantallaPrincipal.CHATBOT -> Rutas.Chatbot.ruta
                                    PantallaPrincipal.PERFIL -> Rutas.Perfil.ruta
                                }
                            if (navController.currentDestination?.route != ruta) {
                                navController.navigate(ruta) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        )
        { paddingInterno -> contenido(paddingInterno) }
    }
}