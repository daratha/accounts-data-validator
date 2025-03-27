package com.example.features.accounting.model

import kotlinx.serialization.Serializable

@Serializable
data class BenfordResult(
    val observedDistribution: Map<Int, Int>,  // Actual percentages
    val expectedDistribution: Map<Int, Int>, // Benford's Law theoretical percentages
    val chiSquareStatistic: Double,
    val criticalValue: Double,
    val isBenfordCompliant: Boolean
)
