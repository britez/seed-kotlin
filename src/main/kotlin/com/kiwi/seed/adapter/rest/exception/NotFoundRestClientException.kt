package com.kiwi.seed.adapter.rest.exception

import com.kiwi.seed.config.ErrorCode

class NotFoundRestClientException(errorCode: ErrorCode) : RestClientGenericException(errorCode = errorCode) {

}
