package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.screens.inicio.SaludoConUbicacion
import com.example.familiasquesuman.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.ui.navigation.Rutas

enum class SeccionPerfil { INFORMACION,  ACTIVIDADES}
@Composable
fun PerfilScreen(
    navController: NavHostController,
    viewModel: PerfilViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var seccionSeleccionada by remember { mutableStateOf(SeccionPerfil.INFORMACION) }
    val context = LocalContext.current

    // Avisos cortos después de una acción ("Invitación enviada", errores de botones sueltos...)
    LaunchedEffect(uiState.mensaje) {
        uiState.mensaje?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.mensajeMostrado()
        }
    }
    val usuarioId = SesionManager.usuario?.id
    LaunchedEffect(usuarioId) { viewModel.cargar() }
    DialogosPerfil(uiState = uiState, viewModel = viewModel)
    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.PERFIL
    ) { paddingInterno ->
        LazyColumn( modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .background(CremaFondo),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 2.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { SaludoConUbicacion(
                    nombreUsuario = com.example.familiasquesuman.data.SesionManager.usuario?.nombre
                    ?.substringBefore(" ") ?: "familia",
                    ciudad = "Monterrey, NL"
                )
            }

            item { SelectorPerfil(
                    seleccion = seccionSeleccionada,
                    onSeleccionChange = {
                        seccionSeleccionada = it
                    }
                )
            }

            if (seccionSeleccionada == SeccionPerfil.INFORMACION) {
                when {
                    !SesionManager.estaAutenticado -> item {
                        PerfilInvitadoCard(onIniciarSesion = { navController.navigate(Rutas.Login.ruta) })
                    }

                    uiState.cargando -> item {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Ambar)
                        }
                    }

                    uiState.errorCarga != null && uiState.familia == null -> item {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(uiState.errorCarga ?: "", color = GrisTexto)
                            TextButton(onClick = { viewModel.cargar() }) { Text("Reintentar", color = Ambar) }
                        }
                    }

                    uiState.familia != null -> item {
                        FamiliaCard(
                            familia = uiState.familia!!,
                            miId = uiState.miId,
                            guardando = uiState.guardando,
                            onEditarMiParentesco = { viewModel.abrirDialogo(DialogoPerfil.MiParentesco) },
                            onInvitar = { viewModel.abrirDialogo(DialogoPerfil.InvitarAdulto) },
                            onCancelarInvitacion = { viewModel.cancelarInvitacion(it) },
                            onAgregarMenor = { viewModel.abrirDialogo(DialogoPerfil.Menor(null)) },
                            onEditarMenor = { viewModel.abrirDialogo(DialogoPerfil.Menor(it)) },
                            onBorrarMenor = { viewModel.abrirDialogo(DialogoPerfil.BorrarMenor(it)) },
                            onSalir = { viewModel.abrirDialogo(DialogoPerfil.SalirDeFamilia) }
                        )
                    }

                    else -> {
                        item { SinFamiliaCard(onCrearFamilia = { viewModel.abrirDialogo(DialogoPerfil.CrearFamilia) }) }
                        if (uiState.invitacionesRecibidas.isNotEmpty()) {
                            item {
                                InvitacionesRecibidasCard(
                                    invitaciones = uiState.invitacionesRecibidas,
                                    guardando = uiState.guardando,
                                    onAceptar = { viewModel.aceptarInvitacion(it) },
                                    onRechazar = { viewModel.rechazarInvitacion(it) }
                                )
                            }
                        }
                    }
                }
                item { MisInsigniasCard() }
            }
            if (seccionSeleccionada == SeccionPerfil.ACTIVIDADES) {
                item {  MisActividadesPerfil() }
            }
            }
        }
    }
/** Muestra el diálogo que indique el estado (o ninguno). */
@Composable
private fun DialogosPerfil(uiState: PerfilUiState, viewModel: PerfilViewModel) {
    val cerrar = { viewModel.cerrarDialogo() }
    when (val d = uiState.dialogo) {
        null -> Unit

        DialogoPerfil.CrearFamilia -> DialogoCrearFamilia(
            guardando = uiState.guardando,
            error = uiState.errorDialogo,
            onConfirmar = { nombre, parentesco -> viewModel.crearFamilia(nombre, parentesco) },
            onCerrar = cerrar
        )

        DialogoPerfil.MiParentesco -> DialogoParentesco(
            actual = uiState.miParentesco,
            guardando = uiState.guardando,
            error = uiState.errorDialogo,
            onConfirmar = { viewModel.cambiarMiParentesco(it) },
            onCerrar = cerrar
        )

        DialogoPerfil.InvitarAdulto -> DialogoInvitarAdulto(
            guardando = uiState.guardando,
            error = uiState.errorDialogo,
            onConfirmar = { viewModel.invitarAdulto(it) },
            onCerrar = cerrar
        )

        is DialogoPerfil.Menor -> DialogoMenor(
            menor = d.menor,
            guardando = uiState.guardando,
            error = uiState.errorDialogo,
            onConfirmar = { nombre, fecha -> viewModel.guardarMenor(d.menor?.id, nombre, fecha) },
            onCerrar = cerrar
        )

        is DialogoPerfil.BorrarMenor -> DialogoConfirmar(
            titulo = "Eliminar menor",
            mensaje = "¿Seguro que quieres quitar a ${d.menor.nombre} de la familia?",
            textoConfirmar = "Eliminar",
            guardando = uiState.guardando,
            error = uiState.errorDialogo,
            onConfirmar = { viewModel.borrarMenor(d.menor.id) },
            onCerrar = cerrar
        )

        DialogoPerfil.SalirDeFamilia -> DialogoConfirmar(
            titulo = "Salir de la familia",
            mensaje = if (uiState.soyUltimoAdulto) {
                "Eres el único adulto de esta familia. Si sales, se borrarán la familia y sus menores registrados."
            } else {
                "Dejarás de ver y editar a esta familia. Otro adulto podrá volver a invitarte."
            },
            textoConfirmar = "Salir",
            guardando = uiState.guardando,
            error = uiState.errorDialogo,
            onConfirmar = { viewModel.salirDeFamilia() },
            onCerrar = cerrar
        )
    }
}
@Composable
fun SelectorPerfil(seleccion: SeccionPerfil, onSeleccionChange: (SeccionPerfil) -> Unit)
{
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .background(
            color = CremaFondo,
            shape = RoundedCornerShape(30.dp)
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .padding(3.dp)
            .background(
                color = if (seleccion == SeccionPerfil.INFORMACION) {
                    Ambar
                } else {
                    CremaFondo
                },
                shape = RoundedCornerShape(28.dp)
            )
            .clickable { onSeleccionChange(SeccionPerfil.INFORMACION)
            },
            contentAlignment = Alignment.Center
        ) {

            Text( text = "Mi información",
                color = if (seleccion == SeccionPerfil.INFORMACION) { androidx.compose.ui.graphics.Color.White
                } else {
                    AzulMarino.copy(alpha = 0.65f)
                },
                fontWeight = if (seleccion == SeccionPerfil.INFORMACION) { FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                },
                fontSize = 15.sp
            )
        }

        Box( modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .padding(3.dp)
            .background(
                color = if (seleccion == SeccionPerfil.ACTIVIDADES) {
                    Ambar
                } else {
                    CremaFondo
                },
                shape = RoundedCornerShape(28.dp)
            )
            .clickable { onSeleccionChange(SeccionPerfil.ACTIVIDADES)
            },
            contentAlignment = Alignment.Center
        ) {

            Text( text = "Mis actividades",
                color = if (seleccion == SeccionPerfil.ACTIVIDADES) {
                    androidx.compose.ui.graphics.Color.White
                } else {
                    AzulMarino.copy(alpha = 0.65f)
                },
                fontWeight = if (seleccion == SeccionPerfil.ACTIVIDADES) { FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                },
                fontSize = 15.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PerfilScreenPreview() {
    FamiliasQueSumanTheme {  val navController = rememberNavController()
        PerfilScreen(  navController = navController )
    }
}