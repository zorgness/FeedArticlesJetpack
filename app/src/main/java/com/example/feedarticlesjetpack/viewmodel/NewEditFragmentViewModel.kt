package com.example.feedarticlesjetpack.viewmodel

import ID_DIVERS_CATEGORY
import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import SHAREDPREF_SESSION_USER_ID
import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.common.responseNewArticleStatus
import com.example.feedarticlesjetpack.dataclass.NewArticleDto
import com.example.feedarticlesjetpack.dataclass.StatusDto
import com.example.feedarticlesjetpack.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewEditFragmentViewModel @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    private val _categoryIdLiveData = MutableLiveData<Int>().apply { postValue(ID_DIVERS_CATEGORY) }

    private val categoryIdLiveData: LiveData<Int>
        get() = _categoryIdLiveData

    private val _messageLiveData = MutableLiveData<String>()

    private val _statusLiveData = MutableLiveData<Int>()

    val messageLiveData: LiveData<String>
        get() = _messageLiveData

    val statusLiveData: LiveData<Int>
        get() = _statusLiveData

    fun getCheckedCategory(checkedId: Int) {
        _categoryIdLiveData.value = checkedId
    }

    fun newArticle(title: String, description: String, imageUrl: String) {

        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {
            val token = getString(SHAREDPREF_SESSION_TOKEN, null)
            val userId = getInt(SHAREDPREF_SESSION_USER_ID, 0)
            val headers = HashMap<String, String>()
            val categoryId = categoryIdLiveData.value

            if (token != null) {
                headers["token"] = token
            }

            if (title.isNotBlank() && description.isNotBlank() && imageUrl.isNotBlank() && categoryId != null) {
                if (userId != 0 && token != null) {

                    viewModelScope.launch {
                        val responseNewArticle: Response<StatusDto>? = withContext(Dispatchers.IO) {
                            apiService.newArticles(NewArticleDto(userId, title, description, imageUrl, categoryId), headers)
                        }

                        val body = responseNewArticle?.body()

                        when {
                            responseNewArticle == null -> {
                                _messageLiveData.value = "erreur serveur"
                            }

                            responseNewArticle.isSuccessful && (body != null) -> {
                                _messageLiveData.value = responseNewArticleStatus(body.status, context)
                                _statusLiveData.value = body.status
                            }

                            responseNewArticle.code() == 403 ->
                                _messageLiveData.value = "probléme d'authorisation"

                        }
                    }

                } else {
                    _messageLiveData.value = "probléme d'authentification"
                }
            } else {
                _messageLiveData.value = "tous les champs doivent être remplis"
            }
        }
    }
}