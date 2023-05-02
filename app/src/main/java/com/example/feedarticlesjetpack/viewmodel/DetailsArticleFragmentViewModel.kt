package com.example.feedarticlesjetpack.viewmodel

import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import WITH_FAVORITES
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class DetailsArticleFragmentViewModel @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
): ViewModel() {
    private val _messageLiveData = MutableLiveData<String>()

    private val _articleLiveData = MutableLiveData<ArticleDto>()

    val articleLiveData: LiveData<ArticleDto>
        get() = _articleLiveData

    val messageLiveData: LiveData<String>
        get() = _messageLiveData


    fun getArticleById(id: Int) {
        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE )) {
            val token = getString(SHAREDPREF_SESSION_TOKEN, null)

            val headers = HashMap<String, String>()

            if (token != null) {
                headers["token"] = token
            }

            viewModelScope.launch {
                val responseArticle: Response<GetArticleDto>? = withContext(Dispatchers.IO) {
                    apiService.getArticleById(headers, id, WITH_FAVORITES,)
                }

                val body = responseArticle?.body()

                when {
                    responseArticle == null-> {
                        _messageLiveData.value = "erreur serveur"
                    }

                    responseArticle.isSuccessful && body != null -> {
                        _messageLiveData.value = responseArticlesStatus(body.status, context)
                        _articleLiveData.value = body.article
                    }

                    responseArticle.code() == 403 -> {
                        _messageLiveData.value = "erreur d'authorisation"
                    }
                }
            }

        }
    }
}