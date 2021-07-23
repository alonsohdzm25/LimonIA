package com.limonia.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://limonia.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}