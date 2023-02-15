package edu.ucsd.cse110.cse110lab4part5;

import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;

public class CompassActivity extends AppCompatActivity {

    private MutableLiveData<Pair<Double, Double>> locationValue;
    private UserLocationService userLocationService;
    private UserOrientationService orientationService;
    private UserLocation userLocation;
    private float userOrientation;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);


        TextView orienta = (TextView)findViewById(R.id.orienta);
        TextView loca = (TextView)findViewById(R.id.loca);

        userLocationService = UserLocationService.singleton(this);
        orientationService = UserOrientationService.singleton(this);

        orientationService.getOrientation().observe(this, orient -> {
            userOrientation = orient;
            orienta.setText(Float.toString(orient));
        });
        userLocationService.getLocation().observe(this, loc -> {
            userLocation = UserLocation.singleton(loc.first, loc.second, "You");
            if (count++ % 10 == 0) {
                loca.setText(Double.toString(loc.first + count/10) + "" + Double.toString(loc.second - count/10));
            }
        });

        // get location data
        Bundle extras = getIntent().getExtras();
        Location familyLocation = new LandmarkLocation(extras.getDouble("family_longitude"),
                extras.getDouble("family_latitude"),
                extras.getString("family_label"));

        Location friendLocation = new LandmarkLocation(extras.getDouble("friend_longitude"),
                extras.getDouble("friend_latitude"),
                extras.getString("friend_label"));

        Location homeLocation = new LandmarkLocation(extras.getDouble("home_longitude"),
                extras.getDouble("home_latitude"),
                extras.getString("home_label"));



        // Hardcoded user location for demo purposes, WIP
        // Location is UCSD center campus facing north (For now we are ignoring user orientation)
        Location userLocation = new UserLocation(32.88014354083708, -117.2318005216365, "selfLocation");

        // update location data
        updateCircleAngle(R.id.familyhouse, (float)LocationUtils.computeAngle(userLocation, familyLocation));
        updateCircleAngle(R.id.friend, (float)LocationUtils.computeAngle(userLocation, friendLocation));
        updateCircleAngle(R.id.home, (float)LocationUtils.computeAngle(userLocation, homeLocation));

    }
    private void updateCircleAngle(int imageViewId, float angle) {
        ImageView imageView = findViewById(imageViewId);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.circleAngle = angle;
        imageView.setLayoutParams(layoutParams);
    }
}

