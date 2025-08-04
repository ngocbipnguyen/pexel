package com.bachnn.image.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachnn.image.data.models.Image
import com.bachnn.image.data.models.PexelsResponse
import com.bachnn.image.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface ImageUiState {
    data class Success(val images: PexelsResponse) : ImageUiState
    object Loading : ImageUiState
    object Error : ImageUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: ImageRepository
) : ViewModel() {
    var imageUiState: ImageUiState by mutableStateOf(ImageUiState.Loading)
        private set

    init {
        imageUiState = ImageUiState.Loading
        viewModelScope.launch {
            val images = withContext(Dispatchers.Main) {
                repository.getImage()
            }
            imageUiState = ImageUiState.Success(images)
        }

    }
}