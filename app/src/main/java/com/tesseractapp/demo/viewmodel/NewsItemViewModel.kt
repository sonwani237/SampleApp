package com.tesseractapp.demo.viewmodel

import android.graphics.Color
import androidx.databinding.BaseObservable
import com.tesseractapp.demo.R
import com.tesseractapp.demo.listeners.NewsListener
import com.tesseractapp.demo.models.UserList

class NewsItemViewModel(model: UserList.UserData?, val mListener: NewsListener) : BaseObservable(){

    private var mModel: UserList.UserData? =null

    init {
        this.mModel = model
    }

    fun setData(mList: UserList.UserData) {
        mModel = mList
        notifyChange()
    }

    fun getTitle() : String{
        return mModel?.first_name?:""
    }

    fun getNews() : String{
        return mModel?.email?:"N/A"
    }


    fun getImage() : String{
        return mModel?.avatar?:""
    }

    fun getSetBg() : Int{
        return when {
            (mModel!!.id+2)%3 == 1 -> {
                Color.RED
            }
            (mModel!!.id+2)%3 == 2 -> {
                Color.GREEN
            }
            else -> {
                Color.BLUE
            }
        }
    }


    fun onView() {
        mListener.onView(mModel!!)
    }


}