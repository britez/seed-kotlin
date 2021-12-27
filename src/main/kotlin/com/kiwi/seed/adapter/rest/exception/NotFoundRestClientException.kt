package com.kiwi.seed.adapter.rest.exception

import com.kiwi.seed.config.exception.ErrorCode

class NotFoundRestClientException(errorCode: ErrorCode) : RestClientGenericException(errorCode = errorCode) {

}
