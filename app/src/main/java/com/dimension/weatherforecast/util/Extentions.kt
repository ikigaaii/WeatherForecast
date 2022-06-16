package com.dimension.weatherforecast.util

import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar

fun Snackbar.setMarginAndShow(){
    val view = this.view
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.setMargins(params.leftMargin,
        params.topMargin,
        params.rightMargin,
        params.bottomMargin + 120)
    view.layoutParams = params
    this.show()
}