package com.example.features.accounting.model

import kotlinx.serialization.Serializable

@Serializable
data class BenfordRequest(
    val data: String,
    val significanceLevel: Double = 0.05
)
