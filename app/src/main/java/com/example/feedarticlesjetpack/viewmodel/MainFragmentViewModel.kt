package com.example.feedarticlesjetpack.viewmodel

import ERROR_403
import ID_ALL_CATEGORY
import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import SHAREDPREF_SESSION_USER_ID
import USER_TOKEN
import WITH_FAVORITES
import android.content.Context
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.dataclass.ArticleDto
import com.example.feedarticlesjetpack.dataclass.GetArticlesDto
import com.example.feedarticlesjetpack.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.feedarticlesjetpack.common.responseArticlesStatus
import com.example.feedarticlesjetpack.extensions.bool
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
) : ViewModel() {

    private val _articlesLiveData = MutableLiveData<List<ArticleDto>>()

    private val _categoryIdLiveData = MutableLiveData<Int>().apply { postValue(ID_ALL_CATEGORY) }

    private val _isFavFilterLiveData = MutableLiveData<Boolean>().apply { postValue(false) }

    private val _messageLiveData = MutableLiveData<String>()

    private val _isLogoutLiveData = MutableLiveData<Boolean>()

    private val _progressBarVisibilityLiveData = MutableLiveData<Boolean>()

    val articlesLiveData: LiveData<List<ArticleDto>>
        get() = _articlesLiveData

    private val categoryIdLiveData: LiveData<Int>
        get() = _categoryIdLiveData

    val isFavFilterLiveData: LiveData<Boolean>
        get() = _isFavFilterLiveData

    val messageLiveData: LiveData<String>
        get() = _messageLiveData

    val isLogoutLiveData: LiveData<Boolean>
        get() = _isLogoutLiveData

    val progressBarVisibilityLiveData: LiveData<Boolean>
        get() = _progressBarVisibilityLiveData

    init {
        getAllArticles()
        /* Handler().postDelayed({
             getAllArticles()
         }, 1500)*/
    }

    fun getCheckedCategory(checkedId: Int) {
        _categoryIdLiveData.value = checkedId
        getAllArticles()
    }

    fun setFavoriteFilter() {
        _isFavFilterLiveData.value = !_isFavFilterLiveData.value!!
        getAllArticles()
    }

    fun getAllArticles() {

        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {

            val token = this.getString(SHAREDPREF_SESSION_TOKEN, null)
            val headers = HashMap<String, String>()

            if (token != null) {
                headers[USER_TOKEN] = token
            }
            viewModelScope.launch {
                _progressBarVisibilityLiveData.value = true

                val responseCategories: Response<GetArticlesDto>? = withContext(Dispatchers.IO) {
                    apiService.getAllArticles(WITH_FAVORITES, headers)
                }

                val body = responseCategories?.body()

                when {
                    responseCategories == null -> {
                        _messageLiveData.value = context.getString(R.string.server_error)
                    }

                    responseCategories.isSuccessful && (body != null) -> {
                        _messageLiveData.value =
                            responseArticlesStatus(body.status, context)
                        if (categoryIdLiveData.value == ID_ALL_CATEGORY || categoryIdLiveData.value == null)
                            if (isFavFilterLiveData.value == true)
                                _articlesLiveData.value =
                                    body.articles.filter { element -> element.isFav.bool == isFavFilterLiveData.value }
                            else
                                _articlesLiveData.value = body.articles
                        else
                            if (isFavFilterLiveData.value == true)
                                _articlesLiveData.value = body.articles.filter { element ->
                                    element.categorie == categoryIdLiveData.value
                                }
                                    .filter { element -> element.isFav.bool == isFavFilterLiveData.value }
                            else
                                _articlesLiveData.value = body.articles.filter { element ->
                                    element.categorie == categoryIdLiveData.value
                                }
                        _progressBarVisibilityLiveData.value = false
                    }

                    responseCategories.code() == ERROR_403 ->
                        _messageLiveData.value = context.getString(R.string.unauthorized)
                }

            }

        }
    }

    fun logout() {
        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {
            edit().remove(SHAREDPREF_SESSION_TOKEN).remove(SHAREDPREF_SESSION_USER_ID).apply()
            _isLogoutLiveData.value = true
            _messageLiveData.value = context.getString(R.string.bye_bye)
        }
    }
}