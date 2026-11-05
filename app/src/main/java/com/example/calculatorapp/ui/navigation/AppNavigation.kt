package com.example.calculatorapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculatorapp.ui.calculator.CalculatorScreen
import com.example.calculatorapp.ui.history.HistoryScreen
//import com.example.calculatorapp.ui.history.HistoryScreen
import com.example.calculatorapp.viewmodel.CalculatorViewModel


private const val CALCULATOR_ROUTE = "calculator"
private const val HISTORY_ROUTE = "history"

@Composable
fun AppNavigation( viewModel: CalculatorViewModel){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CALCULATOR_ROUTE
    ){
        composable(CALCULATOR_ROUTE){
            CalculatorScreen(
                viewModel = viewModel,
                onHistoryClick = { navController.navigate(HISTORY_ROUTE) }
            )
        }

        composable(HISTORY_ROUTE){
            HistoryScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

    }
}

