package com.example.feedarticlesjetpack.viewmodel

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
class DetailsArticleFragmentViewModel @Inject constructor(
    private val apiService: ApiService,
    private val context: Context
): ViewModel() {
    private val _messageLiveData = MutableLiveData<String>()

    val messageLiveData: LiveData<String>
        get() = _messageLiveData


}
