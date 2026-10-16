package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco

/**
 * Foto de perfil circular. Si la persona no tiene foto (o no carga), muestra sus iniciales;
 * si tampoco hay nombre (invitado), un ícono de persona.
 *
 * @param fotoUrl lo que manda el backend ("/imagenes/abc.jpg") o null.
 * @param onClick si no es null, la foto se puede tocar y muestra un botoncito de cámara.
 * @param cargando muestra un indicador encima (mientras se sube una foto nueva).
 */
@Composable
fun FotoPerfil(
    fotoUrl: String?,
    nombre: String?,
    tamano: Dp,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    cargando: Boolean = false
) {
    val iniciales = remember(nombre) { inicialesDe(nombre) }
    val url = RetrofitInstance.urlCompleta(fotoUrl)

    Box(modifier = modifier.size(tamano)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(AmbarClaro)
                .then(if (onClick != null && !cargando) Modifier.clickable(onClick = onClick) else Modifier),
            contentAlignment = Alignment.Center
        ) {
            if (url == null) {
                SinFoto(iniciales, tamano)
            } else {
                SubcomposeAsyncImage(
                    model = url,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    loading = { SinFoto(iniciales, tamano) },
                    error = { SinFoto(iniciales, tamano) }
                )
            }

            if (cargando) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Blanco,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(tamano * 0.4f)
                    )
                }
            }
        }

        // Botoncito de cámara: indica que se puede cambiar
        if (onClick != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(tamano * 0.34f)
                    .clip(CircleShape)
                    .background(Ambar)
                    .border(2.dp, Blanco, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.PhotoCamera,
                    contentDescription = "Cambiar foto",
                    tint = Blanco,
                    modifier = Modifier.size(tamano * 0.19f)
                )
            }
        }
    }
}

@Composable
private fun SinFoto(iniciales: String, tamano: Dp) {
    if (iniciales.isEmpty()) {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = null,
            tint = AzulMarino,
            modifier = Modifier.size(tamano * 0.55f)
        )
    } else {
        Text(
            text = iniciales,
            color = AzulMarino,
            fontWeight = FontWeight.Bold,
            fontSize = (tamano.value * 0.36f).sp
        )
    }
}


private fun inicialesDe(nombre: String?): String =
    nombre.orEmpty()
        .trim()
        .split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }