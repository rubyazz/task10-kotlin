package com.example.task10.repository

import com.example.task10.model.Post
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PostRepository {
    // Simulated data source (в реальном приложении здесь был бы API вызов)
    private val posts = listOf(
        Post(1, "Введение в Kotlin Coroutines", "Kotlin Coroutines позволяют писать асинхронный код в синхронном стиле...", 1),
        Post(2, "Основы Flow", "Flow - это поток данных, который эмитит значения последовательно...", 1),
        Post(3, "StateFlow vs SharedFlow", "StateFlow хранит последнее значение и имеет начальное значение...", 2),
        Post(4, "viewModelScope в действии", "viewModelScope автоматически отменяет корутины при уничтожении ViewModel...", 2),
        Post(5, "Обработка ошибок в Coroutines", "Используйте try-catch или Result для обработки ошибок...", 3),
        Post(6, "Dispatchers explained", "IO - для операций ввода-вывода, Main - для UI, Default - для CPU-интенсивных задач...", 3),
        Post(7, "Cold vs Hot Flows", "Cold Flow начинает работу при подписке, Hot Flow активен всегда...", 4),
        Post(8, "MVVM архитектура", "Model-View-ViewModel разделяет бизнес-логику и UI...", 4),
        Post(9, "Тестирование Coroutines", "Используйте TestDispatcher и runTest для тестирования...", 5),
        Post(10, "Best Practices", "Избегайте GlobalScope, используйте structured concurrency...", 5)
    )

    /**
     * Suspend-функция для асинхронной загрузки постов
     * Возвращает Result для безопасной обработки ошибок
     */
    suspend fun getPosts(): Result<List<Post>> {
        return try {
            // Симуляция сетевой задержки
            delay(1500)
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Flow-версия для реактивного обновления данных
     * Полезно при необходимости множественных эмиссий
     */
    fun getPostsFlow(): Flow<List<Post>> = flow {
        // Симуляция загрузки
        delay(1500)
        emit(posts)
    }

    /**
     * Функция для демонстрации ошибки
     */
    suspend fun getPostsWithError(): Result<List<Post>> {
        delay(1000)
        return Result.failure(Exception("Ошибка загрузки данных: сервер недоступен"))
    }
}
