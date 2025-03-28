package com.example.features.accounting.service

import com.example.exceptions.InvalidInputException
import com.example.features.accounting.model.BenfordResult
import org.apache.commons.math3.distribution.ChiSquaredDistribution
import java.math.BigDecimal
import java.math.RoundingMode

class BenfordsAnalysisService {
    fun analyzeDataByBenfordsLaw(accountsData: String, significanceLevel: Double): BenfordResult {
        if (accountsData.isEmpty()) {
            throw InvalidInputException("Data cannot be empty")
        }
        val digitsCounter = IntArray(9)
        val totalDigitCount = parseAndCountLeadingDigits(accountsData, digitsCounter)

        val observedDistribution = calculateObservedDistribution(digitsCounter, totalDigitCount)
        val expectedDistribution = calculateExpectedDistribution()

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

    private fun calculateChiSquareStatistic(digitsCounter: IntArray, total: Double): Double {
        val chiSquare = (1..9).sumOf { digit ->
            val observed = digitsCounter[digit - 1].toDouble()
            val expected = Math.log10(1.0 + 1.0 / digit) * total
            Math.pow(observed - expected, 2.0) / expected
        }
        return BigDecimal(chiSquare).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    private fun calculateExpectedDistribution(): Map<Int, Int> {
        return (1..9).associateWith { digit ->
            val distribution = Math.log10(1.0 + 1.0 / digit) * 100
            distribution.toInt()
        }
    }

    private fun calculateObservedDistribution(
        digitsCounter: IntArray,
        totalDigitCount: Double
    ): Map<Int, Int> {
        return digitsCounter.mapIndexed { index, count ->
            index + 1 to ((count * 100.0 / totalDigitCount)).toInt()
        }.toMap()
    }

    private fun parseAndCountLeadingDigits(accountsData: String, digitsCounter: IntArray): Double {
        accountsData.split(";\\s*".toRegex())
            .map { pair ->
                // Validate each pair before processing
                if (!pair.contains(":")) {
                    throw InvalidInputException("Invalid data format in pair: '$pair'. Expected 'key:value'")
                }
                val valuePart = pair.substringAfter(":").trim()
                if (valuePart.isEmpty()) {
                    throw InvalidInputException("Empty value in pair: '$pair'")
                }
                valuePart
            }
            .filter { it.isNotEmpty() }
            .forEach { value ->
                value.firstOrNull { c -> c.isDigit() && c != '0' }?.let { firstDigit ->
                    digitsCounter[firstDigit.digitToInt() - 1]++
                }
            }
        return digitsCounter.sum().toDouble()
    }
}