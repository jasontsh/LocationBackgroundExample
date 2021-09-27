package com.jasontsh.interviewkickstart.locationexample

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.util.Log

class LocationService : Service() {

    private lateinit var locationManager : LocationManager
    private val locationListener : MyLocationListener by lazy {
        MyLocationListener(this)
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return super.onStartCommand(intent, flags, startId)
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            10f,
            locationListener
        )
        return super.onStartCommand(intent, flags, startId)
    }

    class MyLocationListener constructor(private val context: Context) : LocationListener {
        override fun onLocationChanged(loc: Location) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(Constants.PREFERENCE_KEY, Context.MODE_PRIVATE)
            var string: String? = sharedPreferences.getString(Constants.PREFERENCE_VALUE_KEY, null)
            if (string == null) {
                string = loc.latitude.toString() + " " + loc.longitude
            } else {
                string += "\n" + loc.latitude + " " + loc.longitude
            }
            sharedPreferences.edit().putString(Constants.PREFERENCE_VALUE_KEY, string).apply()
        }
    }
}
