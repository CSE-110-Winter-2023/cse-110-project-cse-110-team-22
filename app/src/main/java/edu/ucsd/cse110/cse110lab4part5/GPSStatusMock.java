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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GPSStatusMock implements LocationListener{
    private Context context;
    private Long lastActiveTime; //sharedPref stores this
    public boolean hasGPSService; //pass the boolean to Mediator
    public String timeSpanDisconnected = "0 m."; //Count the second since last connected
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private LocationManager locationManager;
    private  FriendMediator friendMediator = FriendMediator.getInstance();

    /**
     * call executor to ping GPS service every 3 seconds
     * @param context
     */
    public GPSStatusMock(Context context) {
        this.context = context;
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
    public void storeLastActiveTime(Long lastActiveTime){
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

    public ScheduledFuture<?> setMockNotHaveGPSStatus(int max_iteration, int period){
        hasGPSService = false;
        ScheduledFuture<?> toReturn = executor.scheduleAtFixedRate(new Runnable() {
            int count = 0;
            @Override
            public void run() {
                timeSpanDisconnected();
                count++;
                if(count == max_iteration){
                    executor.shutdown();
                }
            }
        }, 0, period, TimeUnit.SECONDS);
        return toReturn;
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