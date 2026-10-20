package com.example.familiasquesuman.ui.screens.informacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.R
import com.example.familiasquesuman.ui.components.PieAvisoPrivacidad
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.GrisTexto
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Patterns
import androidx.compose.material.icons.outlined.Lock
import com.example.familiasquesuman.data.repository.ContactoRepository
import androidx.compose.material.icons.filled.CheckCircle
import com.example.familiasquesuman.ui.theme.AmbarClaro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoAyudaScreen(navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var enviando by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var mensajeEnviado by remember { mutableStateOf(false) }
    val repositorio = remember { ContactoRepository() }

    // Paso 2: diálogo para escribir el código que llegó al correo
    var mostrarDialogoCodigo by remember { mutableStateOf(false) }
    var codigo by remember { mutableStateOf("") }
    var errorCodigo by remember { mutableStateOf<String?>(null) }
    var confirmando by remember { mutableStateOf(false) }
    var reenviando by remember { mutableStateOf(false) }
    var segundosReenvio by remember { mutableIntStateOf(0) } // el backend pide 60 s entre códigos

    val correoValido = Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()

    LaunchedEffect(segundosReenvio) {
        if (segundosReenvio > 0) {
            delay(1_000)
            segundosReenvio -= 1
        }
    }

    // Paso 1 (y "Reenviar código"): el backend manda un código de 6 dígitos al correo
    fun solicitarCodigo(alTerminar: (String?) -> Unit) {
        scope.launch {
            repositorio.solicitarCodigo(nombre.trim(), correo.trim(), mensaje.trim())
                .onSuccess {
                    segundosReenvio = 60
                    alTerminar(null)
                }
                .onFailure { alTerminar(it.message ?: "No se pudo enviar el código.") }
        }
    }

    if (mostrarDialogoCodigo) {
        DialogoCodigoVerificacion(
            correo = correo.trim(),
            codigo = codigo,
            onCodigoCambia = { nuevo ->
                codigo = nuevo.filter { it.isDigit() }.take(6)
                errorCodigo = null
            },
            error = errorCodigo,
            confirmando = confirmando,
            reenviando = reenviando,
            segundosReenvio = segundosReenvio,
            onConfirmar = {
                confirmando = true
                errorCodigo = null
                scope.launch {
                    repositorio.confirmarCodigo(correo.trim(), codigo)
                        .onSuccess {
                            mostrarDialogoCodigo = false
                            nombre = ""
                            correo = ""
                            mensaje = ""
                            codigo = ""
                            mensajeEnviado = true
                        }
                        .onFailure { errorCodigo = it.message }
                    confirmando = false
                }
            },
            onReenviar = {
                reenviando = true
                codigo = ""
                errorCodigo = null
                solicitarCodigo { error ->
                    errorCodigo = error ?: "Te enviamos un código nuevo."
                    reenviando = false
                }
            },
            onCancelar = {
                if (!confirmando) {
                    mostrarDialogoCodigo = false
                    codigo = ""
                    errorCodigo = null
                }
            }
        )
    }


    if (mensajeEnviado) {
        ConfirmacionContactoScreen(
            onVolverInicio = {
                navController.navigate(Rutas.Inicio.ruta) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            }
        )
        return
    }

    Scaffold(containerColor = CremaFondo,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = CremaFondo,
                    navigationIconContentColor = AzulMarino,
                    actionIconContentColor = AzulMarino
                ),

                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored
                            .Outlined
                            .ArrowBack,
                            contentDescription = "Regresar",
                            tint = AzulMarino
                        )
                    }
                },
                title = {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.logo2_onb),
                        contentDescription = "Familias que Suman",
                        modifier =
                            Modifier
                                .width(220.dp)
                                .height(54.dp),
                        contentScale =
                            ContentScale.Fit
                    )
                },
                actions = { IconButton(onClick = { navController.navigate(Rutas.Notificaciones.ruta) }) {
                    Icon(imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = AzulMarino
                    )
                }
                }
            )
        }
    ) { paddingInterno ->
        Column(modifier = Modifier
            .padding(paddingInterno)
            .fillMaxSize()
            .background(CremaFondo)
            .verticalScroll(scrollState)
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 18.dp,
                bottom = 28.dp
            )
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Contacto y ayuda",
                        style = MaterialTheme.typography
                            .headlineMedium,
                        color = AzulMarino
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "¿Tienes dudas o sugerencias? Escríbenos.",
                        style = MaterialTheme.typography
                            .titleSmall,
                        color = AzulMarino
                    )
                }
                Icon(imageVector = Icons.Outlined.Send,
                    contentDescription = null,
                    tint = Ambar,
                    modifier = Modifier
                        .size(58.dp)
                        .rotate(-20f)
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            Text(text = "Nos encantaría leerte. Puedes escribirnos si tienes dudas sobre la plataforma, quieres compartir una sugerencia o necesitas más información.",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )
            Spacer(modifier = Modifier.height(26.dp))
            EtiquetaFormulario(texto = "Nombre")
            Spacer(modifier = Modifier.height(7.dp))
            OutlinedTextField(value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Escribe tu nombre",
                        style = MaterialTheme.typography
                            .bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = AzulMarino
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulMarino,
                    unfocusedBorderColor = AzulMarino.copy(
                        alpha = .20f
                    ),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            EtiquetaFormulario(texto = "Correo electrónico")
            Spacer(modifier = Modifier.height(7.dp))
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "tu.correo@ejemplo.com")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = AzulMarino
                    )
                },
                singleLine = true,
                isError = correo.isNotBlank() && !correoValido,
                supportingText = if (correo.isNotBlank() && !correoValido) {
                    { Text("Escribe un correo válido, ej. nombre@gmail.com") }
                } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulMarino,
                    unfocusedBorderColor = AzulMarino.copy(
                        alpha = .20f
                    )
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            EtiquetaFormulario(texto = "Mensaje")
            Spacer(modifier = Modifier.height(7.dp))
            OutlinedTextField(value = mensaje,
                onValueChange = { mensaje = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp),

                placeholder = {
                    Text(text = "Cuéntanos en qué podemos ayudarte...")
                },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .height(145.dp)
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Sms,
                            contentDescription = null,
                            tint = AzulMarino
                        )
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulMarino,
                    unfocusedBorderColor = AzulMarino.copy(
                        alpha = .20f
                    )
                )
            )
            Spacer(modifier = Modifier.height(26.dp))
            Button(
                onClick = {
                    if (!enviando) {
                        enviando = true
                        // Paso 1: pedir el código; si sale bien, se abre el diálogo para escribirlo
                        solicitarCodigo { error ->
                            enviando = false
                            if (error == null) {
                                codigo = ""
                                errorCodigo = null
                                mostrarDialogoCodigo = true
                            } else {
                                scope.launch { snackbarHostState.showSnackbar(error) }
                            }
                        }
                    }
                },
                enabled = nombre.isNotBlank() &&
                        correoValido &&
                        mensaje.isNotBlank() &&
                        !enviando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulMarino,
                    contentColor = Blanco
                )
            ) {
                Text(text = if (enviando) "Enviando..." else "Enviar mensaje",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.weight(1f))
                if (enviando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Blanco,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Icon(imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = GrisTexto,
                    modifier =
                        Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Este formulario es para dudas o sugerencias sobre Familias que Suman+. Para confirmar que el correo es tuyo, te enviaremos un código de 6 dígitos.",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography
                        .bodySmall,
                    color = GrisTexto
                )
            }
            // Aviso de Privacidad
            PieAvisoPrivacidad(
                navController = navController
            )
        }
    }
}

@Composable
private fun ConfirmacionContactoScreen(
    onVolverInicio: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CremaFondo)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CremaFondo)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = AmbarClaro,
                modifier = Modifier.size(96.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Mensaje enviado",
                        tint = AzulMarino,
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "¡Mensaje enviado!",
                style = MaterialTheme.typography.headlineSmall,
                color = AzulMarino
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu mensaje se envió correctamente. Gracias por ponerte en contacto con Familias que Suman.",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onVolverInicio,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AzulMarino)
            ) {
                Text("Volver al inicio")
            }
        }
    }
}

/** Paso 2: escribir el código de 6 dígitos que llegó al correo. */
@Composable
private fun DialogoCodigoVerificacion(
    correo: String,
    codigo: String,
    onCodigoCambia: (String) -> Unit,
    error: String?,
    confirmando: Boolean,
    reenviando: Boolean,
    segundosReenvio: Int,
    onConfirmar: () -> Unit,
    onReenviar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        containerColor = CremaFondo,
        icon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = AzulMarino) },
        title = { Text("Revisa tu correo", color = AzulMarino) },
        text = {
            Column {
                Text(
                    text = "Te enviamos un código de 6 dígitos a $correo. Escríbelo para enviar tu mensaje. Vence en 10 minutos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = codigo,
                    onValueChange = onCodigoCambia,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("000000") },
                    singleLine = true,
                    enabled = !confirmando,
                    isError = error != null && error != "Te enviamos un código nuevo.",
                    supportingText = error?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    textStyle = MaterialTheme.typography.headlineSmall,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulMarino,
                        unfocusedBorderColor = AzulMarino.copy(alpha = .20f)
                    )
                )
                TextButton(
                    onClick = onReenviar,
                    enabled = segundosReenvio == 0 && !reenviando && !confirmando
                ) {
                    Text(
                        text = when {
                            reenviando -> "Enviando código..."
                            segundosReenvio > 0 -> "Reenviar código en $segundosReenvio s"
                            else -> "Reenviar código"
                        },
                        color = if (segundosReenvio == 0 && !reenviando) AzulMarino else GrisTexto
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                enabled = codigo.length == 6 && !confirmando,
                colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Blanco)
            ) {
                if (confirmando) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Blanco, strokeWidth = 2.dp)
                } else {
                    Text("Confirmar y enviar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar, enabled = !confirmando) {
                Text("Cancelar", color = AzulMarino)
            }
        }
    )
}

@Composable
private fun EtiquetaFormulario(texto: String) {
    Text(text = texto,
        style = MaterialTheme.typography
            .titleSmall,
        color = AzulMarino
    )
}