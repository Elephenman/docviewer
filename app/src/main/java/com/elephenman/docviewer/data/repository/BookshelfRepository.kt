package com.elephenman.docviewer.data.repository

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.elephenman.docviewer.data.model.Document
import com.elephenman.docviewer.data.model.DocumentType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bookshelf")

@Singleton
class BookshelfRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val BOOKSHELF_KEY = stringPreferencesKey("bookshelf_entries")
    }

    data class BookshelfEntry(
        val uri: String,
        val name: String,
        val type: DocumentType,
        val lastOpenedAt: Long
    )

    fun getBookshelf(): Flow<List<BookshelfEntry>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[BOOKSHELF_KEY] ?: "[]"
            parseBookshelfJson(json)
        }
    }

    suspend fun addToBookshelf(document: Document) {
        context.dataStore.edit { preferences ->
            val json = preferences[BOOKSHELF_KEY] ?: "[]"
            val entries = parseBookshelfJson(json).toMutableList()

            entries.removeAll { it.uri == document.uri.toString() }
            entries.add(0, BookshelfEntry(
                uri = document.uri.toString(),
                name = document.name,
                type = document.type,
                lastOpenedAt = System.currentTimeMillis()
            ))

            val limited = entries.take(50)
            preferences[BOOKSHELF_KEY] = toBookshelfJson(limited)
        }
    }

    suspend fun removeFromBookshelf(uri: String) {
        context.dataStore.edit { preferences ->
            val json = preferences[BOOKSHELF_KEY] ?: "[]"
            val entries = parseBookshelfJson(json).filter { it.uri != uri }
            preferences[BOOKSHELF_KEY] = toBookshelfJson(entries)
        }
    }

    private fun parseBookshelfJson(json: String): List<BookshelfEntry> {
        return try {
            val list = mutableListOf<BookshelfEntry>()
            if (json.length <= 2) return list

            val content = json.substring(1, json.length - 1)
            if (content.isEmpty()) return list

            var depth = 0
            var current = StringBuilder()
            val objects = mutableListOf<String>()

            for (char in content) {
                when {
                    char == '{' -> {
                        depth++
                        current.append(char)
                    }
                    char == '}' -> {
                        depth--
                        current.append(char)
                        if (depth == 0) {
                            objects.add(current.toString())
                            current = StringBuilder()
                        }
                    }
                    char == ',' && depth == 0 -> {
                        // skip separator between objects
                    }
                    else -> {
                        if (depth > 0) current.append(char)
                    }
                }
            }

            objects.forEach { obj ->
                try {
                    val uri = extractValue(obj, "uri")
                    val name = extractValue(obj, "name")
                    val typeStr = extractValue(obj, "type")
                    val timeStr = extractValue(obj, "lastOpenedAt")
                    if (uri != null && name != null) {
                        list.add(BookshelfEntry(
                            uri = uri,
                            name = name,
                            type = if (typeStr == "MARKDOWN") DocumentType.MARKDOWN else DocumentType.HTML,
                            lastOpenedAt = timeStr?.toLongOrNull() ?: System.currentTimeMillis()
                        ))
                    }
                } catch (_: Exception) {
                }
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun extractValue(json: String, key: String): String? {
        val pattern = "\"$key\"\\s*:\\s*\"([^\"]*)\"".toRegex()
        return pattern.find(json)?.groupValues?.get(1)
    }

    private fun toBookshelfJson(entries: List<BookshelfEntry>): String {
        val sb = StringBuilder("[")
        entries.forEachIndexed { index, entry ->
            if (index > 0) sb.append(",")
            sb.append("{\"uri\":\"${escapeJson(entry.uri)}\",\"name\":\"${escapeJson(entry.name)}\",\"type\":\"${entry.type}\",\"lastOpenedAt\":${entry.lastOpenedAt}}")
        }
        sb.append("]")
        return sb.toString()
    }

    private fun escapeJson(s: String): String {
        return s.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
}
