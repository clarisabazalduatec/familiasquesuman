package com.example.familiasquesuman.data.remote

import com.example.familiasquesuman.data.remote.dto.ActividadDto
import com.example.familiasquesuman.data.remote.dto.CategoriaDto
import com.example.familiasquesuman.data.remote.dto.ChatMensajeEntradaDto
import com.example.familiasquesuman.data.remote.dto.ChatMensajeRespuestaDto
import com.example.familiasquesuman.data.remote.dto.DonacionDto
import com.example.familiasquesuman.data.remote.dto.DonacionGuardarDto
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
import com.example.familiasquesuman.data.remote.dto.ProyectoDto
import retrofit2.http.Query
import com.example.familiasquesuman.data.remote.dto.ImagenSubidaDto
import com.example.familiasquesuman.data.remote.dto.ProyectoGuardarDto
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Header

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

    // ---------- Proyectos ----------
    @GET("proyectos")
    suspend fun listarProyectos(@Query("limit") limit: Int = 100): List<ProyectoDto>

    @GET("proyectos/{id}")
    suspend fun detalleProyecto(@Path("id") id: String): ProyectoDto

    // ---------- Admin: proyectos ----------
    @POST("proyectos")
    suspend fun crearProyecto(@Body datos: ProyectoGuardarDto): ProyectoDto

    @PUT("proyectos/{id}")
    suspend fun editarProyecto(@Path("id") id: String, @Body datos: ProyectoGuardarDto): ProyectoDto

    @DELETE("proyectos/{id}")
    suspend fun borrarProyecto(@Path("id") id: String)

    // ---------- Admin: imágenes ----------
    @Multipart
    @POST("imagenes")
    suspend fun subirImagen(@Part archivo: MultipartBody.Part): ImagenSubidaDto

    // ---------- Chat ----------
    @POST("chat")
    suspend fun enviarMensajeChat(@Body entrada: ChatMensajeEntradaDto): ChatMensajeRespuestaDto

    @GET("actividades")
    suspend fun getActividades(): List<ActividadDto>

    @GET("actividades/{id}")
    suspend fun getActividadDetalle(@Path("id") id: String): ActividadDto

    @GET("categorias")
    suspend fun getCategorias(@Query("ambito") ambito: String? = "ACTIVIDAD"): List<CategoriaDto>

    // ---------- Donaciones ----------
    @GET("donaciones")
    suspend fun listarDonaciones(
        @Query("modalidad") modalidad: String? = null,
        @Query("categoria_id") categoriaId: Int? = null,
        @Query("ciudad_id") ciudadId: Int? = null,
        @Query("incluir_desactivadas") incluirDesactivadas: Boolean? = true
    ): List<DonacionDto>

    @GET("donaciones/{id}")
    suspend fun detalleDonacion(@Path("id") id: String): DonacionDto

    @POST("donaciones")
    suspend fun crearDonacion(@Body datos: DonacionGuardarDto): DonacionDto

    @PUT("donaciones/{id}")
    suspend fun editarDonacion(@Path("id") id: String, @Body datos: DonacionGuardarDto): DonacionDto

    @DELETE("donaciones/{id}")
    suspend fun borrarDonacion(@Path("id") id: String)
}
