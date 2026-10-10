package com.example.familiasquesuman.data.remote

import com.example.familiasquesuman.data.remote.dto.ActividadDto
import com.example.familiasquesuman.data.remote.dto.CategoriaDto
import com.example.familiasquesuman.data.remote.dto.ChatMensajeEntradaDto
import com.example.familiasquesuman.data.remote.dto.ChatMensajeRespuestaDto
import com.example.familiasquesuman.data.remote.dto.LoginRequestDto
import com.example.familiasquesuman.data.remote.dto.TokenResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FamiliasApiService {
    @POST("auth/login")
    suspend fun login(@Body datos: LoginRequestDto): TokenResponseDto

    @POST("chat")
    suspend fun enviarMensajeChat(
        @Header("Authorization") token: String?,
        @Body entrada: ChatMensajeEntradaDto
    ): ChatMensajeRespuestaDto

    @GET("actividades")
    suspend fun getActividades(): List<ActividadDto>

    @GET("actividades/{id}")
    suspend fun getActividadDetalle(@Path("id") id: String): ActividadDto

    @GET("categorias")
    suspend fun getCategorias(@Query("ambito") ambito: String? = "ACTIVIDAD"): List<CategoriaDto>
}
