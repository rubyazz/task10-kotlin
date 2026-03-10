package com.example.task10

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task10.databinding.ActivityMainBinding
import com.example.task10.ui.adapter.PostAdapter
import com.example.task10.ui.state.UiState
import com.example.task10.viewmodel.PostViewModel
import kotlinx.coroutines.launch

/**
 * Главный экран приложения
 * Демонстрирует использование Coroutines, Flow и MVVM архитектуры
 */
class MainActivity : AppCompatActivity() {

    // ViewBinding для безопасного доступа к View
    private lateinit var binding: ActivityMainBinding

    // ViewModel с делегатом viewModels() из Activity KTX
    private val viewModel: PostViewModel by viewModels()

    // Адаптер для RecyclerView
    private val adapter = PostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupRetryButton()
        observeState()
    }

    /**
     * Настройка RecyclerView с LinearLayoutManager
     */
    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    /**
     * Настройка кнопки повтора загрузки
     */
    private fun setupRetryButton() {
        binding.btnRetry.setOnClickListener {
            viewModel.loadPosts()
        }
    }

    /**
     * Наблюдение за состоянием UI через StateFlow
     * Используем lifecycleScope и repeatOnLifecycle для безопасного сбора Flow
     */
    private fun observeState() {
        lifecycleScope.launch {
            // Повторяем сбор при переходе в STARTED состояние
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is UiState.Loading -> showLoading()
                        is UiState.Success -> showPosts(state.data)
                        is UiState.Error -> showError(state.message)
                    }
                }
            }
        }
    }

    /**
     * Отображение состояния загрузки
     */
    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.tvError.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE
    }

    /**
     * Отображение успешной загрузки данных
     */
    private fun showPosts(posts: List<com.example.task10.model.Post>) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE
        adapter.submitList(posts)
    }

    /**
     * Отображение ошибки
     */
    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.tvError.visibility = View.VISIBLE
        binding.btnRetry.visibility = View.VISIBLE
        binding.tvError.text = message
    }
}
