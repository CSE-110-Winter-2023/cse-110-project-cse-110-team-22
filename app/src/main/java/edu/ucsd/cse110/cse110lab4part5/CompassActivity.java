package edu.ucsd.cse110.cse110lab4part5;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Map;

public class CompassActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);


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

