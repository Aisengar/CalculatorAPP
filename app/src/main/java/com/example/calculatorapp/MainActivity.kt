package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.calculatorapp.ui.calculator.CalculatorScreen
import com.example.calculatorapp.ui.theme.CalculatorAppTheme
import com.example.calculatorapp.viewmodel.CalculatorViewModel
class MainActivity : ComponentActivity() {
    private val viewModel: CalculatorViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorAppTheme {
                CalculatorScreen(
                    viewModel = viewModel,
                    onHistoryClick = {}
                )
            }
        }
    }
}


