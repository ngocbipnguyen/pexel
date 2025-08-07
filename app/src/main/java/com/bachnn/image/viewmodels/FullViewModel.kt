package com.bachnn.image.viewmodels

import androidx.lifecycle.ViewModel
import com.bachnn.image.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FullViewModel @Inject constructor(
    val repository: ImageRepository
) : ViewModel() {

    init {

    }
}