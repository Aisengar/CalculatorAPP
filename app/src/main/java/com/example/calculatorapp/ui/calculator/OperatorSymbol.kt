package com.example.calculatorapp.ui.calculator

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.calculatorapp.model.CalculatorOperator
import com.example.calculatorapp.R

@Composable
fun operatorSymbol(operator: CalculatorOperator): String {
    val symbolOperator = when(operator){
        CalculatorOperator.ADD -> R.string.button_add
        CalculatorOperator.SUBTRACT -> R.string.button_subtract
        CalculatorOperator.MULTIPLY -> R.string.button_multiply
        CalculatorOperator.DIVIDE -> R.string.button_divide
    }
    return stringResource(symbolOperator)
}