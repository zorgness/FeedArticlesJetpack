package com.example.feedarticlesjetpack.network

import com.example.feedarticlesjetpack.dataclass.RegisterDto
import com.example.feedarticlesjetpack.dataclass.SessionDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @PUT(ApiRoutes.USER)
    suspend fun register(@Body registerDto: RegisterDto): Response<SessionDto>?

    @FormUrlEncoded
    @POST(ApiRoutes.USER)
    suspend fun login(@Field("login") login: String, @Field("mdp") mdp: String): Response<SessionDto>?
}
