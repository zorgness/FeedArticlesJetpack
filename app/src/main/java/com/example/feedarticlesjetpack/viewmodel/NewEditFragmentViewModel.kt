package com.example.feedarticlesjetpack.viewmodel

import ERROR_403
import ID_DIVERS_CATEGORY
import SHAREDPREF_NAME
import SHAREDPREF_SESSION_TOKEN
import SHAREDPREF_SESSION_USER_ID
import USER_TOKEN
import android.app.AlertDialog
import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
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
import com.example.feedarticlesjetpack.extensions.is80charactersMax
import com.example.feedarticlesjetpack.extensions.isPositive
import com.example.feedarticlesjetpack.fragment.NewEditArticleFragment
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

    private val _articleIdLiveData = MutableLiveData<Long>()

    private val _messageLiveData = MutableLiveData<String>()

    private val _statusLiveData = MutableLiveData<Int>()

    val titleLiveData = MutableLiveData<String>("")
    val descriptionLiveData = MutableLiveData<String>("")
    val imageUrlLiveData = MutableLiveData<String>("")

    private val categoryIdLiveData: LiveData<Int>
        get() = _categoryIdLiveData

    private val articleIdLiveData: LiveData<Long>
        get() = _articleIdLiveData


    val messageLiveData: LiveData<String>
        get() = _messageLiveData

    val statusLiveData: LiveData<Int>
        get() = _statusLiveData

    fun getCheckedCategory(checkedId: Int) {
        _categoryIdLiveData.value = checkedId
    }

    fun getArticleId(articleId: Long) {
        _articleIdLiveData.value = articleId
    }


    fun deleteArticle(articleId: Long) {
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

    fun updateArticle() {
        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {
            val token = getString(SHAREDPREF_SESSION_TOKEN, null)
            val headers = HashMap<String, String>()
            val categoryId = categoryIdLiveData.value

            if (token != null) {
                headers[USER_TOKEN] = token
            }

            if (titleLiveData.value?.isNotBlank() == true
                && descriptionLiveData.value?.isNotBlank() == true
                && imageUrlLiveData.value?.isNotBlank() == true
                && categoryId != null
            ) {
                if (titleLiveData.value?.is80charactersMax == true) {

                    if (token != null) {

                        viewModelScope.launch {
                            val responseUpdateArticle: Response<StatusDto>? =
                                withContext(Dispatchers.IO) {
                                    articleIdLiveData.value?.let { articleId ->
                                        apiService.updateArticle(
                                            articleId,
                                            headers,
                                            UpdateArticleDto(
                                                articleId,
                                                titleLiveData.value!!,
                                                descriptionLiveData.value!!,
                                                imageUrlLiveData.value!!,
                                                categoryId
                                            )
                                        )
                                    }
                                }

                            val body = responseUpdateArticle?.body()

                            when {
                                responseUpdateArticle == null -> {
                                    _messageLiveData.value =
                                        context.getString(R.string.server_error)
                                }

                                responseUpdateArticle.isSuccessful && (body != null) -> {
                                    _messageLiveData.value =
                                        responseUpdateArticleStatus(body.status, context)
                                    _statusLiveData.value = body.status
                                }

                                responseUpdateArticle.code() == ERROR_403 ->
                                    _messageLiveData.value =
                                        context.getString(R.string.unauthorized)

                            }
                        }

                    } else
                        _messageLiveData.value = context.getString(R.string.user_not_authenticated)

                } else
                    _messageLiveData.value = context.getString(R.string.title_too_long)

            } else
                _messageLiveData.value = context.getString(R.string.empty_fields)

        }
    }

    fun newArticle() {

        with(context.getSharedPreferences(SHAREDPREF_NAME, Context.MODE_PRIVATE)) {
            val token = getString(SHAREDPREF_SESSION_TOKEN, null)
            val userId = getLong(SHAREDPREF_SESSION_USER_ID, 0)
            val headers = HashMap<String, String>()
            val categoryId = categoryIdLiveData.value

            if (token != null)
                headers[USER_TOKEN] = token

            if (titleLiveData.value?.isNotBlank() == true
                && descriptionLiveData.value?.isNotBlank() == true
                && imageUrlLiveData.value?.isNotBlank() == true
                && categoryId != null
            ) {
                if (titleLiveData.value?.is80charactersMax == true) {

                    if (userId.isPositive && token != null) {

                        viewModelScope.launch {
                            val responseNewArticle: Response<StatusDto>? =
                                withContext(Dispatchers.IO) {
                                    apiService.newArticle(
                                        NewArticleDto(
                                            userId,
                                            titleLiveData.value!!,
                                            descriptionLiveData.value!!,
                                            imageUrlLiveData.value!!,
                                            categoryId
                                        ), headers
                                    )
                                }

                            val body = responseNewArticle?.body()

                            when {
                                responseNewArticle == null -> {
                                    _messageLiveData.value =
                                        context.getString(R.string.server_error)
                                }

                                responseNewArticle.isSuccessful && (body != null) -> {
                                    _messageLiveData.value =
                                        responseNewArticleStatus(body.status, context)
                                    _statusLiveData.value = body.status
                                }

                                responseNewArticle.code() == ERROR_403 ->
                                    _messageLiveData.value =
                                        context.getString(R.string.unauthorized)

                            }
                        }

                    } else
                        _messageLiveData.value = context.getString(R.string.user_not_authenticated)


                } else
                    _messageLiveData.value = context.getString(R.string.title_too_long)

            } else
                _messageLiveData.value = context.getString(R.string.empty_fields)

        }
    }
}