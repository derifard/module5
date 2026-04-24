package com.example.kotlinmirea.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kotlinmirea.data.DiaryEntry
import com.example.kotlinmirea.data.DiaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DiaryRepository(application)

    private val _entries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    val entries: StateFlow<List<DiaryEntry>> = _entries

    init {
        _entries.value = repository.loadAllEntries()
    }

    fun saveEntry(title: String, text: String) {
        val newEntry = repository.saveEntry(title, text)
        _entries.value = listOf(newEntry) + _entries.value
    }

    fun deleteEntry(fileName: String) {
        repository.deleteEntry(fileName)
        _entries.value = _entries.value.filter { it.fileName != fileName }
    }

    fun readEntry(fileName: String): Pair<String, String> {
        return repository.readEntry(fileName)
    }
}