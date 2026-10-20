package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.FamiliaCrearDto
import com.example.familiasquesuman.data.remote.dto.FamiliaDto
import com.example.familiasquesuman.data.remote.dto.InvitacionCrearDto
import com.example.familiasquesuman.data.remote.dto.InvitacionRecibidaDto
import com.example.familiasquesuman.data.remote.dto.MenorGuardarDto
import com.example.familiasquesuman.data.remote.dto.PerfilActualizarDto
import com.example.familiasquesuman.data.remote.dto.PerfilDto
import com.google.gson.JsonParser
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import android.content.ContentResolver
import android.net.Uri
import com.example.familiasquesuman.data.remote.dto.InsigniaDto

class FamiliaRepository {

    private val api get() = RetrofitInstance.api
    private val imagenes = ImagenRepository()

    suspend fun miPerfil(): Result<PerfilDto> = llamarApi {
        api.miPerfil().also { SesionManager.actualizarUsuario(it.aUsuarioDto()) }
    }

    /** Sube la foto elegida en la galería; la sesión se actualiza para que se vea en toda la app. */
    suspend fun cambiarMiFoto(resolver: ContentResolver, uri: Uri): Result<PerfilDto> = llamarApi {
        val parte = imagenes.parteMultipart(resolver, uri, ladoMaximo = ImagenRepository.LADO_FOTO_PERFIL)
        api.cambiarMiFoto(parte).also { SesionManager.actualizarUsuario(it.aUsuarioDto()) }
    }

    suspend fun quitarMiFoto(): Result<PerfilDto> = llamarApi {
        api.quitarMiFoto().also { SesionManager.actualizarUsuario(it.aUsuarioDto()) }
    }
    suspend fun cambiarMiParentesco(parentesco: String): Result<PerfilDto> = llamarApi {
        api.actualizarMiPerfil(PerfilActualizarDto(parentesco = parentesco))
    }

    suspend fun cambiarMiCiudad(ciudadId: Int): Result<PerfilDto> = llamarApi {
        api.cambiarMiCiudad(com.example.familiasquesuman.data.remote.dto.CiudadActualizarDto(ciudadId)).also { perfil ->
            SesionManager.actualizarUsuario(perfil.aUsuarioDto())
        }
    }

    /** null = el usuario todavía no tiene familia (el backend responde 404). */
    suspend fun miFamilia(): Result<FamiliaDto?> = llamarApi {
        try {
            api.miFamilia()
        } catch (e: HttpException) {
            if (e.code() == 404) null else throw e
        }
    }

    suspend fun crearFamilia(nombre: String?, parentesco: String?): Result<FamiliaDto> = llamarApi {
        api.crearFamilia(FamiliaCrearDto(nombre = nombre?.trim()?.ifBlank { null }, parentesco = parentesco))
    }

    suspend fun salirDeFamilia(): Result<Unit> = llamarApi { api.salirDeFamilia() }

    suspend fun guardarMenor(id: String?, nombre: String, fechaNacimiento: String): Result<Unit> = llamarApi {
        val datos = MenorGuardarDto(nombre = nombre.trim(), fechaNacimiento = fechaNacimiento)
        if (id == null) api.agregarMenor(datos) else api.editarMenor(id, datos)
        Unit
    }

    suspend fun borrarMenor(id: String): Result<Unit> = llamarApi { api.borrarMenor(id) }

    suspend fun invitarAdulto(email: String): Result<Unit> = llamarApi {
        api.invitarAdulto(InvitacionCrearDto(email.trim()))
        Unit
    }

    suspend fun cancelarInvitacion(id: String): Result<Unit> = llamarApi { api.cancelarInvitacion(id) }

    suspend fun misInvitaciones(): Result<List<InvitacionRecibidaDto>> = llamarApi { api.misInvitaciones() }

    suspend fun aceptarInvitacion(id: String): Result<FamiliaDto> = llamarApi { api.aceptarInvitacion(id) }

    suspend fun rechazarInvitacion(id: String): Result<Unit> = llamarApi { api.rechazarInvitacion(id) }

    suspend fun misInsignias(): Result<List<InsigniaDto>> {
        return runCatching {
            api.getMisInsignias()
        }
    }
}

/**
 * Envuelve una llamada al backend y convierte cualquier error en un mensaje en español
 * listo para mostrar (FastAPI manda {"detail": "..."}).
 */
suspend fun <T> llamarApi(bloque: suspend () -> T): Result<T> = try {
    Result.success(bloque())
} catch (e: CancellationException) {
    throw e
} catch (e: HttpException) {
    Result.failure(Exception(mensajeDeErrorHttp(e)))
} catch (e: IOException) {
    Result.failure(Exception("No se pudo conectar con el servidor. Revisa tu conexión."))
} catch (e: Exception) {
    Result.failure(Exception("Ocurrió un error inesperado. Intenta de nuevo."))
}

fun mensajeDeErrorHttp(e: HttpException): String {
    val detalle = try {
        val cuerpo = e.response()?.errorBody()?.string()
        val detail = JsonParser.parseString(cuerpo).asJsonObject.get("detail")
        if (detail != null && detail.isJsonPrimitive) detail.asString else null
    } catch (_: Exception) {
        null
    }
    return detalle ?: when (e.code()) {
        401 -> "Tu sesión expiró. Inicia sesión de nuevo."
        422 -> "Revisa que los datos estén completos y sean válidos"
        in 500..599 -> "El servidor tuvo un problema. Intenta más tarde."
        else -> "No se pudo completar la solicitud (${e.code()})"
    }
}