package com.example.pacermirror.playvideo.view.activity

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.*
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.notificationdemo.Config
import com.example.pacermirror.NotificationUtils
import com.example.pacermirror.R
import com.example.pacermirror.authentication.UpdateFcmKey
import com.example.pacermirror.classes.Constants
import com.example.pacermirror.classes.MyApplication
import com.example.pacermirror.classes.T
import com.example.pacermirror.database.TABLE_ADD_LIST
import com.example.pacermirror.errorHandler.ExceptionHandler
import com.example.pacermirror.playvideo.F
import com.example.pacermirror.playvideo.model.AddDetails
import com.example.pacermirror.playvideo.view.fragment.CurrentLocationFragment
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_download_video.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.File
import java.io.IOException

class DownloadVideoActivity : AppCompatActivity() {

    var CONTENT_DATA = ArrayList<AddDetails>()
    var CONTENT_DATA_TEMP = ArrayList<AddDetails>()
    var cnt : Int = 0
    var contentName : String? = null
    var contentType : String? = null
    var mediaController : MediaController? = null

    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        setContentView(R.layout.activity_download_video)
        marqueeText.setSelected(true)
        mediaController = MediaController(this)

        //call fragment
        val fm = supportFragmentManager
        val currentLocationFragment = CurrentLocationFragment()
        fm.beginTransaction().add(R.id.main_contenier, currentLocationFragment).commit()

        CONTENT_DATA.add(AddDetails("Nippon1.mp4","v"))
        CONTENT_DATA.add(AddDetails("Nippon2.mp4","v"))

        //play staic video
        playVieo(mediaController!!)

        //CONTENT_DATA = TABLE_ADD_LIST.selectContent()
        /*updateFcmKeyf()
        //first download all content data and store into local
        var DEVICE_ID = MyApplication.prefs.getString(Constants.DEVICE_ID,"0");

        if(T.isNetworkAvailable())
        {
            CONTENT_DATA = TABLE_ADD_LIST.selectContent()
            if(CONTENT_DATA.isEmpty())
            {
                fetchAddList(DEVICE_ID)
            }
            else
            {
                playVieo(mediaController!!)
            }
        }
        else
        {
            CONTENT_DATA = TABLE_ADD_LIST.selectContent()
            if(CONTENT_DATA.isEmpty())
            {
                T.t("Oops ! no internet connection")
            }
            else
            {
                playVieo(mediaController!!)
            }

        }*/
    }
    var INDEX_DATA = ArrayList<Int>()
    private fun updateFcmKeyf() {


        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                // checking for type intent filter
                if (intent.action == Config.REGISTRATION_COMPLETE) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)


                    if(T.isNetworkAvailable())
                    {
                        displayFirebaseRegId()
                    }

                }
                else if (intent.action == Config.PUSH_NOTIFICATION)
                {
                    // new push notification is received

                    try
                    {
                        val messageJson = intent.getStringExtra("message")
                        Log.e("PACER_LOG","messageJson : "+messageJson)

                        if(messageJson != null)
                        {
                            val json = JSONTokener(messageJson).nextValue()
                            if(json is JSONObject)
                            {
                                val messageJsonObject = JSONObject(messageJson)

                                // var dataJSONOBJ = messageJsonObject.getJSONObject("data")
                                var device_id = messageJsonObject.getString("device_id")
                                var type = messageJsonObject.getString("type")//add remove status
                                var file_name = messageJsonObject.getString("file_name")

                              //  Log.e("PACER_LOG","device_id : "+device_id)
                              //  Log.e("PACER_LOG","type : "+type)
                              //  Log.e("PACER_LOG","file_name : "+file_name)
                                var jsonArray = JSONArray(file_name)

                                if(type.equals("RM"))//REmove
                                {
                                    //check content data is empty or not
                                    if(!CONTENT_DATA.isEmpty())
                                    {
                                        INDEX_DATA.clear()
                                        Log.e("PACER_LOG","jsonArray.length() : "+jsonArray.length())
                                        //delete file from
                                        for(a in 0 until jsonArray.length())
                                        {
                                            var fileDataArray = jsonArray.get(a).toString().split("#")
                                            var jsonFileName = fileDataArray.get(1)
                                            Log.e("PACER_LOG","json  file_name : "+jsonFileName)

                                            for(b in 0 until CONTENT_DATA.size)
                                            {
                                                Log.e("PACER_LOG","CONTENT_DATA["+b+"]")
                                            }
                                            for(i in 0 until CONTENT_DATA.size)
                                            {

                                                var fileName = CONTENT_DATA.get(i).contentName.trim()
                                                if(fileName.equals(jsonFileName))
                                                {
                                                    F.deleteVideoFile(jsonFileName)
                                                    INDEX_DATA.add(i)
                                                }
                                                else
                                                {
                                                    Log.e("PACER_LOG","else")
                                                }

                                            }
                                        }
                                        Log.e("PACER_LOG","before delete CONTENT_DATA size : "+CONTENT_DATA.size)
                                        //remove arraylist data
                                        if(!INDEX_DATA.isEmpty())
                                        {
                                            for(i in 0 until INDEX_DATA.size)
                                            {
                                                CONTENT_DATA.removeAt(INDEX_DATA.get(i))
                                                Log.e("PACER_LOG","DELETED DATA")
                                            }
                                        }
                                        Log.e("PACER_LOG","after delete CONTENT_DATA size : "+CONTENT_DATA.size)
                                    }
                                    else
                                    {
                                        T.e("Remove : No add available to delete")
                                    }
                                }
                                else  if(type.equals("SV"))//Save
                                {
                                    CONTENT_DATA_TEMP.clear()
                                    for (i in 0 until jsonArray.length())
                                    {
                                        var addData = jsonArray.get(i).toString()

                                        Log.e("PACER_LOG","addData : "+addData)
                                        if(addData.contains("#"))
                                        {
                                            var addDataArray = addData.split("#")
                                            if(addDataArray.size == 3)
                                            {
                                                var id = addDataArray.get(0)
                                                var fileName = addDataArray.get(1)
                                                var type = addDataArray.get(2)
                                                //check duplicate add
                                                var status = TABLE_ADD_LIST.checkDuplicateAdd(id)
                                                if(status.equals("0"))
                                                {

                                                    TABLE_ADD_LIST.insertAddList(id,fileName,type)
                                                    Log.e("PACER_LOG","insert local : "+addDataArray)
                                                    CONTENT_DATA_TEMP.add(AddDetails(fileName,type))
                                                }

                                            }
                                        }
                                    }
                                    cntDownload = 0
                                    //ready to download content data second
                                    readyToDownloadVideo("second")
                                }
                            }
                            else
                            {
                                Log.e("PACER_LOG","Invalid JSON format : "+messageJson)
                            }
                        }
                        else
                        {
                            Log.e("PACER_LOG","messageJson null found")

                        }
                    }
                    catch (e : Exception)
                    {
                        Log.e("PACER_LOG","Exception : "+e)
                    }
                }
            }
        }
        displayFirebaseRegId()
    }

    private fun displayFirebaseRegId()
    {
        Log.e("PACER_LOG", "displayFirebaseRegId() call")
        val pref = applicationContext.getSharedPreferences(Config.SHARED_PREF, 0)
        val regId = pref.getString("regId", null)
        Log.e("PACER_LOG", "Firebase reg id: " + regId!!)
        var DEVICE_ID = MyApplication.prefs.getString(Constants.DEVICE_ID,"0");
        UpdateFcmKey.updateFcmKey(DEVICE_ID,regId)
    }
    private fun fetchAddList(DEVICE_ID : String)
    {
        try
        {
            var progressDialog = ProgressDialog(this);
            progressDialog.setMessage("Downloading Video...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            //  Log.e("DDDDDDDDDDDD","DEVICE_ID : "+DEVICE_ID)
            val stringRequest = object : StringRequest
                (

                Request.Method.POST,
                Constants.GET_VIDEO_LIST,
                Response.Listener<String>
                {
                        response ->
                    progressDialog.dismiss()

                    //   Log.e("DDDDDDDDDDDD",""+response)
                    parseResponse(response)

                },
                object : Response.ErrorListener
                {
                    override fun onErrorResponse(volleyError: VolleyError)
                    {
                        progressDialog.dismiss()
                        T.t("Volley : "+volleyError)

                    }
                }
            )
            {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>
                {
                    val params = HashMap<String, String>()
                    params.put(Constants.DEVICE_ID, DEVICE_ID)

                    return params
                }


            }
            //adding request to queue
            MyApplication.instance?.addToRequestQueue(stringRequest)

        }
        catch (c : Exception)
        {
            //M.t(""+c)
        }
    }


    private fun parseResponse(response: String?) {


        var id = Constants.NA
        var content_name = Constants.NA
        var type = Constants.NA
        try
        {
            if(response != null || response!!.length > 0)
            {
                var json = JSONTokener(response).nextValue()

                if(json is JSONObject)
                {
                    var jsonObject = JSONObject(response)
                    var status = jsonObject.getString("status")
                    if(status.equals("1"))
                    {
                        var jsonArray = jsonObject.getJSONArray("data")

                        for (i in 0 until jsonArray.length())
                        {
                            var jsonObject = jsonArray.getJSONObject(i)

                            if(jsonObject.has("id") && !jsonObject.isNull("id"))
                            {
                                id = jsonObject.getString("id")
                            }

                            if(jsonObject.has("content_name") && !jsonObject.isNull("content_name"))
                            {
                                content_name = jsonObject.getString("content_name")
                            }

                            if(jsonObject.has("type") && !jsonObject.isNull("type"))
                            {
                                type = jsonObject.getString("type")
                            }
                            //check duplicate add
                            var status = TABLE_ADD_LIST.checkDuplicateAdd(id)
                            if(status.equals("0"))
                            {
                                TABLE_ADD_LIST.insertAddList(id,content_name,type)
                            }
                        }
                        T.t("Add successfully fetched")
                        //ready to play video
                        CONTENT_DATA = TABLE_ADD_LIST.selectContent()
                        readyToDownloadVideo("first")

                    }
                    else if(status.equals("0"))
                    {
                        T.t("Oops ! add not found")
                    }
                    else
                    {
                        T.t("Oops ! Server failed")
                    }
                }
                else
                {
                    T.t("Incorrect json format")
                }

            }
            else
            {
                T.t("response != null || response.length > 0")
            }
        }
        catch (e : Exception)
        {
            T.t("Exception : parseLoginResponse "+e)
        }
    }

    var cntDownload : Int = 0
    private fun readyToDownloadVideo(dnldStatus : String)
    {


        if(dnldStatus.equals("first"))
        {
            if(cntDownload < CONTENT_DATA.size)
            {
                val rootFolder = File(Environment.getExternalStorageDirectory(), "Pacer")
                val root = File(Environment.getExternalStorageDirectory(), "Pacer/"+CONTENT_DATA[cntDownload].contentName)
                Log.e("PATHDATA",""+ Environment.getExternalStorageDirectory())
                if (!rootFolder.exists())
                {
                    rootFolder.mkdirs()
                }
                if (!root.exists())
                {
                    root.createNewFile()
                }
                DownloadData(CONTENT_DATA[cntDownload].contentName,CONTENT_DATA[cntDownload].contentType,root,"first").execute()
            }
            else
            {
                playVieo(mediaController!!)
            }
        }
        else if(dnldStatus.equals("second"))
        {
            if(cntDownload < CONTENT_DATA_TEMP.size)
            {
                val rootFolder = File(Environment.getExternalStorageDirectory(), "Pacer")
                val root = File(Environment.getExternalStorageDirectory(), "Pacer/"+CONTENT_DATA_TEMP[cntDownload].contentName)
                Log.e("PATHDATA",""+ Environment.getExternalStorageDirectory())
                if (!rootFolder.exists())
                {
                    rootFolder.mkdirs()
                }
                if (!root.exists())
                {
                    root.createNewFile()
                }
                DownloadData(CONTENT_DATA_TEMP[cntDownload].contentName,CONTENT_DATA_TEMP[cntDownload].contentType,root,"second").execute()
            }
            else
            {
                playVieo(mediaController!!)
                /*//if content array list is empty then first time no add downloaded when notification came then content add fill and then play video
                if(CONTENT_DATA.isEmpty())
                {
                    CONTENT_DATA = TABLE_ADD_LIST.selectContent()

                }*/
            }
        }
    }

    fun playVieo(mediaController : MediaController)
    {
        if (cnt == CONTENT_DATA.size)
        {
            cnt = 0
            CONTENT_DATA.clear()
            CONTENT_DATA = TABLE_ADD_LIST.selectContent()
            playVieo(mediaController)
        }
        else
        {
            contentName = CONTENT_DATA[cnt].contentName
            // contentName = "Best_Advertisement_ever-Winner_of_Best_Ad_2014.mp4"
            contentType = CONTENT_DATA[cnt].contentType
            //contentType = "v"

            if(contentType.equals("v"))
            {
                addImageView.setVisibility(View.GONE)
                videoViewPlay.setVisibility(View.VISIBLE)
                mediaController!!.setAnchorView(videoViewPlay)
                videoViewPlay.setMediaController(MediaController(this))
                handler.sendEmptyMessage(1)
                //playVieo(mediaController!!)
            }
            /*else if (contentType.equals("i"))
            {
                //set image
                setImageAdd(CONTENT_DATA[cnt].contentName)
                T.e("contentName : "+CONTENT_DATA[cnt].contentName)
                T.e("cnt : "+cnt)

            }*/
            else
            {

                cnt++
                playVieo(mediaController)
            }

        }

    }

    // var imageNameDisp : String? = null
    fun setImageAdd(imageName : String)
    {
        object : CountDownTimer(5000, 1000) { // 1000 = 1 sec

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish()
            {
                videoViewPlay.setVisibility(View.GONE)
                addImageView.setVisibility(View.VISIBLE)
                var imagePath = Environment.getExternalStorageDirectory().toString() + "/Pacer/"+imageName
                val bitmap = BitmapFactory.decodeFile(imagePath)
                addImageView.setImageBitmap(bitmap)
                cnt++
                playVieo(mediaController!!)
            }
        }.start()


    }
    var handler: Handler = object : Handler()
    {

        override fun handleMessage(msg: Message)
        {

            val pos = msg.what
            if (pos == 1)
            {

                var fileExist = File(Environment.getExternalStorageDirectory().toString() + "/Pacer/"+contentName)

                if(fileExist.exists())
                {
                    videoViewPlay.setVideoPath(Environment.getExternalStorageDirectory().toString() + "/Pacer/"+contentName)
                    videoViewPlay.requestFocus()
                    videoViewPlay.start()
                    //insert playing video name
                    //MyApplication.editor.putString(Constants.ADD_CONTENT_NAME,contentName).commit()

                    Log.d("Before Video Finish", "i m in before video finish")
                    videoViewPlay.setOnCompletionListener {

                        cnt++
                        playVieo(mediaController!!)

                    }
                }
                else
                {
                    cnt++
                    playVieo(mediaController!!)
                }

            }
        }
    }
    inner class DownloadData(internal var contentName: String,internal var contentType: String,internal var localFile: File,internal  var dnldStatus : String) : AsyncTask<Void, Void, Void>()
    {
        val progressDialog = ProgressDialog(this@DownloadVideoActivity)
        override fun onPreExecute() {
            super.onPreExecute()
            if(dnldStatus.equals("first"))
            {
                progressDialog.setMessage("Downloading... "+(cntDownload + 1)+"/"+CONTENT_DATA.size)
                progressDialog.show()
            }
            else if(dnldStatus.equals("second"))
            {
                textViewDnldtext.setVisibility(View.VISIBLE)
                textViewDnldtext.setText("Downloading... "+(cntDownload + 1)+"/"+CONTENT_DATA_TEMP.size)
            }
        }


        override fun doInBackground(vararg urls: Void): Void?
        {
            try
            {
                F.downloadAndSaveFile(contentName,contentType,localFile)
            }
            catch (e: IOException)
            {
                Log.d("PACER", "doInBackground Exception : $e")
                e.printStackTrace()
            }

            return null
        }
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)

            if(dnldStatus.equals("first"))
            {
                progressDialog.dismiss()
            }
            else if(dnldStatus.equals("second"))
            {
                textViewDnldtext.setVisibility(View.GONE)
            }


            if(dnldStatus.equals("second"))
            {
                CONTENT_DATA.add(AddDetails(contentName,contentType))
            }

            cntDownload++
            readyToDownloadVideo(dnldStatus)

        }
    }

    override fun onResume() {
        super.onResume()

        /*// register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mRegistrationBroadcastReceiver!!,
            IntentFilter(Config.REGISTRATION_COMPLETE)
        )

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mRegistrationBroadcastReceiver!!,
            IntentFilter(Config.PUSH_NOTIFICATION)
        )

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(applicationContext)*/
    }

    override fun onPause() {
       // LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver!!)
        super.onPause()
    }
}
