package com.example.di

import com.example.service.BenfordsAnalysisService
import org.koin.dsl.module

val appModule = module {
    single { BenfordsAnalysisService() }
}