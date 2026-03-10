package com.example.task10.ui.state

/**
 * Sealed class для представления состояний UI
 * Используется для отображения Loading/Success/Error состояний
 */
sealed class UiState<out T> {
    /**
     * Состояние загрузки данных
     */
    object Loading : UiState<Nothing>()

    /**
     * Состояние успешной загрузки
     * @param data загруженные данные
     */
    data class Success<T>(val data: T) : UiState<T>()

    /**
     * Состояние ошибки
     * @param message сообщение об ошибке для отображения пользователю
     */
    data class Error(val message: String) : UiState<Nothing>()
}
