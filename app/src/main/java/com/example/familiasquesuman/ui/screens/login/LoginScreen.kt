package com.example.familiasquesuman.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.R
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import com.example.familiasquesuman.ui.theme.GrisBorde
import com.example.familiasquesuman.ui.theme.GrisTexto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = viewModel()
) {

    // =========================================================
    // MISMO ESTADO QUE YA TENÍAS
    // =========================================================

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var mostrarPassword by remember {
        mutableStateOf(false)
    }

    var recordarme by remember {
        mutableStateOf(false)
    }

    val uiState by loginViewModel.uiState.collectAsState()


    Scaffold(
        containerColor = CremaFondo,

        topBar = {

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CremaFondo,
                    navigationIconContentColor = AzulMarino
                ),

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.navigate(
                                Rutas.Inicio.ruta
                            )
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            modifier = Modifier.size(26.dp)
                        )
                    }
                },

                title = {}
            )
        }

    ) { paddingInterno ->

        Column(
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(horizontal = 28.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            // LOGO
            Image(
                painter = painterResource(
                    id = R.drawable.logo2_onb
                ),
                contentDescription = "Familias que Suman",
                modifier = Modifier
                    .width(255.dp)
                    .height(80.dp),
                contentScale = ContentScale.Fit
            )


// FAMILIA
// Sigue midiendo 300 x 220, solamente la acercamos al logo.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.TopCenter
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.fam_onb
                    ),
                    contentDescription = "Familias que Suman",
                    modifier = Modifier
                        .width(350.dp)
                        .height(270.dp)
                        .offset(y = (-20).dp),
                    contentScale = ContentScale.Fit
                )
            }


            //bienvenida
            Text(
                text = "Bienvenido de nuevo",
                style =
                    MaterialTheme.typography.headlineSmall,
                color = AzulMarino,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))


            Text(
                text =
                    "Inicia sesión para inscribirte, donar y dar\n" +
                            "seguimiento a tu impacto.",

                style =
                    MaterialTheme.typography.bodyMedium,

                color = AzulMarino,

                textAlign = TextAlign.Center,

                lineHeight = 18.sp
            )


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // =================================================
            // CORREO
            // =================================================

            OutlinedTextField(
                value = email,

                onValueChange = {
                    email = it
                },

                placeholder = {
                    Text(
                        text =
                            "Correo electrónico",
                        color = GrisTexto
                    )
                },

                singleLine = true,

                shape =
                    RoundedCornerShape(8.dp),

                colors =
                    OutlinedTextFieldDefaults.colors(

                        focusedBorderColor =
                            Ambar,

                        unfocusedBorderColor =
                            GrisBorde,

                        focusedContainerColor =
                            Blanco,

                        unfocusedContainerColor =
                            Blanco,

                        cursorColor =
                            AzulMarino,

                        focusedTextColor =
                            AzulMarino,

                        unfocusedTextColor =
                            AzulMarino
                    ),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
            )


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            // =================================================
            // CONTRASEÑA
            // =================================================

            OutlinedTextField(
                value = password,

                onValueChange = {
                    password = it
                },

                placeholder = {

                    Text(
                        text = "Contraseña",
                        color = GrisTexto
                    )
                },

                singleLine = true,

                visualTransformation =
                    if (mostrarPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },

                trailingIcon = {

                    IconButton(
                        onClick = {
                            mostrarPassword =
                                !mostrarPassword
                        }
                    ) {

                        Icon(
                            imageVector =
                                if (mostrarPassword) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },

                            contentDescription =
                                if (mostrarPassword) {
                                    "Ocultar contraseña"
                                } else {
                                    "Mostrar contraseña"
                                },

                            tint = GrisTexto
                        )
                    }
                },

                shape =
                    RoundedCornerShape(8.dp),

                colors =
                    OutlinedTextFieldDefaults.colors(

                        focusedBorderColor =
                            Ambar,

                        unfocusedBorderColor =
                            GrisBorde,

                        focusedContainerColor =
                            Blanco,

                        unfocusedContainerColor =
                            Blanco,

                        cursorColor =
                            AzulMarino,

                        focusedTextColor =
                            AzulMarino,

                        unfocusedTextColor =
                            AzulMarino
                    ),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
            )


            // =================================================
            // RECORDARME + OLVIDASTE
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = recordarme,

                        onCheckedChange = {
                            recordarme = it
                        },

                        colors =
                            CheckboxDefaults.colors(

                                checkedColor =
                                    Ambar,

                                uncheckedColor =
                                    GrisTexto,

                                checkmarkColor =
                                    Blanco
                            )
                    )


                    Text(
                        text = "Recordarme",

                        style =
                            MaterialTheme
                                .typography
                                .bodySmall,

                        color =
                            AzulMarino
                    )
                }


                TextButton(
                    onClick = {
                        /* pantalla de recuperar contraseña, luego */
                    },

                    contentPadding =
                        PaddingValues(
                            horizontal = 4.dp
                        )
                ) {

                    Text(
                        text =
                            "¿Olvidaste tu contraseña?",

                        style =
                            MaterialTheme
                                .typography
                                .bodySmall,

                        color =
                            Ambar,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }


            // =================================================
            // ERROR
            // =================================================

            if (
                uiState is LoginUiState.Error
            ) {

                Text(
                    text =
                        (uiState as LoginUiState.Error)
                            .mensaje,

                    color =
                        MaterialTheme.colorScheme.error,

                    style =
                        MaterialTheme.typography.bodySmall,

                    modifier =
                        Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )
            }


            // =================================================
            // INICIAR SESIÓN
            // MISMA LÓGICA DE LOGIN
            // =================================================

            Button(
                onClick = {

                    loginViewModel.iniciarSesion(
                        email,
                        password
                    )
                },

                enabled =
                    uiState !is LoginUiState.Cargando,

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            Ambar,

                        contentColor =
                            Blanco,

                        disabledContainerColor =
                            Ambar.copy(
                                alpha = 0.55f
                            )
                    ),

                shape =
                    RoundedCornerShape(8.dp),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {

                if (
                    uiState is
                            LoginUiState.Cargando
                ) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(20.dp),

                        color =
                            Blanco,

                        strokeWidth =
                            2.dp
                    )

                } else {

                    Text(
                        text =
                            "Iniciar sesión",

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,

                        color =
                            Blanco
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            // =================================================
            // CREAR CUENTA
            // MISMO CALLBACK QUE TENÍAS
            // =================================================

            OutlinedButton(
                onClick = {
                    /* navegar a pantalla de registro, luego */
                },

                shape =
                    RoundedCornerShape(8.dp),

                colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = Ambar
                    ),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .border(
                        width = 1.dp,
                        color = Ambar,
                        shape =
                            RoundedCornerShape(8.dp)
                    )
            ) {

                Text(
                    text = "Crear cuenta",
                    color = Ambar,
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // =================================================
            // DIVISOR
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                HorizontalDivider(
                    modifier =
                        Modifier.weight(1f),

                    color =
                        GrisBorde
                )


                Text(
                    text =
                        "   o continúa con   ",

                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,

                    color =
                        GrisTexto
                )


                HorizontalDivider(
                    modifier =
                        Modifier.weight(1f),

                    color =
                        GrisBorde
                )
            }


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            // =================================================
            // GOOGLE
            // MISMO CALLBACK VACÍO
            // =================================================

            OutlinedButton(
                onClick = {
                    /* login con Google, más adelante */
                },

                shape =
                    RoundedCornerShape(8.dp),

                colors =
                    ButtonDefaults
                        .outlinedButtonColors(
                            containerColor =
                                Blanco,
                            contentColor =
                                AzulMarino
                        ),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {

                // G visual sin necesitar otro drawable
                Text(
                    text = "G",
                    color = Color(0xFF4285F4),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )


                Spacer(
                    modifier =
                        Modifier.width(12.dp)
                )


                Text(
                    text = "Google",
                    color = AzulMarino,
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // =================================================
            // MENSAJE DE SEGURIDAD
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically,

                horizontalArrangement =
                    Arrangement.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.Shield,

                    contentDescription = null,

                    modifier =
                        Modifier.size(30.dp),

                    tint =
                        AzulMarino
                )


                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )


                Text(
                    text =
                        "Tu información está protegida y solo\n" +
                                "se usa para gestionar tu participación.",

                    style =
                        MaterialTheme
                            .typography
                            .labelSmall,

                    color =
                        AzulMarino,

                    lineHeight =
                        15.sp
                )
            }


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )
        }
    }
}


@Preview(
    showBackground = true
)
@Composable
private fun LoginScreenPreview() {

    FamiliasQueSumanTheme {

        LoginScreen(
            navController =
                rememberNavController()
        )
    }
}