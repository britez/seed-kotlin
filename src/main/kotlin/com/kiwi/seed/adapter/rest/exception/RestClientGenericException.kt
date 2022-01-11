package com.kiwi.seed.adapter.rest.exception

import com.kiwi.seed.config.ErrorCode
import com.kiwi.seed.config.exception.GenericException

open class RestClientGenericException(errorCode: ErrorCode, reason: String): GenericException(
    errorCode = errorCode,
    message = reason,
    cause = null) {
}