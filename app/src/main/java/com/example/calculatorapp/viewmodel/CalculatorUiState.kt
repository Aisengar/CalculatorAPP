package com.example.calculatorapp.viewmodel

import com.example.calculatorapp.model.CalculatorOperator
import com.example.calculatorapp.model.HistoryEntry

data class CalculatorUiState(
    val displayedNumber: String = "0",
    val previousNumber: String? = null,
    val expression: String = "",
    val pendingOperator: CalculatorOperator? = null,
    val error: CalculatorError? = null,
    val history: List<HistoryEntry> = emptyList(),
    val currentInput:String = "",
    val lastOperation: String? = null
)