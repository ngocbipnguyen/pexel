package com.bachnn.image.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachnn.image.data.models.CollectionResponse
import com.bachnn.image.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CollectionUiState {
    data class Success(val collection: CollectionResponse) : CollectionUiState
    object Loading : CollectionUiState
    data class Error(val error: String) : CollectionUiState
}

@HiltViewModel
class CollectionViewModel @Inject constructor(
    val repository: ImageRepository
) : ViewModel() {
    var collectionUiState: CollectionUiState by mutableStateOf(CollectionUiState.Loading)
        private set

    init {
        viewModelScope.launch {
            collectionUiState = CollectionUiState.Loading
            try {
                val collectionResponse = repository.getCollection(1, 15)
                collectionResponse.collections.forEach {
                    val collectionById = repository.getCollectionById(it.id)
                    it.media = collectionById.media
                    Log.e("CollectionViewModel", "collectionById : ${collectionById.media.size}")
                    Log.e("CollectionViewModel", "getCollectionById : ${it.media?.size}")
                }
                collectionUiState = CollectionUiState.Success(collectionResponse)
            } catch (e: Exception) {
                collectionUiState = CollectionUiState.Error(e.toString())
            }
        }
    }
}