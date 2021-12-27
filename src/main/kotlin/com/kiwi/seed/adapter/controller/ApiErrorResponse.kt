package com.kiwi.seed.adapter.controller

import java.time.LocalDateTime

data class ApiErrorResponse(
    val name:String,
    val status: Int,
    val timestamp: LocalDateTime,
    val code: Int,
    val resource:String,
    val detail:String,
    val metadata: Map<String,String>
) {

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS]['Z']"
    }
}