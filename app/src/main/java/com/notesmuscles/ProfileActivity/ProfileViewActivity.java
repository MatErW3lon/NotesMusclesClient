package com.notesmuscles.ProfileActivity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.R;

public class ProfileViewActivity extends AppCompatActivity{

    private TextView firstnameView, lastnameView, bilkentIDView;
    private String firstname, lastname, bilkentID;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view_activity);

    }
}
