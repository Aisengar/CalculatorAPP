package com.example.calculatorapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.calculatorapp.R
import com.example.calculatorapp.model.CalculatorOperator

@Composable
fun operatorSymbol(operation: CalculatorOperator): String {
    val operatorSym = when(operation){
        CalculatorOperator.ADD -> R.string.button_add
        CalculatorOperator.SUBTRACT -> R.string.button_subtract
        CalculatorOperator.MULTIPLY -> R.string.button_multiply
        CalculatorOperator.DIVIDE -> R.string.button_divide
    }
    return stringResource(operatorSym)
}