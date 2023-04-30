package com.example.feedarticlesjetpack.viewmodel

import REGISTER_STR
import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import SHAREDPREF_SESSION_USER_ID
import STATUS_USER_SUCCESS
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.feedarticlesjetpack.dataclass.RegisterDto
import com.example.feedarticlesjetpack.dataclass.SessionDto
import com.example.feedarticlesjetpack.fragment.LoginFragmentDirections
import com.example.feedarticlesjetpack.fragment.RegisterFragmentDirections
import com.example.feedarticlesjetpack.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import responseUserStatus
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {


    private val _messageLiveData = MutableLiveData<String>()

    private val _statusLiveData = MutableLiveData<Int>()

    val messageLiveData: LiveData<String>
        get() = _messageLiveData

    val statusLiveData: LiveData<Int>
        get() = _statusLiveData

    fun register(login: String, password: String, confirmPassword: String) {
        if (login.isNotBlank() && password.isNotBlank()) {
            if (validatePassword(password, confirmPassword)) {

                viewModelScope.launch {

                    val responseRegister: Response<SessionDto>? = withContext(Dispatchers.IO) {
                        apiService.register(RegisterDto(login, password))
                    }

                    val body = responseRegister?.body()

                    when {
                        responseRegister == null -> {

                            _messageLiveData.value = "erreur du serveur"
                        }

                        responseRegister.isSuccessful && (body != null) -> {
                            _messageLiveData.value =
                                responseUserStatus(body.status, REGISTER_STR, context)
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


                        responseRegister.code() == 403 ->
                            _messageLiveData.value = "probléme d'authorisation"
                    }

                }
            } else
                _messageLiveData.value = "mot de passe et confirmation sont différents"

        } else
            _messageLiveData.value = "les champs ne peuvent-être vide"

    }

    private fun validatePassword(password: String, confirm: String): Boolean {
        return password == confirm
    }
}