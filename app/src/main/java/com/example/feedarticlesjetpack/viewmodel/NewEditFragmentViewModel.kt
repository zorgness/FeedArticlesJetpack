package com.example.feedarticlesjetpack.viewmodel

import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.feedarticlesjetpack.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewEditFragmentViewModel @Inject constructor(
    private val apiService: ApiService
): ViewModel() {

    private val _categoryIdLiveData = MutableLiveData<Int>()

    private val categoryIdLiveData: LiveData<Int>
        get() = _categoryIdLiveData

    fun getCheckedCategory(checkedId: Int) {
        _categoryIdLiveData.value = checkedId
    }

    fun newArticle(title: String, description: String, imageUrl: String) {
        println("New article $title $description $imageUrl ${categoryIdLiveData.value}")
    }
}