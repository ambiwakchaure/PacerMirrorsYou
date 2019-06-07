package com.example.pacermirror.authentication

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.pacermirror.classes.Constants
import com.example.pacermirror.classes.MyApplication

class UpdateFcmKey
{
    companion object {

        fun updateFcmKey(device_id: String,fcm_key : String)
        {
            try
            {


                val stringRequest = object : StringRequest
                    (

                    Request.Method.POST,
                    Constants.LOGIN,
                    Response.Listener<String>
                    {
                            response ->

                        //parseLoginResponse(response)

                    },
                    object : Response.ErrorListener
                    {
                        override fun onErrorResponse(volleyError: VolleyError)
                        {

                        }
                    }
                )
                {
                    @Throws(AuthFailureError::class)
                    override fun getParams(): Map<String, String>
                    {
                        val params = HashMap<String, String>()
                        params.put(Constants.DEVICE_ID, device_id)
                        params.put(Constants.FCM_KEY, fcm_key)
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

    }
}