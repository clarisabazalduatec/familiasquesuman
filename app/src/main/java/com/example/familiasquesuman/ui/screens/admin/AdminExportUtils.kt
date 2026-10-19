package com.example.familiasquesuman.ui.screens.admin

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.familiasquesuman.data.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.Q)
suspend fun descargarCsv(context: Context) {
    withContext(Dispatchers.IO) {
        try {
            val response = RetrofitInstance.api.exportarEncuestasCsv()
            if (response.isSuccessful && response.body() != null) {
                val csvContent = response.body()!!.string()

                val filename = "reporte_encuestas_${System.currentTimeMillis()}.csv"

                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "text/plain") // text/plain para que el emulador lo reconozca
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(csvContent.toByteArray(Charsets.UTF_8))
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "✅ Guardado en Descargas", Toast.LENGTH_SHORT).show()

                        // Intent para abrir de inmediato con el visor del emulador
                        val openIntent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, "text/plain")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        try {
                            context.startActivity(openIntent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Archivo guardado en la carpeta Downloads", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error al generar el reporte (${response.code()})", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}