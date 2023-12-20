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
            clientId = "AddRC6syLdDnzCPsYP9rrifxTjzMK90SSQDn1QuphLe79cF6R26xET3xp6vUPLBw7IZhRNs5Uvl4S_Nm",
            environment = Environment.SANDBOX,
            returnUrl = "com.example.ecommercemobile://paypalpay",
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(

                loggingEnabled = true

            )
        )
        PayPalCheckout.setConfig(config)
    }
}