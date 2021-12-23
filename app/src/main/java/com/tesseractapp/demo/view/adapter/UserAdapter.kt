package com.tesseractapp.demo.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tesseractapp.demo.R
import com.tesseractapp.demo.databinding.UserItemsBinding
import com.tesseractapp.demo.listeners.NewsListener
import com.tesseractapp.demo.models.UserList
import com.tesseractapp.demo.viewmodel.NewsItemViewModel

class UserAdapter(val mListener: NewsListener) : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    var mDataList: ArrayList<UserList.UserData>? = null

    init {
        mDataList = arrayListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val dataBinding: UserItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.user_items, parent, false)
        return MyViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return mDataList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindList(mDataList!![position], mListener)
    }

    fun setList(dataList: ArrayList<UserList.UserData>) {
        mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemBinding: UserItemsBinding) : RecyclerView.ViewHolder(itemBinding.item) {
        private var mItemBinding: UserItemsBinding? = itemBinding

        fun bindList(mList: UserList.UserData, mListener: NewsListener) {
            if (mItemBinding!!.itemViewModel == null) {
                mItemBinding!!.itemViewModel = NewsItemViewModel(mList, mListener)
            } else {
                mItemBinding!!.itemViewModel!!.setData(mList)
            }
        }
    }

}