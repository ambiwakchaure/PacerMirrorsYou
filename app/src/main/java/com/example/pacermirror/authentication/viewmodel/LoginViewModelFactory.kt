package com.example.pacermirror.authentication.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.pacermirror.authentication.view.LoginActivity

class LoginViewModelFactory (private val listner : LoginResultCallbacks,val context : LoginActivity) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel (listner,context) as T
    }
}