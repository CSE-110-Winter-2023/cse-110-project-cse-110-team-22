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

    private int uuid_generated;
    //    private long leastSigBits;
//    private long mostSigBits;
//    private long[] sigDigits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uid_showing);
        TextView your_uid = (TextView) this.findViewById(R.id.your_uid);
        this.uuid_generated = generate_own_uid();
        //TODO: Store the random generated UUID into sharedPref and
        // check if it is already stored everytime this page is started
//        while(uuid_generated in sharedPref){
//            int uuid_generated = generate_own_uid();
//        }
        String uuid_str = uuid_toString(this.uuid_generated);
        your_uid.setText(uuid_str);
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