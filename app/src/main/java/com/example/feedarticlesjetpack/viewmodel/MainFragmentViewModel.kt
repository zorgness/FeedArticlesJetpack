package com.example.feedarticlesjetpack.viewmodel

import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import SHAREDPREF_SESSION_USER_ID
import android.content.Context
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor(
    val context: Context
): ViewModel() {

    fun logout() {
        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {

            edit().remove(SHAREDPREF_SESSION_TOKEN).remove(SHAREDPREF_SESSION_USER_ID).apply()

            //navController.navigate(R.id.loginFragment)

        }
    }
}