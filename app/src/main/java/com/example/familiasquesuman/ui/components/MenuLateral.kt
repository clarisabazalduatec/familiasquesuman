package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.GrisBorde


@Composable
fun MenuLateral(
    onInicioClick: () -> Unit,
    onIniciarSesionClick: () -> Unit,
    onCrearCuentaClick: () -> Unit,
    onComunidadClick: () -> Unit,
    onActividadesClick: () -> Unit = {},
    onProyectosClick: () -> Unit = {},
    onDonarClick: () -> Unit = {},
    onDirectorioClick: () -> Unit = {},
    onContactoClick: () -> Unit = {}
) {

    ModalDrawerSheet(
        modifier = Modifier
            .width(330.dp)
            .fillMaxHeight(),
        drawerContainerColor = CremaFondo,
        drawerContentColor = AzulMarino,
        drawerShape = RoundedCornerShape(
            topEnd = 28.dp,
            bottomEnd = 28.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    horizontal = 18.dp,
                    vertical = 22.dp
                )
        ) {

            //logo
            Image(
                painter = painterResource(
                    id = R.drawable.logo2_onb
                ),
                contentDescription = "Familias que Suman",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                contentScale = ContentScale.Fit
            )
            HorizontalDivider(
                color = GrisBorde,
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                )
            )


            //nav principal
            ItemMenuLateral(
                texto = "Inicio",
                icono = Icons.Outlined.Home,
                selected = true,
                onClick = onInicioClick
            )
            ItemMenuLateral(
                texto = "Actividades en Familia",
                icono = Icons.Outlined.CalendarMonth,
                onClick = onActividadesClick
            )
            ItemMenuLateral(
                texto = "Proyectos",
                icono = Icons.Outlined.Lightbulb,
                onClick = onProyectosClick
            )
            ItemMenuLateral(
                texto = "Donar",
                icono = Icons.Outlined.FavoriteBorder,
                onClick = onDonarClick
            )
            ItemMenuLateral(
                texto = "Comunidad",
                icono = Icons.Outlined.Groups,
                onClick = onComunidadClick
            )
            ItemMenuLateral(
                texto = "Directorio de Visiteo",
                icono = Icons.Outlined.Place,
                onClick = onDirectorioClick
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                color = GrisBorde,
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                )
            )

            //cuenta
            ItemMenuLateral(
                texto = "Iniciar sesión",
                icono = Icons.Outlined.Login,
                onClick = onIniciarSesionClick
            )
            ItemMenuLateral(
                texto = "Crear cuenta",
                icono = Icons.Outlined.PersonAdd,
                onClick = onCrearCuentaClick
            )
            ItemMenuLateral(
                texto = "Contacto y ayuda",
                icono = Icons.Outlined.HelpOutline,
                onClick = onContactoClick
            )
            Spacer(modifier = Modifier.weight(1f))

            //footer
            HorizontalDivider(
                color = GrisBorde,
                modifier = Modifier.padding(
                    horizontal = 10.dp
                )
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Aviso de privacidad",
                    style = MaterialTheme.typography.labelSmall,
                    color = AzulMarino.copy(alpha = 0.65f)
                )
                Text(
                    text = "   |   ",
                    color = AzulMarino.copy(alpha = 0.35f)
                )
                Text(
                    text = "Términos",
                    style = MaterialTheme.typography.labelSmall,
                    color = AzulMarino.copy(alpha = 0.65f)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(55.dp)
                        .height(1.dp)
                        .background(GrisBorde)
                )
                Text(
                    text = "♡",
                    fontSize = 25.sp,
                    color = Ambar,
                    modifier = Modifier.padding(
                        horizontal = 14.dp
                    )
                )
                Box(
                    modifier = Modifier
                        .width(55.dp)
                        .height(1.dp)
                        .background(GrisBorde)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
private fun ItemMenuLateral(
    texto: String,
    icono: ImageVector,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight =
                    if (selected) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    }
            )
        },
        icon = {
            Icon(
                imageVector = icono,
                contentDescription = null,
                modifier = Modifier.size(26.dp)
            )
        },
        selected = selected,
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = AmbarClaro,
            selectedIconColor = AzulMarino,
            selectedTextColor = AzulMarino,
            unselectedContainerColor = Color.Transparent,
            unselectedIconColor = AzulMarino,
            unselectedTextColor = AzulMarino
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    )
}