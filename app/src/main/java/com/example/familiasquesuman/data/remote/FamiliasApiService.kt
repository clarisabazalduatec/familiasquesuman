package com.example.familiasquesuman.data.remote

import com.example.familiasquesuman.data.remote.dto.ActividadDto
import com.example.familiasquesuman.data.remote.dto.ActividadGuardarDto
import com.example.familiasquesuman.data.remote.dto.CategoriaCrearDto
import com.example.familiasquesuman.data.remote.dto.CategoriaDto
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
import com.example.familiasquesuman.data.remote.dto.ProyectoDto
import retrofit2.http.Query
import com.example.familiasquesuman.data.remote.dto.ImagenSubidaDto
import com.example.familiasquesuman.data.remote.dto.ProyectoGuardarDto
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Header
import com.example.familiasquesuman.data.remote.dto.CentroDto
import com.example.familiasquesuman.data.remote.dto.CentroGuardarDto

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

    // ---------- Directorio de visiteo ----------
    @GET("centros")
    suspend fun listarCentros(@Query("limit") limit: Int = 100): List<CentroDto>

    @GET("centros/{id}")
    suspend fun detalleCentro(@Path("id") id: String): CentroDto

    // ---------- Admin: directorio ----------
    @GET("centros/admin")
    suspend fun listarCentrosAdmin(): List<CentroDto>

    @GET("centros/admin/{id}")
    suspend fun detalleCentroAdmin(@Path("id") id: String): CentroDto

    @POST("centros")
    suspend fun crearCentro(@Body datos: CentroGuardarDto): CentroDto

    @PUT("centros/{id}")
    suspend fun editarCentro(@Path("id") id: String, @Body datos: CentroGuardarDto): CentroDto

    @DELETE("centros/{id}")
    suspend fun borrarCentro(@Path("id") id: String)

    // ---------- Chat ----------
    @POST("chat")
    suspend fun enviarMensajeChat(@Body entrada: ChatMensajeEntradaDto): ChatMensajeRespuestaDto

    @GET("actividades")
    suspend fun getActividades(): List<ActividadDto>

    @GET("actividades/{id}")
    suspend fun getActividadDetalle(@Path("id") id: String): ActividadDto

    @GET("categorias")
    suspend fun getCategorias(@Query("ambito") ambito: String? = "ACTIVIDAD"): List<CategoriaDto>

    // ---------- Admin: actividades ----------
    @GET("actividades/admin")
    suspend fun listarActividadesAdmin(@Query("limit") limit: Int = 100): List<ActividadDto>

    @GET("actividades/admin/{id}")
    suspend fun detalleActividadAdmin(@Path("id") id: String): ActividadDto

    @POST("actividades")
    suspend fun crearActividad(@Body datos: ActividadGuardarDto): ActividadDto

    @PUT("actividades/{id}")
    suspend fun editarActividad(@Path("id") id: String, @Body datos: ActividadGuardarDto): ActividadDto

    @DELETE("actividades/{id}")
    suspend fun borrarActividad(@Path("id") id: String)

    @POST("categorias")
    suspend fun crearCategoria(@Body datos: CategoriaCrearDto): CategoriaDto
}
