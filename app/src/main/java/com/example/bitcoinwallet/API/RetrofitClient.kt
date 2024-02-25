package com.example.bitcoinwallet.API

import com.example.bitcoinwallet.API.Services.CurrencyApiService
import com.example.bitcoinwallet.Helpers.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = Constants.BASE_URL

    val currencyApiService: CurrencyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApiService::class.java)
    }
}
