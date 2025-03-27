package com.example.features.accounting.domain

import com.example.features.accounting.model.BenfordResult

interface AccountingService {
    fun analyzeAccountingDataByBenfordsLaw(accountsData: String, significanceLevel: Double): BenfordResult
}