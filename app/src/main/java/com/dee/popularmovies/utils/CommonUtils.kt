package com.dee.popularmovies.utils

import android.app.Activity
import android.os.Build
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dee.popularmovies.R


fun ImageView.loadImage(image: String) {
    val options: RequestOptions = RequestOptions().format(DecodeFormat.PREFER_RGB_565)
    Glide.with(context).load(image).apply(options).placeholder(R.drawable.ic_placeholder)
        .error(R.drawable.ic_placeholder)
        .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory())).centerCrop()
        .into(this)
}

fun String.isValidEmail(): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(this).matches()
}

fun Activity.hideStatusNavigationBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window?.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
    } else {
        @Suppress("DEPRECATION") window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}

fun Activity.showStatusNavigationBar() {
    // Check if the current version is 30 or higher
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window?.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
    } else {
        @Suppress("DEPRECATION") window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

}