package com.example.tempo_isaq.services
import com.example.tempo_isaq.model.Main
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
interface Api {
    @GET("weather")

    fun weatherMap(
        @Query("q") cityName: String,
        @Query("appid") app_id: String

    ):Call<Main>
}