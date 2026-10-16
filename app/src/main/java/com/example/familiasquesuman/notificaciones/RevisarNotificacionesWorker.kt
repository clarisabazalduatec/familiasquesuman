package com.example.familiasquesuman.notificaciones

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.data.remote.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException

/**
 * Corre en segundo plano (cada ~15 min y al abrir la app): pregunta al backend por las
 * notificaciones nuevas desde la última que vio este celular y las muestra en la barra.
 *
 * Solo funciona con la app cerrada si la sesión se guardó ("Recordarme"); si no, el token
 * vive solo en memoria y al cerrarse la app ya no hay a quién preguntarle.
 */
class RevisarNotificacionesWorker(
    contexto: Context,
    parametros: WorkerParameters
) : CoroutineWorker(contexto, parametros) {

    override suspend fun doWork(): Result {
        val contexto = applicationContext
        SesionManager.init(contexto) // por si el proceso se creó solo para este trabajo
        val usuarioId = SesionManager.usuario?.id
        if (!SesionManager.estaAutenticado || usuarioId == null) return Result.success()

        val api = RetrofitInstance.api
        return try {
            val cursor = CursorNotificaciones.leer(contexto, usuarioId)
            if (cursor == null) {
                // Primera vez en este celular: no se avisa de todo el historial, solo de lo que llegue después
                val resumen = api.resumenNotificaciones()
                CursorNotificaciones.guardar(contexto, usuarioId, resumen.ultimoId ?: 0L)
                EstadoNotificaciones.noLeidas = resumen.noLeidas
                return Result.success()
            }

            val nuevas = api.misNotificaciones(despuesDe = cursor) // de la más vieja a la más nueva
            nuevas.filter { !it.leida }.forEach { NotificacionesLocales.mostrar(contexto, it) }
            // El cursor se guarda DESPUÉS de mostrarlas: si algo falla antes, se reintenta sin perder ninguna
            nuevas.maxOfOrNull { it.id }?.let { CursorNotificaciones.guardar(contexto, usuarioId, it) }

            EstadoNotificaciones.noLeidas = api.resumenNotificaciones().noLeidas
            Result.success()
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            // 401: la sesión expiró (el interceptor ya la cerró); no tiene caso reintentar
            if (e.code() == 401) Result.success() else Result.retry()
        } catch (e: IOException) {
            Result.retry() // sin red o servidor apagado: WorkManager vuelve a intentar más tarde
        }
    }
}

/** Último id de notificación que ya se revisó en este celular, por usuario. */
object CursorNotificaciones {
    private const val PREFS = "familias_notificaciones"

    fun leer(contexto: Context, usuarioId: String): Long? {
        val p = contexto.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val clave = "ultimo_id_$usuarioId"
        return if (p.contains(clave)) p.getLong(clave, 0L) else null
    }

    fun guardar(contexto: Context, usuarioId: String, ultimoId: Long) {
        contexto.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putLong("ultimo_id_$usuarioId", ultimoId).apply()
    }
}

/** Programa / cancela la revisión en segundo plano. */
object ProgramadorNotificaciones {
    private const val PERIODICO = "revisar_notificaciones"
    private const val INMEDIATO = "revisar_notificaciones_ahora"

    private val conRed = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    /** Cada 15 min (el mínimo que permite Android). KEEP: si ya estaba programado, no lo reinicia. */
    fun programar(contexto: Context) {
        val trabajo = PeriodicWorkRequestBuilder<RevisarNotificacionesWorker>(15, TimeUnit.MINUTES)
            .setConstraints(conRed)
            .build()
        WorkManager.getInstance(contexto)
            .enqueueUniquePeriodicWork(PERIODICO, ExistingPeriodicWorkPolicy.KEEP, trabajo)
    }

    /** Una revisión ya (al abrir la app o al iniciar sesión). */
    fun revisarAhora(contexto: Context) {
        val trabajo = OneTimeWorkRequestBuilder<RevisarNotificacionesWorker>()
            .setConstraints(conRed)
            .build()
        WorkManager.getInstance(contexto)
            .enqueueUniqueWork(INMEDIATO, ExistingWorkPolicy.KEEP, trabajo)
    }

    /** Al cerrar sesión: que no se sigan pidiendo notificaciones de esa cuenta. */
    fun cancelar(contexto: Context) {
        WorkManager.getInstance(contexto).apply {
            cancelUniqueWork(PERIODICO)
            cancelUniqueWork(INMEDIATO)
        }
    }
}