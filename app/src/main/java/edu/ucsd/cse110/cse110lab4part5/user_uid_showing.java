package edu.ucsd.cse110.cse110lab4part5;

import static edu.ucsd.cse110.cse110lab4part5.UserUUID.generate_own_uid;
import static edu.ucsd.cse110.cse110lab4part5.UserUUID.uuid_toString;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Random;
import java.math.BigInteger;
import java.util.Base64;
import java.util.UUID;

public class user_uid_showing extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uid_showing);
        TextView your_uid = (TextView) this.findViewById(R.id.your_uid);
        int publicUUID = FriendMediator.getInstance().getOrGenerateUUID(this);
        your_uid.setText(String.valueOf(publicUUID));
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