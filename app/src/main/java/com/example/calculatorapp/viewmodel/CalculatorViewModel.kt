package com.example.calculatorapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.calculatorapp.model.CalculatorModel
import com.example.calculatorapp.model.CalculatorOperator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel: ViewModel() {

    private val calculatorModel = CalculatorModel()

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private var isNextDigit = false
    private var repeatOperator: CalculatorOperator?= null
    private var repeatOperand: String? = null

    fun onDigitClick(digit: int){
        val state = currentStateClearingError()
        val digitText = digit.toString()

        val newNumber = when {
            isNextDigit -> digitText
            state.displayedNumber =="0" -> digitText
            countDigits(state.displayedNumber) >=  MAX_DIGITS -> state.displayedNumbe
            else -> state.displayNumber + digitText
        }

        isNextDigit = false
        _uiState.value =  state.copy(displayedNumber = newNumber)
    }

    fun currentStateClearingError(): CalculatorUiState{
        val state = _uiState.value
        if (state.error == null) return state

        resetInternalMemory()
        val cleanState = CalculatorUiState(history = state.history)
        _uiState.value = cleanState
        return cleanState
    }

    private fun resetInternalMemory(){
        isNextDigit = false
        repeatOperator = null
        repeatOperand = null
    }

}