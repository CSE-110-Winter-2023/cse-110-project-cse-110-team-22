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
//    private UserOrientationService orientationService;
//    private UserLocationService userLocationService;
//    private Location userLocation;
//    private double userOrientation;

//    private MutableLiveData<Pair<Double, Double>> locationValue;
//    private UserLocationService userLocationService;
//    private UserOrientationService orientationService;
//    private UserLocation userLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if locations exist already. If so pass a flag that will indicate to InputCoordinateActivity
        // that it should go straight to compass based on stored data
//        Intent intent = new Intent(this, InputCoordinateActivity.class);
//
//        if(SharedPrefUtils.hasStoredLocations(this)){
//            intent.putExtra("activity_flag", 0);
//        }
//        startActivity(intent);

        FriendMediator.getInstance().init(this);
        if(SharedPrefUtils.hasName(this)){
            Intent intent = new Intent(this, user_uid_showing.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, input_name.class);
            startActivity(intent);
        }
    }
}