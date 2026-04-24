package com.example.kotlinmirea.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kotlinmirea.viewmodel.PhotoViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoViewScreen(
    filePath: String,
    onBack: () -> Unit,
    photoViewModel: PhotoViewModel = viewModel()
) {
    val snackbarMessage by photoViewModel.snackbarMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val file = remember { File(filePath) }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            photoViewModel.clearSnackbar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(file.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { photoViewModel.exportToGallery(file) }) {
                        Icon(Icons.Default.Share, contentDescription = "Экспорт в галерею")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = file,
                contentDescription = file.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )

            Button(
                onClick = { photoViewModel.exportToGallery(file) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text("Экспорт в галерею")
            }
        }
    }
}