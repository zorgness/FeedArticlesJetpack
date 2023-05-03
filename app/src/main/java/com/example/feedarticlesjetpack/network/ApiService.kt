package com.example.feedarticlesjetpack.network

import com.example.feedarticlesjetpack.dataclass.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @PUT(ApiRoutes.USER)
    suspend fun register(@Body registerDto: RegisterDto): Response<SessionDto>?

    @FormUrlEncoded
    @POST(ApiRoutes.USER)
    suspend fun login(
        @Field("login") login: String,
        @Field("mdp") mdp: String
    ): Response<SessionDto>?


    @GET(ApiRoutes.ARTICLES)
    suspend fun getAllArticles(
        @Query("with_fav") withFav: Int?,
        @HeaderMap headers: Map<String, String>,
    ): Response<GetArticlesDto>?


    @PUT(ApiRoutes.ARTICLES)
    suspend fun newArticle(
        @Body newArticleDto: NewArticleDto,
        @HeaderMap headers: Map<String, String>,
    ): Response<StatusDto>?

    @GET(ApiRoutes.ARTICLES)
    suspend fun getArticleById(
        @HeaderMap headers: Map<String, String>,
        @Query("id") articleId: Int,
        @Query("with_fav") withFav: Int?
    ): Response<GetArticleDto>?

    @POST(ApiRoutes.ARTICLES)
    suspend fun updateArticle(
        @Query("id") articleId: Int,
        @HeaderMap headers: Map<String, String>,
        @Body updateArticleDto: UpdateArticleDto


    ): Response<StatusDto>?

    @DELETE(ApiRoutes.ARTICLES)
    suspend fun deleteArticle(
        @Query("id") articleId: Int,
        @HeaderMap headers: Map<String, String>
    ): Response<StatusDto>?

    @PUT(ApiRoutes.ARTICLES)
    suspend fun addArticleToFavorite(
        @Query("id") articleId: Int,
        @HeaderMap headers: Map<String, String>
    ): Response<StatusDto>?

}
