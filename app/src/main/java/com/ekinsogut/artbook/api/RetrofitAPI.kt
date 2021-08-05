package com.ekinsogut.artbook.api

import com.ekinsogut.artbook.model.ImageResponse
import com.ekinsogut.artbook.util.Util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {

    //Pixabay ile görsel arama için retrofit apisi

    @GET("/api/") //Pixabay dökümantasyonu doğrultusunda /api'ye istek atılır
    suspend fun imageSearch(
        @Query("q") searchQuery : String, //Retrofit Query'si, String değer döner
        @Query("key") apiKey : String = API_KEY //Oluşturduğumuz api key'i "key" sorgusuna eşitleme
    ) : Response<ImageResponse> //Fonksiyon'dan dönen response oluşturulan ImageResponse olacak
}

