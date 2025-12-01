package com.terpel.app.api

import com.terpel.app.data.model.RegisterRequest
import com.terpel.app.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TerpelApiService {
    @POST("ClientManagement/v2/")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>
}
