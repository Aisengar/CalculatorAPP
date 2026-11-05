package com.example.calculatorapp.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatorapp.R
import com.example.calculatorapp.model.HistoryEntry
import com.example.calculatorapp.viewmodel.CalculatorViewModel

@Composable
fun HistoryScreen(
    viewModel: CalculatorViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.calculator_background))
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text(
                text = stringResource(R.string.history_back),
                color = colorResource(R.color.button_operator_background)
            )
        }

        Text(
            text = stringResource(R.string.history_title),
            color = colorResource(R.color.display_text),
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState.history.isEmpty()) {
            EmptyHistoryMessage()
        } else {
            HistoryList(entries = uiState.history)
        }
    }
}

@Composable
private fun EmptyHistoryMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.history_empty),
            color = colorResource(R.color.secondary_text),
            fontSize = 18.sp
        )
    }
}

@Composable
private fun HistoryList(entries: List<HistoryEntry>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(entries) { entry ->
            HistoryItem(entry = entry)
        }
    }
}
val CVM = CalculatorViewModel()
@Composable
private fun HistoryItem(entry: HistoryEntry) {
    Text(
        text = stringResource(
            R.string.history_entry,
            entry.leftNumber.toPlainString(),
            CVM.symbolFor(entry.operator),
            entry.rightNumber.toPlainString(),
            entry.result.toPlainString()
        ),
        color = colorResource(R.color.display_text),
        fontSize = 24.sp,
        textAlign = TextAlign.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    )
}