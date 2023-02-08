package edu.ucsd.cse110.cse110lab4part5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToRotate(View view) {
        Intent intent = new Intent(this, RotateActivity.class);
        startActivity(intent);
    }

    public void goToClock(View view) {
        Intent intent = new Intent(this, ClockActivity.class);
        startActivity(intent);
    }
}