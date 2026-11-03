package com.example.calculatorapp.model

import java.math.BigDecimal

data class HistoryEntry (

    val leftNumber: BigDecimal,
    val operator: CalculatorOperator,
    val rightNumber: BigDecimal,
    val result: BigDecimal,
)