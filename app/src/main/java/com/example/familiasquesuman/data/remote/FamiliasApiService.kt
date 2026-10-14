package com.example.familiasquesuman.data.remote

import com.example.familiasquesuman.data.remote.dto.ChatMensajeEntradaDto
import com.example.familiasquesuman.data.remote.dto.ChatMensajeRespuestaDto
import com.example.familiasquesuman.data.remote.dto.FamiliaCrearDto
import com.example.familiasquesuman.data.remote.dto.FamiliaDto
import com.example.familiasquesuman.data.remote.dto.InvitacionCrearDto
import com.example.familiasquesuman.data.remote.dto.InvitacionEnviadaDto
import com.example.familiasquesuman.data.remote.dto.InvitacionRecibidaDto
import com.example.familiasquesuman.data.remote.dto.LoginRequestDto
import com.example.familiasquesuman.data.remote.dto.MenorDto
import com.example.familiasquesuman.data.remote.dto.MenorGuardarDto
import com.example.familiasquesuman.data.remote.dto.PerfilActualizarDto
import com.example.familiasquesuman.data.remote.dto.PerfilDto
import com.example.familiasquesuman.data.remote.dto.RegistroRequestDto
import com.example.familiasquesuman.data.remote.dto.TokenResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/** El token lo agrega AuthInterceptor (en RetrofitInstance); no se pasa a mano. */
interface FamiliasApiService {

    // ---------- Auth ----------
    @POST("auth/login")
    suspend fun login(@Body datos: LoginRequestDto): TokenResponseDto

    @POST("auth/registro")
    suspend fun registro(@Body datos: RegistroRequestDto): TokenResponseDto

    // ---------- Perfil ----------
    @GET("usuarios/me")
    suspend fun miPerfil(): PerfilDto

    @PATCH("usuarios/me")
    suspend fun actualizarMiPerfil(@Body datos: PerfilActualizarDto): PerfilDto

    // ---------- Familia ----------
    @GET("familias/mia")
    suspend fun miFamilia(): FamiliaDto // 404 = todavía no tiene familia

    @POST("familias")
    suspend fun crearFamilia(@Body datos: FamiliaCrearDto): FamiliaDto

    @POST("familias/mia/salir")
    suspend fun salirDeFamilia()

    @POST("familias/mia/menores")
    suspend fun agregarMenor(@Body datos: MenorGuardarDto): MenorDto

    @PATCH("familias/mia/menores/{id}")
    suspend fun editarMenor(@Path("id") id: String, @Body datos: MenorGuardarDto): MenorDto

    @DELETE("familias/mia/menores/{id}")
    suspend fun borrarMenor(@Path("id") id: String)

    @POST("familias/mia/invitaciones")
    suspend fun invitarAdulto(@Body datos: InvitacionCrearDto): InvitacionEnviadaDto

    @DELETE("familias/mia/invitaciones/{id}")
    suspend fun cancelarInvitacion(@Path("id") id: String)

    // ---------- Invitaciones recibidas ----------
    @GET("invitaciones")
    suspend fun misInvitaciones(): List<InvitacionRecibidaDto>

    @POST("invitaciones/{id}/aceptar")
    suspend fun aceptarInvitacion(@Path("id") id: String): FamiliaDto

    @POST("invitaciones/{id}/rechazar")
    suspend fun rechazarInvitacion(@Path("id") id: String)

    // ---------- Chat ----------
    @POST("chat")
    suspend fun enviarMensajeChat(@Body entrada: ChatMensajeEntradaDto): ChatMensajeRespuestaDto
}