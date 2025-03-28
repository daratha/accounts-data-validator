package com.example

import com.example.exceptions.BenfordAnalysisException
import com.example.exceptions.InvalidContentTypeException
import com.example.features.accounting.service.BenfordsAnalysisService
import com.example.features.accounting.model.BenfordRequest
import com.example.util.Constants.BASE_PATH_
import com.example.util.Constants.BENFORDS_ANALYSIS_PATH
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

//val service by inject<BenfordsAnalysisService>()
private val service = BenfordsAnalysisService()


fun Application.configureRouting() {
    routing {
        get ("$BASE_PATH_") {
            call.respondText("Hello, world!")
        }
        post("$BASE_PATH_$BENFORDS_ANALYSIS_PATH") {

            try {
                if (!call.request.isJsonContentType()) {
                    throw InvalidContentTypeException()
                }
                val request = call.receive<BenfordRequest>()



                val result = service.analyzeDataByBenfordsLaw(request.data, request.significanceLevel)
                call.respond(result)
            } catch (ex: IllegalStateException) {
                log.error("Error : " + ex.message);
                call.respond(HttpStatusCode.BadRequest, "Invalid Request")

            } catch (ex: JsonConvertException) {
                log.error("Error : " + ex.message);
                call.respond(HttpStatusCode.BadRequest, "Invalid JSON format")

            } catch (ex: BenfordAnalysisException) {
                log.error("Error : " + ex.printStackTrace());
                call.respond(ex.httpStatusCode, ex.userMessage)

            } catch (ex: Exception) {
                log.error("Error : " + ex.printStackTrace());
                call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
            }
        }
    }
}

fun ApplicationRequest.isJsonContentType(): Boolean {
    return contentType().match(ContentType.Application.Json)
}