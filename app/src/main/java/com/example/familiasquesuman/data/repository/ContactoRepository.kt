package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.ConfirmarCodigoDto
import com.example.familiasquesuman.data.remote.dto.ContactoRequestDto
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Contacto y ayuda en dos pasos (así lo pide el backend para verificar el correo):
 *  1. solicitarCodigo: manda el mensaje; al correo de la persona llega un código de 6 dígitos.
 *  2. confirmarCodigo: con ese código, el backend envía el mensaje a Familias que Suman.
 * Los errores regresan el texto del backend ("Código incorrecto o vencido.", "Espera 60 segundos...").
 */
class ContactoRepository {

    suspend fun solicitarCodigo(nombre: String, correo: String, mensaje: String): Result<Unit> = llamar {
        RetrofitInstance.api.solicitarCodigoContacto(ContactoRequestDto(nombre, correo, mensaje))
    }

    suspend fun confirmarCodigo(correo: String, codigo: String): Result<Unit> = llamar {
        RetrofitInstance.api.confirmarCodigoContacto(ConfirmarCodigoDto(correo, codigo))
    }

    private suspend fun llamar(bloque: suspend () -> Any): Result<Unit> = try {
        bloque()
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        Result.failure(Exception(mensajeDeErrorHttp(e)))
    } catch (e: IOException) {
        Result.failure(Exception("No se pudo conectar con el servidor. Revisa tu conexión."))
    } catch (e: Exception) {
        Result.failure(Exception("Ocurrió un error inesperado. Intenta de nuevo."))
    }
}