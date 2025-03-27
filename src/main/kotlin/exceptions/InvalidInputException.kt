package com.example.exceptions

import io.ktor.http.*

class InvalidInputException(details: String = "") : AccountsValidatorException(
    HttpStatusCode.BadRequest,
    "invalid_input",
    "Malformed input data. $details".trim()
)