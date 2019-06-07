package com.example.pacermirror.classes

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.PowerManager
import android.support.multidex.MultiDex
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.pacermirror.database.DBHelper

open class MyApplication : Application()
{
    companion object
    {

        lateinit var context: Context
        private val TAG = MyApplication::class.java.simpleName
        @get:Synchronized var instance: MyApplication? = null
            private set

        lateinit var prefs : SharedPreferences
        lateinit var editor : SharedPreferences.Editor
        var db: DBHelper? = null


    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    override fun onCreate()
    {
        super.onCreate()
        context = applicationContext
        instance = this

        //create db object
        if(db == null)
        {
            db = DBHelper(context)
            db!!.writableDatabase
        }

        //shared preferences
        prefs = getSharedPreferences(Constants.PREF_KEY,0)
        editor = prefs.edit()
        editor.commit()

    }
    val requestQueue: RequestQueue? = null
        get()
        {
            if (field == null)
            {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }
    fun <T> addToRequestQueue(request: Request<T>)
    {
        request.tag = TAG
        request.retryPolicy = DefaultRetryPolicy(30000, 0, 0F)
        requestQueue?.add(request)
    }
}
