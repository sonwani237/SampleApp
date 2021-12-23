package com.tesseractapp.demo.models

import com.google.gson.annotations.SerializedName

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import com.tesseractapp.demo.R
import com.tesseractapp.demo.utils.MyValidations

class LoginRequestModel : BaseObservable() {

    var emailIdError = ObservableField<Int>()
    var passwordError = ObservableField<Int>()
    var isValid = ObservableField<Boolean>()

    @SerializedName("email")
    var emailId: String? = ""
    @SerializedName("password")
    var password: String? = ""

    val isIdValid: Boolean
        @Bindable
        get() = checkIdValid(false)

    val isPassValid: Boolean
        @Bindable
        get() = checkPassValid(false)

    fun checkIdValid(setMsg: Boolean): Boolean {
        if (this.emailId != null && this.emailId!!.isNotEmpty()) {
            return if (MyValidations.checkEmail(emailId.toString())){
                emailIdError.set(null)
                isValid.set(true)
                true
            }else{
                emailIdError.set(R.string.invalid_email)
                isValid.set(false)
                false
            }
        } else {
            if (setMsg) {
                emailIdError.set(R.string.email_hint)
                isValid.set(false)
            }
        }
        return false
    }

    fun checkPassValid(setMsg: Boolean): Boolean {
        if (this.password != null && this.password!!.isNotEmpty() && password!!.length>5) {
            passwordError.set(null)
            return true
        } else {
            if (setMsg) {
                passwordError.set(R.string.empty_password)
            }
        }
        return false
    }

}
