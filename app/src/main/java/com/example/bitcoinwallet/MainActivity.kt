package com.example.bitcoinwallet

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bitcoinwallet.ui.theme.BitcoinWalletTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BitcoinWalletTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrencyConverterView()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyConverterViewPreview() {
    BitcoinWalletTheme {
        CurrencyConverterView()
    }
}

@Composable
fun CurrencyConverterView(viewModel: MyViewModel = viewModel()) {
    val appPreferences = AppPreferences(LocalContext.current)
    var inputValue by remember {
        mutableStateOf(appPreferences.getInputValue())
    }
    val currencyList by viewModel.currencyList
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = inputValue.toString(),
            onValueChange = {
                inputValue = it.toDoubleOrNull() ?: 0.0
            },
            label = { Text("Enter BTC amount you own") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    hideKeyboard(context)
                    appPreferences.saveInputValue(inputValue)
                }
            )
        )

        Button(
            onClick = {
                viewModel.makeApiCall(btcOwned = inputValue)
                appPreferences.saveInputValue(inputValue)
                hideKeyboard(context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Update List")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
        ) {
            itemsIndexed(currencyList) { index, currency ->
                Text(
                    text = "Currency: ${currency.currencyName}," +
                            " Value: ${currency.currencyValue}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.White
                )
            }
        }
    }
}

fun hideKeyboard(context: Context) {
    val view = (context as? ComponentActivity)?.findViewById<View>(android.R.id.content)
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view?.windowToken, 0)
}


