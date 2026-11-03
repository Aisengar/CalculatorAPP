package com.example.calculatorapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.calculatorapp.model.CalculatorModel
import com.example.calculatorapp.model.CalculatorOperator
import com.example.calculatorapp.model.DivisionByZeroException
import com.example.calculatorapp.model.HistoryEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal

private const val MAX_DIGITS = 9


class CalculatorViewModel: ViewModel() {

    private val calculatorModel = CalculatorModel()

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private var isNextDigit = false
    private var repeatOperator: CalculatorOperator?= null
    private var repeatRightNumber: String? = null

    fun onDigitClick(digit: Int){
        val state = currentStateClearingError()
        val digitText = digit.toString()

        val newNumber = when {
            isNextDigit -> digitText
            state.displayedNumber =="0" -> digitText
            countDigits(state.displayedNumber) >=  MAX_DIGITS -> state.displayedNumber
            else -> state.displayedNumber + digitText
        }

        isNextDigit = false
        _uiState.value =  state.copy(displayedNumber = newNumber)
    }

    fun onDecimalClick(){
        val state = currentStateClearingError()

        if(!isNextDigit && state.displayedNumber.contains(".")) return

        val newNumber = if (isNextDigit) "0." else state.displayedNumber + "."

        isNextDigit = false
        _uiState.value = state.copy(displayedNumber = newNumber)
    }


    fun onOperatorClick(operator: CalculatorOperator){
        val state = currentStateClearingError()
        val previousNumber =state.previousNumber
        val pendingOperator = state.pendingOperator
        val isReplacingOperator = pendingOperator != null && isNextDigit
        //caso de operacion 1 "+", el operador antes del segundo numero reemplaza operador de manera dinamica
        if(isReplacingOperator){
            _uiState.value = state.copy(pendingOperator = operator)
            return
        }

        if (previousNumber != null && pendingOperator != null){
            val result = calculateOrShowError(previousNumber, pendingOperator, state.displayedNumber) ?: return
            val resultText = result.toPlainString()

            _uiState.value = state.copy(
                displayedNumber = resultText,
                previousNumber = resultText,
                pendingOperator = operator,
                history = addToHistory(previousNumber, pendingOperator, state.displayedNumber, result)
            )
        } else {
            _uiState.value = state.copy(
                previousNumber = state.displayedNumber,
                pendingOperator = operator
            )
        }
        repeatOperator = null
        repeatOperator = null
        isNextDigit = true


    }

    fun onEqualsClick(){
        val state = currentStateClearingError()
        val previousNumber = state.previousNumber
        val pendingOperator = state.pendingOperator
        val savedOperator = repeatOperator
        val savedOperand = repeatRightNumber

        if (previousNumber != null && pendingOperator != null) {
            finishOperation(state, previousNumber, pendingOperator, state.displayedNumber)
        } else if (savedOperator != null && savedOperand != null) {
            finishOperation(state, state.displayedNumber, savedOperator, savedOperand)
        }
    }
    fun onPercentClick() {
        val state = currentStateClearingError()
        val percentage = calculatorModel.toPercentage(BigDecimal(state.displayedNumber))

        _uiState.value = state.copy(displayedNumber = percentage.toPlainString() )
    }

    fun onChangeClick(){
        val state = currentStateClearingError()
        val negate = calculatorModel.changeSigne(BigDecimal(state.displayedNumber))
        _uiState.value = state.copy( displayedNumber = negate.toPlainString())
    }

    fun onClearClick() {
        resetInternalMemory()
        _uiState.value = CalculatorUiState(history = _uiState.value.history)
    }

    private fun finishOperation(
        state: CalculatorUiState,
        left: String,
        operator: CalculatorOperator,
        right: String
    ) {
        val result = calculateOrShowError(left, operator, right) ?: return

        repeatOperator = operator
        repeatRightNumber = right
        isNextDigit = true

        _uiState.value = state.copy(
            displayedNumber = result.toPlainString(),
            previousNumber = null,
            pendingOperator = null,
            history = addToHistory(left, operator, right, result)
        )
    }
    private fun showError(error: CalculatorError){
        resetInternalMemory()
        _uiState.value = CalculatorUiState(error = error, history =  _uiState.value.history)
    }

    private fun addToHistory(leftNumber: String, operator: CalculatorOperator, rightNumber: String, result: BigDecimal): List<HistoryEntry>{
        val newEntry = HistoryEntry(BigDecimal(leftNumber), operator, BigDecimal(rightNumber), result)
        return listOf(newEntry) + _uiState.value.history
    }

    private fun calculateOrShowError(leftNumber:String, operator: CalculatorOperator, rightNumber:String ): BigDecimal?{
        return try{
            calculatorModel.calculate(BigDecimal(leftNumber),operator, BigDecimal(rightNumber))
        }catch (exception: DivisionByZeroException){
            showError(CalculatorError.DIVISION_BY_ZERO)
            null
        }
    }

    private fun currentStateClearingError(): CalculatorUiState{
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
        repeatRightNumber = null
    }

    private fun countDigits(number: String): Int =
        number.count { it.isDigit() }
}