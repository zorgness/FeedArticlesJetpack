package com.example.feedarticlesjetpack.extensions

import android.content.Context
import android.widget.Toast

fun Context.myToast(message: String) {
    if(message.isNotBlank())
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

val Int.bool:Boolean get() = this == 1

val Long.isPositive:Boolean get() = this > 0

val String.is80charactersMax:Boolean get() = this.length <= 80