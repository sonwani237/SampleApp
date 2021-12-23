package com.tesseractapp.demo.network

import com.google.gson.JsonElement
import com.tesseractapp.demo.models.HeadlineModel
import com.tesseractapp.demo.models.LoginRequestModel
import com.tesseractapp.demo.utils.ApplicationConstant
import io.reactivex.Observable
import retrofit2.Response

class Repository(private val apiCallInterface: WebService) {

    suspend fun executeUsers(page:String): Response<JsonElement> {
        return apiCallInterface.users(page)
    }

    suspend fun executeLogin(loginRequestModel: LoginRequestModel): Response<JsonElement> {
        return apiCallInterface.login(loginRequestModel)
    }



}
