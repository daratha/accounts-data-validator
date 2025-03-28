package com.example.route

import com.example.exceptions.BenfordAnalysisException
import com.example.exceptions.InvalidContentTypeException
import com.example.model.BenfordRequest
import com.example.service.BenfordsAnalysisService
import com.example.util.Constants.BASE_PATH_
import com.example.util.Constants.BENFORDS_ANALYSIS_PATH
import com.example.util.Constants.V1_PATH_
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BenfordsRoute : KoinComponent {

    val service: BenfordsAnalysisService by inject()

    fun configureRoutes(application: Application) {
        application.routing {
            //  /api/v1/analysis/benfords
            post("$BASE_PATH_$V1_PATH_$BENFORDS_ANALYSIS_PATH") {
                try {
                    if (!call.request.isJsonContentType()) {
                        throw InvalidContentTypeException()
                    }
                    val request = call.receive<BenfordRequest>()
                    val result = service.analyzeDataByBenfordsLaw(request.data, request.significanceLevel)
                    call.respond(result)
                } catch (ex: IllegalStateException) {
//                    log.error("Error : " + ex.message);
                    ex.printStackTrace()
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")

                } catch (ex: JsonConvertException) {
//                    log.error("Error : " + ex.message);
                    call.respond(HttpStatusCode.BadRequest, "Invalid JSON format")

                } catch (ex: BenfordAnalysisException) {
//                    log.error("Error : " + ex.printStackTrace());
                    call.respond(ex.httpStatusCode, ex.userMessage)

                } catch (ex: Exception) {
//                    log.error("Error : " + ex.printStackTrace());
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                }
            }
        }
    }

    fun ApplicationRequest.isJsonContentType(): Boolean {
        return contentType().match(ContentType.Application.Json)
    }
}