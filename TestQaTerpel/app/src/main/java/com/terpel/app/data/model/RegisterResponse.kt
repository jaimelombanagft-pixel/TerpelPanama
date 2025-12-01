package com.terpel.app.data.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("statusCode")
    val statusCode: Int = 0,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: RegisterResponseData? = null,

    @SerializedName("success")
    val success: Boolean = false
)

data class RegisterResponseData(
    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("document")
    val document: String? = null,

    @SerializedName("firstName")
    val firstName: String? = null,

    @SerializedName("lastName")
    val lastName: String? = null,

    @SerializedName("status")
    val status: String? = null
)
