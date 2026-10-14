package com.example.familiasquesuman.data.remote

import com.example.familiasquesuman.data.remote.dto.ChatMensajeEntradaDto
import com.example.familiasquesuman.data.remote.dto.ChatMensajeRespuestaDto
import com.example.familiasquesuman.data.remote.dto.LoginRequestDto
import com.example.familiasquesuman.data.remote.dto.PerfilDto
import com.example.familiasquesuman.data.remote.dto.RegistroRequestDto
import com.example.familiasquesuman.data.remote.dto.TokenResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/** El token lo agrega AuthInterceptor (en RetrofitInstance); no se pasa a mano. */
interface FamiliasApiService {
    @POST("auth/login")
    suspend fun login(@Body datos: LoginRequestDto): TokenResponseDto

    @POST("auth/registro")
    suspend fun registro(@Body datos: RegistroRequestDto): TokenResponseDto

    @GET("usuarios/me")
    suspend fun miPerfil(): PerfilDto

    @POST("chat")
    suspend fun enviarMensajeChat(@Body entrada: ChatMensajeEntradaDto): ChatMensajeRespuestaDto
}