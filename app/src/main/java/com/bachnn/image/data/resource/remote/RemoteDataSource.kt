package com.bachnn.image.data.resource.remote

import com.bachnn.image.data.models.CollectionMediaResponse
import com.bachnn.image.data.models.CollectionResponse
import com.bachnn.image.data.models.Image
import com.bachnn.image.data.models.PexelsResponse
import com.bachnn.image.data.resource.AppDataSource
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiInterface: ApiInterface
): AppDataSource.Remote {
    override suspend fun getImages(page : Int, perPage: Int): PexelsResponse {
        return apiInterface.getImages(perPage, page)
    }

    override suspend fun getCollection(
        page: Int,
        perPage: Int
    ): CollectionResponse {
        return apiInterface.getCollection(perPage, page)
    }

    override suspend fun getCollectionById(id: String): CollectionMediaResponse {
        return apiInterface.getCollectionById(id)
    }
}