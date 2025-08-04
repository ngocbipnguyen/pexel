package com.bachnn.image.data.resource.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class PexelInterceptor(val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader("Authorization", apiKey).build()
        return chain.proceed(request)
    }
}