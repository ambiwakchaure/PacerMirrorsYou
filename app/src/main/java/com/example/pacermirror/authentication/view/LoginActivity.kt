package com.example.pacermirror.authentication.view

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.example.pacermirror.R
import com.example.pacermirror.authentication.viewmodel.LoginResultCallbacks
import com.example.pacermirror.authentication.viewmodel.LoginViewModel
import com.example.pacermirror.authentication.viewmodel.LoginViewModelFactory
import com.example.pacermirror.classes.Constants
import com.example.pacermirror.classes.MyApplication
import com.example.pacermirror.classes.T
import com.example.pacermirror.databinding.ActivityLoginBinding
import com.example.pacermirror.playvideo.F
import com.example.pacermirror.playvideo.view.activity.DownloadVideoActivity
import org.json.JSONArray
import java.util.*

class LoginActivity : AppCompatActivity(), LoginResultCallbacks {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        val activityLogin = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        //insatantiate new ViewModels
        activityLogin.loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory(this,this)).get(LoginViewModel::class.java)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        prseData()
        requestPermission()

    }

    private fun prseData() {

        var data = "[\"58#Nippon_csk.mp4#v\",\"61#Myer_-_Christmas_TV_Ad_2015.mp4#v\",\"62#index.jpeg#i\",\"63#601708.jpg#i\",\"64#videoplayback.mp4#v\",\"65#videoplayback.mp4#v\"]"

        var jsonArray = JSONArray(data)
        for(a in 0 until jsonArray.length())
        {
            var fileDataArray = jsonArray.get(a).toString().split("#")
            var jsonFileName = fileDataArray.get(1)
            Log.e("PACER_LOG","json  file_name : "+jsonFileName)
        }
    }

    override fun onLoginSuccess(deviceId: String)
    {

        MyApplication.editor.putString(Constants.DEVICE_ID,deviceId).commit()
        val intent = Intent(this, DownloadVideoActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLoginFailure(messageFail: String)
    {
        T.t(messageFail)
    }
    override fun displayMessage(messageFail: String)
    {
        T.t(messageFail)
    }
    private fun requestPermission()
    {

        if (F.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        }
        else {
            F.askPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (F.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
        }
        else {
            F.askPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        }

    }
}
