package com.example.bitcoinwallet.Features.BitcoinList

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bitcoinwallet.Helpers.AppPreferences

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
                viewModel.fetchData(btcOwned = inputValue)
                appPreferences.saveInputValue(inputValue)
                hideKeyboard(context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Fetch values for your BTC")
        }
        if (viewModel.currencyList.value.isEmpty()) {
            Text(
                text = "Your Bitcoin value for different currencies will be shown here",
                color = Color.Black,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Magenta)
            ) {
                itemsIndexed(currencyList) { index, currency ->
                    Text(
                        text = "Currency: ${currency.currencyName}\n" +
                                "Value: ${currency.currencyValue}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                    )
                }
            }
        }
    }
}

fun hideKeyboard(context: Context) {
    val view = (context as? ComponentActivity)?.findViewById<View>(android.R.id.content)
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view?.windowToken, 0)
}


