package com.example.ecommercemobile

import android.app.Application
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ECommerceApp: Application() {
    override fun onCreate() {
        super.onCreate()
        val config = CheckoutConfig(
            application = this,
            clientId = "AVjnwCEM4shxQzgAJZv37ZwTvmV87opjddOt8L61H9h1ZTLXeH5qav70bOPThoEKV7XnN6t2H2uCARxB",
            environment = Environment.SANDBOX,
            returnUrl = "com.example.ecommercemobile://paypalpay",
            currencyCode = CurrencyCode.EUR,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(

                loggingEnabled = true

            )
        )
        PayPalCheckout.setConfig(config)
    }
}