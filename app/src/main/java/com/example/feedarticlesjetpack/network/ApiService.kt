package com.example.feedarticlesjetpack.network

import com.example.feedarticlesjetpack.dataclass.*
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


    @GET(ApiRoutes.ARTICLES)
    suspend fun getAllArticles(@Query("with_fav") withFav: Int?, @HeaderMap headers: Map<String, String>,): Response<GetArticlesDto>?

    @Headers("Content-Type: application/json")
    @PUT(ApiRoutes.ARTICLES)
    suspend fun newArticles(@Body newArticleDto: NewArticleDto, @HeaderMap headers: Map<String, String>,): Response<StatusDto>?
}
