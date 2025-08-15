package com.bachnn.image.data.resource

import com.bachnn.image.data.models.CollectionMediaResponse
import com.bachnn.image.data.models.CollectionResponse
import com.bachnn.image.data.models.Image
import com.bachnn.image.data.models.PexelsResponse
import retrofit2.http.Path

interface AppDataSource {

    interface Local {

    }

    interface Remote {

        suspend fun getImages(page : Int = 1, perPage: Int = 20): PexelsResponse

        suspend fun getCollection(page : Int = 1, perPage: Int = 20): CollectionResponse

        suspend fun getCollectionById(
            id: String,
        ): CollectionMediaResponse
    }

}