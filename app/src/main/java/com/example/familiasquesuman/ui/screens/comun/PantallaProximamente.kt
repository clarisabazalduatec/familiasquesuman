package com.example.familiasquesuman.ui.screens.comun

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.ui.components.BarraNavegacionInferior
import com.example.familiasquesuman.ui.components.MenuLateral
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.actividad.BannerSincronizarCalendario
import com.example.familiasquesuman.ui.screens.actividad.HeaderDiaActividades
import com.example.familiasquesuman.ui.screens.actividad.LeyendaFiltros
import com.example.familiasquesuman.ui.screens.actividad.Separador
import com.example.familiasquesuman.ui.screens.actividad.TarjetaActividadDia
import com.example.familiasquesuman.ui.theme.AzulMarinoOscuro
import com.example.familiasquesuman.ui.theme.GrisClaroFondo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaProximamente(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.ACTIVIDADES,
        acciones = {
            IconButton(onClick = { navController.navigate(Rutas.Notificaciones.ruta) }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
            }
        }
    ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item{Text("Proximamente :)")}
            }
        }
    }
