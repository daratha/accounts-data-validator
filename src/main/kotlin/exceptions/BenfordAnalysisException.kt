package com.example.exceptions

import io.ktor.http.*

sealed class BenfordAnalysisException(
    val httpStatusCode: HttpStatusCode,
    val errorCode: String,
    val userMessage: String) : RuntimeException(userMessage)