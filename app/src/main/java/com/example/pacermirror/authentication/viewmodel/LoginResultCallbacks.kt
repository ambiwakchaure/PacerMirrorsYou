package com.example.pacermirror.authentication.viewmodel

interface LoginResultCallbacks
{
    fun onLoginSuccess(messageSuccess : String)
    fun onLoginFailure(messageFail : String)
    fun displayMessage(messageFail : String)
}