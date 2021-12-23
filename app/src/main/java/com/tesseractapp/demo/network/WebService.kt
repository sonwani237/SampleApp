package com.tesseractapp.demo.network

import com.google.gson.JsonElement
import com.tesseractapp.demo.models.LoginRequestModel
import com.tesseractapp.demo.utils.Urls.LOGIN
import com.tesseractapp.demo.utils.Urls.USER
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WebService {

    @GET(USER)
    suspend fun users(
        @Query("page") page: String,
    ): Response<JsonElement>

    @POST(LOGIN)
    suspend fun login(
        @Body loginRequestModel: LoginRequestModel
    ): Response<JsonElement>
}