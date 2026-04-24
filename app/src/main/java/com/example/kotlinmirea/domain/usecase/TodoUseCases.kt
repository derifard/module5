package com.example.kotlinmirea.domain.usecase

import com.example.kotlinmirea.domain.model.TodoItem
import com.example.kotlinmirea.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class GetTodosUseCase(private val repository: TodoRepository) {
    operator fun invoke(): Flow<List<TodoItem>> = repository.getTodos()
}

class AddTodoUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(item: TodoItem) = repository.addTodo(item)
}

class UpdateTodoUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(item: TodoItem) = repository.updateTodo(item)
}

class DeleteTodoUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(item: TodoItem) = repository.deleteTodo(item)
}

class ToggleTodoUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(id: Int) = repository.toggleTodo(id)
}

class SeedTodosUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke() = repository.seedFromJsonIfEmpty()
}