package com.example.bitcoinwallet.API.Services

import com.example.bitcoinwallet.API.ResponseModels.CurrencyResponse
import com.example.bitcoinwallet.API.ResponseModels.FluctuationResponse
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

    @GET("fluctuation")
    fun getFluctuationCurrencyRates(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("base") baseCurrency: String,
        @Query("symbols") symbols: String,
        @Header("apikey") apiKey: String
    ): Call<FluctuationResponse>
}
