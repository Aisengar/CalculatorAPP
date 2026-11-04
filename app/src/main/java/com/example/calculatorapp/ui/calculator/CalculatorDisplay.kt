package com.example.calculatorapp.ui.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.calculatorapp.R
import com.example.calculatorapp.model.CalculatorOperator
import com.example.calculatorapp.viewmodel.CalculatorError
import com.example.calculatorapp.viewmodel.CalculatorUiState

@Composable
fun CalculatorDisplay(
    uiState: CalculatorUiState,
    modifier: Modifier = Modifier
) {
    val error = uiState.error
    val mainText = if (error != null) errorMessage(error) else uiState.displayedNumber

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        // Texto superior: Muestra la expresión binaria (ej. "3 + 6 =")
        Text(
            text = uiState.expression,
            color = colorResource(R.color.secondary_text),
            fontSize = 24.sp,
            textAlign = TextAlign.End,
            maxLines = 1
        )
        // Texto inferior: Muestra el número actual / resultado (ej. "9")
        Text(
            text = mainText,
            color = colorResource(R.color.display_text),
            fontSize = displayFontSize(mainText),
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.End,
            maxLines = 2
        )
    }
}

@Composable
private fun errorMessage(error: CalculatorError): String {
    val messageResource = when (error) {
        CalculatorError.DIVISION_BY_ZERO -> R.string.error_division_by_zero
    }
    return stringResource(messageResource)
}

private fun displayFontSize(text: String) = when {
    text.length <= 6 -> 80.sp
    text.length <= 9 -> 64.sp
    text.length <= 12 -> 48.sp
    else -> 32.sp
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CalculatorDisplayPreview() {
    CalculatorDisplay(
        uiState = CalculatorUiState(
            displayedNumber = "3",
            previousNumber = "5",
            pendingOperator = CalculatorOperator.ADD
        )
    )
}