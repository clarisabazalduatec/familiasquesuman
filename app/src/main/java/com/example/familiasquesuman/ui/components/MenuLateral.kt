package com.example.familiasquesuman.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddModerator
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.ui.model.TipoSesion
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.GrisBorde
import com.example.familiasquesuman.ui.theme.GrisTexto


@Composable
fun MenuLateral(
    // ESTADO DE SESIÓN
    tipoSesion: TipoSesion,
    onTipoSesionChange: (TipoSesion) -> Unit,
    nombreUsuario: String = "María Gzz",
    @DrawableRes
    fotoPerfilRes: Int? = null,
    // CALLBACKS EXISTENTES
    onInicioClick: () -> Unit,
    onIniciarSesionClick: () -> Unit,
    onCrearCuentaClick: () -> Unit,
    onComunidadClick: () -> Unit,
    onActividadesClick: () -> Unit = {},
    onProyectosClick: () -> Unit = {},
    onDonarClick: () -> Unit = {},
    onDirectorioClick: () -> Unit = {},
    onChatbotClick: () -> Unit,
    onSobreNosotrosClick: () -> Unit = {},
    onContactoClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onAdminClick: () -> Unit = {},
    onCerrarSesionClick: () -> Unit = {},
    // Cierra el drawer con la X
    onBackClick: () -> Unit
) {

    val sesionIniciada = tipoSesion == TipoSesion.USUARIO || tipoSesion == TipoSesion.ADMIN
    val esAdmin = tipoSesion == TipoSesion.ADMIN

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
                    vertical = 18.dp
                )
        ) {
            // HEADER
            if (sesionIniciada) {
                HeaderUsuario(
                    nombreUsuario = nombreUsuario,
                    fotoPerfilRes = fotoPerfilRes,
                    tipoSesion = tipoSesion,
                    onTipoSesionChange = { onTipoSesionChange(it) },
                    onPerfilClick = onPerfilClick,
                    onCerrarDrawer = onBackClick
                )
            } else {
                HeaderUsuario(
                    nombreUsuario = "Bienvenido",
                    fotoPerfilRes = fotoPerfilRes,
                    tipoSesion = TipoSesion.USUARIO,
                    onTipoSesionChange = { onTipoSesionChange(it) },
                    onPerfilClick = {},
                    onCerrarDrawer = onBackClick
                )
            }


            HorizontalDivider(
                color = GrisBorde,

                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 10.dp
                )
            )


            // =====================================================
            // CONTENIDO SCROLLEABLE
            // =====================================================

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {

                // =================================================
                // NAVEGACIÓN PRINCIPAL
                // =================================================

                ItemMenuLateral(
                    texto = "Inicio",
                    icono = Icons.Outlined.Home,
                    onClick = onInicioClick
                )

                ItemMenuLateral(
                    texto = "Actividades en familia",
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
                    texto = "Directorio de Visitas",
                    icono = Icons.Outlined.Place,
                    onClick = onDirectorioClick
                )


                HorizontalDivider(
                    color = GrisBorde,

                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 10.dp
                    )
                )


                // =================================================
                // INFORMACIÓN / AYUDA
                // =================================================

                ItemMenuLateral(
                    texto = "Chatbot",
                    icono = Icons.Outlined.ChatBubble,
                    onClick = onChatbotClick
                )

                ItemMenuLateral(
                    texto = "Sobre nosotros",
                    icono = Icons.Outlined.Groups,
                    onClick = onSobreNosotrosClick
                )

                ItemMenuLateral(
                    texto = "Contacto",
                    icono = Icons.Outlined.Email,
                    onClick = onContactoClick
                )


                HorizontalDivider(
                    color = GrisBorde,

                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 10.dp
                    )
                )


                // =================================================
                // ADMIN
                // =================================================

                if (esAdmin) {

                    ItemMenuLateral(
                        texto = "Admin",
                        icono = Icons.Outlined.AddModerator,
                        onClick = onAdminClick
                    )
                }



                // =================================================
                // PERFIL
                // =================================================

                if (sesionIniciada) {

                    ItemMenuLateral(
                        texto = "Mi perfil",
                        icono = Icons.Outlined.Person,
                        onClick = onPerfilClick
                    )
                }
            }


            // =====================================================
            // PARTE INFERIOR
            // =====================================================

            if (!sesionIniciada) {

                // ===============================================
                // INVITADO
                // ===============================================

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    onClick = onIniciarSesionClick,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),

                    shape = RoundedCornerShape(14.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulMarino,
                        contentColor = Blanco
                    )
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Login,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Text(
                        text = "Iniciar sesión",
                        style = MaterialTheme.typography.titleSmall
                    )
                }


                Spacer(
                    modifier = Modifier.height(10.dp)
                )


                OutlinedButton(
                    onClick = onCrearCuentaClick,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),

                    shape = RoundedCornerShape(14.dp),

                    border = BorderStroke(
                        width = 1.dp,
                        color = GrisBorde
                    )
                ) {

                    Icon(
                        imageVector = Icons.Outlined.PersonAdd,
                        contentDescription = null,
                        tint = AzulMarino,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Text(
                        text = "Crear cuenta",
                        style = MaterialTheme.typography.titleSmall,
                        color = AzulMarino
                    )
                }

            } else {

                // ===============================================
                // USUARIO O ADMIN
                // ===============================================

                HorizontalDivider(
                    color = GrisBorde,

                    modifier = Modifier.padding(
                        bottom = 12.dp
                    )
                )


                OutlinedButton(
                    onClick = onCerrarSesionClick,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),

                    shape = RoundedCornerShape(14.dp),

                    border = BorderStroke(
                        width = 1.dp,
                        color = GrisBorde
                    )
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        contentDescription = null,
                        tint = AzulMarino,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Text(
                        text = "Cerrar sesión",
                        style = MaterialTheme.typography.titleSmall,
                        color = AzulMarino
                    )
                }
            }
        }
    }
}


// =============================================================
// HEADER INVITADO
// =============================================================

@Composable
private fun HeaderInvitado(
    onCerrarDrawer: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 6.dp,
                    end = 42.dp,
                    top = 8.dp,
                    bottom = 8.dp
                ),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        AzulMarino.copy(
                            alpha = 0.08f
                        )
                    ),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = AzulMarino,
                    modifier = Modifier.size(38.dp)
                )
            }


            Spacer(
                modifier = Modifier.width(14.dp)
            )


            Column {

                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.titleMedium,
                    color = AzulMarino,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Explora sin iniciar sesión",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )
            }
        }


        IconButton(
            onClick = onCerrarDrawer,

            modifier = Modifier.align(
                Alignment.TopEnd
            )
        ) {

            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Cerrar menú",
                tint = AzulMarino,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}


// =============================================================
// HEADER USUARIO / ADMIN
// =============================================================

@Composable
private fun HeaderUsuario(

    nombreUsuario: String,

    @DrawableRes
    fotoPerfilRes: Int?,

    tipoSesion: TipoSesion,

    onTipoSesionChange: (TipoSesion) -> Unit,

    onPerfilClick: () -> Unit,

    onCerrarDrawer: () -> Unit
) {

    var mostrarSelector by remember {
        mutableStateOf(false)
    }


    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 6.dp,
                    end = 34.dp,
                    top = 10.dp,
                    bottom = 10.dp
                ),

            verticalAlignment = Alignment.CenterVertically
        ) {

            AvatarUsuario(
                fotoPerfilRes = fotoPerfilRes
            )


            Spacer(
                modifier = Modifier.width(12.dp)
            )


            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onPerfilClick()
                    }
            ) {

                Text(
                    text = nombreUsuario,
                    style = MaterialTheme.typography.titleMedium,
                    color = AzulMarino,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Ver perfil",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )
            }


            // =================================================
            // FLECHA SELECTOR DE SESIÓN DEMO
            // =================================================

            Box {

                IconButton(
                    onClick = {
                        mostrarSelector = true
                    }
                ) {

                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = "Cambiar vista de sesión",
                        tint = AzulMarino,
                        modifier = Modifier.size(25.dp)
                    )
                }


                DropdownMenu(
                    expanded = mostrarSelector,

                    onDismissRequest = {
                        mostrarSelector = false
                    }
                ) {

                    OpcionTipoSesion(
                        texto = "Sin iniciar sesión",

                        seleccionado =
                            tipoSesion == TipoSesion.INVITADO,

                        onClick = {

                            onTipoSesionChange(
                                TipoSesion.INVITADO
                            )

                            mostrarSelector = false
                        }
                    )


                    OpcionTipoSesion(
                        texto = "Usuario",

                        seleccionado =
                            tipoSesion == TipoSesion.USUARIO,

                        onClick = {

                            onTipoSesionChange(
                                TipoSesion.USUARIO
                            )

                            mostrarSelector = false
                        }
                    )


                    OpcionTipoSesion(
                        texto = "Administrador",

                        seleccionado =
                            tipoSesion == TipoSesion.ADMIN,
                        onClick = {
                            onTipoSesionChange(
                                TipoSesion.ADMIN
                            )
                            mostrarSelector = false
                        }
                    )
                }
            }
        }


        // =====================================================
        // X CERRAR DRAWER
        // =====================================================
        IconButton(
            onClick = onCerrarDrawer,
            modifier = Modifier.align(
                Alignment.TopEnd
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Cerrar menú",
                tint = AzulMarino,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


// =============================================================
// AVATAR
// =============================================================

@Composable
private fun AvatarUsuario(
    @DrawableRes
    fotoPerfilRes: Int?
) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(CircleShape)
            .background(AmbarClaro),
        contentAlignment = Alignment.Center
    ) {
        if (fotoPerfilRes != null) {
            Image(
                painter = painterResource(
                    id = fotoPerfilRes
                ),
                contentDescription = "Foto de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

        } else {

            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = AzulMarino,
                modifier = Modifier.size(34.dp)
            )
        }
    }
}


// =============================================================
// OPCIÓN SELECTOR DEMO
// =============================================================

@Composable
private fun OpcionTipoSesion(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyMedium,
                color = AzulMarino
            )
        },
        leadingIcon = {
            RadioButton(
                selected = seleccionado,
                onClick = null
            )
        },
        onClick = onClick
    )
}

// ITEM DEL SIDEBAR
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

            unselectedContainerColor =
                Color.Transparent,

            unselectedIconColor =
                AzulMarino,

            unselectedTextColor =
                AzulMarino
        ),

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 1.dp
            )
    )
}