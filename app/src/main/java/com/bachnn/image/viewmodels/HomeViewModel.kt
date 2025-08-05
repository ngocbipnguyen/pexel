package com.bachnn.image.viewmodels

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachnn.image.data.models.Image
import com.bachnn.image.data.models.PexelsPhoto
import com.bachnn.image.data.models.PexelsResponse
import com.bachnn.image.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import javax.inject.Inject

sealed interface ImageUiState {
    data class Success(val images: PexelsResponse) : ImageUiState
    object Loading : ImageUiState
    data class Error(val error: String) : ImageUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: ImageRepository
) : ViewModel() {
    var imageUiState: ImageUiState by mutableStateOf(ImageUiState.Loading)
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var allPhotos = mutableListOf<PexelsPhoto>()
    private var currentPage = 1
    private val perPage = 40

    init {
        loadInitialPhotos()
    }

    private fun loadInitialPhotos() {
        imageUiState = ImageUiState.Loading
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.Main) {
                    repository.getImage(currentPage, perPage)
                }
                allPhotos.clear()
                allPhotos.addAll(response.photos)
                imageUiState = ImageUiState.Success(
                    PexelsResponse(
                        page = response.page,
                        per_page = response.per_page,
                        photos = allPhotos.toList()
                    )
                )
            } catch (e: Exception) {
                imageUiState = ImageUiState.Error(e.toString())
            }
        }
    }

    fun loadMorePhotos() {
        if (_isLoading.value) {
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                currentPage++
                val response = repository.getImage(currentPage, perPage)

                // Add new photos to the existing list
                allPhotos.addAll(response.photos)
                Log.e("loadMorePhotos", "allPhotos : ${allPhotos.size}")

                // Update the state with all accumulated photos
                imageUiState = ImageUiState.Success(
                    PexelsResponse(
                        page = response.page,
                        per_page = response.per_page,
                        photos = allPhotos.toList()
                    )
                )
            } catch (e: Exception) {
                imageUiState = ImageUiState.Error(e.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }
}