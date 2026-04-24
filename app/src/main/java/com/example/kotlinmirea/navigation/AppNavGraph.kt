package com.example.kotlinmirea.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinmirea.presentation.ui.screen.TodoDetailScreen
import com.example.kotlinmirea.presentation.ui.screen.TodoListScreen
import com.example.kotlinmirea.presentation.viewmodel.TodosViewModel

@Composable
fun AppNavGraph(viewModel: TodosViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            val todos = viewModel.todos.collectAsState().value
            val highlight = viewModel.highlightCompleted.collectAsState().value
            TodoListScreen(
                todos = todos,
                highlightCompleted = highlight,
                onToggle = { viewModel.onToggle(it) },
                onOpenDetail = { navController.navigate("detail/$it") },
                onAdd = { title, desc -> viewModel.onAdd(title, desc) },
                onDelete = { viewModel.onDelete(it) },
                onHighlightChanged = { viewModel.setHighlightCompleted(it) }
            )
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val item = viewModel.getTodoById(id)
            TodoDetailScreen(
                item = item,
                onBack = { navController.popBackStack() },
                onUpdate = { viewModel.onUpdate(it) }
            )
        }
    }
}