package com.example.bitcoinwallet

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

import okhttp3.*
import java.io.IOException
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MyViewModel : ViewModel() {
    private val _currencyList = mutableStateOf <List<CurrencyModel>>(emptyList())
    val currencyList: State<List<CurrencyModel>> = _currencyList

    fun updateDataList(currencyList: List<CurrencyModel>) {
        _currencyList.value = currencyList
    }

    fun fetchData(btcOwned: Double) {
        val baseCurrency = "BTC"
        val symbols = "ZAR,USD,AUD"
        val apiKey = "1yJg56aYgDPSAwO5mMhmq7I8AMxje8Zs"

        val call = RetrofitClient.currencyApiService.getCurrencyRates(baseCurrency, symbols, apiKey)

        call.enqueue(object : Callback<CurrencyResponse> {
            override fun onResponse(call: Call<CurrencyResponse>, response: Response<CurrencyResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    // Perform currency calculations
                    val zarCalculated = apiResponse?.rates?.zar?.times(btcOwned) ?: 0.0
                    val usdCalculated = apiResponse?.rates?.usd?.times(btcOwned) ?: 0.0
                    val audCalculated = apiResponse?.rates?.aud?.times(btcOwned) ?: 0.0

                    // Create CurrencyModel objects
                    val zarCurrency = CurrencyModel("ZAR", zarCalculated.toString())
                    val usdCurrency = CurrencyModel("USD", usdCalculated.toString())
                    val audCurrency = CurrencyModel("AUD", audCalculated.toString())

                    // Create a list of CurrencyModel
                    val currencyList = listOf(zarCurrency, usdCurrency, audCurrency)

                    // Update ViewModel with the currency list
                    updateDataList(currencyList)
                } else {
                    println("API call unsuccessful")
                }
            }

            override fun onFailure(call: Call<CurrencyResponse>, t: Throwable) {
                println(t)
            }
        })
    }
}