package edu.ucsd.cse110.cse110lab4part5;

import static edu.ucsd.cse110.cse110lab4part5.UserUUID.String_toUUID;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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

    private int initial = 430;

    private FriendMediator friendMediator = FriendMediator.getInstance();

    private Location userLocation;
    private double userOrientation;
    private double mockOrientationD;
    private int count = 0;
    private double mockAngle = 0.0;
    private boolean GPSSignalGood;
    private String GPSStatusStr;
    Map<String, Friend> uuidToFriendMap;
    LandmarkLocation northLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocation = UserLocation.singleton(0, 0, "You");

        setContentView(R.layout.activity_compass);

        nameToDot = new HashMap<>();
        FriendMediator.getInstance().setCompassActivity(this);


        try {
            // get orientation offset
            Bundle extras = getIntent().getExtras();
            //added junlin chen
            mockAngle = extras.getDouble("mock_angle");
        } catch (Exception e) {
            mockAngle = 0;
        }

        //Landmark locations
        //List<Location> locations = SharedPrefUtils.readAllLocations(this);


//        ImageView imageView1 = findViewById(R.id.home);
//        imageView1.setVisibility(View.INVISIBLE);
//        ImageView imageView2 = findViewById(R.id.friend);
//        imageView2.setVisibility(View.INVISIBLE);
//        ImageView imageView3 = findViewById(R.id.familyhouse);
//        imageView3.setVisibility(View.INVISIBLE);
//        ImageView imageView4 = findViewById(R.id.me);
//        imageView4.setVisibility(View.INVISIBLE);
        //north
        northLocation = new LandmarkLocation(90, 10, "North_Pole");
        northLocation.setIconNum(NORTH);
        List<Location> locList = new ArrayList<>();
        locList.add(northLocation);
//        addFriendToCompass(123456, "jone");
//        updateCircleAngle(nameToDot.get(123456),123456,60,150);

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
            updateCircleAngle(dot_UUID, int_UUID, angle_float, dist);

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

        TextView textView = findViewById(R.id.orienta);
        textView.setText("Orientation: "+String.valueOf(userOrientation));
        updateUI(userOrientation, uuidToAngleMap, uuidToDistanceMap, uuidToFriendMap);
        updateGPS();
    }
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


    void updateCircleAngle(int imageViewId, int textViewId, float angle, int distance) {
        ImageView imageView = findViewById(imageViewId);

        // Set UI icons to border if their distance would bring them past it
        // TODO: should be changed when zooming in/out implemented
        if(distance > initial){
            distance = initial;
        }

        TextView textView = findViewById(textViewId);
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
                1000, // width
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
}