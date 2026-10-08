package com.example.familiasquesuman.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familiasquesuman.R
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.GrisBorde
import com.example.familiasquesuman.ui.theme.GrisClaroFondo
import com.example.familiasquesuman.ui.theme.GrisTexto
import kotlinx.coroutines.launch

private val AmarilloSeleccion = Color(0xFFF5D17C)
private val CremaIconoDefault = Color(0xFFFBF5E4)
private val CremaIconoSeleccionado = Color(0xFFFCF6E9)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onComenzar: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CremaFondo
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(
                    horizontal = 22.dp,
                    vertical = 12.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            // logo
            Image(
                painter = painterResource(id = R.drawable.logo_onb),
                contentDescription = "Logo Familias que Suman",
                modifier = Modifier
                    .fillMaxWidth(0.62f)
                    .height(105.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(12.dp))

            // paginas
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> PrimeraPagina()
                    1 -> SegundaPagina()
                    2 -> TerceraPagina()
                }
            }

            // indicadores de pag actual
            IndicadoresPagina(
                paginaActual = pagerState.currentPage,
                totalPaginas = 3
            )
            Spacer(modifier = Modifier.height(18.dp))
            // botones
            if (pagerState.currentPage == 0) {
                Button(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Ambar,
                        contentColor = AzulMarino
                    ),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Siguiente",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = null
                    )
                }

            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    pagerState.currentPage - 1
                                )
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AzulMarino
                        ),
                        border = BorderStroke(
                            1.dp,
                            AzulMarino
                        ),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Atrás",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Button(
                        onClick = {
                            if (pagerState.currentPage == 2) {
                                onComenzar()
                            } else {
                                scope.launch {
                                    pagerState.animateScrollToPage(
                                        pagerState.currentPage + 1
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Ambar,
                            contentColor = AzulMarino
                        ),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .weight(1.25f)
                            .height(56.dp)
                    ) {
                        Text(
                            text =
                                if (pagerState.currentPage == 2)
                                    "Comenzar"
                                else
                                    "Siguiente",
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (pagerState.currentPage != 2) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}


//pantalla 1
@Composable
private fun PrimeraPagina() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(5.dp))
        Image(
            painter = painterResource(
                id = R.drawable.fam_onb
            ),
            contentDescription = "Ilustración de familia",
            modifier = Modifier
                .fillMaxWidth(0.98f)
                .height(310.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Juntas y juntos\nhacemos más.",
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = AzulMarino,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(14.dp))
        LineaDecorativa()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text =
                "Conecta con actividades, proyectos y\n" +
                        "causas para transformar tu comunidad\n" +
                        "en familia.",
            style = MaterialTheme.typography.bodyLarge,
            color = GrisTexto,
            textAlign = TextAlign.Center
        )
    }
}

//pantalla2
@Composable
private fun SegundaPagina() {
    var opcionSeleccionada by remember {
        mutableStateOf<String?>(null)
    }
    val descripcionSuperior = when (opcionSeleccionada) {
        "actividades" ->
            "Encuentra actividades para convivir,\nayudar y participar en familia."
        "donar" ->
            "Apoya causas de tu comunidad con\ndonaciones en especie o tiempo."
        "proyectos" ->
            "Descubre proyectos con causas claras\ny objetivos específicos."
        "directorio" ->
            "Encuentra centros y espacios cercanos\npara visitar y apoyar en familia."
        else ->
            "Toca una opción para conocer\ncómo puedes ayudar."
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Encuentra cómo ayudar.",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 34.sp,
            color = AzulMarino,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))
        LineaDecorativa()
        Spacer(modifier = Modifier.height(12.dp))

        // descripcion
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = descripcionSuperior,
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // fila 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            OpcionOnboardingCard(
                iconRes = R.drawable.actividad_onb,
                titulo = "Actividades\nen familia",
                descripcion =
                    "Actividades en familia\n" +
                            "para ayudar durante\n" +
                            "el año.",
                selected =
                    opcionSeleccionada == "actividades",
                onClick = {
                    opcionSeleccionada = "actividades"
                },
                modifier = Modifier.weight(1f)
            )

            OpcionOnboardingCard(
                iconRes = R.drawable.donar_onb,
                titulo = "Quiero donar",
                descripcion =
                    "Apoya en especie\n" +
                            "o tiempo.",
                selected =
                    opcionSeleccionada == "donar",
                onClick = {
                    opcionSeleccionada = "donar"
                },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(14.dp))

        // fila 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            OpcionOnboardingCard(
                iconRes = R.drawable.proyectos_onb,
                titulo = "Proyectos",
                descripcion =
                    "Proyectos con causas\n" +
                            "y objetivos específicos.",
                selected =
                    opcionSeleccionada == "proyectos",
                onClick = {
                    opcionSeleccionada = "proyectos"
                },
                modifier = Modifier.weight(1f)
            )

            OpcionOnboardingCard(
                iconRes = R.drawable.directorio_onb,
                titulo = "Directorio\nde visiteo",
                descripcion =
                    "Centros y espacios\n" +
                            "para visitar y apoyar\n" +
                            "en familia.",
                selected =
                    opcionSeleccionada == "directorio",
                onClick = {
                    opcionSeleccionada = "directorio"
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}


//pantalla2
@Composable
private fun OpcionOnboardingCard(
    iconRes: Int,
    titulo: String,
    descripcion: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .height(215.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (selected) {
                    AmarilloSeleccion
                } else {
                    Blanco
                }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 14.dp,
                    vertical = 12.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(78.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected) {
                            CremaIconoSeleccionado
                        } else {
                            CremaIconoDefault
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = titulo,
                    modifier = Modifier
                        .size(68.dp)
                        .graphicsLayer(
                            scaleX = 1.25f,
                            scaleY = 1.25f
                        ),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp,
                lineHeight = 19.sp,
                color = AzulMarino,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // descrpcion y flecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color =
                        if (selected) {
                            AzulMarino
                        } else {
                            GrisTexto
                        },
                    lineHeight = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = AzulMarino,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

//pantalla 3
@Composable
private fun TerceraPagina() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Todo claro,\nreal y cercano.",
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = AzulMarino,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        LineaDecorativa()
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text =
                "Recibe notificaciones, consulta evidencia\n" +
                        "del impacto y mantente al tanto de lo\n" +
                        "que necesita tu comunidad.",
            style = MaterialTheme.typography.bodyLarge,
            color = GrisTexto,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(18.dp))
        CardInfoActualizacion()
        Spacer(modifier = Modifier.height(12.dp))
        CardTuImpacto()
        Spacer(modifier = Modifier.height(12.dp))
        CardComunidadAccion()
    }
}

//card --> actualizacion
@Composable
private fun CardInfoActualizacion() {

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AmbarClaro),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.Campaign,
                    contentDescription = null,
                    tint = AzulMarino
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Actualización oficial",
                    style =
                        MaterialTheme.typography.titleSmall,
                    color = AzulMarino
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text =
                        "Nueva campaña de reforestación\n" +
                                "este sábado en Parque Central.",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color = GrisTexto
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Ver más detalles  ›",
                    style =
                        MaterialTheme.typography.titleSmall,
                    color = AzulMarino
                )
            }

            // Placeholder de fotografía
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(60.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
                    .background(AmbarClaro),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Foto",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color = AzulMarino
                )
            }
        }
    }
}


//card tu impacto
@Composable
private fun CardTuImpacto() {

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AmbarClaro),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = Ambar
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Tu impacto",
                    style =
                        MaterialTheme.typography.titleSmall,
                    color = AzulMarino
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text =
                        "Has apoyado 3 campañas\n" +
                                "y 2 proyectos este año.",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color = GrisTexto
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    LinearProgressIndicator(
                        progress = 0.70f,
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(
                                RoundedCornerShape(10.dp)
                            ),
                        color = Ambar,
                        trackColor = GrisClaroFondo
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "70%",
                        style =
                            MaterialTheme.typography.titleSmall,
                        color = AzulMarino
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "¡Vas por buen camino!",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color = GrisTexto
                )
            }
        }
    }
}

//card --> comunidad
@Composable
private fun CardComunidadAccion() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AmbarClaro),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.Groups,
                    contentDescription = null,
                    tint = AzulMarino
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Comunidad en acción",
                    style =
                        MaterialTheme.typography.titleSmall,
                    color = AzulMarino
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text =
                        "Más de 250 familias ya participan\n" +
                                "en actividades este mes.",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color = GrisTexto
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    AvatarTexto("A")
                    AvatarTexto("L")
                    AvatarTexto("M")
                    AvatarTexto("S")

                    Box(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(GrisClaroFondo),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "+245",
                            style =
                                MaterialTheme.typography.labelSmall,
                            color = AzulMarino
                        )
                    }
                }
            }
        }
    }
}


//avatar temporal
@Composable
private fun AvatarTexto(
    letra: String
) {

    Box(
        modifier = Modifier
            .padding(end = 6.dp)
            .size(30.dp)
            .clip(CircleShape)
            .background(AmbarClaro),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = letra,
            style =
                MaterialTheme.typography.labelSmall,
            color = AzulMarino
        )
    }
}


//linea amarilla
@Composable
private fun LineaDecorativa() {

    Box(
        modifier = Modifier
            .width(34.dp)
            .height(4.dp)
            .clip(
                RoundedCornerShape(50)
            )
            .background(Ambar)
    )
}


//indicadores
@Composable
private fun IndicadoresPagina(
    paginaActual: Int,
    totalPaginas: Int
) {

    Row(
        horizontalArrangement =
            Arrangement.Center,
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        repeat(totalPaginas) { index ->

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == paginaActual)
                            AzulMarino
                        else
                            GrisBorde
                    )
            )
        }
    }
}