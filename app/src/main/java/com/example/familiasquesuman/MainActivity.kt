package com.example.familiasquesuman

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.data.local.OnboardingPreferences
import com.example.familiasquesuman.data.repository.AuthRepository
import com.example.familiasquesuman.data.repository.NotificacionesRepository
import com.example.familiasquesuman.notificaciones.DestinosNotificacion
import com.example.familiasquesuman.notificaciones.EstadoNotificaciones
import com.example.familiasquesuman.notificaciones.NotificacionesLocales
import com.example.familiasquesuman.notificaciones.ProgramadorNotificaciones
import com.example.familiasquesuman.ui.navigation.NavGraphFamilias
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        SesionManager.init(this)
        NotificacionesLocales.crearCanal(this)
        lifecycleScope.launch { AuthRepository().validarSesionGuardada() }
        abrirDesdeNotificacion(intent)

        // Mientras la app está en pantalla: revisar al volver a ella y luego cada minuto.
        // repeatOnLifecycle detiene el ciclo solo al salir (en segundo plano queda WorkManager).
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    if (SesionManager.estaAutenticado) {
                        ProgramadorNotificaciones.revisarAhora(applicationContext)
                    }
                    delay(60_000)
                }
            }
        }

        val preferencias = OnboardingPreferences(this)
        val onboardingCompletado =
            preferencias.yaCompletoOnboarding()

        setContent {
            FamiliasQueSumanTheme {
                // Android 13+: permiso para mostrar notificaciones. Si dice que no, la campana
                // de la app sigue funcionando; solo no salen en la barra del celular.
                val pedirPermiso = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { }

                // Al iniciar o cerrar sesión: programar o cancelar la revisión en segundo plano
                val token = SesionManager.token
                LaunchedEffect(token) {
                    if (token != null) {
                        ProgramadorNotificaciones.programar(applicationContext)
                        ProgramadorNotificaciones.revisarAhora(applicationContext)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            !NotificacionesLocales.tienePermiso(this@MainActivity)
                        ) {
                            pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        ProgramadorNotificaciones.cancelar(applicationContext)
                        NotificacionesLocales.quitarTodas(applicationContext)
                        EstadoNotificaciones.noLeidas = 0
                    }
                }

                NavGraphFamilias(
                    mostrarOnboarding =  !onboardingCompletado,
                    marcarOnboardingVisto = {
                        preferencias.marcarComoCompletado()
                    }
                )
            }
        }
    }

    /** La app ya estaba abierta y se tocó una notificación (launchMode="singleTop"). */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        abrirDesdeNotificacion(intent)
    }

    /** Si la app se abrió tocando una notificación: ir a su pantalla y marcarla como leída. */
    private fun abrirDesdeNotificacion(intent: Intent?) {
        val id = intent?.getLongExtra(NotificacionesLocales.EXTRA_ID, -1L) ?: -1L
        if (id < 0) return
        val ruta = DestinosNotificacion.ruta(
            tipo = intent?.getStringExtra(NotificacionesLocales.EXTRA_TIPO).orEmpty(),
            destinoTipo = intent?.getStringExtra(NotificacionesLocales.EXTRA_DESTINO_TIPO),
            destinoId = intent?.getStringExtra(NotificacionesLocales.EXTRA_DESTINO_ID)
        ) ?: Rutas.Notificaciones.ruta
        EstadoNotificaciones.rutaPendiente = ruta
        intent?.removeExtra(NotificacionesLocales.EXTRA_ID) // que no se vuelva a abrir al girar la pantalla

        lifecycleScope.launch {
            val repo = NotificacionesRepository()
            repo.marcarLeida(id)
            repo.refrescarContador()
        }
    }
}