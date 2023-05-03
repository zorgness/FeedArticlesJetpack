package com.example.feedarticlesjetpack.viewmodel

import ERROR_403
import ID_DIVERS_CATEGORY
import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import SHAREDPREF_SESSION_USER_ID
import USER_TOKEN
import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlesjetpack.MainActivity
import com.example.feedarticlesjetpack.R
import com.example.feedarticlesjetpack.common.responseDeleteArticleStatus
import com.example.feedarticlesjetpack.common.responseNewArticleStatus
import com.example.feedarticlesjetpack.common.responseUpdateArticleStatus
import com.example.feedarticlesjetpack.dataclass.NewArticleDto
import com.example.feedarticlesjetpack.dataclass.StatusDto
import com.example.feedarticlesjetpack.dataclass.UpdateArticleDto
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


    fun deleteArticle(articleId: Int) {
        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {
            val token = getString(SHAREDPREF_SESSION_TOKEN, null)
            val headers = HashMap<String, String>()

            if (token != null) {
                headers[USER_TOKEN] = token
            }

            viewModelScope.launch {
                val responseDeleteArticle: Response<StatusDto>? = withContext(Dispatchers.IO) {
                    apiService.deleteArticle(articleId, headers)
                }

                val body = responseDeleteArticle?.body()

                when {
                    responseDeleteArticle == null -> {
                        _messageLiveData.value = context.getString(R.string.server_error)
                    }

                    responseDeleteArticle.isSuccessful && (body != null) -> {
                        _messageLiveData.value = responseDeleteArticleStatus(body.status, context)
                        _statusLiveData.value = body.status
                    }

                    responseDeleteArticle.code() == ERROR_403 ->
                        _messageLiveData.value = context.getString(R.string.unauthorized)

                }
            }

        }

    }

    fun updateArticle(articleId: Int, title: String, description: String, imageUrl: String) {
        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {
            val token = getString(SHAREDPREF_SESSION_TOKEN, null)
            val headers = HashMap<String, String>()
            val categoryId = categoryIdLiveData.value

            if (token != null) {
                headers[USER_TOKEN] = token
            }

            if (title.isNotBlank() && description.isNotBlank() && imageUrl.isNotBlank() && categoryId != null) {
                if (token != null) {

                    viewModelScope.launch {
                        val responseUpdateArticle: Response<StatusDto>? =
                            withContext(Dispatchers.IO) {
                                apiService.updateArticle(
                                    articleId,
                                    headers,
                                    UpdateArticleDto(
                                        articleId,
                                        title,
                                        description,
                                        imageUrl,
                                        categoryId
                                    )
                                )
                            }

                        val body = responseUpdateArticle?.body()

                        when {
                            responseUpdateArticle == null -> {
                                _messageLiveData.value = context.getString(R.string.server_error)
                            }

                            responseUpdateArticle.isSuccessful && (body != null) -> {
                                _messageLiveData.value =
                                    responseUpdateArticleStatus(body.status, context)
                                _statusLiveData.value = body.status
                            }

                            responseUpdateArticle.code() == ERROR_403 ->
                                _messageLiveData.value = context.getString(R.string.unauthorized)

                        }
                    }

                } else {
                    _messageLiveData.value = context.getString(R.string.user_not_authenticated)
                }
            } else {
                _messageLiveData.value = context.getString(R.string.empty_fields)
            }
        }
    }

    fun newArticle(title: String, description: String, imageUrl: String) {

        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {
            val token = getString(SHAREDPREF_SESSION_TOKEN, null)
            val userId = getInt(SHAREDPREF_SESSION_USER_ID, 0)
            val headers = HashMap<String, String>()
            val categoryId = categoryIdLiveData.value

            if (token != null)
                headers[USER_TOKEN] = token

            if (title.isNotBlank() && description.isNotBlank() && imageUrl.isNotBlank() && categoryId != null) {
                if (userId != 0 && token != null) {

                    viewModelScope.launch {
                        val responseNewArticle: Response<StatusDto>? = withContext(Dispatchers.IO) {
                            apiService.newArticle(
                                NewArticleDto(
                                    userId,
                                    title,
                                    description,
                                    imageUrl,
                                    categoryId
                                ), headers
                            )
                        }

                        val body = responseNewArticle?.body()

                        when {
                            responseNewArticle == null -> {
                                _messageLiveData.value = context.getString(R.string.server_error)
                            }

                            responseNewArticle.isSuccessful && (body != null) -> {
                                _messageLiveData.value =
                                    responseNewArticleStatus(body.status, context)
                                _statusLiveData.value = body.status
                            }

                            responseNewArticle.code() == ERROR_403 ->
                                _messageLiveData.value = context.getString(R.string.unauthorized)

                        }
                    }

                } else {
                    _messageLiveData.value = context.getString(R.string.user_not_authenticated)
                }
            } else {
                _messageLiveData.value = context.getString(R.string.empty_fields)
            }
        }
    }
}