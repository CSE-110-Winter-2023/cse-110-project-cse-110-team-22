package edu.ucsd.cse110.cse110lab4part5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class  MainActivity extends AppCompatActivity {

    private MutableLiveData<Pair<Double, Double>> locationValue;
    private UserLocationService userLocationService;
    private UserOrientationService orientationService;
    private UserLocation userLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle location permissions
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }

        // initialize services for location, orientation, and UserLocation
        userLocationService = UserLocationService.singleton(this);
        orientationService = new UserOrientationService(this);
        userLocation = UserLocation.singleton(0, 0, "you");

        // check if locations exist already. If so go straight to compass, otherwise to input UI
        if(SharedPrefUtils.hasStoredLocations(this)){

            Intent intent = new Intent(this, CompassActivity.class);
            intent.putExtra("activity_flag", 0);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this, InputCoordinateActivity.class);
            intent.putExtra("activity_flag", 1);
            startActivity(intent);
        }



    }
}