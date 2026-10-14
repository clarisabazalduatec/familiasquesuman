package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.FormularioFamiliaCard
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

enum class SeccionPerfil { INFORMACION,  ACTIVIDADES}
@Composable
fun PerfilScreen(
    navController: NavHostController,
    viewModel: PerfilViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var seccionSeleccionada by remember { mutableStateOf(SeccionPerfil.INFORMACION) }

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
                item {
                    when {
                        uiState.enModoEdicion -> {
                            FormularioFamiliaCard(
                                uiState = uiState,
                                listaAdultos = viewModel.listaAdultos,
                                listaMenores = viewModel.listaMenores,
                                onAgregarAdulto = { viewModel.agregarAdulto() },
                                onEliminarAdulto = { adulto -> viewModel.eliminarAdulto(adulto) },
                                onActualizarAdulto = { adulto -> viewModel.actualizarAdulto(adulto) },
                                onAgregarMenor = { viewModel.agregarMenor() },
                                onEliminarMenor = { menor -> viewModel.eliminarMenor(menor) },
                                onActualizarMenor = { menor -> viewModel.actualizarMenor(menor) },
                                onCancelar = { viewModel.cancelarEdicion() },
                                onGuardar = { viewModel.guardarPerfil() }
                            )
                        }
                        uiState.esPerfilRegistrado -> {
                            DatosFamiliaRegistradaCard(
                                uiState = uiState,
                                listaAdultos = viewModel.listaAdultos,
                                listaMenores = viewModel.listaMenores,
                                onEditarClick = { viewModel.abrirEdicion() }
                            )
                        }
                        else -> {
                            DatosFamiliaVacioCard(
                                onRegistrarClick = { viewModel.abrirEdicion() }
                            )
                        }
                    }
                }
                item {MisInsigniasCard()}
            }
            if (seccionSeleccionada == SeccionPerfil.ACTIVIDADES) {
                item {  MisActividadesPerfil() }
            }
            }
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