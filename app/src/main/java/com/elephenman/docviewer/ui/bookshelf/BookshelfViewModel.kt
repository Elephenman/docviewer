package com.elephenman.docviewer.ui.bookshelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elephenman.docviewer.data.repository.BookshelfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookshelfViewModel @Inject constructor(
    private val bookshelfRepository: BookshelfRepository
) : ViewModel() {

    private val _bookshelfEntries = MutableStateFlow<List<BookshelfRepository.BookshelfEntry>>(emptyList())
    val bookshelfEntries: StateFlow<List<BookshelfRepository.BookshelfEntry>> = _bookshelfEntries.asStateFlow()

    init {
        viewModelScope.launch {
            bookshelfRepository.getBookshelf().collect { entries ->
                _bookshelfEntries.value = entries
            }
        }
    }

    fun removeFromBookshelf(uri: String) {
        viewModelScope.launch {
            bookshelfRepository.removeFromBookshelf(uri)
        }
    }
}
