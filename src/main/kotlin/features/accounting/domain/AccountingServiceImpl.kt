package com.example.features.accounting.domain

import com.example.exceptions.InvalidInputException
import com.example.features.accounting.model.BenfordResult
import org.apache.commons.math3.distribution.ChiSquaredDistribution
import java.math.BigDecimal
import java.math.RoundingMode

class AccountingServiceImpl : AccountingService {
    override fun analyzeAccountingDataByBenfordsLaw(accountsData: String, significanceLevel: Double): BenfordResult {
        if (accountsData.isEmpty()) {
            throw InvalidInputException("Accounting data cannot be empty")
        }
        val digitsCounter = IntArray(9)

        // 1. Parse and count leading digits
        val totalDigitCount = parseAndCountLeadingDigits(accountsData, digitsCounter)

        // 2. Calculate distributions
        val observedDistribution = calculateObservedDistribution(digitsCounter, totalDigitCount)
        val expectedDistribution = calculateExpectedDistribution()

        // 3. Chi-square Statistics
        val chiSquare = calculateChiSquareStatistic(digitsCounter, totalDigitCount)
        val criticalValue = calculateCriticalValue(significanceLevel)

        return BenfordResult(
            observedDistribution = observedDistribution,
            expectedDistribution = expectedDistribution,
            chiSquareStatistic = chiSquare,
            criticalValue = criticalValue,
            isBenfordCompliant = chiSquare <= criticalValue
        )
    }

    private fun calculateCriticalValue(significanceLevel: Double): Double {
        val degreesOfFreedom = 8
        val criticalValue = ChiSquaredDistribution(degreesOfFreedom.toDouble())
            .inverseCumulativeProbability(1 - significanceLevel)
        return BigDecimal(criticalValue).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    /* Calculates the chi-square statistic for Benford's Law compliance testing.
     *
     * @param digitsCounter Array of observed counts for digits 1-9
     * @param total Total number of observations
     * @return Calculated chi-square statistic value
     */
    private fun calculateChiSquareStatistic(digitsCounter: IntArray, total: Double): Double {
        val chiSquare = (1..9).sumOf { digit ->
            val observed = digitsCounter[digit - 1].toDouble()
            val expected = Math.log10(1.0 + 1.0 / digit) * total
            Math.pow(observed - expected, 2.0) / expected
        }
        return BigDecimal(chiSquare).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    private fun calculateExpectedDistribution(): Map<Int, Int> {
        val expectedDistribution = (1..9).associateWith { digit ->
            val distribution = Math.log10(1.0 + 1.0 / digit) * 100 // Convert to percentage
            distribution.toInt()
        }
        return expectedDistribution
    }

    private fun calculateObservedDistribution(
        digitsCounter: IntArray,
        totalDigitCount: Double
    ): Map<Int, Int> {
        val observedDistribution = digitsCounter.mapIndexed { index, count ->
            index + 1 to ((count * 100.0 / totalDigitCount)).toInt()
        }.toMap()
        return observedDistribution
    }

    private fun parseAndCountLeadingDigits(accountsData: String, digitsCounter: IntArray): Double {
        accountsData.split(";\\s*".toRegex())
            .map { it.substringAfter(":").trim() }
            .filter { it.isNotEmpty() }
            .forEach { value ->
                value.firstOrNull { c -> c.isDigit() && c != '0' }?.let { firstDigit ->
                    digitsCounter[firstDigit.digitToInt() - 1]++
                }
            }

        val totalDigitCount = digitsCounter.sum().toDouble()
        return totalDigitCount
    }
}