package com.example.ecommercemobile.utils

import android.view.View

object ExtensionFunctions {
    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) {
            multiplier *= 10
        }
        return kotlin.math.round(this * multiplier) / multiplier
    }

    // El this es el progressbar (o shimmer) y el container es el recycler/TV/ET/IV...
    fun View.isVisible(isLoading: Boolean, container: View) {
        if (isLoading) {
            this.visibility = View.VISIBLE
            container.visibility = View.GONE
        } else {
            this.visibility = View.GONE
            container.visibility = View.VISIBLE
        }
    }
}