package com.example.kotlinmirea.data

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotoRepository(private val context: Context) {

    private val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())

    fun getPhotosDir(): File {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    }

    fun loadAllPhotos(): List<File> {
        return getPhotosDir()
            .listFiles()
            ?.filter { it.extension.lowercase() == "jpg" }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }

    fun createPhotoFile(): File {
        val fileName = "IMG_${dateFormat.format(Date())}.jpg"
        return File(getPhotosDir(), fileName)
    }

    fun getUriForFile(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun exportToGallery(file: File): Boolean {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/KotlinMIREA"
                )
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return false

            context.contentResolver.openOutputStream(uri)?.use { output ->
                file.inputStream().use { input ->
                    input.copyTo(output)
                }
            }

            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(uri, contentValues, null, null)
            true
        } catch (e: Exception) {
            false
        }
    }
}