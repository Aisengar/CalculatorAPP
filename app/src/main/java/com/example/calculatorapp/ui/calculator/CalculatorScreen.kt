package com.example.calculatorapp.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.calculatorapp.R
import com.example.calculatorapp.viewmodel.CalculatorViewModel

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    onHistoryClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.calculator_background))
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        TextButton(
            onClick = onHistoryClick,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = stringResource(R.string.history_title),
                color = colorResource(R.color.button_operator_background)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CalculatorDisplay(
            uiState = uiState,
            modifier = Modifier.fillMaxWidth()
        )

        CalculatorKeyPad(
            onDigitClick = viewModel::onDigitClick,
            onDecimalClick = viewModel::onDecimalClick,
            onOperatorClick = viewModel::onOperatorClick,
            onEqualsClick = viewModel::onEqualsClick,
            onPercentClick = viewModel::onPercentClick,
            onChangeClick = viewModel::onChangeClick,
            onClearClick = viewModel::onClearClick,
        )
    }
}