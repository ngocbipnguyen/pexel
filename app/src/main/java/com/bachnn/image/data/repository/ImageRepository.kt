package com.bachnn.image.data.repository

import android.content.Context
import com.bachnn.image.data.models.CollectionMediaResponse
import com.bachnn.image.data.models.CollectionResponse
import com.bachnn.image.data.models.Image
import com.bachnn.image.data.models.PexelsResponse
import com.bachnn.image.data.models.PhotoCollectionResponse
import com.bachnn.image.data.resource.remote.RemoteDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class ImageRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun getImage(page : Int = 1, perPage: Int = 20): PexelsResponse {
        return remoteDataSource.getImages(page, perPage)
    }

    suspend fun getCollection(
        page: Int,
        perPage: Int
    ): CollectionResponse {
        return remoteDataSource.getCollection(page, perPage)
    }

    suspend fun getCollectionById(id: String): CollectionMediaResponse {
        return remoteDataSource.getCollectionById(id)
    }


    suspend fun getCollectionById(id: String, page: Int, perPage: Int): PhotoCollectionResponse {
        return remoteDataSource.getPhotoCollectionById(id, page, perPage)
    }

}