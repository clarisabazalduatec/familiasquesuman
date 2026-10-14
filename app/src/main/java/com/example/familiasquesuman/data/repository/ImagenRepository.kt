package com.example.familiasquesuman.data.repository

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.familiasquesuman.data.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

/**
 * Sube fotos elegidas del teléfono al backend (POST /imagenes, solo admin).
 *
 * El SeleccionadorImagenes guarda las fotos como "content://...": esas se suben.
 * Las que ya son del servidor (vienen de editar un proyecto) se dejan igual.
 */
class ImagenRepository {

    companion object {
        private const val LIMITE_SIN_REDUCIR = 1_500_000 // ~1.5 MB: arriba de esto se reduce
        private const val LADO_MAXIMO = 1600             // px del lado más largo al reducir
    }

    /** Regresa la URL lista para guardar en el backend (relativa si es nuestra). */
    suspend fun subirSiHaceFalta(resolver: ContentResolver, url: String): String {
        val esLocal = url.startsWith("content:") || url.startsWith("file:")
        if (!esLocal) return RetrofitInstance.urlParaGuardar(url)

        val (bytes, mime) = withContext(Dispatchers.IO) { leerYReducir(resolver, Uri.parse(url)) }
        val extension = if (mime == "image/png") "png" else if (mime == "image/webp") "webp" else "jpg"
        val parte = MultipartBody.Part.createFormData(
            "archivo",
            "foto.$extension",
            bytes.toRequestBody(mime.toMediaType())
        )
        return RetrofitInstance.api.subirImagen(parte).url
    }

    private fun leerYReducir(resolver: ContentResolver, uri: Uri): Pair<ByteArray, String> {
        val mime = resolver.getType(uri) ?: "image/jpeg"
        val original = resolver.openInputStream(uri)?.use { it.readBytes() }
            ?: throw IllegalStateException("No se pudo leer la foto")
        if (original.size <= LIMITE_SIN_REDUCIR) return original to mime

        // Foto pesada (cámara de celular): se reduce y se pasa a JPEG.
        val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(original, 0, original.size, limites)
        var muestreo = 1
        while (maxOf(limites.outWidth, limites.outHeight) / (muestreo * 2) >= LADO_MAXIMO) muestreo *= 2

        val bitmap = BitmapFactory.decodeByteArray(
            original, 0, original.size,
            BitmapFactory.Options().apply { inSampleSize = muestreo }
        ) ?: return original to mime

        val salida = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, salida)
        bitmap.recycle()
        return salida.toByteArray() to "image/jpeg"
    }
}