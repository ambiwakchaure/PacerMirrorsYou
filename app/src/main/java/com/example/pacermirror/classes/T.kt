package com.example.pacermirror.classes

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import java.util.*

class T
{
    companion object {

        fun getAddress(lat: Double?, landi: Double?): String {


            var locationAddress = "NA"
            try {
                val geocoder: Geocoder
                val addresses: List<Address>
                geocoder = Geocoder(MyApplication.context, Locale.ENGLISH)

                addresses = geocoder.getFromLocation(
                    lat!!,
                    landi!!,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                val address =
                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()


                if (address == null || address == null || address == "") {
                    locationAddress = "NA"
                } else {
                    locationAddress = "$address."
                }
                //locationAddress = address + ", " + city + ", " + state + ", " + country + ","+postalCode+".";
            } catch (e: Exception) {
                locationAddress = "NA"
            }

            return locationAddress
        }
        fun t(message : String)
        {
            Toast.makeText(MyApplication.context,message, Toast.LENGTH_LONG).show()
        }

        fun e(message: String)
        {
            Log.e("PACER_LOG",message)
        }

        fun isNetworkAvailable(): Boolean
        {
            val connectivityManager = MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }

    }
}