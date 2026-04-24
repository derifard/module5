package com.example.kotlinmirea.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.example.kotlinmirea.data.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class PhotoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PhotoRepository(application)

    private val _photos = MutableStateFlow<List<File>>(emptyList())
    val photos: StateFlow<List<File>> = _photos

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    private var pendingPhotoFile: File? = null

    init {
        _photos.value = repository.loadAllPhotos()
    }

    fun createPhotoFile(): File {
        val file = repository.createPhotoFile()
        pendingPhotoFile = file
        return file
    }

    fun getUriForFile(file: File): Uri {
        return repository.getUriForFile(file)
    }

    fun onPhotoCaptured(success: Boolean) {
        if (success) {
            pendingPhotoFile?.let { file ->
                if (file.exists()) {
                    _photos.value = listOf(file) + _photos.value
                }
            }
        }
        pendingPhotoFile = null
    }

    fun exportToGallery(file: File) {
        val success = repository.exportToGallery(file)
        _snackbarMessage.value = if (success) "Фото добавлено в галерею" else "Ошибка экспорта"
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}