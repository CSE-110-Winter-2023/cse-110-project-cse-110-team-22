package edu.ucsd.cse110.cse110lab4part5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.Optional;

public class  MainActivity extends AppCompatActivity {

    private MutableLiveData<Pair<Double, Double>> locationValue;
    private UserLocationService userLocationService;
    private UserOrientationService orientationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_coordinate);
        orientationService = new UserOrientationService(this);
        // Below is code for updates from orientation
//        orientationService.getOrientation().observe(this, orient -> {txt.setText(Float.toString(orient));});

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }

        userLocationService = UserLocationService.singleton(this);

        TextView test = (TextView) findViewById(R.id.serviceTextView);

        userLocationService.getLocation().observe(this, loc ->{
            test.setText(Double.toString(loc.first) + " , "+Double.toString(loc.second));
        });
    }
    public void submit_button(View view) {
        //TODO if
        //Utilities.showAlert(this, "Testing");

        Intent intent = new Intent(this, CompassActivity.class);
        putIntent(intent);
        startActivity(intent);
    }

    private void putIntent(Intent intent) {
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

        double home_longitude_val = Double.parseDouble(home_longitude_str);
        double home_latitude_val = Double.parseDouble(home_latitude_str);
        double friend_longitude_val = Double.parseDouble(friend_longitude_str);
        double friend_latitude_val = Double.parseDouble(friend_latitude_str);
        double family_longitude_val = Double.parseDouble(family_longitude_str);
        double family_latitude_val = Double.parseDouble(family_latitude_str);
//        Optional<Integer> maybeMaxCount = Utilities.parseCount(maxCountStr);
//        if (!maybeMaxCount.isPresent()) {
//            Utilities.showAlert(this, "That isn't a number!");
//            return;
//        }
//        int maxCount = maybeMaxCount.get();
//        if (maxCount <= 0) {
//            Utilities.showAlert(this, "Please enter a positive number!");
//            return;
//        }

        intent.putExtra("home_longitude", home_longitude_val);
        intent.putExtra("home_latitude", home_latitude_val);
        intent.putExtra("friend_longitude", friend_longitude_val);
        intent.putExtra("friend_latitude", friend_latitude_val);
        intent.putExtra("family_longitude", family_longitude_val);
        intent.putExtra("family_latitude", family_latitude_val);
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationService.unregisterSensorListeners();
    }
}