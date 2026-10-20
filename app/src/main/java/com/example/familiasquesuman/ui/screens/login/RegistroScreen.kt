package com.example.familiasquesuman.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
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
fun RegistroScreen(
    navController: NavHostController,
    registroViewModel: RegistroViewModel = viewModel()
) {
    val uiState by registroViewModel.uiState.collectAsState()
    var mostrarPassword by remember { mutableStateOf(false) }

    // Cuenta creada = sesión iniciada: ir a Inicio y limpiar el historial.
    LaunchedEffect(uiState.exito) {
        if (uiState.exito) {
            navController.navigate(Rutas.Inicio.ruta) {
                popUpTo(navController.graph.id) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // "Ya tengo cuenta": si vine desde el login, regreso a él; si no, lo abro.
    val irALogin: () -> Unit = {
        val vengoDeLogin = navController.previousBackStackEntry?.destination?.route == Rutas.Login.ruta
        if (vengoDeLogin) {
            navController.popBackStack()
        } else {
            navController.navigate(Rutas.Login.ruta) {
                popUpTo(Rutas.Registro.ruta) { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = CremaFondo,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CremaFondo,
                    navigationIconContentColor = AzulMarino
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        if (!navController.popBackStack()) navController.navigate(Rutas.Inicio.ruta)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo2_onb),
                contentDescription = "Familias que Suman",
                modifier = Modifier
                    .width(220.dp)
                    .height(68.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Crea tu cuenta",
                style = MaterialTheme.typography.headlineSmall,
                color = AzulMarino,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Súmate para inscribir a tu familia en actividades,\n" +
                        "donar y dar seguimiento a tu impacto.",
                style = MaterialTheme.typography.bodyMedium,
                color = AzulMarino,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            CampoRegistro(
                valor = uiState.nombre,
                onValueChange = registroViewModel::onNombre,
                placeholder = "Nombre completo",
                icono = Icons.Outlined.Person,
                error = uiState.errorNombre,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )

            CampoRegistro(
                valor = uiState.email,
                onValueChange = registroViewModel::onEmail,
                placeholder = "Correo electrónico",
                icono = Icons.Outlined.Email,
                error = uiState.errorEmail,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            com.example.familiasquesuman.ui.components.SelectorCiudad(
                ciudadIdSeleccionada = uiState.ciudadId,
                onCiudadSeleccionada = { registroViewModel.onCiudad(it.id) },
                etiqueta = "Ciudad",
                modifier = Modifier.padding(bottom = 12.dp)
            )

            val iconoOjo: @Composable () -> Unit = {
                IconButton(onClick = { mostrarPassword = !mostrarPassword }) {
                    Icon(
                        imageVector = if (mostrarPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (mostrarPassword) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = GrisTexto
                    )
                }
            }

            CampoRegistro(
                valor = uiState.password,
                onValueChange = registroViewModel::onPassword,
                placeholder = "Contraseña",
                icono = Icons.Outlined.Lock,
                error = uiState.errorPassword,
                ayuda = "Mínimo ${RegistroViewModel.MIN_PASSWORD} caracteres",
                esPassword = !mostrarPassword,
                trailingIcon = iconoOjo,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )

            CampoRegistro(
                valor = uiState.confirmarPassword,
                onValueChange = registroViewModel::onConfirmar,
                placeholder = "Confirmar contraseña",
                icono = Icons.Outlined.Lock,
                error = uiState.errorConfirmar,
                esPassword = !mostrarPassword,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { registroViewModel.crearCuenta() })
            )

            uiState.errorGeneral?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = { registroViewModel.crearCuenta() },
                enabled = !uiState.cargando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Ambar,
                    contentColor = Blanco,
                    disabledContainerColor = Ambar.copy(alpha = 0.55f)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (uiState.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Blanco,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Crear cuenta",
                        style = MaterialTheme.typography.titleMedium,
                        color = Blanco
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            ) {
                Text(
                    text = "¿Ya tienes cuenta?",
                    style = MaterialTheme.typography.bodySmall,
                    color = AzulMarino
                )
                TextButton(onClick = irALogin) {
                    Text(
                        text = "Inicia sesión",
                        style = MaterialTheme.typography.bodySmall,
                        color = Ambar,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = AzulMarino
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Tu información está protegida y solo\n" +
                            "se usa para gestionar tu participación.",
                    style = MaterialTheme.typography.labelSmall,
                    color = AzulMarino,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/** Campo con el mismo estilo que el login, más ícono y mensaje de error/ayuda debajo. */
@Composable
private fun CampoRegistro(
    valor: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icono: ImageVector,
    error: String?,
    ayuda: String? = null,
    esPassword: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    // Tipo explícito para que Compose trate la lambda como @Composable.
    val textoAbajo: (@Composable () -> Unit)? = when {
        error != null -> { { Text(text = error) } }
        ayuda != null -> { { Text(text = ayuda, color = GrisTexto) } }
        else -> null
    }

    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = GrisTexto) },
        leadingIcon = { Icon(imageVector = icono, contentDescription = null, tint = GrisTexto) },
        trailingIcon = trailingIcon,
        singleLine = true,
        isError = error != null,
        supportingText = textoAbajo,
        visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Ambar,
            unfocusedBorderColor = GrisBorde,
            focusedContainerColor = Blanco,
            unfocusedContainerColor = Blanco,
            errorContainerColor = Blanco,
            cursorColor = AzulMarino,
            focusedTextColor = AzulMarino,
            unfocusedTextColor = AzulMarino,
            errorTextColor = AzulMarino
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = if (error == null && ayuda == null) 12.dp else 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun RegistroScreenPreview() {
    FamiliasQueSumanTheme {
        RegistroScreen(navController = rememberNavController())
    }
}