package com.example.bitcoinwallet.API.Services

import com.example.bitcoinwallet.API.ResponseModels.CurrencyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CurrencyApiService {

    @GET("latest")
    fun getCurrencyRates(
        @Query("base") baseCurrency: String,
        @Query("symbols") symbols: String,
        @Header("apikey") apiKey: String
    ): Call<CurrencyResponse>
}
