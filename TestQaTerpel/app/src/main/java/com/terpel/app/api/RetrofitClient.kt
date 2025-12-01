package com.terpel.app.api

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Para emulador Android: 10.0.2.2 es el alias para localhost del host
    // Para dispositivo físico: cambiar a la IP del servidor
    private const val BASE_URL = "http://10.0.2.2:3000/services/apexrest/"
    private const val TAG = "RetrofitClient"

    val retrofitInstance: Retrofit by lazy {
        Log.d(TAG, "Inicializando Retrofit con URL: $BASE_URL")
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(): TerpelApiService {
        Log.d(TAG, "Obteniendo servicio de API")
        return retrofitInstance.create(TerpelApiService::class.java)
    }
}
