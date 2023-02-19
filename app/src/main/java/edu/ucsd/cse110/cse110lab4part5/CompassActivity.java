package edu.ucsd.cse110.cse110lab4part5;

import android.os.Bundle;
import android.view.View;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.List;

public class CompassActivity extends AppCompatActivity {

    private MutableLiveData<Pair<Double, Double>> locationValue;
    private UserLocationService userLocationService;
    private UserOrientationService orientationService;
    private UserLocation userLocation;
    private double userOrientation;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        List<Location> locations = SharedPrefUtils.readAllLocations(this);
        LandmarkLocation homeLocation = (LandmarkLocation) locations.get(0);
        homeLocation.setIconNum(2);
        LandmarkLocation friendLocation = (LandmarkLocation) locations.get(1);
        friendLocation.setIconNum(1);
        LandmarkLocation familyLocation = (LandmarkLocation) locations.get(2);
        familyLocation.setIconNum(0);

        List<Location> locList = new ArrayList<>();
        locList.add(familyLocation);
        locList.add(friendLocation);
        locList.add(homeLocation);


        TextView orienta = (TextView)findViewById(R.id.orienta);
        TextView loca = (TextView)findViewById(R.id.loca);


        userLocationService = UserLocationService.singleton(this);
        orientationService = UserOrientationService.singleton(this);

        userLocationService.getLocation().observe(this, loc -> {
            userLocation = UserLocation.singleton(loc.first, loc.second, "You");
            update(userOrientation, LocationUtils.computeAllAngles(userLocation, locList));
        });

        orientationService.getOrientation().observe(this, orient -> {
            userOrientation = Math.toDegrees((double)orient);
            orienta.setText(Float.toString(orient));
            update(userOrientation, LocationUtils.computeAllAngles(userLocation, locList));
        });


    }
    private void updateCircleAngle(int imageViewId, float angle) {
        ImageView imageView = findViewById(imageViewId);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.circleAngle = angle;
        imageView.setLayoutParams(layoutParams);
    }

    public void clearDataClicked(View view) {
        SharedPrefUtils.clearLocationSharedPreferences(this);
    }
    public void update(double userOrientation, Map<Integer, Double> directionMap){
        for (Map.Entry<Integer, Double> entry : directionMap.entrySet()) {
            int imageViewId = entry.getKey();
            double direction = entry.getValue();
            double directionRadians = Math.toRadians(direction);
            directionRadians -= Math.toRadians(userOrientation);
            float directionDegree = (float) Math.toDegrees(directionRadians);

            updateCircleAngle(imageViewId, directionDegree);
        }
    }
}

