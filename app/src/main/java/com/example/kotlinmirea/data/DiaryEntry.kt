package com.example.kotlinmirea.data

data class DiaryEntry(
    val fileName: String,
    val title: String,
    val preview: String,
    val date: String,
    val fullText: String
)