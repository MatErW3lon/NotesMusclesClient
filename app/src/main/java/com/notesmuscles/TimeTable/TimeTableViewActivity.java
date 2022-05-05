package com.notesmuscles.TimeTable;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.R;

public class TimeTableViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_timetable_activity);
        getSupportActionBar().hide();

    }
}
