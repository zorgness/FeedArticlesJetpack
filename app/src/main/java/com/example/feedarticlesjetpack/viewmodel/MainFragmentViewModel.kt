package com.example.feedarticlesjetpack.viewmodel

import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import SHAREDPREF_SESSION_USER_ID
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    val context: Context
) : ViewModel() {

    private val _messageLiveData = MutableLiveData<String>()

    private val _isLogoutInLiveData = MutableLiveData<Boolean>()

    val messageLiveData: LiveData<String>
        get() = _messageLiveData

    val isLogoutInLiveData: LiveData<Boolean>
        get() = _isLogoutInLiveData

    fun logout() {
        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {
            edit().remove(SHAREDPREF_SESSION_TOKEN).remove(SHAREDPREF_SESSION_USER_ID).apply()
            _isLogoutInLiveData.value = true
            _messageLiveData.value = "bye bye"

        }
    }
}