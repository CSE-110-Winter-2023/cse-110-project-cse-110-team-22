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

public class MainActivity extends AppCompatActivity {

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
    public void submit_alert(View view) {
        Intent intent = new Intent(this, CompassActivity.class);
        startActivity(intent);
//        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationService.unregisterSensorListeners();
    }
}