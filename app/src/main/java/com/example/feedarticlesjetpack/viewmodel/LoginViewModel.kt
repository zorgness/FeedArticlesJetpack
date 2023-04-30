package com.example.feedarticlesjetpack.viewmodel

import androidx.lifecycle.ViewModel
import com.example.feedarticlesjetpack.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService
): ViewModel() {
}