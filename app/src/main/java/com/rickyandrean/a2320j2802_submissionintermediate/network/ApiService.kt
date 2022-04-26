package com.rickyandrean.a2320j2802_submissionintermediate.network

import com.rickyandrean.a2320j2802_submissionintermediate.model.AuthenticationResponse
import com.rickyandrean.a2320j2802_submissionintermediate.model.FileUploadResponse
import com.rickyandrean.a2320j2802_submissionintermediate.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<AuthenticationResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<AuthenticationResponse>

    @GET("stories")
    suspend fun storiesPaging(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @GET("stories")
    fun storiesMap(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): Call<FileUploadResponse>
}