package com.terpel.app.data.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RegisterRequest(
    @SerializedName("FirstName")
    val firstName: String,

    @SerializedName("FirstLastName")
    val lastName: String,

    @SerializedName("DocumentTypeName")
    val documentType: String,

    @SerializedName("Document")
    val document: String,

    @SerializedName("Country")
    val country: String = "Panamá",

    @SerializedName("CellPhone")
    val cellPhone: String,

    @SerializedName("Email")
    val email: String,

    @SerializedName("VehicleType")
    val vehicleType: String,

    @SerializedName("AgreedTerms")
    val agreedTerms: Boolean = true,

    @SerializedName("AgreedHD")
    val agreedHD: Boolean = true,

    @SerializedName("HDDate")
    val hdDate: String,

    @SerializedName("HDVersion")
    val hdVersion: String = "1.0",

    @SerializedName("AgreedTC")
    val agreedTC: Boolean = true,

    @SerializedName("TCDate")
    val tcDate: String,

    @SerializedName("TCVersion")
    val tcVersion: String = "1.0",

    @SerializedName("IP")
    val ip: String = "0.0.0.0",

    @SerializedName("Program")
    val program: String = "LoyaltyProgram"
) {
    companion object {
        fun create(
            firstName: String,
            lastName: String,
            documentType: String,
            document: String,
            cellPhone: String,
            email: String,
            vehicleType: String
        ): RegisterRequest {
            val currentDate = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale("es", "ES")).format(Date())
            return RegisterRequest(
                firstName = firstName,
                lastName = lastName,
                documentType = documentType,
                document = document,
                cellPhone = cellPhone,
                email = email,
                vehicleType = vehicleType,
                hdDate = currentDate,
                tcDate = currentDate
            )
        }
    }
}
