package com.example.feedarticlesjetpack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.feedarticlesjetpack.dataclass.ArticleDto

class SharedViewModel: ViewModel() {

    private val _refreshListLiveData = MutableLiveData<Boolean>().apply { postValue(false)}

    private val _articleIdLiveData = MutableLiveData<Int>()

    private val _articleDtoLiveData = MutableLiveData<ArticleDto>()

    val refreshListLiveData: LiveData<Boolean>
        get() = _refreshListLiveData

    val articleIdLiveData: LiveData<Int>
        get() = _articleIdLiveData

    val articleDtoLiveData: LiveData<ArticleDto>
        get() = _articleDtoLiveData

    fun askForRefreshArticlesList() {
        _refreshListLiveData.value = true
    }

    fun askForArticleById(articleId: Int) {
        _articleIdLiveData.value = articleId
    }

    fun getArticleDto(articleDto: ArticleDto) {
        _articleDtoLiveData.value = articleDto
    }
}