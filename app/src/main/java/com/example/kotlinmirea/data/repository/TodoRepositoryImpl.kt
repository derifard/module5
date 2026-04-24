package com.example.kotlinmirea.data.repository

import android.content.Context
import com.example.kotlinmirea.data.local.AppDatabase
import com.example.kotlinmirea.data.local.TodoEntity
import com.example.kotlinmirea.data.model.TodoItemDto
import com.example.kotlinmirea.domain.model.TodoItem
import com.example.kotlinmirea.domain.repository.TodoRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(private val context: Context) : TodoRepository {

    private val dao = AppDatabase.getInstance(context).todoDao()
    private val gson = Gson()

    // Разово импортировать задачи из JSON если БД пустая
    override suspend fun seedFromJsonIfEmpty() {
        if (dao.count() == 0) {
            val json = context.assets.open("todos.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<TodoItemDto>>() {}.type
            val dtos: List<TodoItemDto> = gson.fromJson(json, type)
            val entities = dtos.map {
                TodoEntity(id = it.id, title = it.title, description = it.description, isCompleted = it.isCompleted)
            }
            dao.insertAll(entities)
        }
    }

    override fun getTodos(): Flow<List<TodoItem>> {
        return dao.getAllTodos().map { list ->
            list.map { TodoItem(it.id, it.title, it.description, it.isCompleted) }
        }
    }

    override suspend fun addTodo(item: TodoItem) {
        dao.insert(TodoEntity(title = item.title, description = item.description, isCompleted = item.isCompleted))
    }

    override suspend fun updateTodo(item: TodoItem) {
        dao.update(TodoEntity(id = item.id, title = item.title, description = item.description, isCompleted = item.isCompleted))
    }

    override suspend fun deleteTodo(item: TodoItem) {
        dao.delete(TodoEntity(id = item.id, title = item.title, description = item.description, isCompleted = item.isCompleted))
    }

    override suspend fun toggleTodo(id: Int) {
        val entity = dao.getById(id) ?: return
        dao.update(entity.copy(isCompleted = !entity.isCompleted))
    }
}