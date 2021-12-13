package com.app.expresstaxi.models

data class UsuarioRead(
    val id: Long?,
    val correo: String,
    val rol: String?,
    val tokenfb: String?
)
