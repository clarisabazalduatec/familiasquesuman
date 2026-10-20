package com.example.familiasquesuman.data.remote

import com.example.familiasquesuman.data.remote.dto.NotificacionDto
import com.example.familiasquesuman.data.remote.dto.ResumenNotificacionesDto
import com.example.familiasquesuman.data.remote.dto.AsistenciaGuardarDto
import com.example.familiasquesuman.data.remote.dto.InscripcionAdminDto
import com.example.familiasquesuman.data.remote.dto.InscripcionDto
import com.example.familiasquesuman.data.remote.dto.InscripcionGuardarDto
import com.example.familiasquesuman.data.remote.dto.MiInscripcionDto
import com.example.familiasquesuman.data.remote.dto.ActividadDto
import com.example.familiasquesuman.data.remote.dto.CiudadActualizarDto
import com.example.familiasquesuman.data.remote.dto.CiudadDto
import com.example.familiasquesuman.data.remote.dto.ActividadGuardarDto
import com.example.familiasquesuman.data.remote.dto.CategoriaCrearDto
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
import retrofit2.Response
import com.example.familiasquesuman.data.remote.dto.CentroDto
import com.example.familiasquesuman.data.remote.dto.CentroGuardarDto
import com.example.familiasquesuman.data.remote.dto.PublicacionDto
import com.example.familiasquesuman.data.remote.dto.ComentarioDto
import com.example.familiasquesuman.data.remote.dto.CrearComentarioRequest
import com.example.familiasquesuman.data.remote.dto.CrearPublicacionRequest
import com.example.familiasquesuman.data.remote.dto.CrearAvisoOficialRequest
import com.example.familiasquesuman.data.remote.dto.EncuestaInicialRequest
import com.example.familiasquesuman.data.remote.dto.InsigniaDto
import com.example.familiasquesuman.data.remote.dto.RechazarPublicacionRequest
import com.example.familiasquesuman.domain.EncuestaActividadRequest
import okhttp3.ResponseBody

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

    @PUT("usuarios/me/ciudad")
    suspend fun cambiarMiCiudad(@Body datos: CiudadActualizarDto): PerfilDto

    @Multipart
    @PUT("usuarios/me/foto")
    suspend fun cambiarMiFoto(@Part archivo: MultipartBody.Part): PerfilDto

    @DELETE("usuarios/me/foto")
    suspend fun quitarMiFoto(): PerfilDto

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

    // ---------- Comunidad: imágenes ----------
    @Multipart
    @POST("comunidad/imagenes")
    suspend fun subirImagenComunidad(@Part archivo: MultipartBody.Part): ImagenSubidaDto

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

    // ---------- Ciudades ----------
    @GET("ciudades")
    suspend fun listarCiudades(): List<CiudadDto>

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

    // ---- COMUNIDAD -----
    @GET("comunidad") suspend fun listarPublicacionesComunidad(@Query("limit") limit: Int = 50, @Query("offset") offset: Int = 0): List<PublicacionDto>
    @GET("comunidad/{id}") suspend fun detallePublicacionComunidad(@Path("id") id: String): PublicacionDto
    @GET("comunidad/avisos") suspend fun listarAvisosOficiales(@Query("limit") limit: Int = 50, @Query("offset") offset: Int = 0): List<PublicacionDto>
    @POST("comunidad") suspend fun crearPublicacionComunidad(@Body datos: CrearPublicacionRequest): PublicacionDto
    @DELETE("comunidad/{id}") suspend fun eliminarPublicacion(@Path("id") id: String)

    // ---------- Admin: Avisos Oficiales ----------
    @POST("comunidad/avisos") suspend fun crearAvisoOficial(@Body datos: CrearAvisoOficialRequest): PublicacionDto
    // ---------- LIKES ----------
    @POST("comunidad/{id}/like")  suspend fun darLikePublicacion(@Path("id") id: String)
    @DELETE("comunidad/{id}/like") suspend fun quitarLikePublicacion(@Path("id") id: String)
        // ---------- COMENTARIOS ----------
    @GET("comunidad/{id}/comentarios") suspend fun obtenerComentariosPublicacion(@Path("id") id: String, @Query("limit") limit: Int = 50, @Query("offset") offset: Int = 0): List<ComentarioDto>
    @POST("comunidad/{id}/comentarios") suspend fun publicarComentario(@Path("id") id: String, @Body comentario: CrearComentarioRequest): ComentarioDto
    // ---------- Admin: Moderación de Comunidad ----------
    @GET("comunidad/pendientes") suspend fun listarPublicacionesPendientes(
        @Query("limit") limit: Int = 50, @Query("offset") offset: Int = 0): List<PublicacionDto>
    @POST("comunidad/{id}/aprobar") suspend fun aprobarPublicacion(@Path("id") id: String): PublicacionDto
    @POST("comunidad/{id}/rechazar") suspend fun rechazarPublicacion(
        @Path("id") id: String, @Body datos: RechazarPublicacionRequest): PublicacionDto
    @GET("comunidad/aprobadas") suspend fun listarPublicacionesAprobadas(): List<PublicacionDto>
    @GET("comunidad/rechazadas") suspend fun listarPublicacionesRechazadas(): List<PublicacionDto>
    @GET("comunidad/mias") suspend fun listarMisPublicaciones(
        @Query("limit") limit: Int = 100, @Query("offset") offset: Int = 0): List<PublicacionDto>

    // ---------- Admin: actividades ----------
    @GET("actividades/admin") suspend fun listarActividadesAdmin(@Query("limit") limit: Int = 100): List<ActividadDto>
    @GET("actividades/admin/{id}") suspend fun detalleActividadAdmin(@Path("id") id: String): ActividadDto
    @POST("actividades") suspend fun crearActividad(@Body datos: ActividadGuardarDto): ActividadDto
    @PUT("actividades/{id}") suspend fun editarActividad(@Path("id") id: String, @Body datos: ActividadGuardarDto): ActividadDto
    @DELETE("actividades/{id}") suspend fun borrarActividad(@Path("id") id: String)
    @POST("categorias") suspend fun crearCategoria(@Body datos: CategoriaCrearDto): CategoriaDto

    // ---------- Inscripciones ----------
    // Familia:
    @POST("actividades/{id}/inscripcion")
    suspend fun inscribirActividad(@Path("id") id: String, @Body datos: InscripcionGuardarDto): InscripcionDto

    @PUT("actividades/{id}/inscripcion")
    suspend fun modificarInscripcionActividad(@Path("id") id: String, @Body datos: InscripcionGuardarDto): InscripcionDto

    @GET("actividades/{id}/inscripcion")
    suspend fun miInscripcionActividad(@Path("id") id: String): InscripcionDto

    @DELETE("actividades/{id}/inscripcion")
    suspend fun cancelarInscripcionActividad(@Path("id") id: String)

    @GET("familias/mia/inscripciones")
    suspend fun misInscripcionesFamilia(@Query("incluir_canceladas") incluirCanceladas: Boolean = false): List<MiInscripcionDto>

    // Admin:
    @GET("actividades/{id}/inscripciones")
    suspend fun listarInscripcionesAdmin(@Path("id") id: String): List<InscripcionAdminDto>

    @PUT("actividades/{id}/asistencia")
    suspend fun guardarAsistenciaAdmin(@Path("id") id: String, @Body datos: AsistenciaGuardarDto): List<InscripcionAdminDto>

    @DELETE("actividades/{id}/inscripciones/{inscripcionId}")
    suspend fun borrarInscripcionAdmin(@Path("id") id: String, @Path("inscripcionId") inscripcionId: Any)

    // ---------- Notificaciones ----------
    /** Sin despues_de: las más recientes primero. Con despues_de: solo las nuevas, de la más vieja a la más nueva. */
    @GET("notificaciones")
    suspend fun misNotificaciones(
        @Query("despues_de") despuesDe: Long? = null,
        @Query("limit") limit: Int = 50
    ): List<NotificacionDto>

    @GET("notificaciones/resumen")
    suspend fun resumenNotificaciones(): ResumenNotificacionesDto

    @POST("notificaciones/{id}/leida")
    suspend fun marcarNotificacionLeida(@Path("id") id: Long)

    @POST("notificaciones/leidas")
    suspend fun marcarTodasLeidas()

    // --- Encuesta después de una actividad ---
    @POST("actividades/{actividad_id}/encuesta")
    suspend fun enviarEncuestaActividad(
        @Path("actividad_id") actividadId: String,
        @Body encuesta: EncuestaActividadRequest
    ): Response<Unit>

    @GET("actividades/encuestas/exportar")
    suspend fun exportarEncuestasCsv(): Response<ResponseBody>

    // --- Insignias de actividades ---
    @GET("usuarios/me/insignias")
    suspend fun getMisInsignias(): List<InsigniaDto>

    // --- Encuesta inicial ---
    @POST("actividades/{actividad_id}/encuesta-inicial")
    suspend fun enviarEncuestaInicial(
        @Path("actividad_id") actividadId: String,
        @Body request: EncuestaInicialRequest
    ): Response<ResponseBody>
}
