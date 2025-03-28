package com.example.exceptions

import io.ktor.http.*

private const val INVALID_INPUT = "invalid_input"

class InvalidInputException(details: String = "") : AccountsValidatorException(
    HttpStatusCode.BadRequest,
    INVALID_INPUT,
    "Malformed input data: $details".trim()

)