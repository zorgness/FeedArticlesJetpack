package com.example.feedarticlesjetpack.viewmodel

import ERROR_403
import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import USER_TOKEN
import WITH_FAVORITES
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.common.responseArticlesStatus
import com.example.feedarticlesjetpack.dataclass.ArticleDto
import com.example.feedarticlesjetpack.dataclass.GetArticleDto
import com.example.feedarticlesjetpack.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    private val _messageLiveData = MutableLiveData<String>()

    private val _articleLiveData = MutableLiveData<ArticleDto>()

    private val _refreshListLiveData = MutableLiveData<Boolean>().apply { postValue(false) }

    val articleLiveData: LiveData<ArticleDto>
        get() = _articleLiveData

    val messageLiveData: LiveData<String>
        get() = _messageLiveData


    val refreshListLiveData: LiveData<Boolean>
        get() = _refreshListLiveData

    fun askForRefreshArticlesList() {
        _refreshListLiveData.value = true
    }


    fun getArticleById(id: Int) {
        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {
            val token = getString(SHAREDPREF_SESSION_TOKEN, null)

            val headers = HashMap<String, String>()

            if (token != null) {
                headers[USER_TOKEN] = token
            }

            viewModelScope.launch {
                val responseArticle: Response<GetArticleDto>? = withContext(Dispatchers.IO) {
                    apiService.getArticleById(headers, id, WITH_FAVORITES)
                }

                val body = responseArticle?.body()

                when {
                    responseArticle == null -> {
                        _messageLiveData.value = context.getString(R.string.server_error)
                    }

                    responseArticle.isSuccessful && body != null -> {
                        _messageLiveData.value = responseArticlesStatus(body.status, context)
                        _articleLiveData.value = body.article
                    }

                    responseArticle.code() == ERROR_403 -> {
                        _messageLiveData.value = context.getString(R.string.unauthorized)
                    }
                }
            }

        }
    }

}