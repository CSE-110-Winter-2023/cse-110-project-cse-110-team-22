package edu.ucsd.cse110.cse110lab4part5;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GPSStatus implements LocationListener{
    private Context context;
    private Long lastActiveTime; //sharedPref stores this
    public boolean hasGPSService; //pass the boolean to Mediator
    public String timeSpanDisconnected = "0 m."; //Count the second since last connected
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private LocationManager locationManager;

    /**
     * call executor to ping GPS service every 3 seconds
     * @param context
     */
    public GPSStatus(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        executor.scheduleAtFixedRate(() -> {
            Log.d("ExecutorTest", "1");
            onStatusChanged(LocationManager.GPS_PROVIDER, 0, null);
            Log.d("timeSinceDisconnected", timeSpanDisconnected);
        }, 0, 3, TimeUnit.SECONDS);
    }

    /**
     * run onStatus change in the background
     */
//    private Runnable statusCheckRunnable = () -> onStatusChanged(LocationManager.GPS_PROVIDER, 0, null);


    /**
     * TODO: get the last active time from SharedPrefUti
     */
    public void getLastActiveTime() {
        this.lastActiveTime = SharedPrefUtils.getLastGPSTime(this.context);
    }
    /**
     * store the current active time to SharedPrefUtil
     */
    private void storeLastActiveTime(Long lastActiveTime){
        //sharedPreUtil
        SharedPrefUtils.storeLastGPSTime(this.context,lastActiveTime);
    }
    /**
     * update the time span of GPS service being disconnected to timeDisconnected
     */
    private void timeSpanDisconnected(){
        getLastActiveTime();
        Long currentTime=System.currentTimeMillis()/60000; //get current time in milliseconds
        if(this.lastActiveTime == -1) {
            lastActiveTime=currentTime;
            SharedPrefUtils.storeLastGPSTime(this.context,this.lastActiveTime);
        }
        long timeSpanDisconnectedLong = currentTime-lastActiveTime;
        if(timeSpanDisconnectedLong<60){
            timeSpanDisconnected = String.valueOf(timeSpanDisconnectedLong)+" m.";
            return;
        }
        timeSpanDisconnected= String.valueOf(timeSpanDisconnectedLong/60)+"hr. ";
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    /**
     * check gps status and update the UI accordingly
     * @param provider
     * @param status
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Long currentTime=System.currentTimeMillis()/60000; //get current time in minutes
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //update lastActiveTime and store it to SharePrefUtil
            lastActiveTime = currentTime;
            hasGPSService=true;
            storeLastActiveTime(lastActiveTime);
            Log.d("hasGPSService",String.valueOf(hasGPSService));

        }
        else{
            //update timeSpanDisconnected,inform mediator hasGPSService=false
            timeSpanDisconnected();
            hasGPSService=false;
        }
//        notifyObservers();
        Log.d("GPSStatus",String.valueOf(hasGPSService));
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

}