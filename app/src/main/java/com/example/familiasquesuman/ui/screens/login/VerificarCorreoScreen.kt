package com.example.familiasquesuman.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MarkEmailUnread
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.familiasquesuman.R
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.data.repository.AuthRepository
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.GrisTexto
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VerificarCorreoUiState(
    val codigo: String = "",
    val error: String? = null,
    val aviso: String? = null,          // mensajes que no son error ("Te enviamos un código nuevo")
    val verificando: Boolean = false,
    val reenviando: Boolean = false,
    val segundosReenvio: Int = 0,       // el backend pide 60 s entre códigos
    val verificado: Boolean = false
)

class VerificarCorreoViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerificarCorreoUiState())
    val uiState: StateFlow<VerificarCorreoUiState> = _uiState
    private var cuentaRegresiva: Job? = null
    private var iniciado = false

    /** Al entrar: si viene del registro el código ya se mandó (solo arranca la espera); si no, se pide uno. */
    fun iniciar(codigoRecienEnviado: Boolean) {
        if (iniciado) return
        iniciado = true
        if (codigoRecienEnviado) esperarParaReenviar() else reenviar()
    }

    fun onCodigo(texto: String) = _uiState.update {
        it.copy(codigo = texto.filter(Char::isDigit).take(6), error = null, aviso = null)
    }

    fun verificar() {
        val s = _uiState.value
        if (s.codigo.length != 6 || s.verificando) return
        _uiState.update { it.copy(verificando = true, error = null, aviso = null) }
        viewModelScope.launch {
            authRepository.confirmarCodigoVerificacion(s.codigo)
                .onSuccess { _uiState.update { it.copy(verificando = false, verificado = true) } }
                .onFailure { e -> _uiState.update { it.copy(verificando = false, error = e.message) } }
        }
    }

    fun reenviar() {
        val s = _uiState.value
        if (s.reenviando || s.segundosReenvio > 0) return
        _uiState.update { it.copy(reenviando = true, error = null, aviso = null, codigo = "") }
        viewModelScope.launch {
            authRepository.enviarCodigoVerificacion()
                .onSuccess {
                    _uiState.update { it.copy(reenviando = false, aviso = "Te enviamos un código nuevo.") }
                    esperarParaReenviar()
                }
                .onFailure { e -> _uiState.update { it.copy(reenviando = false, error = e.message) } }
        }
    }

    private fun esperarParaReenviar() {
        cuentaRegresiva?.cancel()
        cuentaRegresiva = viewModelScope.launch {
            for (s in 60 downTo 1) {
                _uiState.update { it.copy(segundosReenvio = s) }
                delay(1_000)
            }
            _uiState.update { it.copy(segundosReenvio = 0) }
        }
    }
}

/**
 * Verificar el correo con el código de 6 dígitos.
 * - Después de registrarse (no hay pantalla anterior): "Hacerlo después" y al verificar van al Inicio.
 * - Desde el Perfil o un aviso (sí hay pantalla anterior): se pide un código al entrar y luego se regresa.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificarCorreoScreen(
    navController: NavHostController,
    viewModel: VerificarCorreoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val vieneDelRegistro = remember { navController.previousBackStackEntry == null }
    val correo = SesionManager.usuario?.email.orEmpty()

    val salir: () -> Unit = {
        if (vieneDelRegistro) {
            navController.navigate(Rutas.Inicio.ruta) {
                popUpTo(navController.graph.id) { inclusive = true }
                launchSingleTop = true
            }
        } else {
            navController.popBackStack()
        }
    }

    LaunchedEffect(Unit) {
        when {
            !SesionManager.estaAutenticado -> navController.navigate(Rutas.Login.ruta)
            SesionManager.correoVerificado -> salir()            // ya estaba verificado
            else -> viewModel.iniciar(codigoRecienEnviado = vieneDelRegistro)
        }
    }

    if (uiState.verificado) {
        CorreoVerificado(onContinuar = salir)
        return
    }

    Scaffold(containerColor = CremaFondo) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo2_onb),
                contentDescription = "Familias que Suman",
                modifier = Modifier.width(220.dp).height(68.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(24.dp))

            Surface(shape = CircleShape, color = AmbarClaro, modifier = Modifier.size(84.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.MarkEmailUnread, contentDescription = null, tint = AzulMarino,
                        modifier = Modifier.size(44.dp))
                }
            }
            Spacer(modifier = Modifier.height(18.dp))

            Text("Verifica tu correo", style = MaterialTheme.typography.headlineSmall, color = AzulMarino)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Te enviamos un código de 6 dígitos a\n$correo",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.codigo,
                onValueChange = viewModel::onCodigo,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("000000", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                singleLine = true,
                enabled = !uiState.verificando,
                isError = uiState.error != null,
                supportingText = (uiState.error ?: uiState.aviso)?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    textAlign = TextAlign.Center, letterSpacing = 8.sp, fontWeight = FontWeight.Bold
                ),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulMarino,
                    unfocusedBorderColor = AzulMarino.copy(alpha = .25f)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = viewModel::verificar,
                enabled = uiState.codigo.length == 6 && !uiState.verificando,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Blanco)
            ) {
                if (uiState.verificando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Blanco, strokeWidth = 2.dp)
                } else {
                    Text("Verificar correo", style = MaterialTheme.typography.titleSmall)
                }
            }

            TextButton(
                onClick = viewModel::reenviar,
                enabled = uiState.segundosReenvio == 0 && !uiState.reenviando && !uiState.verificando
            ) {
                Text(
                    text = when {
                        uiState.reenviando -> "Enviando código..."
                        uiState.segundosReenvio > 0 -> "Reenviar código en ${uiState.segundosReenvio} s"
                        else -> "¿No te llegó? Reenviar código"
                    },
                    color = if (uiState.segundosReenvio == 0 && !uiState.reenviando) AzulMarino else GrisTexto
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Puedes explorar la app sin verificar, pero para inscribirte a actividades, " +
                        "publicar o comentar necesitas confirmar tu correo. Revisa también la carpeta de spam.",
                style = MaterialTheme.typography.bodySmall,
                color = GrisTexto,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = salir, enabled = !uiState.verificando) {
                Text("Hacerlo después", color = AzulMarino, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun CorreoVerificado(onContinuar: () -> Unit) = Surface(color = CremaFondo, modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡Correo verificado!", style = MaterialTheme.typography.headlineSmall, color = AzulMarino)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Ya puedes inscribir a tu familia en actividades y participar en la comunidad.",
            style = MaterialTheme.typography.bodyMedium, color = GrisTexto, textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onContinuar,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Blanco)
        ) { Text("Continuar") }
    }
}
