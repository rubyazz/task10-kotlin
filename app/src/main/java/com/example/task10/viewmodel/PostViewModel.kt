package com.example.task10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task10.model.Post
import com.example.task10.repository.PostRepository
import com.example.task10.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel для управления состоянием экрана со списком постов
 * Использует viewModelScope для автоматической отмены корутин
 */
class PostViewModel : ViewModel() {

    private val repository = PostRepository()

    // Внутренний MutableStateFlow для изменения состояния
    private val _state = MutableStateFlow<UiState<List<Post>>>(UiState.Loading)

    // Публичный StateFlow только для чтения
    val state: StateFlow<UiState<List<Post>>> = _state.asStateFlow()

    init {
        loadPosts()
    }

    /**
     * Загрузка постов с использованием viewModelScope и suspend-функции
     */
    fun loadPosts() {
        viewModelScope.launch {
            // Устанавливаем состояние загрузки
            _state.value = UiState.Loading

            // Вызываем suspend-функцию репозитория
            repository.getPosts()
                .onSuccess { posts ->
                    _state.value = UiState.Success(posts)
                }
                .onFailure { exception ->
                    _state.value = UiState.Error(
                        exception.message ?: "Неизвестная ошибка"
                    )
                }
        }
    }

    /**
     * Альтернативный метод с использованием Flow
     */
    fun loadPostsWithFlow() {
        viewModelScope.launch {
            _state.value = UiState.Loading

            repository.getPostsFlow()
                .catch { exception ->
                    _state.value = UiState.Error(
                        exception.message ?: "Ошибка загрузки"
                    )
                }
                .collect { posts ->
                    _state.value = UiState.Success(posts)
                }
        }
    }

    /**
     * Метод для демонстрации обработки ошибок
     */
    fun loadPostsWithError() {
        viewModelScope.launch {
            _state.value = UiState.Loading

            repository.getPostsWithError()
                .onSuccess { posts ->
                    _state.value = UiState.Success(posts)
                }
                .onFailure { exception ->
                    _state.value = UiState.Error(
                        exception.message ?: "Неизвестная ошибка"
                    )
                }
        }
    }
}
