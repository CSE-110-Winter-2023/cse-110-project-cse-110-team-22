package edu.ucsd.cse110.cse110lab4part5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class entering_friend_uid extends AppCompatActivity {

    private FriendMediator friendMediator = FriendMediator.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entering_friend_uid);
    }

    public void onAddFriendToDatabaseClicked(View view){
        TextView friendView = findViewById(R.id.enter_friend_id_blank);
        String friendName = friendView.getText().toString();
        friendMediator.addFriend(this,friendName);
    }

    public void back_to_showing_uid(View view){

        Intent intent = new Intent(this, user_uid_showing.class);
        startActivity(intent);

    }
}