package com.tesseractapp.demo.models

data class UserList(val page: Int, val data: ArrayList<UserData>) {
    data class UserData(
        val id: Int,
        val email: String,
        val first_name: String,
        val last_name: String,
        val avatar: String
    )
}
