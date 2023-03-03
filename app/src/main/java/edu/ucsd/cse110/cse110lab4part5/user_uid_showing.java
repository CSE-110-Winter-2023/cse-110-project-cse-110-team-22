package edu.ucsd.cse110.cse110lab4part5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class user_uid_showing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uid_showing);
    }

    public void proceed_to_compass(View view){
        Intent intent = new Intent(this, CompassActivity.class);
        startActivity(intent);

    }

    public void proceed_to_enter_uid(View view){
        Intent intent = new Intent(this, entering_friend_uid.class);
        startActivity(intent);

    }
}