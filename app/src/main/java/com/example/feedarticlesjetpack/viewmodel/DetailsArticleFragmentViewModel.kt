package com.example.feedarticlesjetpack.viewmodel

import ERROR_403
import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import STATUS_MUTATION_ARTICLE_SUCCESS
import USER_TOKEN
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.common.responseFavoriteStateArticleStatus
import com.example.feedarticlesjetpack.dataclass.StatusDto
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
) : ViewModel() {
    private val _messageLiveData = MutableLiveData<String>()

    private val _isArticleFavoriteLiveData = MutableLiveData<Boolean>()

    val messageLiveData: LiveData<String>
        get() = _messageLiveData

    val isArticleFavoriteLiveData: LiveData<Boolean>
        get() = _isArticleFavoriteLiveData

    fun getArticleFavoriteStatus(isFav: Boolean) {
        _isArticleFavoriteLiveData.value = isFav
    }

    fun setFavoriteStatus(articleId: Long) {

        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {
            val token = getString(SHAREDPREF_SESSION_TOKEN, null)
            val header = HashMap<String, String>()

            if(token != null)
                header[USER_TOKEN] = token

            viewModelScope.launch {
                val responseSetFavorite: Response<StatusDto>? = withContext(Dispatchers.IO) {
                    apiService.changeFavoriteStatusArticle(articleId, header)
                }

                val body = responseSetFavorite?.body()

                when {
                    responseSetFavorite == null -> {
                        _messageLiveData.value = context.getString(R.string.server_error)
                    }

                    responseSetFavorite.isSuccessful && (body != null) -> {
                        _messageLiveData.value = responseFavoriteStateArticleStatus(body.status, context)
                        if(body.status == STATUS_MUTATION_ARTICLE_SUCCESS)
                            _isArticleFavoriteLiveData.value = !_isArticleFavoriteLiveData.value!!
                    }

                    responseSetFavorite.code() == ERROR_403 -> {
                        _messageLiveData.value = context.getString(R.string.unauthorized)
                    }
                }
            }
        }
    }


}
