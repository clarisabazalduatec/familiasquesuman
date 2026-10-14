package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.LoginRequestDto
import com.example.familiasquesuman.data.remote.dto.RegistroRequestDto
import com.google.gson.JsonParser
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class AuthRepository {

    suspend fun login(email: String, password: String, recordar: Boolean): Result<Unit> = llamar {
        val respuesta = RetrofitInstance.api.login(LoginRequestDto(email.trim(), password))
        SesionManager.guardarSesion(respuesta.access_token, respuesta.usuario, recordar)
    }

    suspend fun registrar(nombre: String, email: String, password: String, ubicacion: String?): Result<Unit> = llamar {
        val respuesta = RetrofitInstance.api.registro(
            RegistroRequestDto(
                nombre = nombre.trim(),
                email = email.trim(),
                password = password,
                ubicacion = ubicacion?.trim()?.ifBlank { null }
            )
        )
        SesionManager.guardarSesion(respuesta.access_token, respuesta.usuario, recordar = true)
    }

    /** Al abrir la app: confirma con el backend que la sesión guardada sigue sirviendo. */
    suspend fun validarSesionGuardada() {
        if (!SesionManager.estaAutenticado) return
        try {
            val perfil = RetrofitInstance.api.miPerfil()
            SesionManager.actualizarUsuario(perfil.aUsuarioDto())
        } catch (_: Exception) {
            // sin red: se conserva la sesión; si fue 401, el interceptor ya la cerró
        }
    }

    fun cerrarSesion() = SesionManager.cerrarSesion()

    private inline fun llamar(bloque: () -> Unit): Result<Unit> = try {
        bloque()
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        Result.failure(Exception(mensajeDeError(e)))
    } catch (e: IOException) {
        Result.failure(Exception("No se pudo conectar con el servidor. Revisa tu conexión."))
    } catch (e: Exception) {
        Result.failure(Exception("Ocurrió un error inesperado. Intenta de nuevo."))
    }

    /** FastAPI manda {"detail": "mensaje"}; en errores 422 "detail" es una lista. */
    private fun mensajeDeError(e: HttpException): String {
        val detalle = try {
            val cuerpo = e.response()?.errorBody()?.string()
            val detail = JsonParser.parseString(cuerpo).asJsonObject.get("detail")
            if (detail != null && detail.isJsonPrimitive) detail.asString else null
        } catch (_: Exception) {
            null
        }
        return detalle ?: when (e.code()) {
            401 -> "Correo o contraseña incorrectos"
            422 -> "Revisa que los datos estén completos y el correo sea válido"
            in 500..599 -> "El servidor tuvo un problema. Intenta más tarde."
            else -> "No se pudo completar la solicitud (${e.code()})"
        }
    }
}