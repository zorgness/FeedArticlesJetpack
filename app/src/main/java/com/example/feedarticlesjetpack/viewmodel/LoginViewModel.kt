package com.example.feedarticlesjetpack.viewmodel

import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import SHAREDPREF_SESSION_USER_ID
import STATUS_USER_SUCCESS
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.dataclass.RegisterDto
import com.example.feedarticlesjetpack.dataclass.SessionDto
import com.example.feedarticlesjetpack.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import responseLoginStatus
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    private val _messageLiveData = MutableLiveData<String>()

    private val _statusLiveData = MutableLiveData<Int>()

    val messageLiveData: LiveData<String>
        get() = _messageLiveData

    val statusLiveData: LiveData<Int>
        get() = _statusLiveData

    fun login(login: String, password: String) {
        if (login.isNotBlank() && password.isNotBlank()) {
            viewModelScope.launch {

                val responseLogin: Response<SessionDto>? = withContext(Dispatchers.IO) {
                    apiService.login(login, password)
                }

                val body = responseLogin?.body()

                when {
                    responseLogin == null -> {

                        _messageLiveData.value = "erreur du serveur"
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

                    responseLogin.code() == 403 ->
                        _messageLiveData.value = "probléme d'authorisation"
                }

            }

        } else
            _messageLiveData.value = "les champs ne peuvent-être vide"


    }
}