package edu.ucsd.cse110.cse110lab4part5;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.util.Pair;
import android.view.ViewGroup;
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

    static final int FAMILY = 0;
    static final int FRIEND = 1;
    static final int HOME = 2;
    static final int NORTH = 3;

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
        userLocation = UserLocation.singleton(0, 0, "You");

        setContentView(R.layout.activity_compass);


        try {
            // get orientation offset
            Bundle extras = getIntent().getExtras();
            //added junlin chen
            mockAngle = extras.getDouble("mock_angle");
        } catch (Exception e) {
            mockAngle = 0;
        }

        TextView home_label = findViewById(R.id.home_label_text);
        TextView friend_label = findViewById(R.id.friend_label_text);
        TextView family_label = findViewById(R.id.family_label_text);

        //Landmark locations
        List<Location> locations = SharedPrefUtils.readAllLocations(this);
        LandmarkLocation homeLocation = (LandmarkLocation) locations.get(0);
        homeLocation.setIconNum(HOME);
        LandmarkLocation friendLocation = (LandmarkLocation) locations.get(1);
        friendLocation.setIconNum(FRIEND);
        LandmarkLocation familyLocation = (LandmarkLocation) locations.get(2);
        familyLocation.setIconNum(FAMILY);

        //north
        LandmarkLocation northLocation = new LandmarkLocation(90, 10, "North_Pole");
        northLocation.setIconNum(NORTH);

        home_label.setText(homeLocation.getLabel());
        friend_label.setText(friendLocation.getLabel());
        family_label.setText(familyLocation.getLabel());

        List<Location> locList = new ArrayList<>();
        locList.add(familyLocation);
        locList.add(friendLocation);
        locList.add(homeLocation);
        locList.add(northLocation);


        TextView orienta = (TextView) findViewById(R.id.orienta);
        TextView loca = (TextView) findViewById(R.id.loca);


        userLocationService = UserLocationService.singleton(this);
        orientationService = UserOrientationService.singleton(this);
        userLocation = UserLocation.singleton(0, 0, "you");

        userLocationService.getLocation().observe(this, loc -> {
            userLocation = UserLocation.singleton(loc.first, loc.second, "You");
            update(mockOrientationD, LocationUtils.computeAllAngles(userLocation, locList));
        });

        orientationService.getOrientation().observe(this, orient -> {
            userOrientation = Math.toDegrees((double) orient);
            orienta.setText(Float.toString(orient));
            double mockOrientationR = Math.toRadians(userOrientation) + Math.toRadians(mockAngle);
            mockOrientationD = Math.toDegrees(mockOrientationR);
            update(mockOrientationD, LocationUtils.computeAllAngles(userLocation, locList));
        });

        addFriendToCompass(12345, "jone");
        updateCircleAngle(R.id.familyhouse, 12345, 100, 500);
        addFriendToCompass(556789, "hile");
        updateCircleAngle(R.id.familyhouse, 556789, 340, 400);

    }

    /**
     * This method update the angle of icons in compass activity
     * @param imageViewId ID of icons
     * @param angle angle to update
     */
    void updateCircleAngle(int imageViewId, float angle) {
        ImageView imageView = findViewById(imageViewId);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.circleAngle = angle;
        imageView.setLayoutParams(layoutParams);
    }

    /**
     * Bottom handler to clear data entered
     * @param view
     */
    public void clearDataClicked(View view) {
        SharedPrefUtils.clearLocationSharedPreferences(this);
    }

    /**
     * This method calculate the angle to be updated and call updateCircleAngle
     * @param userOrientation user direction
     * @param directionMap Map that store the angle for each icons
     */
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

    /**
     * Bottom handler for go back to enter_friend_uid
     * @param view
     */
    public void go_back(View view) {
        finish();
    }

    void updateCircleAngle(int imageViewId, int textViewId, float angle, int distance) {
        ImageView imageView = findViewById(imageViewId);
        TextView textView = findViewById(textViewId);
        ConstraintLayout.LayoutParams layoutParamsText = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParamsDot = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        layoutParamsDot.circleAngle = angle;
        layoutParamsText.circleAngle = angle;
        layoutParamsText.circleRadius = distance;
        textView.setLayoutParams(layoutParamsText);
        imageView.setLayoutParams(layoutParamsDot);
    }

    public void addFriendToCompass(Integer id, String name){
        ConstraintLayout constraintLayout = findViewById(R.id.clock);
        TextView textView = new TextView(this);
        textView.setId(id);
        textView.setText(name);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                50, // width
                50 // height
        );
        layoutParams.circleConstraint = R.id.clock;
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.bottomToTop = R.id.clock_face;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.setMargins(
                23, // start margin
                0, // top margin
                23, // end margin
                23 // bottom margin
        );
        textView.setLayoutParams(layoutParams);
        constraintLayout.addView(textView);
    }

}

