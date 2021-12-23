package com.tesseractapp.demo.listeners

import com.tesseractapp.demo.models.UserList

interface NewsListener {
    fun onView(articles: UserList.UserData)
}