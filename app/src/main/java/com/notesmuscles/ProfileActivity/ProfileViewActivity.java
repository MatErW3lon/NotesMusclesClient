package com.notesmuscles.ProfileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.R;

public class ProfileViewActivity extends AppCompatActivity{

    private TextView firstnameView, lastnameView, bilkentIDView;
    private String firstname, lastname, bilkentID;
    private Button returnButton;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view_activity);
        getSupportActionBar().hide();

        setView();
        Intent intent = getIntent();
        firstname = intent.getStringExtra("firstname");
        lastname = intent.getStringExtra("lastname");
        bilkentID = intent.getStringExtra("bilkentID");

        firstnameView.setText("FIRST NAME: " + firstname);
        lastnameView.setText("LAST NAME: " + lastname);
        bilkentIDView.setText("BILKENT ID: " + bilkentID);
    }

    private void setView(){
        firstnameView = (TextView) findViewById(R.id.Profile_firstname_Text);
        lastnameView = (TextView) findViewById(R.id.Profile_lastname_Text);
        bilkentIDView = (TextView) findViewById(R.id.Profile_bilkentID_Text);

        returnButton = (Button) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
