package com.example.kotlinmirea.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmirea.data.preferences.UserPreferencesRepository
import com.example.kotlinmirea.domain.model.TodoItem
import com.example.kotlinmirea.domain.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodosViewModel(
    private val getTodosUseCase: GetTodosUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase,
    private val seedTodosUseCase: SeedTodosUseCase,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val todos: StateFlow<List<TodoItem>> = getTodosUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val highlightCompleted: StateFlow<Boolean> = preferencesRepository.highlightCompleted
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    init {
        viewModelScope.launch { seedTodosUseCase() }
    }

    fun onToggle(id: Int) {
        viewModelScope.launch { toggleTodoUseCase(id) }
    }

    fun onAdd(title: String, description: String) {
        viewModelScope.launch {
            addTodoUseCase(TodoItem(title = title, description = description, isCompleted = false))
        }
    }

    fun onUpdate(item: TodoItem) {
        viewModelScope.launch { updateTodoUseCase(item) }
    }

    fun onDelete(item: TodoItem) {
        viewModelScope.launch { deleteTodoUseCase(item) }
    }

    fun setHighlightCompleted(value: Boolean) {
        viewModelScope.launch { preferencesRepository.setHighlightCompleted(value) }
    }

    fun getTodoById(id: Int): TodoItem? = todos.value.firstOrNull { it.id == id }
}