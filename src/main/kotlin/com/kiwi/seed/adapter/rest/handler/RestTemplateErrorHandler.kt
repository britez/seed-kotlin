package com.kiwi.seed.adapter.rest.handler

import com.kiwi.seed.adapter.rest.exception.RestClientGenericException
import com.kiwi.seed.config.exception.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler

class RestTemplateErrorHandler(private val exceptionsMap:Map<HttpStatus, RuntimeException> ):ResponseErrorHandler {

    override fun hasError(response: ClientHttpResponse): Boolean = response.statusCode.isError

    override fun handleError(response: ClientHttpResponse) = throw exceptionsMap.getOrDefault(response.statusCode,
        RestClientGenericException(ErrorCode.WEB_CLIENT_GENERIC)
    );
}