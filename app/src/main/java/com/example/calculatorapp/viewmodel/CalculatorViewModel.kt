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

    fun onDigitClick(digit: Int) {
        val state = currentStateClearingError()
        val digitText = digit.toString()

        val newEnteredNumber = when {
            isNextDigit -> digitText
            state.displayedNumber == "0" -> digitText
            countDigits(state.displayedNumber) >= MAX_DIGITS -> state.displayedNumber
            else -> state.displayedNumber + digitText
        }

        isNextDigit = false

        val previousNumber = state.previousNumber
        val pendingOperator = state.pendingOperator

        if (previousNumber != null && pendingOperator != null) {
            // 1. La expresión superior muestra "3 + 5"
            val updatedExpression = "$previousNumber ${symbolFor(pendingOperator)} $newEnteredNumber"
            // 2. El display inferior calcula y muestra "8"
            val liveResult = calculatePreview(previousNumber, pendingOperator, newEnteredNumber)

            _uiState.value = state.copy(
                displayedNumber = liveResult,
                expression = updatedExpression
            )
        } else {
            _uiState.value = state.copy(
                displayedNumber = newEnteredNumber
            )
        }
    }

    fun onDecimalClick() {
        val state = currentStateClearingError()

        if (!isNextDigit && state.displayedNumber.contains(".")) return

        val newEnteredNumber = if (isNextDigit) "0." else state.displayedNumber + "."
        isNextDigit = false

        val previousNumber = state.previousNumber
        val pendingOperator = state.pendingOperator

        if (previousNumber != null && pendingOperator != null) {
            val updatedExpression = "$previousNumber ${symbolFor(pendingOperator)} $newEnteredNumber"
            val liveResult = calculatePreview(previousNumber, pendingOperator, newEnteredNumber)

            _uiState.value = state.copy(
                displayedNumber = liveResult,
                expression = updatedExpression
            )
        } else {
            _uiState.value = state.copy(
                displayedNumber = newEnteredNumber
            )
        }
    }


    fun onOperatorClick(operator: CalculatorOperator) {
        val state = currentStateClearingError()
        val previousNumber = state.previousNumber
        val pendingOperator = state.pendingOperator
        val isReplacingOperator = pendingOperator != null && isNextDigit

        if (isReplacingOperator) {
            val newExpression = "$previousNumber ${symbolFor(operator)}"
            _uiState.value = state.copy(
                pendingOperator = operator,
                expression = newExpression
            )
            return
        }

        // El resultText es el acumulado anterior
        val currentBaseNumber = state.displayedNumber
        val newExpression = "$currentBaseNumber ${symbolFor(operator)}"

        _uiState.value = state.copy(
            previousNumber = currentBaseNumber,
            pendingOperator = operator,
            expression = newExpression
        )

        repeatOperator = null
        repeatRightNumber = null
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
            expression = "$left ${symbolFor(operator)} $right =",
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

    private fun symbolFor(operator: CalculatorOperator): String {
        return when (operator) {
            CalculatorOperator.ADD -> "+"
            CalculatorOperator.SUBTRACT -> "-"
            CalculatorOperator.MULTIPLY -> "×"
            CalculatorOperator.DIVIDE -> "÷"
        }
    }

    private fun calculatePreview(leftText: String, operator: CalculatorOperator, rightText: String): String {
        val left = BigDecimal(leftText)
        val right = BigDecimal(rightText)
        return try {
            calculatorModel.calculate(left, operator, right).toPlainString()
        } catch (e: Exception) {
            // En caso de división por cero parcial, se mantiene el número actual
            rightText
        }
    }

    private fun countDigits(number: String): Int =
        number.count { it.isDigit() }
}