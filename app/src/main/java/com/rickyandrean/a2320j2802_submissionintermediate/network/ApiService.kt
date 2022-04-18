package com.rickyandrean.a2320j2802_submissionintermediate.network

import android.util.Log
import com.rickyandrean.a2320j2802_submissionintermediate.model.AuthenticationResponse
import com.rickyandrean.a2320j2802_submissionintermediate.model.StoryResponse
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
    fun stories(
        @Header("Authorization") token: String
    ): Call<StoryResponse>

//    @GET("search/users")
//    fun searchUser(@Query("q") username: String): Call<UserResponseSearch>
//
//    @GET("users/{username}")
//    fun getUserDetail(@Path("username") username: String): Call<UserDetail>
//
//    @GET("users/{username}/followers")
//    fun getFollowers(@Path("username") username: String): Call<ArrayList<UserResponseItem>>
//
//    @GET("users/{username}/following")
//    fun getFollowing(@Path("username") username: String): Call<ArrayList<UserResponseItem>>
}