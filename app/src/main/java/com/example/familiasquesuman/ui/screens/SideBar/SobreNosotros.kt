package com.example.familiasquesuman.ui.screens.informacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.R
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.GrisTexto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SobreNosotrosScreen(
    navController: NavHostController
) {

    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = CremaFondo,

        topBar = {

            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = CremaFondo,
                    navigationIconContentColor = AzulMarino,
                    actionIconContentColor = AzulMarino
                ),

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Regresar",
                            tint = AzulMarino
                        )
                    }
                },

                title = {

                    Image(
                        painter = painterResource(
                            id = R.drawable.logo2_onb
                        ),
                        contentDescription = "Familias que Suman",
                        modifier = Modifier
                            .width(220.dp)
                            .height(54.dp),
                        contentScale = ContentScale.Fit
                    )
                },

                actions = {

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
            )
        }
    ) { paddingInterno ->

        Column(
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .background(CremaFondo)
                .verticalScroll(scrollState)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 12.dp,
                    bottom = 30.dp
                )
        ) {

            Text(
                text = "Todo empezó en familia",
                style = MaterialTheme.typography.headlineMedium,
                color = AzulMarino
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Image(
                painter = painterResource(
                    id = R.drawable.fam_onb
                ),
                contentDescription = "Familia",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(
                        RoundedCornerShape(18.dp)
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            ParrafoSobreNosotros(
                texto =
                    "Familias que Suman nació de algo muy sencillo: las ganas de ayudar en familia."
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            ParrafoSobreNosotros(
                texto =
                    "Nos dimos cuenta de que muchas personas quieren involucrarse, pero no siempre saben cómo hacerlo, dónde ayudar o cómo encontrar causas reales y confiables."
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            ParrafoSobreNosotros(
                texto =
                    "Todo comenzó desde casa, con una intención muy personal: enseñarles a nuestros hijos con el ejemplo. Queríamos que crecieran entendiendo el valor de ayudar, de ser empáticos y de descubrir que pequeñas acciones sí pueden generar una gran diferencia."
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            ParrafoSobreNosotros(
                texto =
                    "Con el tiempo entendimos algo muy poderoso: hay muchísimas familias con ganas reales de sumar. Cada una desde sus posibilidades, talentos, tiempo, ideas o profesión."
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            ParrafoSobreNosotros(
                texto =
                    "Así nació esta plataforma: un espacio que conecta familias con campañas, actividades y centros verificados de una manera cercana, fácil y humana."
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            ParrafoSobreNosotros(
                texto =
                    "Porque cuando una familia ayuda, muchas más se inspiran."
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            ParrafoSobreNosotros(
                texto =
                    "Hoy conectamos familias de Monterrey con oportunidades reales para ayudar, con el sueño de seguir creciendo y construir una comunidad de familias que suman en muchas más ciudades."
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = AmbarClaro
            ) {

                Row(
                    modifier = Modifier.padding(
                        horizontal = 18.dp,
                        vertical = 18.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.FormatQuote,
                        contentDescription = null,
                        tint = Ambar,
                        modifier = Modifier.size(38.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Text(
                        text =
                            "Cuando una familia ayuda,\nmuchas más se inspiran.",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                        color = AzulMarino
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(
                onClick = {
                    navController.navigate(
                        Rutas.Actividades.ruta
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),

                shape = RoundedCornerShape(16.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulMarino,
                    contentColor = Blanco
                )
            ) {

                Text(
                    text = "Quiero ser parte",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun ParrafoSobreNosotros(
    texto: String
) {

    Text(
        text = texto,
        style = MaterialTheme.typography.bodyMedium,
        color = AzulMarino
    )
}