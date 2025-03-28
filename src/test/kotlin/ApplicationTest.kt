package com.example

import com.example.features.accounting.model.BenfordResult
import com.example.util.Constants.BASE_PATH_
import com.example.util.Constants.BENFORDS_ANALYSIS_PATH
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import kotlin.test.*

class ApplicationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `should return valid Benford analysis and isBenfordCompliant is true for valid input format`() = testApplication {
        application {
            module()
        }

        val testData = "inv_001:1230.00; inv_002:1050.00; inv_003:1875.50; inv_004:1123.20; exp_utilities:1560.00; " +
                "refund_01:115.75; deposit_01:1980.00; inv_005:2267.50; exp_software:2450.00; salary_01:2890.00; " +
                "tax_vat:235.60; inv_006:3199.99; payment_03:3214.00; consulting:3250.00; exp_marketing:450.00; " +
                "fee_bank:415.20; exp_office:567.89; adjustment_01:523.45; exp_travel:632.00; misc_01:678.10; " +
                "payment_02:721.15; refund_02:875.50; insurance:912.00"

        val response = client.post("$BASE_PATH_$BENFORDS_ANALYSIS_PATH") {
            contentType(ContentType.Application.Json)
            setBody("""{"data": "$testData", "significanceLevel": 0.05}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val result = json.decodeFromString<BenfordResult>(response.bodyAsText())

        assertEquals(30, result.expectedDistribution[1]!!)
        assertTrue(result.observedDistribution.isNotEmpty())
        assertTrue(result.expectedDistribution.isNotEmpty())
        assertNotNull(result.chiSquareStatistic)
        assertTrue(result.isBenfordCompliant)
    }

    @Test
    fun `should return valid Benford analysis and isBenfordCompliant is false for valid input format`() = testApplication {
        application {
            module()
        }
        val testData = "inv_001:9230.00; inv_002:9050.00; inv_003:9875.50; inv_004:9123.20; exp_utilities:9560.00; " +
                "refund_01:115.75; deposit_01:9980.00; inv_005:2267.50; exp_software:2450.00; salary_01:2890.00; " +
                "tax_vat:235.60; inv_006:3199.99; payment_03:3214.00; consulting:3250.00; exp_marketing:450.00; " +
                "fee_bank:415.20; exp_office:567.89; adjustment_01:523.45; exp_travel:632.00; misc_01:678.10; " +
                "payment_02:721.15; refund_02:875.50; insurance:912.00"
        val response = client.post("$BASE_PATH_$BENFORDS_ANALYSIS_PATH") {
            contentType(ContentType.Application.Json)
            setBody("""{"data": "$testData", "significanceLevel": 0.05}""")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val result = json.decodeFromString<BenfordResult>(response.bodyAsText())
        assertTrue(result.observedDistribution.isNotEmpty())
        assertTrue(result.expectedDistribution.isNotEmpty())
        assertNotNull(result.chiSquareStatistic)
        assertFalse(result.isBenfordCompliant)
    }

    @Test
    fun `should return 415 for unsupported content type`() = testApplication {
        application { module() }
        val response = client.post("$BASE_PATH_$BENFORDS_ANALYSIS_PATH") {
            contentType(ContentType.Text.Plain)
            setBody("raw text")
        }
        assertEquals(HttpStatusCode.UnsupportedMediaType, response.status)
    }

    @Test
    fun `should return 400 for malformed input`() = testApplication {
        application { module() }
        val response = client.post("$BASE_PATH_$BENFORDS_ANALYSIS_PATH") {
            contentType(ContentType.Application.Json)
            setBody("""{"data": "invalid format", "significanceLevel": 0.05}""")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

}
