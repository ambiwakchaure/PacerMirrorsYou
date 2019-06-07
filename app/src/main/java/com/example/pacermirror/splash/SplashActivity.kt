package com.example.pacermirror.splash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.pacermirror.R
import com.example.pacermirror.authentication.view.LoginActivity
import com.example.pacermirror.classes.Constants
import com.example.pacermirror.classes.MyApplication
import com.example.pacermirror.playvideo.view.activity.DownloadVideoActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        var USER_ID = MyApplication.prefs.getString(Constants.DEVICE_ID,"0")

        if(USER_ID.equals("0"))
        {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else
        {
            val intent = Intent(this, DownloadVideoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
