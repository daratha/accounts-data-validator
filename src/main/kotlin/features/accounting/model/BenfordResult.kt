package com.example.features.accounting.model

data class BenfordResult(
    val observedDistribution: Map<Int, Double>,  // Actual percentages
    val expectedDistribution: Map<Int, Double>, // Benford's Law theoretical percentages
    val chiSquareStatistic: Double,
    val criticalValue: Double,
    val isBenfordCompliant: Boolean
)
