package com.example.calculatorapp.model

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class CalculatorModel {
    private val oneHundred = BigDecimal(100)
    private val mc = MathContext(10, RoundingMode.HALF_UP)
    fun calculate(
        leftNumber: BigDecimal,
        operator: CalculatorOperator,
        rightNumber: BigDecimal
    ): BigDecimal{
        val result = when (operator){
            CalculatorOperator.ADD -> leftNumber.add(rightNumber)
            CalculatorOperator.SUBTRACT -> leftNumber.subtract(rightNumber)
            CalculatorOperator.MULTIPLY -> leftNumber.multiply(rightNumber)
            CalculatorOperator.DIVIDE -> divide(leftNumber,rightNumber)
        }
        return result.stripTrailingZeros()
    }

    fun toPercentage(value: BigDecimal): BigDecimal{
        return value.divide(oneHundred).stripTrailingZeros()
    }

    fun changeSigne(value: BigDecimal): BigDecimal{
        return value.negate()
    }
    private fun divide(leftNumber: BigDecimal,rightNumber: BigDecimal): BigDecimal{
        if (rightNumber.compareTo(BigDecimal.ZERO)==0){
            throw DivisionByZeroException()
        }
        val division = leftNumber.divide(rightNumber, mc)
        return division
    }
}