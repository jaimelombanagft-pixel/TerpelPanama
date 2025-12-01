package com.terpel.app.data.repository

import com.terpel.app.data.datasource.RegisterRemoteDataSource
import com.terpel.app.data.model.RegisterRequest
import com.terpel.app.data.model.RegisterResponse

class RegisterRepository {
    private val remoteDataSource = RegisterRemoteDataSource()

    suspend fun registerUser(
        firstName: String,
        lastName: String,
        documentType: String,
        document: String,
        cellPhone: String,
        email: String,
        vehicleType: String
    ): Result<RegisterResponse> {
        return try {
            val request = RegisterRequest.create(
                firstName = firstName,
                lastName = lastName,
                documentType = documentType,
                document = document,
                cellPhone = cellPhone,
                email = email,
                vehicleType = vehicleType
            )
            remoteDataSource.registerUser(request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
