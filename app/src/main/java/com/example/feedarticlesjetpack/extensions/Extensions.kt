package com.example.feedarticlesjetpack.extensions

import android.content.Context
import android.widget.Toast

fun Context.myToast(message: String) {
    if(message.isNotBlank())
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

val Int.bool:Boolean get() = this == 1

