package com.kiwi.seed.adapter.controller

import com.kiwi.seed.adapter.rest.exception.NotFoundRestClientException
import com.kiwi.seed.adapter.rest.exception.TimeoutRestClientException
import com.kiwi.seed.config.TraceSleuthInterceptor
import com.kiwi.seed.config.ErrorCode
import com.kiwi.seed.config.exception.GenericException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.sleuth.Tracer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest


@ControllerAdvice
class PokemonErrorHandler(
    @Value("\${spring.profiles.active:}")
    private val activeProfile: String? = null,
    private val httpServletRequest: HttpServletRequest,
    private val tracer: Tracer) {

    private val log = LoggerFactory.getLogger(PokemonErrorHandler::class.java)

    @ExceptionHandler(Throwable::class)
    fun handle(ex: Throwable): ResponseEntity<ApiErrorResponse?>? {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase, ex)
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex, ErrorCode.INTERNAL_ERROR)
    }

    @ExceptionHandler(value = [NotFoundRestClientException::class])
    fun handle(ex: GenericException): ResponseEntity<ApiErrorResponse?>? {
        log.error(HttpStatus.NOT_FOUND.reasonPhrase, ex)
        return buildResponseError(HttpStatus.NOT_FOUND, ex, ex.errorCode)
    }

    @ExceptionHandler(TimeoutRestClientException::class)
    fun handle(ex: TimeoutRestClientException): ResponseEntity<ApiErrorResponse?>? {
        log.error(HttpStatus.REQUEST_TIMEOUT.reasonPhrase, ex)
        return buildResponseError(HttpStatus.REQUEST_TIMEOUT, ex, ex.errorCode)
    }

    private fun buildResponseError(
        httpStatus: HttpStatus,
        ex: Throwable,
        errorCode: ErrorCode
    ): ResponseEntity<ApiErrorResponse?> {
        val debugMessage: String = activeProfile
            .takeIf { it != null && it.contains(PROD_PROFILE) }
            .let { if (it != null) {ex.localizedMessage} else ex.stackTrace.toString() }
        val traceId: String = tracer.currentSpan()?.context()?.traceId() ?: TraceSleuthInterceptor.TRACE_ID_NOT_EXISTS
        val spanId: String = tracer.currentSpan()?.context()?.spanId() ?: TraceSleuthInterceptor.SPAND_ID_NOT_EXISTS
        val queryString: String = httpServletRequest.queryString ?: ""
        val metaData =
            mapOf(
            X_B3_TRACE_ID to traceId,
            X_B3_SPAN_ID to spanId,
            QUERY_STRING to queryString,
            STACK_TRACE to debugMessage)
        val apiErrorResponse = ApiErrorResponse(
            timestamp = LocalDateTime.now(),
            name = httpStatus.reasonPhrase,
            detail = "${ex.javaClass.canonicalName}:${ex.message}",
            status = httpStatus.value(),
            code = errorCode.value,
            resource = httpServletRequest.requestURI,
            metadata = metaData)
        return ResponseEntity(apiErrorResponse, httpStatus)
    }

    companion object {
        private const val X_B3_TRACE_ID = "X-B3-TraceId"
        private const val X_B3_SPAN_ID = "X-B3-SpanId"
        private const val PROD_PROFILE = "prod"
        private const val QUERY_STRING = "query_string"
        private const val STACK_TRACE = "stack_trace"
    }

}