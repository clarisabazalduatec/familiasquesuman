package com.example.familiasquesuman.ui.screens.perfil

import com.example.familiasquesuman.data.remote.dto.FamiliaDto
import com.example.familiasquesuman.data.remote.dto.InvitacionRecibidaDto
import com.example.familiasquesuman.data.remote.dto.MenorDto

/** Qué diálogo está abierto en Perfil (null = ninguno). */
sealed interface DialogoPerfil {
    object CrearFamilia : DialogoPerfil
    object InvitarAdulto : DialogoPerfil
    data class Menor(val menor: MenorDto?) : DialogoPerfil // null = agregar; con valor = editar
    data class BorrarMenor(val menor: MenorDto) : DialogoPerfil
    object MiParentesco : DialogoPerfil
    object SalirDeFamilia : DialogoPerfil
}

data class PerfilUiState(
    val cargando: Boolean = true,
    val errorCarga: String? = null,

    val familia: FamiliaDto? = null,
    val invitacionesRecibidas: List<InvitacionRecibidaDto> = emptyList(),
    val miId: String? = null,
    val miParentesco: String? = null,

    val dialogo: DialogoPerfil? = null,
    val errorDialogo: String? = null,  // error que se muestra dentro del diálogo abierto
    val guardando: Boolean = false,    // una acción está en curso (deshabilita botones)
    val mensaje: String? = null        // aviso corto tras una acción
) {
    val tieneFamilia: Boolean get() = familia != null

    /** Si salgo yo, ¿se borra la familia? */
    val soyUltimoAdulto: Boolean get() = (familia?.adultos?.size ?: 0) <= 1
}