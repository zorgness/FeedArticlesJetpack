package com.example.feedarticlesjetpack.common

import CATEGORY_ID
import IS_FAVORITE_FILTER_ON
import androidx.lifecycle.LiveData

class SingletonHashMap() {
    private val map = hashMapOf<String, LiveData<Any>>()


    fun get(key: String): LiveData<Any>? {
        return map[key]
    }

    fun set(key: String, value: LiveData<Any>) {
        map[key] = value
    }

    fun clear() {
        map.clear()
    }

    fun removeCategoryId() {
        map.remove(CATEGORY_ID)
    }

    fun removeIsFavoriteFilter() {
        map.remove(IS_FAVORITE_FILTER_ON)
    }
}