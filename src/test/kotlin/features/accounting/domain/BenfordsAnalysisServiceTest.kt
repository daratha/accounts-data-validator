package features.accounting.domain

import com.example.exceptions.InvalidInputException
import com.example.features.accounting.service.BenfordsAnalysisService
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

private val service: BenfordsAnalysisService = BenfordsAnalysisService()

class BenfordsAnalysisServiceTest {
    @Test
    fun `analyzeAccountingDataByBenfordsLaw should return compliant result for Benford data`() {
        val data = "a:123; b:456; c:789; d:112; e:105; f:200; g:305; h:412"
        val result = service.analyzeAccountingDataByBenfordsLaw(data, 0.05)
        assertTrue(result.isBenfordCompliant)
        assertEquals(30, result.expectedDistribution[1]!!)
        assertTrue(result.chiSquareStatistic < result.criticalValue)
    }

    @Test
    fun `analyzeAccountingDataByBenfordsLaw should return non-compliant for fake data`() {
        val fakeData = "a:900; b:950; c:925; d:975"
        val result = service.analyzeAccountingDataByBenfordsLaw(fakeData, 0.05)
        assertFalse(result.isBenfordCompliant)
        assertTrue(result.chiSquareStatistic > result.criticalValue)
    }

    @Test
    fun `analyzeAccountingDataByBenfordsLaw should handle empty input`() {
        val emptyData = ""
        val significanceLevel = 0.05
        val exception = assertThrows<InvalidInputException> {
            service.analyzeAccountingDataByBenfordsLaw(emptyData, significanceLevel)
        }
        assertEquals("Malformed input data: Data cannot be empty", exception.message)
    }
}