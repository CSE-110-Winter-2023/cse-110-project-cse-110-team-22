package edu.ucsd.cse110.cse110lab4part5;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class InputCoordinateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        try {
            // check if we should go straight to compass UI based off of existing data
            if (extras.getInt("activity_flag") == 0) {
                Log.d("InputCoordinateActivity", "onCreate going straight to compass");
                Intent intent = new Intent(this, CompassActivity.class);
                startActivity(intent);
            }
        } catch (NullPointerException e){
            Log.d("InputCoordinateActivity", "onCreate staying on input UI");
        }

        setContentView(R.layout.activity_input_coordinate);
//        SharedPrefUtils.clearLocationSharedPreferences(this);
    }

    /**
     * Button handler for the submit button on the UI
     * @param view
     */
    public void submit_button(View view) {
        //TODO handle empty input boxes
        //Utilities.showAlert(this, "Testing");
        List<Location> locations = getLocationsFromUI();
        double mockAngle = getMockAngleFromUI();
        if (locations.size() < 3){
            showAlert("You must enter coordinates for all 3 locations");
        } else {
            Intent intent = new Intent(this, CompassActivity.class);
            intent.putExtra("mock_angle", mockAngle);
            for (Location location : locations) {
                SharedPrefUtils.writeLocation(this, location);
            }
            //putIntent(intent);
            startActivity(intent);
        }
    }
    private void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(R.drawable.sad_android)
                .show();
    }

    /**
     * Reads all the relavent Text boxes from the UI to construct locations based off of what the user entered.
     * @return list of locations parsed from the input UI
     */
    private List<Location> getLocationsFromUI() {
        List<Location> locations = new ArrayList<>();

        // read in the longitude, latitude, and labels for home, friend, and family
        TextView home_longitude_view = findViewById(R.id.longitude_home);
        TextView home_latitude_view = findViewById(R.id.latitude_home);
        TextView family_longitude_view = findViewById(R.id.longitude_family);
        TextView family_latitude_view = findViewById(R.id.latitude_family);
        TextView friend_longitude_view = findViewById(R.id.longitude_friend);
        TextView friend_latitude_view = findViewById(R.id.latitude_friend);

        String home_longitude_str = home_longitude_view.getText().toString();
        String home_latitude_str = home_latitude_view.getText().toString();
        String family_longitude_str = family_longitude_view.getText().toString();
        String family_latitude_str = family_latitude_view.getText().toString();
        String friend_longitude_str = friend_longitude_view.getText().toString();
        String friend_latitude_str = friend_latitude_view.getText().toString();
        TextView home_view = (TextView) findViewById(R.id.label_home);
        TextView family_view = (TextView) findViewById(R.id.label_family);
        TextView friend_view = (TextView) findViewById(R.id.label_friend);

        // Set label as default hint if none is entered by user
        String home_label = (home_view).getText().toString();
        if(home_label.equals("")){
            home_label = home_view.getHint().toString();
        }

        String family_label = (family_view).getText().toString();
        if(family_label.equals("")){
            family_label = family_view.getHint().toString();
        }

        String friend_label = (friend_view).getText().toString();
        if(friend_label.equals("")){
            friend_label = friend_view.getHint().toString();
        }

        // try is to catch the user not entering all coordinates
        try {
            // convert long/lat to doubles
            double home_longitude_val = Double.parseDouble(home_longitude_str);
            double home_latitude_val = Double.parseDouble(home_latitude_str);
            double friend_longitude_val = Double.parseDouble(friend_longitude_str);
            double friend_latitude_val = Double.parseDouble(friend_latitude_str);
            double family_longitude_val = Double.parseDouble(family_longitude_str);
            double family_latitude_val = Double.parseDouble(family_latitude_str);

            // construct new locations, add to list
            locations.add(new LandmarkLocation(home_longitude_val, home_latitude_val, home_label));
            locations.add(new LandmarkLocation(friend_longitude_val, friend_latitude_val, friend_label));
            locations.add(new LandmarkLocation(family_longitude_val, family_latitude_val, family_label));
        } catch(java.lang.NumberFormatException e){
            Log.d("InputUI", "User hit enter without all coordinates");
        }


        return locations;
    }

    private double getMockAngleFromUI() {
        TextView mockAngleText = findViewById(R.id.mock_test);
        String mockAngleStr = mockAngleText.getText().toString();
        double mockAngle;
        try {
            mockAngle = Double.parseDouble(mockAngleStr);
        } catch (Exception e) {
            mockAngle = 0;
        }
        return mockAngle;
    }


}
