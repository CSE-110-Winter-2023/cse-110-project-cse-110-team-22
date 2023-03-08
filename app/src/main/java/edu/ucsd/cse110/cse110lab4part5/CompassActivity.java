package edu.ucsd.cse110.cse110lab4part5;

import static edu.ucsd.cse110.cse110lab4part5.UserUUID.String_toUUID;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompassActivity extends AppCompatActivity {
    static final int NORTH = 3;

    private Map<Integer, Integer> nameToDot;

    private int initial = 100;

    private Location userLocation;
    private double userOrientation;
    private double mockOrientationD;
    private int count = 0;
    private double mockAngle = 0.0;
    private boolean GPSSignalGood;
    private String GPSStatusStr;
    Map<String, Friend> uuidToFriendMap;


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

        //Landmark locations
        List<Location> locations = SharedPrefUtils.readAllLocations(this);


        //north
        LandmarkLocation northLocation = new LandmarkLocation(90, 10, "North_Pole");
        northLocation.setIconNum(NORTH);
        List<Location> locList = new ArrayList<>();
        locList.add(northLocation);
    }


    /**
     * Bottom handler to clear data entered
     * @param view
     */
    public void clearDataClicked(View view) {
        SharedPrefUtils.clearLocationSharedPreferences(this);
    }


    public void updateUI(double userOrientation, Map<String, Double> uuidToAngleMap,
                         Map<String, Double> uuidToDistanceMap, Map<String, Friend> uuidToFriendMap){
        for (String uuid: uuidToAngleMap.keySet()) {

            double angle = uuidToAngleMap.get(uuid);
            double angleRadian = Math.toRadians(angle);
            angleRadian -= Math.toRadians(userOrientation);
            float angle_float = (float) Math.toDegrees(angleRadian);
            int dist = uuidToDistanceMap.get(uuid).intValue();
            int int_UUID = String_toUUID(uuid);
            int dot_UUID = nameToDot.get(int_UUID);
            updateCircleAngle(int_UUID, dot_UUID, angle_float, dist);

        }



    }
    public void update(double userOrientation, Map<Integer, Double> directionMap){
        for (Map.Entry<Integer, Double> entry : directionMap.entrySet()) {
            int imageViewId = entry.getKey();
            double direction = entry.getValue();
            double directionRadians = Math.toRadians(direction);
            directionRadians -= Math.toRadians(userOrientation);
            float directionDegree = (float) Math.toDegrees(directionRadians);
        }
    }

    public void update(double userOrientation, Map<String, Double> uuidToAngleMap, Map<String, Double> uuidToDistanceMap){
        for (String uuid: uuidToAngleMap.keySet()) {

            double angle = uuidToAngleMap.get(uuid);
            double angleRadian = Math.toRadians(angle);
            angleRadian -= Math.toRadians(userOrientation);
            float angle_float = (float) Math.toDegrees(angleRadian);
            int dist = uuidToDistanceMap.get(uuid).intValue();
            int int_UUID = String_toUUID(uuid);
            int dot_UUID = nameToDot.get(int_UUID);
            updateCircleAngle(int_UUID, dot_UUID, angle_float, dist);

        }



    }
    void updateCircleAngle(int imageViewId, float angle) {
        ImageView imageView = findViewById(imageViewId);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.circleAngle = angle;
        imageView.setLayoutParams(layoutParams);
    }

    /**
     * Bottom handler for go back to enter_friend_uid
     * @param view
     */
    public void go_back(View view) {
        finish();
    }

    public void updateGPSStatus() {
        // TODO
    }

    /**
     * update uuidToFriendMap and update UI. This is called when the server updates or
     * after a new friend uuid is verified and added by the mediator.
     * @param uuidToFriendMap
     */
    public void updateFriendsMap(Map<String, Friend> uuidToFriendMap) {
        this.uuidToFriendMap = uuidToFriendMap;
    }

    /**
     * prepare what is needed for the UI update, then call updateUI(...).
     * Compute uuidToAngleMap, uuidToDistanceMap, uuidToNameMap inside
     */
    public void callUIUpdate() {
        Map<String, Double> uuidToAngleMap = LocationUtils
                .computeAllFriendAngles(userLocation, uuidToFriendMap);
        Map<String, Double> uuidToDistanceMap = LocationUtils
                .computeAllDistances(userLocation, uuidToFriendMap);

        updateUI(userOrientation, uuidToAngleMap, uuidToDistanceMap, uuidToFriendMap);
    }



    void updateCircleAngle(int imageViewId, int textViewId, float angle, int distance) {
        ImageView imageView = findViewById(imageViewId);
        TextView textView = findViewById(textViewId);
        ConstraintLayout.LayoutParams layoutParamsText = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParamsDot = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        layoutParamsDot.circleAngle = angle;
        layoutParamsText.circleAngle = angle;
        layoutParamsText.circleRadius = distance;
        layoutParamsDot.circleRadius = initial;
        textView.setLayoutParams(layoutParamsText);
        imageView.setLayoutParams(layoutParamsDot);
        if(distance > initial){
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }
        else{
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    public void addFriendToCompass(Integer id, String name){
        ConstraintLayout constraintLayout = findViewById(R.id.clock);
        TextView textView = new TextView(this);
        textView.setId(id);
        textView.setText(name);
        ImageView myImage = new ImageView(this);
        myImage.setImageResource(R.drawable.dot);
        int imageID = View.generateViewId();
        nameToDot.put(id, imageID);
        myImage.setId(imageID);
        textView.setTextColor(Color.BLACK);
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
        myImage.setLayoutParams(layoutParams);
        constraintLayout.addView(textView);
        constraintLayout.addView(myImage);
    }

    public void updateUser(Location userLocation, double userOrientation) {
        this.userLocation = userLocation;
        this.userOrientation = userOrientation;
    }

    public void updateGPSStatus(boolean GPSSignalGood, String GPSStatusStr) {
        this.GPSSignalGood = GPSSignalGood;
        this.GPSStatusStr = GPSStatusStr;
    }

    public void display() {
        callUIUpdate();
    }
}