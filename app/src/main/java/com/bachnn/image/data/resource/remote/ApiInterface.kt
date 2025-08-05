package com.bachnn.image.data.resource.remote

import com.bachnn.image.data.models.Image
import com.bachnn.image.data.models.PexelsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface ApiInterface {

    @GET("v1/curated")
    suspend fun getImages(
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): PexelsResponse

}