package com.example.bitcoinwallet

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

import okhttp3.*
import java.io.IOException

class MyViewModel : ViewModel() {
    private val _dataList = mutableStateOf(listOf("Item 1", "Item 2"))
    val dataList: State<List<String>> = _dataList

    fun updateDataList(newList: List<String>) {
        _dataList.value = newList
    }

    fun makeApiCall() {
        val baseUrl = "https://api.apilayer.com/fixer/latest"
        val baseCurrency = "BTC"
        val symbols = "ZAR,USD,AUD"
        val apiKey = "1yJg56aYgDPSAwO5mMhmq7I8AMxje8Zs"

        val url = "$baseUrl?base=$baseCurrency&symbols=$symbols"

        val request = Request.Builder()
            .url(url)
            .addHeader("apikey", apiKey)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val gson = Gson()
                val apiResponse = gson.fromJson(responseBody, ApiResponse::class.java)

                // Access the parsed data
                if (apiResponse.success) {
                    val timestamp = apiResponse.timestamp
                    val baseCurrency = apiResponse.base
                    val date = apiResponse.date
                    val zarRate = apiResponse.rates.zar
                    val usdRate = apiResponse.rates.usd
                    val audRate = apiResponse.rates.aud

                    // Now you can use these values as needed
                    println("Timestamp: $timestamp")
                    println("Base Currency: $baseCurrency")
                    println("Date: $date")
                    println("ZAR Rate: $zarRate")
                    println("USD Rate: $usdRate")
                    println("AUD Rate: $audRate")
                } else {
                    // Handle the case where the API call was not successful
                    println("API call unsuccessful")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println(e)
            }
        })
    }
}