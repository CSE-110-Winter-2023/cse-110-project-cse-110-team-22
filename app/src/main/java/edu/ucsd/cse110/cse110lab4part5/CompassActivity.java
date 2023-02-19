package edu.ucsd.cse110.cse110lab4part5;

import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompassActivity extends AppCompatActivity {

    private MutableLiveData<Pair<Double, Double>> locationValue;
    private UserLocationService userLocationService;
    private UserOrientationService orientationService;
    private UserLocation userLocation;
    private double userOrientation;
    private double mockOrientationD;
    private int count = 0;
    private double mockAngle = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);


        TextView orienta = (TextView)findViewById(R.id.orienta);
        TextView loca = (TextView)findViewById(R.id.loca);


        // get location data
        Bundle extras = getIntent().getExtras();
        //added junlin chen
        mockAngle = extras.getDouble("mock_angle");
        //
        Location familyLocation = new LandmarkLocation(extras.getDouble("family_longitude"),
                extras.getDouble("family_latitude"),
                extras.getString("family_label"), 0);

        Location friendLocation = new LandmarkLocation(extras.getDouble("friend_longitude"),
                extras.getDouble("friend_latitude"),
                extras.getString("friend_label"), 1);

        Location homeLocation = new LandmarkLocation(extras.getDouble("home_longitude"),
                extras.getDouble("home_latitude"),
                extras.getString("home_label"), 2);
        List<Location> locList = new ArrayList<>();
        locList.add(familyLocation);
        locList.add(friendLocation);
        locList.add(homeLocation);

        userLocationService = UserLocationService.singleton(this);
        orientationService = UserOrientationService.singleton(this);

        orientationService.getOrientation().observe(this, orient -> {
            userOrientation = Math.toDegrees((double)orient);
            orienta.setText(Float.toString(orient));
            double mockOrientationR = Math.toRadians(userOrientation) + Math.toRadians(mockAngle);
            mockOrientationD = Math.toDegrees(mockOrientationR);
            update(mockOrientationD, LocationUtils.computeAllAngles(userLocation, locList));
        });
        userLocationService.getLocation().observe(this, loc -> {
            userLocation = UserLocation.singleton(loc.first, loc.second, "You");
            update(mockOrientationD, LocationUtils.computeAllAngles(userLocation, locList));
        });

        // Hardcoded user location for demo purposes, WIP
        // Location is UCSD center campus facing north (For now we are ignoring user orientation)
        // Location userLocation = new UserLocation(32.88014354083708, -117.2318005216365, "selfLocation");

        // update location data
        //updateCircleAngle(R.id.familyhouse, (float)LocationUtils.computeAngle(userLocation, familyLocation));
        //updateCircleAngle(R.id.friend, (float)LocationUtils.computeAngle(userLocation, friendLocation));
        //updateCircleAngle(R.id.home, (float)LocationUtils.computeAngle(userLocation, homeLocation));

    }
    private void updateCircleAngle(int imageViewId, float angle) {
        ImageView imageView = findViewById(imageViewId);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.circleAngle = angle;
        imageView.setLayoutParams(layoutParams);
    }

    public void update(double userOrientation, Map<Integer, Double> directionMap){
        for (Map.Entry<Integer, Double> entry : directionMap.entrySet()) {
            int imageViewId = entry.getKey();
            double direction = entry.getValue();
            double directionRadians = Math.toRadians(direction);
            directionRadians += Math.toRadians(userOrientation);
            float directionDegree = (float) Math.toDegrees(directionRadians);

            updateCircleAngle(imageViewId, directionDegree);
        }
    }
}

