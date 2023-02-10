package edu.ucsd.cse110.cse110lab4part5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private UserOrientationService orientationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        orientationService = new UserOrientationService(this);
        TextView txt = findViewById(R.id.orientationText);
    }

    public void goToRotate(View view) {
        Intent intent = new Intent(this, RotateActivity.class);
        startActivity(intent);
    }

    public void goToClock(View view) {
        Intent intent = new Intent(this, ClockActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationService.unregisterSensorListeners();
    }
}