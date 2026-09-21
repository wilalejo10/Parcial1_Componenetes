package com.example.parcial_1

object CaseTrackRoutes {
    const val LISTA = "lista"
    const val CREAR = "crear"
    const val EDITAR = "editar/{casoId}"
    const val DETALLE = "detalle/{casoId}"

    fun editar(casoId: Int) = "editar/$casoId"
    fun detalle(casoId: Int) = "detalle/$casoId"
}