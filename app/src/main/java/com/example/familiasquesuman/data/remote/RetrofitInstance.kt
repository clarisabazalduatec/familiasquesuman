package com.example.familiasquesuman.data.remote

import com.example.familiasquesuman.data.SesionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Emulador: 10.0.2.2 apunta al localhost de tu computadora.
    private const val BASE_URL = "http://localhost:8000/"

    /** Agrega el token a todas las peticiones; si el backend responde 401, cierra la sesión. */
    private class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = SesionManager.token
            val original = chain.request()
            val request = if (token != null && original.header("Authorization") == null) {
                original.newBuilder().header("Authorization", "Bearer $token").build()
            } else {
                original
            }

            val respuesta = chain.proceed(request)

            val esAuth = original.url.encodedPath.startsWith("/auth/")
            if (respuesta.code == 401 && token != null && !esAuth && SesionManager.token == token) {
                SesionManager.cerrarSesion()
            }
            return respuesta
        }
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    val api: FamiliasApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FamiliasApiService::class.java)
    }
}