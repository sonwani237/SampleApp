package com.tesseractapp.demo.view.activity

import android.Manifest
import android.app.Dialog
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tesseractapp.demo.R
import com.tesseractapp.demo.base.BaseActivity
import com.tesseractapp.demo.app.MyApplication
import com.tesseractapp.demo.databinding.ActivityMainBinding
import com.tesseractapp.demo.listeners.NewsListener
import com.tesseractapp.demo.models.UserList
import com.tesseractapp.demo.network.ApiResponse
import com.tesseractapp.demo.network.Status
import com.tesseractapp.demo.services.GPSTracker
import com.tesseractapp.demo.utils.UtilMethods
import com.tesseractapp.demo.view.adapter.UserAdapter
import com.tesseractapp.demo.viewmodel.DashboardViewModel
import com.tesseractapp.demo.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.preview_dialog.*
import javax.inject.Inject
import android.location.Geocoder
import java.util.*
import kotlin.collections.ArrayList


class Dashboard : BaseActivity<ActivityMainBinding>(), NewsListener {

    @Inject
    lateinit var viewModelProviders: ViewModelFactory

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var viewModel: DashboardViewModel
    lateinit var mAdapter: UserAdapter
    private val handler = Handler()
    private var runnable: Runnable? =null

    var PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MyApplication).getAppComponent().doInjection(this)
        viewModel = ViewModelProviders.of(this, viewModelProviders).get(DashboardViewModel::class.java)
        dataBinding.viewModel = viewModel

        viewModel.apiResponse.observe(this, Observer { data -> onApiRes(data) })
        if (UtilMethods.checkInternetAvailability(this)) {
            viewModel.getUsers("1")
        } else {
            UtilMethods.showSnackBar(this, getString(R.string.network_error), dataBinding.parent)
        }

        if (hasPermissions(PERMISSIONS)) {
            getLocation()
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 101)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else {
            UtilMethods.showSnackBar(this, "Please allow permission", dataBinding.parent)
        }
    }

    private fun getLocation() {
        runnable = object : Runnable {
            override fun run() {
                val gpsTracker = GPSTracker(this@Dashboard, this@Dashboard)
                val lat = gpsTracker.latitude
                val lng = gpsTracker.longitude

                val geocoder = Geocoder(this@Dashboard, Locale.getDefault())
                val addresses: List<Address> = geocoder.getFromLocation(lat, lng, 1)
                UtilMethods.printLog("Address",">>> "+addresses[0].locality)
                viewModel.location.set("${addresses[0].locality} $lat/$lat")
                handler.postDelayed(this, 300000) //wait 5 min and run again
            }
        }
        handler.postDelayed(runnable!!, 0)
    }

    private fun hasPermissions( permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun onApiRes(apiResponse: ApiResponse) {
        //when you get response from api you can access data like {apiResponse.data}

        when (apiResponse.status) {

            Status.LOADING -> Toast.makeText(this, "Data Loading....", Toast.LENGTH_LONG).show()

            Status.SUCCESS -> {
                val data = Gson().fromJson(apiResponse.data, UserList::class.java)
                setData(data.data, dataBinding.rvFeeds)
            }

            Status.ERROR -> Toast.makeText(this, "Something went wrong..", Toast.LENGTH_LONG).show()
        }
    }

    private fun setData(model: ArrayList<UserList.UserData>, recyclerView: RecyclerView) {
        mAdapter = UserAdapter(this)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.setList(model)
    }

    override fun onView(mData: UserList.UserData) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.preview_dialog)
        val window: Window? = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.name.text = mData.first_name
        Glide.with(this).load(mData.avatar)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .circleCrop()
            .into(dialog.ivPreviewImg)
        dialog.show()
    }

}
