package com.bachnn.image.data.resource.remote

import com.bachnn.image.data.models.CollectionMediaResponse
import com.bachnn.image.data.models.CollectionResponse
import com.bachnn.image.data.models.Image
import com.bachnn.image.data.models.PexelsResponse
import com.bachnn.image.data.models.PhotoCollectionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface ApiInterface {

    @GET("v1/curated")
    suspend fun getImages(
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): PexelsResponse


    @GET("v1/collections/featured")
    suspend fun getCollection(
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1
    ): CollectionResponse

    @GET("v1/collections/{id}")
    suspend fun getCollectionById(
        @Path("id") id: String,
        @Query("type") type: String = "photos",
        @Query("per_page") perPage: Int = 3
    ): CollectionMediaResponse



    @GET("v1/collections/{id}")
    suspend fun getPhotoCollectionById(
        @Path("id") id: String,
        @Query("type") type: String = "photos",
        @Query("per_page") perPage: Int = 3,
        @Query("page") page : Int = 1,
        @Query("sort") sort: String = "desc"
    ): PhotoCollectionResponse


}