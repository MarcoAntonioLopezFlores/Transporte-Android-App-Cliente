package com.app.expresstaxi.models

import java.util.*

data class Servicio(
    val id:Long?,
    val fechaRegistro: Date?,
    val calificacion: Float?,
    val latitudInicial: Double,
    val longitudInicial: Double,
    val latitudFinal: Double,
    val longitudFinal: Double,
    val estado: Estado?,
    val cliente: Cliente?,
    val conductor: Conductor?
    )