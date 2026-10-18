package com.example.familiasquesuman.data.repository

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.example.familiasquesuman.data.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * Sube fotos elegidas del teléfono al backend.
 * - subirSiHaceFalta: imágenes de proyectos/actividades/centros (POST /imagenes, solo admin).
 * - parteMultipart: arma el archivo listo para mandar; lo usa también la foto de perfil.
 *
 * El SeleccionadorImagenes guarda las fotos como "content://...": esas se suben.
 * Las que ya son del servidor (vienen de editar un proyecto) se dejan igual.
 */
class ImagenRepository {

    companion object {
        private const val LIMITE_SIN_REDUCIR = 1_500_000 // ~1.5 MB: arriba de esto se reduce
        private const val LADO_MAXIMO = 1600             // px del lado más largo al reducir
        const val LADO_FOTO_PERFIL = 800                 // la foto de perfil se ve chiquita: basta con 800 px
    }

    /** Regresa la URL lista para guardar en el backend (relativa si es nuestra). */
    suspend fun subirSiHaceFalta(resolver: ContentResolver, url: String): String {
        val esLocal = url.startsWith("content:") || url.startsWith("file:")
        if (!esLocal) return RetrofitInstance.urlParaGuardar(url)

        val parte = parteMultipart(resolver, Uri.parse(url))
        return RetrofitInstance.api.subirImagen(parte).url
    }

    /** Sube fotografías de publicaciones de Comunidad.**/
    suspend fun subirImagenComunidad(resolver: ContentResolver, url: String): String {
        val esLocal = url.startsWith("content:") || url.startsWith("file:")
        if (!esLocal) { return RetrofitInstance.urlParaGuardar(url) }
        val parte = parteMultipart(resolver, Uri.parse(url))
        return RetrofitInstance.api.subirImagenComunidad(parte).url
    }

    /**
     * Lee la foto, la reduce si hace falta y la empaqueta como el campo "archivo" del multipart.
     * @param ladoMaximo si se pasa, la foto SIEMPRE se reduce a ese tamaño (lado más largo) y va en JPEG.
     */
    suspend fun parteMultipart(
        resolver: ContentResolver,
        uri: Uri,
        ladoMaximo: Int? = null
    ): MultipartBody.Part {
        val (bytes, mime) = withContext(Dispatchers.IO) {
            if (ladoMaximo != null) {
                leerOriginal(resolver, uri).let { (datos, tipo) -> reducir(datos, ladoMaximo) ?: (datos to tipo) }
            } else {
                leerYReducir(resolver, uri)
            }
        }
        val extension = if (mime == "image/png") "png" else if (mime == "image/webp") "webp" else "jpg"
        return MultipartBody.Part.createFormData(
            "archivo",
            "foto.$extension",
            bytes.toRequestBody(mime.toMediaType())
        )
    }

    private fun leerOriginal(resolver: ContentResolver, uri: Uri): Pair<ByteArray, String> {
        val mime = resolver.getType(uri) ?: "image/jpeg"
        val original = resolver.openInputStream(uri)?.use { it.readBytes() }
            ?: throw IllegalStateException("No se pudo leer la foto")
        return original to mime
    }

    private fun leerYReducir(resolver: ContentResolver, uri: Uri): Pair<ByteArray, String> {
        val (original, mime) = leerOriginal(resolver, uri)
        if (original.size <= LIMITE_SIN_REDUCIR) return original to mime
        // Foto pesada (cámara de celular): se reduce y se pasa a JPEG.
        return reducir(original, LADO_MAXIMO) ?: (original to mime)
    }

    /** Reduce a ~ladoMaximo px (lado más largo) y pasa a JPEG. null si no se pudo decodificar. */
    private fun reducir(original: ByteArray, ladoMaximo: Int): Pair<ByteArray, String>? {
        val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(original, 0, original.size, limites)
        if (limites.outWidth <= 0 || limites.outHeight <= 0) return null

        var muestreo = 1
        while (maxOf(limites.outWidth, limites.outHeight) / (muestreo * 2) >= ladoMaximo) muestreo *= 2

        val decodificado = BitmapFactory.decodeByteArray(
            original, 0, original.size,
            BitmapFactory.Options().apply { inSampleSize = muestreo }
        ) ?: return null

        // inSampleSize solo reduce en potencias de 2: se ajusta al tamaño exacto si todavía sobra.
        // Además se aplica la rotación del EXIF: las fotos de cámara vienen "acostadas" y el
        // celular las endereza con ese dato, que se pierde al volver a comprimir.
        val matriz = Matrix()
        val ladoActual = maxOf(decodificado.width, decodificado.height)
        if (ladoActual > ladoMaximo) {
            val escala = ladoMaximo.toFloat() / ladoActual
            matriz.postScale(escala, escala)
        }
        val grados = rotacionExif(original)
        if (grados != 0f) matriz.postRotate(grados)

        val bitmap = if (matriz.isIdentity) {
            decodificado
        } else {
            Bitmap.createBitmap(decodificado, 0, 0, decodificado.width, decodificado.height, matriz, true)
                .also { if (it !== decodificado) decodificado.recycle() }
        }

        val salida = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, salida)
        bitmap.recycle()
        return salida.toByteArray() to "image/jpeg"
    }

    private fun rotacionExif(datos: ByteArray): Float = try {
        when (ExifInterface(ByteArrayInputStream(datos))
            .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
    } catch (_: Exception) {
        0f // PNG/WEBP sin EXIF o archivo raro: se deja como está
    }
}