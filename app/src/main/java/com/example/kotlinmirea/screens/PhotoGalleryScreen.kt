package com.example.kotlinmirea.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kotlinmirea.viewmodel.PhotoViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryScreen(
    onOpenPhoto: (String) -> Unit,
    photoViewModel: PhotoViewModel = viewModel()
) {
    val context = LocalContext.current
    val photos by photoViewModel.photos.collectAsState()
    val snackbarMessage by photoViewModel.snackbarMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        photoViewModel.onPhotoCaptured(success)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = photoViewModel.createPhotoFile()
            cameraLauncher.launch(photoViewModel.getUriForFile(file))
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            photoViewModel.clearSnackbar()
        }
    }

    fun launchCamera() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                val file = photoViewModel.createPhotoFile()
                cameraLauncher.launch(photoViewModel.getUriForFile(file))
            }
            else -> permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Моя галерея") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { launchCamera() }) {
                Icon(Icons.Default.AddAPhoto, contentDescription = "Сделать фото")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (photos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "У вас пока нет фото",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { launchCamera() }) {
                        Icon(Icons.Default.AddAPhoto, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Сделать первое фото")
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(photos, key = { it.absolutePath }) { file ->
                    PhotoItem(
                        file = file,
                        onClick = { onOpenPhoto(file.absolutePath) },
                        onExport = { photoViewModel.exportToGallery(file) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoItem(
    file: File,
    onClick: () -> Unit,
    onExport: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(2.dp)
            .aspectRatio(1f)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = { menuExpanded = true }
                )
        ) {
            AsyncImage(
                model = file,
                contentDescription = file.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        IconButton(
            onClick = { menuExpanded = true },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Меню",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Экспорт в галерею") },
                onClick = {
                    menuExpanded = false
                    onExport()
                }
            )
        }
    }
}