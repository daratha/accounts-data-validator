package com.example.exceptions

import io.ktor.http.*

sealed class AccountsValidatorException(
    val httpStatusCode: HttpStatusCode,
    val errorCode: String,
    val userMessage: String) : RuntimeException(userMessage)