package com.example.bitcoinwallet.Features.BitcoinList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.bitcoinwallet.API.ResponseModels.CurrencyResponse
import com.example.bitcoinwallet.API.RetrofitClient
import com.example.bitcoinwallet.Helpers.Constants
import com.example.bitcoinwallet.Models.CurrencyModel

import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MyViewModel : ViewModel() {
    private val _currencyList = mutableStateOf <List<CurrencyModel>>(emptyList())
    val currencyList: State<List<CurrencyModel>> = _currencyList

    fun updateDataList(currencyList: List<CurrencyModel>) {
        _currencyList.value = currencyList
    }

    fun fetchCurrencyList(btcOwned: Double) {
        val baseCurrency = Constants.BASE_CURRENCY
        val symbols = Constants.SYMBOLS
        val apiKey = Constants.API_KEY

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