package com.example.exceptions

import io.ktor.http.HttpStatusCode

class InvalidContentTypeException : AccountsValidatorException(
    HttpStatusCode.UnsupportedMediaType,
    "unsupported_media_type",
    "Content-Type must be application/json"
)