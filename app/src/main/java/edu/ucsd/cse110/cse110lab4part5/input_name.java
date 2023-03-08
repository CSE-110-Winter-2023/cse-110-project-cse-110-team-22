package edu.ucsd.cse110.cse110lab4part5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class input_name extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_name);
    }

    public void continue_onclick(View view){
        TextView nameView = findViewById(R.id.enter_name);
        String name = nameView.getText().toString();
        FriendMediator.getInstance().setName(this, name);
        Intent intent = new Intent(this, user_uid_showing.class);
        startActivity(intent);

    }
}