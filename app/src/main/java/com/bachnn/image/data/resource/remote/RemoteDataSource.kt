package com.bachnn.image.data.resource.remote

import com.bachnn.image.data.models.Image
import com.bachnn.image.data.models.PexelsResponse
import com.bachnn.image.data.resource.AppDataSource
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiInterface: ApiInterface
): AppDataSource.Remote {
    override suspend fun getImages(): PexelsResponse {
        return apiInterface.getImages()
    }
}