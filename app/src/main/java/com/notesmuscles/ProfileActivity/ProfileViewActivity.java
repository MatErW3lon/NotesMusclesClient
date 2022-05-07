package com.notesmuscles.ProfileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.R;

public class ProfileViewActivity extends AppCompatActivity{

    private TextView firstnameView, lastnameView, bilkentIDView;
    private String firstname, lastname, bilkentID;
    private Button returnButton, deleteAccBtn;
    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) { }
                    }
            );

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

        deleteAccBtn = (Button) findViewById(R.id.deleteAccButton);
        deleteAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DeleteConfirmPopActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        returnButton = (Button) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
