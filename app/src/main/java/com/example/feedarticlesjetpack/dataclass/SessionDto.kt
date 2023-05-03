package com.example.feedarticlesjetpack.dataclass


import com.squareup.moshi.Json

data class SessionDto(
    @Json(name = "status")
    val status: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "token")
    val token: String?
)