package com.example.calculatorapp.ui.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.calculatorapp.R
import com.example.calculatorapp.model.CalculatorOperator

private val keySpacing = 12.dp

@Composable
fun CalculatorKeyPad(
    onDigitClick: (Int) -> Unit,
    onDecimalClick: () -> Unit,
    onOperatorClick: (CalculatorOperator) -> Unit,
    onEqualsClick: () -> Unit,
    onPercentClick: () -> Unit,
    onChangeClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier) {
        Row {
            FunctionKey(stringResource(R.string.button_clear), onClearClick)
            FunctionKey(stringResource(R.string.button_change_sign), onChangeClick)
            FunctionKey(stringResource(R.string.button_percent), onPercentClick)
            OperatorKey(stringResource(R.string.button_divide)) { onOperatorClick(CalculatorOperator.DIVIDE) }
        }
        Row{
            NumberKey("7") { onDigitClick(7) }
            NumberKey("8") { onDigitClick(8) }
            NumberKey("9") { onDigitClick(9) }
            OperatorKey(stringResource(R.string.button_multiply)) {onOperatorClick(CalculatorOperator.MULTIPLY)}
        }
        Row{
            NumberKey("4") { onDigitClick(4) }
            NumberKey("5") { onDigitClick(5) }
            NumberKey("6") { onDigitClick(6) }
            OperatorKey(stringResource(R.string.button_subtract)) {onOperatorClick(CalculatorOperator.SUBTRACT)}
        }
        Row{
            NumberKey("1") { onDigitClick(1) }
            NumberKey("2") { onDigitClick(2) }
            NumberKey("3") { onDigitClick(3) }
            OperatorKey(stringResource(R.string.button_add)) {onOperatorClick(CalculatorOperator.ADD)}
        }
        Row{
            NumberKey("0", widthWeight = 2f) { onDigitClick(0) }
            NumberKey(stringResource(R.string.button_decimal), onClick = onDecimalClick)
            OperatorKey(stringResource(R.string.button_equals), onClick =onEqualsClick)
        }
    }
}

@Composable
private fun RowScope.NumberKey(
    text: String,
    widthWeight: Float =1f,
    onClick: () -> Unit,
){
    KeypadKey(
        text = text,
        backgroundColor = colorResource(R.color.button_digit_background),
        colorResource(R.color.button_text_light),
        widthWeight = widthWeight,
        onClick = onClick
    )
}
@Composable
private fun RowScope.FunctionKey(
    text: String,
    onClick: () -> Unit
) {
    KeypadKey(
        text = text,
        textColor = colorResource(R.color.button_text_dark),
        backgroundColor = colorResource(R.color.button_function_background),
        widthWeight = 1f,
        onClick = onClick
    )
}
@Composable
private fun RowScope.OperatorKey(
    text:String,
    onClick: () -> Unit
){
    KeypadKey(
        text = text,
        backgroundColor = colorResource(R.color.button_operator_background),
        textColor = colorResource(R.color.button_text_dark),
        widthWeight = 1f,
        onClick = onClick
    )
}

@Composable
private fun RowScope.KeypadKey(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    widthWeight: Float,
    onClick: () -> Unit
) {
    CalculatorButton(
        text = text,
        backgroundColor = backgroundColor,
        textColor = textColor,
        onClick = onClick,
        modifier = Modifier
            .weight(widthWeight)
            .aspectRatio(widthWeight)
            .padding(keySpacing / 2)
    )
}