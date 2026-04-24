package com.example.kotlinmirea.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Query("SELECT COUNT(*) FROM todos")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todos: List<TodoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity)

    @Update
    suspend fun update(todo: TodoEntity)

    @Delete
    suspend fun delete(todo: TodoEntity)

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getById(id: Int): TodoEntity?
}