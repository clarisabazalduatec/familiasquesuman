package com.example.familiasquesuman.notificaciones

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.familiasquesuman.MainActivity
import com.example.familiasquesuman.R
import com.example.familiasquesuman.data.remote.dto.NotificacionDto

/** Notificaciones que aparecen en la barra del celular. */
object NotificacionesLocales {

    private const val CANAL = "avisos_familias"
    private const val COLOR_AMBAR = 0xFFE8A33D.toInt()

    // Extras del Intent que abre la app al tocar la notificación
    const val EXTRA_ID = "notificacion_id"
    const val EXTRA_TIPO = "notificacion_tipo"
    const val EXTRA_DESTINO_TIPO = "notificacion_destino_tipo"
    const val EXTRA_DESTINO_ID = "notificacion_destino_id"

    /** Android 8+: sin canal no se muestra nada. Se puede llamar muchas veces (no duplica). */
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL,
                "Avisos de Familias que Suman",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Publicaciones, comentarios, invitaciones, avisos y recordatorios de actividades"
            }
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
        }
    }

    /** Android 13+ pide permiso; antes se tiene siempre. */
    fun tienePermiso(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission") // se revisa con tienePermiso() justo antes
    fun mostrar(context: Context, n: NotificacionDto) {
        if (!tienePermiso(context)) return

        val abrirApp = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_ID, n.id)
            putExtra(EXTRA_TIPO, n.tipo)
            putExtra(EXTRA_DESTINO_TIPO, n.destinoTipo)
            putExtra(EXTRA_DESTINO_ID, n.destinoId)
        }
        val alTocar = PendingIntent.getActivity(
            context,
            n.id.toInt(), // un requestCode distinto por notificación para que no se pisen los extras
            abrirApp,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(context, CANAL)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setColor(COLOR_AMBAR)
            .setContentTitle(n.titulo)
            .setContentText(n.cuerpo)
            .setStyle(NotificationCompat.BigTextStyle().bigText(n.cuerpo))
            .setContentIntent(alTocar)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // El id del backend es estable: si por algo se muestra dos veces, reemplaza en vez de duplicar
        NotificationManagerCompat.from(context).notify(n.id.toInt(), notificacion)
    }

    fun quitarTodas(context: Context) = NotificationManagerCompat.from(context).cancelAll()
}