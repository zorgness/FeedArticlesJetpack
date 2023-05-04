package com.example.feedarticlesjetpack.viewmodel

import ERROR_403
import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import SHAREDPREF_SESSION_USER_ID
import STATUS_USER_FAILURE
import STATUS_USER_SUCCESS
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.dataclass.SessionDto
import com.example.feedarticlesjetpack.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.feedarticlesjetpack.common.responseLoginStatus
import com.example.feedarticlesjetpack.dataclass.LoginInfo
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    private val _messageLiveData = MutableLiveData<String>()

    private val _statusLiveData = MutableLiveData<Int>()

    val loginLiveData = MutableLiveData<String>().apply { postValue("") }
    val passwordLiveData = MutableLiveData<String>().apply { postValue("") }

    val messageLiveData: LiveData<String>
        get() = _messageLiveData

    val statusLiveData: LiveData<Int>
        get() = _statusLiveData



    fun login() {
        if (loginLiveData.value?.isNotBlank() == true && passwordLiveData.value?.isNotBlank() == true) {
            viewModelScope.launch {

                val responseLogin: Response<SessionDto>? = withContext(Dispatchers.IO) {
                    apiService.login(loginLiveData.value!!, passwordLiveData.value!!)
                }

                val body = responseLogin?.body()

                when {
                    responseLogin == null -> {

                        _messageLiveData.value = context.getString(R.string.server_error)
                    }

                    responseLogin.isSuccessful && (body != null) -> {
                        _messageLiveData.value =
                            responseLoginStatus(body.status, context)
                        if (body.status == STATUS_USER_SUCCESS) {
                            context.getSharedPreferences(
                                SHAREDPREF_NAME,
                                Context.MODE_PRIVATE
                            )?.edit()
                                ?.putString(SHAREDPREF_SESSION_TOKEN, body.token)
                                ?.putInt(SHAREDPREF_SESSION_USER_ID, body.id)
                                ?.apply()

                            _statusLiveData.value = body.status

                        }
                    }

                    responseLogin.code() == ERROR_403 ->
                        _messageLiveData.value = context.getString(R.string.unauthorized)
                }

            }

        } else
            _messageLiveData.value = context.getString(R.string.empty_fields)


    }
}