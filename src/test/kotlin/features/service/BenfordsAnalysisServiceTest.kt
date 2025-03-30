package com.example.features.service

import com.example.exceptions.InvalidInputException
import com.example.service.BenfordsAnalysisService
import io.ktor.http.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

private val service: BenfordsAnalysisService = BenfordsAnalysisService()

class BenfordsAnalysisServiceTest {
    @Test
    fun `analyzeDataByBenfordsLaw should return compliant result for Benford data`() {
        val data = "a:123; b:456; c:789; d:112; e:105; f:200; g:305; h:412"
        val result = service.analyzeDataByBenfordsLaw(data, 0.05)
        assertTrue(result.isBenfordCompliant)
        assertEquals(30, result.expectedDistribution[1]!!)
        assertTrue(result.chiSquareStatistic < result.criticalValue)
    }

    @Test
    fun `analyzeDataByBenfordsLaw should return non-compliant for fake data`() {
        val fakeData = "a:900; b:950; c:925; d:975"
        val result = service.analyzeDataByBenfordsLaw(fakeData, 0.05)
        assertFalse(result.isBenfordCompliant)
        assertTrue(result.chiSquareStatistic > result.criticalValue)
    }

    @Test
    fun `analyzeDataByBenfordsLaw should return 400 for malformed input`() {
        val invalidData = "a:900; b:950; c:925; d:975; invalid format"
        val exception = assertThrows<InvalidInputException> {
            service.analyzeDataByBenfordsLaw(invalidData, 0.05)
        }
        assertEquals(HttpStatusCode.BadRequest, exception.httpStatusCode)
    }

    @Test
    fun `analyzeDataByBenfordsLaw should handle empty input`() {
        val emptyData = ""
        val significanceLevel = 0.05
        val exception = assertThrows<InvalidInputException> {
            service.analyzeDataByBenfordsLaw(emptyData, significanceLevel)
        }
        assertEquals(HttpStatusCode.BadRequest, exception.httpStatusCode)
        assertEquals("Invalid input data: Data cannot be empty", exception.message)
    }

    @Test
    fun `should reject significance level below range`() {
        assertThrows<InvalidInputException> {
            service.analyzeDataByBenfordsLaw("a:123", 0.005)
        }
    }

    @Test
    fun `should reject significance level above range`() {
        assertThrows<InvalidInputException> {
            service.analyzeDataByBenfordsLaw("a:123", 0.2)
        }
    }

    @Test
    fun `should accept boundary values`() {
        assertDoesNotThrow {
            service.analyzeDataByBenfordsLaw("a:123", 0.01)
            service.analyzeDataByBenfordsLaw("a:123", 0.1)
        }
    }
}