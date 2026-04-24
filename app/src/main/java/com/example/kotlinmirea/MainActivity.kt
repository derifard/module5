package com.example.kotlinmirea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.kotlinmirea.data.preferences.UserPreferencesRepository
import com.example.kotlinmirea.data.repository.TodoRepositoryImpl
import com.example.kotlinmirea.domain.usecase.*
import com.example.kotlinmirea.navigation.AppNavGraph
import com.example.kotlinmirea.presentation.viewmodel.TodosViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<TodosViewModel> {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val repo = TodoRepositoryImpl(applicationContext)
                val prefsRepo = UserPreferencesRepository(applicationContext)
                @Suppress("UNCHECKED_CAST")
                return TodosViewModel(
                    getTodosUseCase = GetTodosUseCase(repo),
                    addTodoUseCase = AddTodoUseCase(repo),
                    updateTodoUseCase = UpdateTodoUseCase(repo),
                    deleteTodoUseCase = DeleteTodoUseCase(repo),
                    toggleTodoUseCase = ToggleTodoUseCase(repo),
                    seedTodosUseCase = SeedTodosUseCase(repo),
                    preferencesRepository = prefsRepo
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavGraph(viewModel = viewModel)
        }
    }
}