package com.tesseractapp.demo.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseractapp.demo.models.LoginRequestModel
import com.tesseractapp.demo.network.ApiResponse
import com.tesseractapp.demo.network.Repository
import com.tesseractapp.demo.utils.UtilMethods
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*


class LoginViewModel(private val repository: Repository) : ViewModel() {

    private var job: Job? = null

    val apiResponse = MutableLiveData<ApiResponse>()

    var loginModel: LoginRequestModel? = null
        private set
    var userIdFocusChangeListener: View.OnFocusChangeListener? = null
        private set
    var passFocusChangeListener: View.OnFocusChangeListener? = null
        private set

    private val buttonClick = MutableLiveData<LoginRequestModel>()

    init {
        loginModel = LoginRequestModel()
        userIdFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                loginModel!!.checkIdValid(true)
            }
        }

        passFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                loginModel!!.checkPassValid(true)
            }
        }
    }

    fun observeLoginRes(): MutableLiveData<LoginRequestModel> {
        return buttonClick
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    fun hitLogin(loginRequestModel: LoginRequestModel) {
        job?.cancel()
        ApiResponse.loading()
        job = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                val asyncPost = async {
                    repository.executeLogin(loginRequestModel)
                }

                val response = asyncPost.await()

                if (response.isSuccessful) {
                    viewModelScope.launch(Dispatchers.Main) {
                        apiResponse.value = response.body()?.let { ApiResponse.success(it) }
                    }
                } else {
                    viewModelScope.launch(Dispatchers.Main) {
                        apiResponse.value = ApiResponse.error(response.errorBody().toString())
                    }
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    ApiResponse.error(e.message.toString())
                    UtilMethods.printLog("Success", ">>> $e")
                }
            }
        }
    }

    fun onEmailChange( s: CharSequence, start: Int, before : Int, count :Int){
        loginModel?.emailId = s.toString()
        loginModel!!.checkIdValid(true)
    }

    fun onLoginClick() {
        if (loginModel!!.isIdValid && loginModel!!.isPassValid) {
            buttonClick.setValue(loginModel)
        } else {
            if (!loginModel!!.isIdValid) {
                loginModel!!.checkIdValid(true)
            } else if (!loginModel!!.isPassValid) {
                loginModel!!.checkPassValid(true)
            }
        }
    }

}
