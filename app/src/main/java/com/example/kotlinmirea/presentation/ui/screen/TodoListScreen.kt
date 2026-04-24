package com.example.kotlinmirea.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlinmirea.domain.model.TodoItem
import com.example.kotlinmirea.presentation.ui.component.TodoItemRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    todos: List<TodoItem>,
    highlightCompleted: Boolean,
    onToggle: (Int) -> Unit,
    onOpenDetail: (Int) -> Unit,
    onAdd: (String, String) -> Unit,
    onDelete: (TodoItem) -> Unit,
    onHighlightChanged: (Boolean) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo List") },
                actions = {
                    Text("Цвет завершённых", style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(end = 4.dp))
                    Switch(
                        checked = highlightCompleted,
                        onCheckedChange = onHighlightChanged
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить задачу")
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(items = todos, key = { it.id }) { item ->
                TodoItemRow(
                    item = item,
                    highlightCompleted = highlightCompleted,
                    onToggle = onToggle,
                    onClick = onOpenDetail,
                    onDelete = onDelete
                )
            }
        }

        if (showAddDialog) {
            AddTodoDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, desc ->
                    onAdd(title, desc)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddTodoDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новая задача") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (title.isNotBlank()) onConfirm(title, description) },
                enabled = title.isNotBlank()
            ) { Text("Добавить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}