package com.bachnn.image.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachnn.image.data.models.PhotoCollectionResponse
import com.bachnn.image.data.models.PhotoMediaItem
import com.bachnn.image.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    private var currentPage = 1
    private val perPage = 20

    lateinit var id: String

    private var collectionPhotos = mutableListOf<PhotoMediaItem>()

    fun loadPhotoCollection(id: String) {
        this.id = id
        viewModelScope.launch {
            photoCollectionUiState = PhotoCollectionUiState.Loading
            collectionPhotos.clear()
            try {
                val photoCollection = imageRepository.getCollectionById(id = id, page = 1, perPage = 20)
                collectionPhotos.addAll(photoCollection.media)
                photoCollectionUiState = PhotoCollectionUiState.Success(
                    PhotoCollectionResponse(
                        id = photoCollection.id,
                        media = collectionPhotos.toList()
                    )
                )
            } catch (e: Exception) {
                photoCollectionUiState = PhotoCollectionUiState.Error(e.toString())
            }
        }
    }

    fun loadMore() {
        Log.e("loadMore", "loadMore ${_isLoading.value}")
        if (_isLoading.value) {
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                currentPage++
                val collectionPhotoResponse = imageRepository.getCollectionById(id = id, page = currentPage, perPage = perPage)
                collectionPhotos.addAll(collectionPhotoResponse.media)
                Log.e("loadMore", "collectionPhotos ${collectionPhotos.size}")
                photoCollectionUiState = PhotoCollectionUiState.Success(
                    PhotoCollectionResponse(
                        id = collectionPhotoResponse.id,
                        media = collectionPhotos.toList()
                    )
                )
            } catch (e: Exception) {
                Log.e("loadMore", "exception : ${e.toString()}")
                photoCollectionUiState = PhotoCollectionUiState.Error(e.toString())
            } finally {
                _isLoading.value = false
                Log.e("loadMore", "finally ${_isLoading.value}")
            }
        }
    }


}