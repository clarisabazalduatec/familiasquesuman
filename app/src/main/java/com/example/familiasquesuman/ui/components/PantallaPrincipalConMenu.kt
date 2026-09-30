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

    // =========================================================
    // ESTOS PARÁMETROS YA LOS USABAN OTRAS PANTALLAS
    // NO LOS QUITAMOS
    // =========================================================

    navbar: Boolean = true,

    chatbot: Boolean = true,

    backButton: Boolean = false,

    onBackClick: () -> Unit = {},

    acciones: @Composable RowScope.() -> Unit = {},

    contenido: @Composable (
        paddingInterno: PaddingValues
    ) -> Unit
) {

    val drawerState =
        rememberDrawerState(
            initialValue = DrawerValue.Closed
        )

    val scope =
        rememberCoroutineScope()


    // =========================================================
    // SESIÓN TEMPORAL PARA EL FRONTEND
    // =========================================================

    val tipoSesion =
        SesionDemoState.tipoSesion


    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            MenuLateral(

                // =================================================
                // TIPO DE SESIÓN
                // =================================================

                tipoSesion = tipoSesion,

                nombreUsuario = "María Gzz",

                onTipoSesionChange = { nuevoTipo ->

                    SesionDemoState.tipoSesion =
                        nuevoTipo
                },


                // =================================================
                // INICIO
                // =================================================

                onInicioClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    navController.navigate(
                        Rutas.Inicio.ruta
                    ) {
                        launchSingleTop = true
                    }
                },


                // =================================================
                // ACTIVIDADES
                // =================================================

                onActividadesClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    navController.navigate(
                        Rutas.Actividades.ruta
                    ) {
                        launchSingleTop = true
                    }
                },


                // =================================================
                // PROYECTOS
                // =================================================

                onProyectosClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    navController.navigate(
                        Rutas.Proyectos.ruta
                    ) {
                        launchSingleTop = true
                    }
                },


                // =================================================
                // DONAR
                // =================================================

                onDonarClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    navController.navigate(
                        Rutas.Donar.ruta
                    ) {
                        launchSingleTop = true
                    }
                },


                // =================================================
                // COMUNIDAD
                // =================================================

                onComunidadClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    navController.navigate(
                        Rutas.Comunidad.ruta
                    ) {
                        launchSingleTop = true
                    }
                },


                // =================================================
                // DIRECTORIO
                // =================================================

                onDirectorioClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    navController.navigate(
                        Rutas.Directorio.ruta
                    ) {
                        launchSingleTop = true
                    }
                },


                // =================================================
                // CHATBOT
                // =================================================

                onChatbotClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    navController.navigate(
                        Rutas.Chatbot.ruta
                    ) {
                        launchSingleTop = true
                    }
                },
                onSobreNosotrosClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    navController.navigate(
                        Rutas.SobreNosotros.ruta
                    ) {
                        launchSingleTop = true
                    }
                },
                onContactoClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    navController.navigate(
                        Rutas.ContactoAyuda.ruta
                    ) {
                        launchSingleTop = true
                    }
                },
                onPerfilClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    navController.navigate(
                        Rutas.Perfil.ruta
                    ) {
                        launchSingleTop = true
                    }
                },


                // =================================================
                // ADMIN
                // =================================================

                onAdminClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    navController.navigate(
                        Rutas.Admin.ruta
                    ) {
                        launchSingleTop = true
                    }
                },


                // =================================================
                // CONFIGURACIÓN
                // =================================================

                onConfiguracionClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    /*
                     * Ahorita solo cerramos el drawer.
                     *
                     * Cuando exista la pantalla de configuración:
                     *
                     * navController.navigate(
                     *     Rutas.Configuracion.ruta
                     * )
                     */
                },


                // =================================================
                // INICIAR SESIÓN
                // =================================================

                onIniciarSesionClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    /*
                     * DEMO FRONTEND.
                     *
                     * Más adelante esto lo manejará
                     * el AuthViewModel.
                     */

                    SesionDemoState.tipoSesion =
                        TipoSesion.USUARIO

                    navController.navigate(
                        Rutas.Login.ruta
                    ) {
                        launchSingleTop = true
                    }
                },


                // =================================================
                // CREAR CUENTA
                // =================================================

                onCrearCuentaClick = {

                    scope.launch {
                        drawerState.close()
                    }

                    /*
                     * Temporalmente abre Login.
                     * Cuando tengan Registro,
                     * aquí se cambia la ruta.
                     */

                    navController.navigate(
                        Rutas.Login.ruta
                    ) {
                        launchSingleTop = true
                    }
                },


                // =================================================
                // CERRAR SESIÓN
                // =================================================

                onCerrarSesionClick = {

                    SesionDemoState.tipoSesion =
                        TipoSesion.INVITADO

                    scope.launch {
                        drawerState.close()
                    }

                    navController.navigate(
                        Rutas.Inicio.ruta
                    ) {
                        launchSingleTop = true
                    }
                },


                // =================================================
                // X DEL SIDEBAR
                // =================================================

                onBackClick = {

                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {

        Scaffold(

            containerColor = CremaFondo,


            // =====================================================
            // TOP BAR
            // =====================================================

            topBar = {

                TopAppBar(

                    colors =
                        TopAppBarDefaults
                            .topAppBarColors(

                                containerColor =
                                    CremaFondo,

                                navigationIconContentColor =
                                    AzulMarino,

                                actionIconContentColor =
                                    AzulMarino
                            ),


                    // =================================================
                    // AQUÍ DECIDIMOS SI SALE ← O ☰
                    // =================================================

                    navigationIcon = {

                        if (backButton) {

                            // -----------------------------------------
                            // BOTÓN REGRESAR
                            // -----------------------------------------

                            IconButton(
                                onClick = onBackClick
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.AutoMirrored
                                            .Filled
                                            .ArrowBack,

                                    contentDescription =
                                        "Regresar",

                                    tint =
                                        AzulMarino
                                )
                            }

                        } else {

                            // -----------------------------------------
                            // BOTÓN SIDEBAR
                            // -----------------------------------------

                            IconButton(
                                onClick = {

                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.Menu,

                                    contentDescription =
                                        "Menú",

                                    tint =
                                        AzulMarino
                                )
                            }
                        }
                    },


                    // =================================================
                    // LOGO
                    // =================================================

                    title = {

                        Image(
                            painter =
                                painterResource(
                                    id =
                                        R.drawable.logo2_onb
                                ),

                            contentDescription =
                                titulo,

                            modifier =
                                Modifier
                                    .width(300.dp)
                                    .height(62.dp),

                            contentScale =
                                ContentScale.Fit
                        )
                    },


                    // =================================================
                    // ACCIONES: NOTIFICACIONES, ETC.
                    // =================================================

                    actions =
                        acciones
                )
            },


            // =====================================================
            // CHATBOT FLOTANTE
            // =====================================================

            floatingActionButton = {

                if (chatbot) {

                    FloatingActionButton(

                        onClick = {

                            navController.navigate(
                                Rutas.Chatbot.ruta
                            ) {
                                launchSingleTop = true
                            }
                        },

                        containerColor =
                            AzulMarino,

                        contentColor =
                            Color.White,

                        shape =
                            CircleShape,

                        elevation =
                            FloatingActionButtonDefaults
                                .elevation(

                                    defaultElevation =
                                        6.dp,

                                    pressedElevation =
                                        9.dp
                                ),

                        modifier =
                            Modifier.size(68.dp)
                    ) {

                        Icon(
                            imageVector =
                                Icons.Outlined.SmartToy,

                            contentDescription =
                                "Abrir chatbot",

                            modifier =
                                Modifier.size(34.dp),

                            tint =
                                Color.White
                        )
                    }
                }
            },


            // =====================================================
            // NAVBAR INFERIOR
            // =====================================================

            bottomBar = {

                /*
                 * IMPORTANTE:
                 *
                 * Algunas pantallas como Confirmación
                 * usan navbar = false.
                 */

                if (navbar) {

                    BarraNavegacionInferior(

                        pantallaActual =
                            pantallaActual,

                        onPantallaSeleccionada = {
                                pantalla ->


                            val ruta =
                                when (pantalla) {

                                    PantallaPrincipal.INICIO ->
                                        Rutas.Inicio.ruta


                                    PantallaPrincipal.ACTIVIDADES ->
                                        Rutas.Actividades.ruta


                                    PantallaPrincipal.PROYECTOS ->
                                        Rutas.Proyectos.ruta


                                    PantallaPrincipal.DONAR ->
                                        Rutas.Donar.ruta


                                    PantallaPrincipal.DIRECTORIO ->
                                        Rutas.Directorio.ruta


                                    PantallaPrincipal.CHATBOT ->
                                        Rutas.Chatbot.ruta


                                    PantallaPrincipal.PERFIL ->
                                        Rutas.Perfil.ruta
                                }


                            navController.navigate(
                                ruta
                            ) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

        ) { paddingInterno ->

            contenido(
                paddingInterno
            )
        }
    }
}