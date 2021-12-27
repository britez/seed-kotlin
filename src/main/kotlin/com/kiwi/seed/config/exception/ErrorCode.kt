package com.kiwi.seed.config.exception

enum class ErrorCode(val value:Int, val reason:String) {

    INTERNAL_ERROR(100, "Error interno del servidor"),
    WEB_CLIENT_GENERIC(101, "Error del cliente http"),
    POKEMON_NOT_FOUND(200, "Pokemon no encontrado"),
    POKEMON_TIMEOUT(201, "Timeout invocando a servicio"),
}