package com.example.exceptions

import io.ktor.http.*

class StatisticalCalculationException : BenfordAnalysisException (
    HttpStatusCode.InternalServerError,
    "statistical_calculation_exception",
    "Error in statistical calculation"
)