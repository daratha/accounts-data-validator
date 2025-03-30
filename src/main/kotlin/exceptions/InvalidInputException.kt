package com.example.exceptions

import io.ktor.http.*

private const val INVALID_INPUT = "invalid_input"

class InvalidInputException(details: String = "") : BenfordAnalysisException(
    HttpStatusCode.BadRequest,
    INVALID_INPUT,
    "Invalid input data: $details".trim()

)