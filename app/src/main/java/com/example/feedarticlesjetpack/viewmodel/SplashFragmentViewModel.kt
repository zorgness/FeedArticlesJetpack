package com.example.feedarticlesjetpack.viewmodel

import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import android.content.Context
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.example.feedarticlesjetpack.fragment.SplashFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashFragmentViewModel @Inject constructor(
    val context: Context
) : ViewModel() {

    private val _destinationLiveData = MutableLiveData<NavDirections>()

    val destinationLiveData: LiveData<NavDirections>
        get() = _destinationLiveData

    init {
        Handler().postDelayed({
            getDestination()
        }, 2000)
    }

    private fun getDestination() {

        val sessionToken = context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)
            .getString(SHAREDPREF_SESSION_TOKEN, null)
        _destinationLiveData.value = if (sessionToken != null) {
            SplashFragmentDirections.actionSplashFragmentToMainFragment()
        } else {
            SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        }
    }

}
