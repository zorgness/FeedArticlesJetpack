package com.example.feedarticlesjetpack.module

import com.example.feedarticlesjetpack.dataclass.RegisterDto
import com.example.feedarticlesjetpack.dataclass.SessionDto
import com.example.feedarticlesjetpack.network.ApiRoutes
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @Headers("Content-Type: application/json")
    @PUT(ApiRoutes.USER)
    fun register(@Body registerDto: RegisterDto): Call<SessionDto>?

    @FormUrlEncoded
    @POST(ApiRoutes.USER)
    fun login(@Field("login") login: String, @Field("mdp") mdp: String): Response<SessionDto>?
}
