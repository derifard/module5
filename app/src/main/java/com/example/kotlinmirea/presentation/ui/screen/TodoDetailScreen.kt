package com.example.kotlinmirea.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlinmirea.domain.model.TodoItem

@Composable
fun TodoDetailScreen(
    item: TodoItem?,
    onBack: () -> Unit,
    onUpdate: (TodoItem) -> Unit
) {
    var title by remember(item) { mutableStateOf(item?.title ?: "") }
    var description by remember(item) { mutableStateOf(item?.description ?: "") }
    var editMode by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onBack) { Text("Назад") }
        Spacer(modifier = Modifier.height(16.dp))

        if (item == null) {
            Text("Задача не найдена")
            return@Column
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (editMode) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Название") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Описание") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            onUpdate(item.copy(title = title, description = description))
                            editMode = false
                        }) { Text("Сохранить") }
                        OutlinedButton(onClick = { editMode = false }) { Text("Отмена") }
                    }
                } else {
                    Text(text = title, style = MaterialTheme.typography.titleLarge)
                    Text(text = description)
                    Text(text = "Статус: ${if (item.isCompleted) "Выполнена" else "Не выполнена"}")
                    Button(onClick = { editMode = true }) { Text("Редактировать") }
                }
            }
        }
    }
}