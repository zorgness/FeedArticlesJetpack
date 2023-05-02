package com.example.feedarticlesjetpack.viewmodel

import ID_ALL_CATEGORY
import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import SHAREDPREF_SESSION_USER_ID
import WITH_FAVORITES
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.dataclass.ArticleDto
import com.example.feedarticlesjetpack.dataclass.GetArticlesDto
import com.example.feedarticlesjetpack.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.feedarticlesjetpack.common.responseArticlesStatus
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    private val _articlesLiveData = MutableLiveData<List<ArticleDto>>()

    private val _categoryIdLiveData = MutableLiveData<Int>()

    private val _messageLiveData = MutableLiveData<String>()

    private val _isLogoutLiveData = MutableLiveData<Boolean>()

    val articlesLiveData: LiveData<List<ArticleDto>>
        get() = _articlesLiveData

    private val categoryIdLiveData: LiveData<Int>
        get() = _categoryIdLiveData

    val messageLiveData: LiveData<String>
        get() = _messageLiveData

    val isLogoutLiveData: LiveData<Boolean>
        get() = _isLogoutLiveData

    init {
        getAllArticles()
    }


    fun getCheckedCategory(checkedId: Int) {
        _categoryIdLiveData.value = checkedId
        getAllArticles()
    }

    private fun getAllArticles() {
        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {

            val token = this.getString(SHAREDPREF_SESSION_TOKEN, null)
            val headers = HashMap<String, String>()

            if (token != null) {
                headers["token"] = token
            }
            viewModelScope.launch {

                val responseCategories: Response<GetArticlesDto>? = withContext(Dispatchers.IO) {
                    apiService.getAllArticles(WITH_FAVORITES, headers)
                }

                val body = responseCategories?.body()

                when {
                    responseCategories == null -> {
                        _messageLiveData.value = "erreur du serveur"
                    }

                    responseCategories.isSuccessful && (body != null) -> {
                        _messageLiveData.value =
                             responseArticlesStatus(body.status, context)
                        if (categoryIdLiveData.value == ID_ALL_CATEGORY || categoryIdLiveData.value == null)
                            _articlesLiveData.value = body.articles
                        else
                            _articlesLiveData.value = body.articles.filter { element ->
                                element.categorie == categoryIdLiveData.value
                            }
                    }


                    responseCategories.code() == 403 ->
                        _messageLiveData.value = "erreur d'authorisation"
                }

            }
        }
    }

    fun logout() {
        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {
            edit().remove(SHAREDPREF_SESSION_TOKEN).remove(SHAREDPREF_SESSION_USER_ID).apply()
            _isLogoutLiveData.value = true
            _messageLiveData.value = "bye bye"

        }
    }
}