package edu.ucsd.cse110.cse110lab4part5;

import static edu.ucsd.cse110.cse110lab4part5.UserUUID.generate_own_uid;
import static edu.ucsd.cse110.cse110lab4part5.UserUUID.uuid_toString;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Random;
import java.math.BigInteger;
import java.util.Base64;
import java.util.UUID;

public class user_uid_showing extends AppCompatActivity {
    private FriendMediator friendMediator = FriendMediator.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uid_showing);
        TextView your_uid = (TextView) this.findViewById(R.id.your_uid);
        String publicUUID = friendMediator.getOrGenerateUUID(this);
        your_uid.setText(publicUUID);
        // Handle location permissions
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            Log.d("MainActivity", "Asking for location permissions");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }
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