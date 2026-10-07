package com.example.familiasquesuman.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.familiasquesuman.ui.theme.*

@Composable
fun SeleccionadorImagenes(
    modifier: Modifier = Modifier,
    listaImagenes: MutableList<String>,
    maxImagenes: Int = 5,
    titulo: String? = null,
    subtitulo: String? = null,
) {
    var mostrarUrlDialog by remember { mutableStateOf(value = false) }
    var urlNuevaImagen by remember { mutableStateOf("") }

    // Launcher para elegir múltiples imágenes de la galería del teléfono
    val galeriaMultipleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
    ) { uris: List<Uri> ->
        uris.forEach { uri ->
            if (listaImagenes.size < maxImagenes) {
                listaImagenes.add(uri.toString())
            }
        }
    }

    // Launcher para elegir una sola imagen (por ejemplo para logos de proyectos/donaciones)
    val galeriaIndividualLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let {
            if (maxImagenes == 1) {
                listaImagenes.clear()
                listaImagenes.add(it.toString())
            } else if (listaImagenes.size < maxImagenes) {
                listaImagenes.add(it.toString())
            }
        }
    }

    val tituloSeccion = titulo ?: if (maxImagenes == 1) "Logo / Imagen principal" else "Galería de fotos"
    val subtituloSeccion = subtitulo ?: if (maxImagenes == 1) "Selecciona 1 foto de la galería" else "Hasta $maxImagenes fotos de la galería"

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "$tituloSeccion (${listaImagenes.size}/$maxImagenes)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                )
                Text(
                    text = subtituloSeccion,
                    fontSize = 11.sp,
                    color = GrisTexto,
                )
            }

            if (listaImagenes.size < maxImagenes) {
                TextButton(
                    onClick = {
                        if (maxImagenes == 1) {
                            galeriaIndividualLauncher.launch("image/*")
                        } else {
                            galeriaMultipleLauncher.launch("image/*")
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = AzulMarino,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (maxImagenes == 1) "Elegir foto" else "Galería",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulMarino,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (listaImagenes.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GrisClaroFondo,
                border = BorderStroke(1.dp, GrisBorde),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (maxImagenes == 1) {
                            galeriaIndividualLauncher.launch("image/*")
                        } else {
                            galeriaMultipleLauncher.launch("image/*")
                        }
                    },
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        tint = AzulMarino,
                        modifier = Modifier.size(28.dp),
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (maxImagenes == 1) "Toca para elegir foto de la galería" else "Toca para elegir fotos de tu teléfono ($maxImagenes máx.)",
                        color = AzulMarino,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                    )
                    Text(
                        text = "o ingresa una URL web externa",
                        color = GrisTexto,
                        fontSize = 10.sp,
                    )
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(listaImagenes.size) { index ->
                    val url = listaImagenes[index]
                    Box(
                        modifier = Modifier
                            .size(if (maxImagenes == 1) 120.dp else 90.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GrisClaroFondo),
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                        IconButton(
                            onClick = { listaImagenes.removeAt(index) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(22.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Eliminar",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp),
                            )
                        }
                    }
                }

                if (listaImagenes.size < maxImagenes) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GrisClaroFondo,
                            border = BorderStroke(1.dp, GrisBorde),
                            modifier = Modifier
                                .size(if (maxImagenes == 1) 120.dp else 90.dp)
                                .clickable {
                                    if (maxImagenes == 1) {
                                        galeriaIndividualLauncher.launch("image/*")
                                    } else {
                                        galeriaMultipleLauncher.launch("image/*")
                                    }
                                },
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = null,
                                    tint = AzulMarino,
                                    modifier = Modifier.size(24.dp),
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "+ Agregar",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AzulMarino,
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(
                onClick = {
                    urlNuevaImagen = ""
                    mostrarUrlDialog = true
                },
            ) {
                Text(
                    text = "Ingresar enlace / URL externa",
                    fontSize = 11.sp,
                    color = GrisTexto,
                )
            }
        }
    }

    if (mostrarUrlDialog) {
        AlertDialog(
            onDismissRequest = { mostrarUrlDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = null,
                    tint = AzulMarino,
                    modifier = Modifier.size(36.dp),
                )
            },
            title = {
                Text(
                    text = "Agregar URL de Imagen",
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                    fontSize = 18.sp,
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Selecciona una muestra rápida o pega la URL de la imagen:",
                        fontSize = 12.sp,
                        color = GrisTexto,
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        val muestras = listOf(
                            "https://images.unsplash.com/photo-1593113598332-cd288d649433?q=80&w=600",
                            "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?q=80&w=600",
                            "https://images.unsplash.com/photo-1503676260728-1c00da094a0b?q=80&w=600",
                        )
                        muestras.forEachIndexed { i, url ->
                            FilterChip(
                                selected = urlNuevaImagen == url,
                                onClick = { urlNuevaImagen = url },
                                label = { Text("Foto ${i + 1}", fontSize = 11.sp) },
                                shape = RoundedCornerShape(10.dp),
                            )
                        }
                    }

                    OutlinedTextField(
                        value = urlNuevaImagen,
                        onValueChange = { urlNuevaImagen = it },
                        label = { Text("URL de la imagen (https://...)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (urlNuevaImagen.isNotBlank()) {
                            if (maxImagenes == 1) {
                                listaImagenes.clear()
                                listaImagenes.add(urlNuevaImagen.trim())
                            } else if (listaImagenes.size < maxImagenes) {
                                listaImagenes.add(urlNuevaImagen.trim())
                            }
                            mostrarUrlDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Ambar, contentColor = AzulMarino),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Agregar Foto", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarUrlDialog = false }) {
                    Text("Cancelar", color = GrisTexto)
                }
            },
        )
    }
}