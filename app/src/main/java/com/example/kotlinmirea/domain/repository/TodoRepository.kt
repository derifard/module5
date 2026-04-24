package com.example.kotlinmirea.domain.repository

import com.example.kotlinmirea.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun seedFromJsonIfEmpty()
    fun getTodos(): Flow<List<TodoItem>>
    suspend fun addTodo(item: TodoItem)
    suspend fun updateTodo(item: TodoItem)
    suspend fun deleteTodo(item: TodoItem)
    suspend fun toggleTodo(id: Int)
}