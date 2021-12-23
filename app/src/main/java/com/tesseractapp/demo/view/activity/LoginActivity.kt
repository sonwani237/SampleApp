package com.tesseractapp.demo.view.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tesseractapp.demo.R
import com.tesseractapp.demo.base.BaseActivity
import com.tesseractapp.demo.databinding.ActivityLoginBinding
import com.tesseractapp.demo.network.ApiResponse
import com.tesseractapp.demo.network.Status
import com.tesseractapp.demo.app.MyApplication
import com.tesseractapp.demo.utils.UtilMethods
import com.tesseractapp.demo.viewmodel.LoginViewModel
import com.tesseractapp.demo.viewmodel.ViewModelFactory
import javax.inject.Inject

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    @Inject
    lateinit var viewModelProviders: ViewModelFactory

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var viewModel: LoginViewModel

    override val layoutRes: Int
        get() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MyApplication).getAppComponent().doInjection(this)
        viewModel = ViewModelProviders.of(this, viewModelProviders).get(LoginViewModel::class.java)
        dataBinding.viewModel = viewModel
        observeData()
    }

    private fun observeData() {

        //adding observer on login button click if validation work it will return request model data
        viewModel.observeLoginRes().observe(this, Observer { data ->
            run {
                if (data != null) {
                    UtilMethods.hideKeyboard(this, dataBinding.parent)
                    if (UtilMethods.checkInternetAvailability(this)){
                        viewModel.hitLogin(data)
                    }else{
                        showNoInternetDialog()
                    }
                }
            }
        })

        viewModel.apiResponse.observe(this, { data -> onLoginRes(data) })

    }

    private fun onLoginRes(apiResponse: ApiResponse) {

        //when you get response from api you can access data like {apiResponse.data}

        Log.e("LoginResp", "onLoginRes: " + apiResponse.data+"onError: " + apiResponse.error)
        when (apiResponse.status) {

            Status.LOADING -> Toast.makeText(this, "Data Loading....", Toast.LENGTH_LONG).show()

            Status.SUCCESS -> {
                Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, Dashboard::class.java))
                finish()
            }

            Status.ERROR -> {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        }

    }

    //after success move to dashboard
    private fun moveToDashboard(){
        startActivity(Intent(this,Dashboard::class.java))
    }

    private fun showNoInternetDialog() {
        val snackBar = UtilMethods.snackBarLong(this, getString(R.string.network_error), dataBinding.parent)
        snackBar.setAction(getString(R.string.retry)) { observeData() }
    }


}
