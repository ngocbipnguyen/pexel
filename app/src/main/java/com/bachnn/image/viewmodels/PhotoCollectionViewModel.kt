package com.bachnn.image.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachnn.image.data.models.PhotoCollectionResponse
import com.bachnn.image.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface PhotoCollectionUiState {
    data class Success(val collection: PhotoCollectionResponse) : PhotoCollectionUiState
    object Loading : PhotoCollectionUiState
    data class Error(val error: String) : PhotoCollectionUiState
}

@HiltViewModel
class PhotoCollectionViewModel @Inject constructor(
    val imageRepository: ImageRepository
) : ViewModel() {

    var photoCollectionUiState: PhotoCollectionUiState by mutableStateOf(PhotoCollectionUiState.Loading)
        private set


    fun loadPhotoCollection(id: String) {
        viewModelScope.launch {
            photoCollectionUiState = PhotoCollectionUiState.Loading
            try {
                val photoCollection = imageRepository.getCollectionById(id = id, page = 1, perPage = 20)
                photoCollectionUiState = PhotoCollectionUiState.Success(photoCollection)
            } catch (e: Exception) {
                photoCollectionUiState = PhotoCollectionUiState.Error(e.toString())
            }
        }
    }


}