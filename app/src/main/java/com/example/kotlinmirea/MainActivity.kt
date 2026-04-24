package com.example.kotlinmirea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinmirea.ui.screens.DiaryEntryScreen
import com.example.kotlinmirea.ui.screens.DiaryListScreen
import com.example.kotlinmirea.ui.theme.KotlinMIREATheme
import com.example.kotlinmirea.viewmodel.DiaryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinMIREATheme {
                DiaryApp()
            }
        }
    }
}

@Composable
fun DiaryApp() {
    val navController = rememberNavController()
    val diaryViewModel: DiaryViewModel = viewModel()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            DiaryListScreen(
                onNewEntry = { navController.navigate("entry") },
                onOpenEntry = { fileName ->
                    navController.navigate("entry/$fileName")
                },
                diaryViewModel = diaryViewModel
            )
        }
        composable("entry") {
            DiaryEntryScreen(
                fileName = null,
                onBack = { navController.popBackStack() },
                diaryViewModel = diaryViewModel
            )
        }
        composable(
            route = "entry/{fileName}",
            arguments = listOf(navArgument("fileName") { type = NavType.StringType })
        ) { backStackEntry ->
            val fileName = backStackEntry.arguments?.getString("fileName")
            DiaryEntryScreen(
                fileName = fileName,
                onBack = { navController.popBackStack() },
                diaryViewModel = diaryViewModel
            )
        }
    }
}