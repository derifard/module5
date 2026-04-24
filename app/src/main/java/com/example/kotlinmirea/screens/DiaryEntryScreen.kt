package com.example.kotlinmirea.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinmirea.viewmodel.DiaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryEntryScreen(
    fileName: String? = null,
    onBack: () -> Unit,
    diaryViewModel: DiaryViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    LaunchedEffect(fileName) {
        if (fileName != null) {
            val (t, b) = diaryViewModel.readEntry(fileName)
            title = t
            text = b
        }
    }

    val isEditing = fileName != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Редактровать" else "Новая запись") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Заголовок (необязательно)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Текст записи") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 250.dp),
                minLines = 10
            )

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        diaryViewModel.saveEntry(title, text)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = text.isNotBlank()
            ) {
                Text("Сохранить запись")
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад")
            }
        }
    }
}