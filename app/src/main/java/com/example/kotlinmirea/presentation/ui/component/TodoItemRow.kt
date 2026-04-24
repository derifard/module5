package com.example.kotlinmirea.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.kotlinmirea.domain.model.TodoItem

@Composable
fun TodoItemRow(
    item: TodoItem,
    highlightCompleted: Boolean,
    onToggle: (Int) -> Unit,
    onClick: (Int) -> Unit,
    onDelete: (TodoItem) -> Unit
) {
    val backgroundColor = if (item.isCompleted && highlightCompleted)
        Color(0xFFB2DFDB) else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick(item.id) },
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
            }
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggle(item.id) },
                modifier = Modifier.testTag("todo_checkbox")
            )
            IconButton(onClick = { onDelete(item) }) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить")
            }
        }
    }
}