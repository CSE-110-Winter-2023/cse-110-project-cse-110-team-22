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
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class  MainActivity extends AppCompatActivity {

//    private MutableLiveData<Pair<Double, Double>> locationValue;
//    private UserLocationService userLocationService;
//    private UserOrientationService orientationService;
//    private UserLocation userLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle location permissions
//        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
//            Log.d("MainActivity", "Asking for location permissions");
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
//        }


        // check if locations exist already. If so pass a flag that will indicate to InputCoordinateActivity
        // that it should go straight to compass based on stored data
//        Intent intent = new Intent(this, InputCoordinateActivity.class);
//
//        if(SharedPrefUtils.hasStoredLocations(this)){
//            intent.putExtra("activity_flag", 0);
//        }
//        startActivity(intent);

        Intent intent = new Intent(this, input_name.class);
        startActivity(intent);
    }
}