package com.kiwi.seed.config.exception

import kotlin.RuntimeException

open class GenericException(
    val errorCode: ErrorCode?,
    message:String?,
    override val cause: Throwable?) : RuntimeException() {
}