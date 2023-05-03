package com.example.feedarticlesjetpack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.feedarticlesjetpack.dataclass.ArticleDto

class SharedViewModel: ViewModel() {

    private val _refreshListLiveData = MutableLiveData<Boolean>().apply { postValue(false)}

    val refreshListLiveData: LiveData<Boolean>
        get() = _refreshListLiveData

    fun askForRefreshArticlesList() {
        _refreshListLiveData.value = true
    }

}