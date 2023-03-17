package edu.ucsd.cse110.cse110lab4part5;

import static edu.ucsd.cse110.cse110lab4part5.UserUUID.String_toUUID;


import android.annotation.SuppressLint;

import android.Manifest;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.util.Pair;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompassActivity extends AppCompatActivity {
    static final int NORTH = 3;

    private Map<Integer, Integer> nameToDot;

    private final int initial = 450;

    private final int First = 1;
    private final int Second = 2;
    private final int Third = 3;
    private final int Fourth = 4;

    private FriendMediator friendMediator = FriendMediator.getInstance();

    private Location userLocation;
    private double userOrientation;
    private double mockOrientationD;
    private int count = 0;
    private double mockAngle = 0.0;
    private boolean GPSSignalGood;
    private String GPSStatusStr;
    Map<String, Friend> uuidToFriendMap;
    Map<String, Integer> uuidToDisplayDistMap = new HashMap<>();
    Map<String, TextRect> uuidToTextRectMap = new HashMap<>();


    LandmarkLocation northLocation;

    int OUTER_THRES = 12000; // The distance of the biggest circle
    int STATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Handle location permissions
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            Log.d("MainActivity", "Asking for location permissions");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }
        STATE = 2;
        userLocation = UserLocation.singleton(0, 0, "You");

        setContentView(R.layout.activity_compass);
        updateRingUI();

        nameToDot = new HashMap<>();
        FriendMediator.getInstance().setCompassActivity(this);
        GPSStatus gpsStatus = new GPSStatus(this);

        try {
            // get orientation offset
            Bundle extras = getIntent().getExtras();
            //added junlin chen
            mockAngle = extras.getDouble("mock_angle");
        } catch (Exception e) {
            mockAngle = 0;
        }


        //north
        northLocation = new LandmarkLocation(90, 10, "North_Pole");
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


    /**
     * intermediate method for calling UI update,
     * recalculating the angle and get data from different Map
     * @param userOrientation   user's direction
     * @param uuidToAngleMap    map for friend's angle (UUID, angle)
     * @param uuidToTextRectMap map for display info (UUID, TextRect)
     */
    public void updateUI(double userOrientation, Map<String, Double> uuidToAngleMap,
                         Map<String, TextRect> uuidToTextRectMap){
        for (String uuid: uuidToAngleMap.keySet()) {

            double angle = uuidToAngleMap.get(uuid);
            double angleRadian = Math.toRadians(angle);
            angleRadian -= Math.toRadians(userOrientation);
            float angle_float = (float) Math.toDegrees(angleRadian);
            int dist = uuidToTextRectMap.get(uuid).getCenterDist();
            int int_UUID = String_toUUID(uuid);
            int dot_UUID = nameToDot.get(int_UUID);
            String newName = uuidToTextRectMap.get(uuid).getName();
            updateCircleAngle(dot_UUID, int_UUID, angle_float, dist, newName);

        }

        updateCircleAngle(R.id.letter_n, -(float)userOrientation);

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

    /**
     * updating North pointer
     * @param imageViewId   image id to be updated
     * @param angle angle to be set
     */
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
        for (String uuid : uuidToFriendMap.keySet()) {
            String name = uuidToFriendMap.get(uuid).getName();
            double angle = uuidToAngleMap.get(uuid);
            double dist = uuidToDistanceMap.get(uuid);
            updateDisplayMaps(uuid, name, angle, dist, STATE);
        }
        avoidCollisions(uuidToTextRectMap, 0);

        TextView textView = findViewById(R.id.orienta);
        textView.setText("Orientation: "+String.valueOf(userOrientation));
        updateUI(userOrientation, uuidToAngleMap, uuidToTextRectMap);
        updateGPS();
    }

    /**
     * updates GPS UI based on the GPS status, also display time.
     */
    public void updateGPS(){
        ImageView green = findViewById(R.id.green);
        ImageView red = findViewById(R.id.red);
        TextView time = findViewById(R.id.time);
        Boolean status = this.GPSSignalGood;
        String timedisplay = this.GPSStatusStr;
        time.setText(timedisplay);
        if(status == true){
            green.setVisibility(View.VISIBLE);
            red.setVisibility(View.INVISIBLE);
            time.setVisibility(View.INVISIBLE);
        }
        else{
            green.setVisibility(View.INVISIBLE);
            red.setVisibility(View.VISIBLE);
            time.setVisibility(View.VISIBLE);
        }

    }

    /**
     * update UI for each Friend, dot and name are paired up for each friend.
     * also check if friend is on edged.
     * @param imageViewId dot on the edge
     * @param textViewId friend's name
     * @param angle friend's direction
     * @param distance  friend's distance to me
     */
    void updateCircleAngle(int imageViewId, int textViewId, float angle, int distance, String newName) {
        ImageView imageView = findViewById(imageViewId);

        // Set UI icons to border if their distance would bring them past it
        // TODO: should be changed when zooming in/out implemented
        if(distance > initial){
            distance = initial;
        }

        TextView textView = findViewById(textViewId);
        textView.setText(newName);
        ConstraintLayout.LayoutParams layoutParamsText = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
        layoutParamsText.circleAngle = angle;
        layoutParamsText.circleRadius = distance;
        textView.setLayoutParams(layoutParamsText);
        ConstraintLayout.LayoutParams layoutParamsDot = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        //TODO: This seems to update the text layout params,
        layoutParamsDot.circleAngle = angle;
        layoutParamsDot.circleRadius = distance;
        imageView.setLayoutParams(layoutParamsDot);


        if(distance >= initial){
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }
        else{
            imageView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }


    }

    /**
     * create friends dynamically with given id and name
     * create both name and dot for each friend with default setup
     * @param id  textview id - translate from UUID
     * @param name  friend's name
     */
    public void addFriendToCompass(Integer id, String name){
        ConstraintLayout constraintLayout = findViewById(R.id.clock);
        TextView textView = new TextView(this);
        textView.setId(id);
        textView.setText(name);
        textView.setGravity(Gravity.CENTER);
        ImageView myImage = new ImageView(this);
        myImage.setImageResource(R.drawable.dot);
        int imageID = View.generateViewId();
        nameToDot.put(id, imageID);
        myImage.setId(imageID);
        textView.setTextColor(Color.BLACK);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                400, // width
                100 // height
        );
        layoutParams.circleConstraint = R.id.clock;
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.bottomToTop = R.id.clock_face;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.circleRadius = 50;

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

        //TODO: This works but other stuff doesn't
        TextView textView2 = findViewById(id);
        ConstraintLayout.LayoutParams layoutParamsText = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
        layoutParamsText.circleAngle = 90;
        layoutParamsText.circleRadius = 100;
        textView.setLayoutParams(layoutParamsText);




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

    public void updateDisplayMaps(String uuid, String name, double angle, double miles, int state) {
        double outerCircle = initial;
        double dist = 0;
        switch (state) {
            case 1: { // 0-1 :1 circle
                dist = 430.0 * miles;
                Log.d("Case1", String.valueOf(state));
                break;
            }
            case 2: { // 1-10 : 2 circles
                double inner = outerCircle / 2;
                double outer = outerCircle / 2;
                if (miles < 1) {
                    dist = inner * miles;
                    break;
                }
                dist = inner + outer / (10 - 1) * (miles - 1);
                Log.d("Case2", String.valueOf(state));
                break;
            }
            case 3: { // 10-500 : 3 circles
                double inner1 = outerCircle / 3;
                double inner2 = outerCircle / 3;
                double outer = outerCircle / 3;
                if (miles < 1) {
                    dist = inner1 * miles;
                    break;
                } else if (miles < 10) {
                    dist = inner1 + inner2 / (10 - 1) * (miles - 1);
                    break;
                } else {
                    dist = inner1 + inner2 + outer / (500 - 10) * (miles - 10);
                    Log.d("Case3", String.valueOf(state));
                    break;
                }
            }
            case 4: { // 500+ : 4 circles
                double inner1 = outerCircle / 4;
                double inner2 = outerCircle / 4;
                double inner3 = outerCircle / 4;
                double outer = outerCircle / 4;
                if (miles < 1) {
                    dist = inner1 * miles;
                    break;
                } else if (miles < 10) {
                    dist = inner1 + inner2 / (10 - 1) * (miles - 1);
                    break;
                } else if (miles < 500) {
                    dist = inner1 + inner2 + inner3 / (500 - 10) * (miles - 10);
                    break;
                }
                dist = inner1 + inner2 + inner3 + outer / (OUTER_THRES - 500) * (miles - 500);
                Log.d("Case4", String.valueOf(state));
                break;
            }
        }
        Log.d("Distance:::", String.valueOf(dist));
        Log.d("STATE:::", String.valueOf(state));
        uuidToTextRectMap.put(uuid, new TextRect(name, (int)(dist + 0.5), angle));
    }

    private void avoidCollisions(Map<String, TextRect> uuidToTextRectMap, int iteration){
        int maxIteration = 50;
        if (iteration >= maxIteration) return;
        iteration += 1;
        List<TextRect> textRectslist = new ArrayList<TextRect>(uuidToTextRectMap.values());
        boolean needIter = false;
        for (int i = 0; i < textRectslist.size(); i++) {
            for (int j = i + 1; j < textRectslist.size(); j++) {
                TextRect tr1 = textRectslist.get(i);
                TextRect tr2 = textRectslist.get(j);
                if (!TextRect.intersect(tr1, tr2)) continue;
                Log.d("Collision", tr1.name + " / " + tr2.name);
                needIter = true;
                boolean truncateSuccess = false;
                if (tr1.truncate() == true) truncateSuccess = true;
                if (tr2.truncate() == true) truncateSuccess = true;
                if (!truncateSuccess) {
                    TextRect.nudge(tr1, tr2);
                }
            }
        }
        if (needIter) avoidCollisions(uuidToTextRectMap, iteration);
    };

    /**
     * button handler for zoom in
     * @param view
     */
    public void zoom_in(View view) {
        if(STATE > First){
            STATE -= First;
            updateRingUI();
            display();
        }
    }

    /**
     * button handler for zoom out
     * @param view
     */
    public void zoom_out(View view) {
        if(STATE < Fourth){
            STATE += First;
            updateRingUI();
            display();
        }
    }

    /**
     * updates ring UI to different stages
     */
    public void updateRingUI(){
        int stage = STATE;
        ImageView ring12 = findViewById(R.id.ring12);
        ImageView ring14 = findViewById(R.id.ring14);
        ImageView ring34 = findViewById(R.id.ring34);
        ImageView ring13 = findViewById(R.id.ring13);
        ImageView ring23 = findViewById(R.id.ring23);
        if(stage == First){
            ring12.setVisibility(View.INVISIBLE);
            ring14.setVisibility(View.INVISIBLE);
            ring34.setVisibility(View.INVISIBLE);
            ring13.setVisibility(View.INVISIBLE);
            ring23.setVisibility(View.INVISIBLE);
        }
        if(stage == Second){
            ring12.setVisibility(View.VISIBLE);
            ring14.setVisibility(View.INVISIBLE);
            ring34.setVisibility(View.INVISIBLE);
            ring13.setVisibility(View.INVISIBLE);
            ring23.setVisibility(View.INVISIBLE);
        }
        if(stage == Third){
            ring12.setVisibility(View.INVISIBLE);
            ring14.setVisibility(View.INVISIBLE);
            ring34.setVisibility(View.INVISIBLE);
            ring13.setVisibility(View.VISIBLE);
            ring23.setVisibility(View.VISIBLE);
        }
        if(stage == Fourth){
            ring12.setVisibility(View.VISIBLE);
            ring14.setVisibility(View.VISIBLE);
            ring34.setVisibility(View.VISIBLE);
            ring13.setVisibility(View.INVISIBLE);
            ring23.setVisibility(View.INVISIBLE);
        }
    }
}