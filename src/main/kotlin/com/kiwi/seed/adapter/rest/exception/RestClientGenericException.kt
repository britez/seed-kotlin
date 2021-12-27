package com.kiwi.seed.adapter.rest.exception

import com.kiwi.seed.config.ErrorCode
import com.kiwi.seed.config.exception.GenericException

open class RestClientGenericException(errorCode: ErrorCode): GenericException(
    errorCode = errorCode,
    message = null,
    cause = null) {
}