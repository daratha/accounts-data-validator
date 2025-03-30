package com.example.service

import com.example.exceptions.InvalidInputException
import com.example.exceptions.StatisticalCalculationException
import com.example.model.BenfordResult
import org.apache.commons.math3.distribution.ChiSquaredDistribution
import java.math.BigDecimal
import java.math.RoundingMode

class BenfordsAnalysisService {
    fun analyzeDataByBenfordsLaw(accountsData: String, significanceLevel: Double): BenfordResult {
        validateInputs(accountsData, significanceLevel)
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

    private fun validateInputs(accountsData: String, significanceLevel: Double) {
        if (accountsData.isEmpty()) {
            throw InvalidInputException("Data cannot be empty")
        }
        if (significanceLevel < 0.01 || significanceLevel > 0.1) {
            throw InvalidInputException(
                "Significance level must be between 0.01 and 0.1 (inclusive)"
            )
        }
    }

    private fun calculateCriticalValue(significanceLevel: Double): Double {
        try {
            val degreesOfFreedom = 8
            val criticalValue = ChiSquaredDistribution(degreesOfFreedom.toDouble())
                .inverseCumulativeProbability(1 - significanceLevel)
            return BigDecimal(criticalValue).setScale(2, RoundingMode.HALF_UP).toDouble()
        } catch (e: Exception) {
            throw StatisticalCalculationException()
        }
    }

    private fun calculateChiSquareStatistic(digitsCounter: IntArray, total: Double): Double {
        val chiSquare = try {
            (1..9).sumOf { digit ->
                val observed = digitsCounter[digit - 1].toDouble()
                val expected = Math.log10(1.0 + 1.0 / digit) * total
                Math.pow(observed - expected, 2.0) / expected
            }
        } catch (e: Exception) {
            throw StatisticalCalculationException()
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