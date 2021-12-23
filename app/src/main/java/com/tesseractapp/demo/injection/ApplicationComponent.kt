package com.tesseractapp.demo.injection

import com.tesseractapp.demo.view.activity.Dashboard
import com.tesseractapp.demo.view.activity.LoginActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppContext::class, UtilModule::class])
@Singleton
interface ApplicationComponent {
    fun doInjection(loginActivity: LoginActivity)
    fun doInjection(dashboard: Dashboard)
}