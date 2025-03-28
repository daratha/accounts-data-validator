package com.example

import com.example.route.BenfordsRoute
import io.ktor.server.application.*

fun Application.configureRouting() {
    BenfordsRoute().configureRoutes(this)
}

