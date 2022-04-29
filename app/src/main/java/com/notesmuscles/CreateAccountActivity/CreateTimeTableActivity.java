package com.notesmuscles.CreateAccountActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.R;

public class CreateTimeTableActivity extends AppCompatActivity {

    private TextView userNameTextView;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_timetable_activity);
        getSupportActionBar().hide();

        userNameTextView = (TextView) findViewById(R.id.welcomeNewUserTitle);

        Intent intent = getIntent();
        String firstName = intent.getStringExtra("first_name");
        userNameTextView.setText("WELCOME, " + firstName + Html.fromHtml("<br><p>please complete your Bilkent timetable</p>"));



    }

}
