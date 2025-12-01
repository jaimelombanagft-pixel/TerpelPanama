package com.terpel.app.data.datasource

import android.util.Log
import com.terpel.app.api.RetrofitClient
import com.terpel.app.data.model.RegisterRequest
import com.terpel.app.data.model.RegisterResponse
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

class RegisterRemoteDataSource {
    private val apiService = RetrofitClient.getApiService()
    private val TAG = "RegisterRemoteDataSource"

    suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            Log.d(TAG, "Iniciando llamada a registerUser con datos: $request")
            
            val response = apiService.registerUser(request)
            Log.d(TAG, "Respuesta recibida - Código: ${response.code()}, Exitosa: ${response.isSuccessful}")
            
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d(TAG, "Registro exitoso: ${body.message}")
                    Result.success(body)
                } else {
                    val errorMsg = "Response body es null después de recibir código ${response.code()}"
                    Log.e(TAG, errorMsg)
                    Result.failure(Exception(errorMsg))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Sin body"
                val errorMsg = "Error HTTP ${response.code()}: ${response.message()} - Body: $errorBody"
                Log.e(TAG, errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: ConnectException) {
            val errorMsg = "Error de conexión: No se puede conectar al servidor. ¿El servidor está corriendo en 10.0.2.2:3000?"
            Log.e(TAG, errorMsg, e)
            Result.failure(Exception(errorMsg))
        } catch (e: SocketTimeoutException) {
            val errorMsg = "Timeout: El servidor tardó demasiado en responder"
            Log.e(TAG, errorMsg, e)
            Result.failure(Exception(errorMsg))
        } catch (e: IOException) {
            val errorMsg = "Error de red: ${e.message} - Verifica tu conexión de internet"
            Log.e(TAG, errorMsg, e)
            Result.failure(Exception(errorMsg))
        } catch (e: Exception) {
            val errorMsg = "Error inesperado: ${e.javaClass.simpleName} - ${e.message}"
            Log.e(TAG, errorMsg, e)
            Result.failure(Exception(errorMsg))
        }
    }
}
