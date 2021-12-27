package com.kiwi.seed.config

import org.springframework.cloud.sleuth.Tracer
import org.springframework.stereotype.Component
import org.springframework.web.servlet.AsyncHandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TraceSleuthInterceptor(private val tracer: Tracer):AsyncHandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val traceId = tracer
            .currentSpan()
            ?.context()
            ?.traceId()
            ?: TRACE_ID_NOT_EXISTS

        if (!response.headerNames.contains(X_B3_TRACE_ID)) {
            response.addHeader(X_B3_TRACE_ID, traceId)
        }

        val spandId = tracer
            .currentSpan()
            ?.context()
            ?.spanId()
            ?: SPAND_ID_NOT_EXISTS

        if (!response.headerNames.contains(X_B3_SPAN_ID)) {
            response.addHeader(X_B3_SPAN_ID, spandId)
        }

        return true
    }

    companion object {
        const val TRACE_ID_NOT_EXISTS = "Trace id not exists"
        const val SPAND_ID_NOT_EXISTS = "Spand id not exists"
        private const val X_B3_TRACE_ID = "X-B3-TraceId"
        private const val X_B3_SPAN_ID = "X-B3-SpanId"
    }
}