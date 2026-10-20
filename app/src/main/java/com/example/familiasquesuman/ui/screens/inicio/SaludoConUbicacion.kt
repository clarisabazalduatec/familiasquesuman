package com.example.familiasquesuman.ui.screens.inicio

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.CiudadDto
import com.example.familiasquesuman.data.repository.FamiliaRepository
import com.example.familiasquesuman.ui.components.FotoPerfil
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.GrisBorde
import com.example.familiasquesuman.ui.theme.GrisTexto
import kotlinx.coroutines.launch

private fun etiquetaCiudad(c: CiudadDto): String =
    if (!c.estado.isNullOrBlank()) "${c.nombre}, ${c.estado}" else c.nombre

private val ciudadesDefault = listOf(
    CiudadDto(id = 1, nombre = "Monterrey", estado = "Nuevo León"),
    CiudadDto(id = 2, nombre = "Hermosillo", estado = "Sonora")
)

@Composable
fun SaludoConUbicacion(
    nombreUsuario: String,
    ciudad: String,
    modifier: Modifier = Modifier,
    onClickFoto: (() -> Unit)? = null, // solo en Perfil: tocar la foto permite cambiarla
    subiendoFoto: Boolean = false,
    onCiudadCambiada: ((CiudadDto) -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repo = remember { FamiliaRepository() }

    var listaCiudades by remember { mutableStateOf<List<CiudadDto>>(ciudadesDefault) }
    var menuExpandido by remember { mutableStateOf(false) }
    var guardandoCiudad by remember { mutableStateOf(false) }
    var ciudadSeleccionadaManual by remember { mutableStateOf<CiudadDto?>(null) }

    LaunchedEffect(Unit) {
        try {
            val lista = RetrofitInstance.api.listarCiudades()
            if (lista.isNotEmpty()) {
                listaCiudades = lista
            }
        } catch (_: Exception) {}
    }

    val usuarioCiudad = SesionManager.usuario?.ciudad
    val ciudadActual = ciudadSeleccionadaManual
        ?: listaCiudades.find { it.id == usuarioCiudad?.id }
        ?: listaCiudades.find { usuarioCiudad != null && (it.nombre.equals(usuarioCiudad.nombre, ignoreCase = true) || etiquetaCiudad(it).contains(usuarioCiudad.nombre, ignoreCase = true)) }
        ?: listaCiudades.find { it.nombre.contains(ciudad, ignoreCase = true) || etiquetaCiudad(it).contains(ciudad, ignoreCase = true) }
        ?: usuarioCiudad
        ?: listaCiudades.firstOrNull()

    // Sincronizar con SesionManager cuando cambia el usuario desde backend
    LaunchedEffect(usuarioCiudad) {
        if (usuarioCiudad != null) {
            ciudadSeleccionadaManual = usuarioCiudad
        }
    }

    val textoUbicacion = ciudadActual?.let { etiquetaCiudad(it) }
        ?: ciudad.ifBlank { "Seleccionar ciudad" }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        // foto de perfil (la de la sesión; sin foto muestra las iniciales)
        FotoPerfil(
            fotoUrl = SesionManager.usuario?.fotoUrl,
            nombre = SesionManager.usuario?.nombre,
            tamano = 68.dp,
            onClick = onClickFoto,
            cargando = subiendoFoto
        )
        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = "¡Hola, $nombreUsuario!",
                style = MaterialTheme.typography.titleLarge,
                color = AzulMarino
            )

            // ubicacion
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(enabled = !guardandoCiudad) { menuExpandido = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = GrisTexto,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = textoUbicacion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrisTexto
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    if (guardandoCiudad) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = AzulMarino
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Cambiar ciudad",
                            tint = AzulMarino,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // menu de ciudades
                DropdownMenu(
                    expanded = menuExpandido,
                    onDismissRequest = { menuExpandido = false },
                    offset = DpOffset(x = 0.dp, y = 6.dp),
                    containerColor = Blanco,
                    tonalElevation = 0.dp,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .width(215.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(width = 1.dp, color = GrisBorde, shape = RoundedCornerShape(14.dp))
                ) {
                    listaCiudades.forEach { ciudadOpcion ->
                        val seleccionada = ciudadActual?.id == ciudadOpcion.id
                        val etiqueta = etiquetaCiudad(ciudadOpcion)
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = etiqueta,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (seleccionada) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (seleccionada) AzulMarino else GrisTexto,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (seleccionada) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Seleccionada",
                                            tint = AzulMarino,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                menuExpandido = false
                                if (!seleccionada) {
                                    val ciudadAnterior = ciudadActual
                                    ciudadSeleccionadaManual = ciudadOpcion
                                    if (SesionManager.estaAutenticado) {
                                        guardandoCiudad = true
                                        scope.launch {
                                            repo.cambiarMiCiudad(ciudadOpcion.id)
                                                .onSuccess {
                                                    guardandoCiudad = false
                                                    Toast.makeText(context, "Ciudad actualizada", Toast.LENGTH_SHORT).show()
                                                    onCiudadCambiada?.invoke(ciudadOpcion)
                                                }
                                                .onFailure { e ->
                                                    guardandoCiudad = false
                                                    ciudadSeleccionadaManual = ciudadAnterior
                                                    Toast.makeText(
                                                        context,
                                                        e.message ?: "Error al cambiar de ciudad",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                        }
                                    } else {
                                        onCiudadCambiada?.invoke(ciudadOpcion)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
