package com.tesseractapp.demo.models

import com.google.gson.annotations.SerializedName

data class LoginResponse (@SerializedName("RESPONSESTATUS") val RESPONSESTATUS: String,
                          @SerializedName("message") val message: String)