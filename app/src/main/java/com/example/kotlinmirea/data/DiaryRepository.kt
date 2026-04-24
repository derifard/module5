package com.example.kotlinmirea.data

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiaryRepository(private val context: Context) {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun loadAllEntries(): List<DiaryEntry> {
        return context.filesDir.listFiles()
            ?.filter { it.name.endsWith(".txt") }
            ?.sortedByDescending { it.lastModified() }
            ?.map { file -> fileToEntry(file) }
            ?: emptyList()
    }

    fun saveEntry(title: String, text: String): DiaryEntry {
        val timestamp = System.currentTimeMillis()
        val safeName = if (title.isNotBlank())
            title.trim().replace(Regex("[^a-zA-Zа-яА-Я0-9_]"), "_").take(30)
        else "note"
        val fileName = "timestamp_${timestamp}_${safeName}.txt"
        val file = File(context.filesDir, fileName)
        val content = if (title.isNotBlank()) "$title\n$text" else text
        file.writeText(content)
        return fileToEntry(file)
    }

    fun deleteEntry(fileName: String) {
        File(context.filesDir, fileName).delete()
    }

    fun readEntry(fileName: String): Pair<String, String> {
        val file = File(context.filesDir, fileName)
        val lines = file.readText().lines()
        return if (lines.size > 1) {
            Pair(lines.first(), lines.drop(1).joinToString("\n"))
        } else {
            Pair("", lines.joinToString("\n"))
        }
    }

    private fun fileToEntry(file: File): DiaryEntry {
        val content = file.readText()
        val lines = content.lines()
        val title = if (lines.size > 1) lines.first() else ""
        val body = if (lines.size > 1) lines.drop(1).joinToString("\n") else content
        val preview = body.take(40).replace("\n", " ")
        val date = dateFormat.format(Date(file.lastModified()))
        return DiaryEntry(
            fileName = file.name,
            title = title,
            preview = preview,
            date = date,
            fullText = body
        )
    }
}