package com.bachnn.image.data.repository

import android.content.Context
import com.bachnn.image.data.models.Image
import com.bachnn.image.data.models.PexelsResponse
import com.bachnn.image.data.resource.remote.RemoteDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class ImageRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun getImage(): PexelsResponse {
        return remoteDataSource.getImages()
    }
}