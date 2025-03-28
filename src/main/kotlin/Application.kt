package com.example

import com.example.di.appModule
import io.ktor.server.application.*
import org.koin.core.context.startKoin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    startKoin {
        modules(appModule)
    }
    configureSerialization()
    configureRouting()
}