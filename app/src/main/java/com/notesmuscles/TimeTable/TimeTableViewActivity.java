package com.notesmuscles.TimeTable;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.R;

public class TimeTableViewActivity extends AppCompatActivity {

    private TextView[] lectureNodes = new TextView[20];
    String bilkentID; //package access only
    private GetTimeTable timeTableRetriever;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_timetable_activity);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        bilkentID = intent.getStringExtra("bilkentID");
        timeTableRetriever = new GetTimeTable(this);
        setLectureNodes();
    }

    private void setLectureNodes(){
        //setting the monday nodes
        lectureNodes[0] = (TextView) findViewById(R.id.Mon_Lec1);
        lectureNodes[1] = (TextView) findViewById(R.id.Mon_Lec2);
        lectureNodes[2] = (TextView) findViewById(R.id.Mon_Lec3);
        lectureNodes[3] = (TextView) findViewById(R.id.Mon_Lec4);
        //setting the tuesday nodes
        lectureNodes[4] = (TextView) findViewById(R.id.Tue_Lec1);
        lectureNodes[5] = (TextView) findViewById(R.id.Tue_Lec2);
        lectureNodes[6] = (TextView) findViewById(R.id.Tue_Lec3);
        lectureNodes[7] = (TextView) findViewById(R.id.Tue_Lec4);
        //setting the wednesday nodes
        lectureNodes[8] = (TextView) findViewById(R.id.Wed_Lec1);
        lectureNodes[9] = (TextView) findViewById(R.id.Wed_Lec2);
        lectureNodes[10] = (TextView) findViewById(R.id.Wed_Lec3);
        lectureNodes[11] = (TextView) findViewById(R.id.Wed_Lec4);
        //setting the thursday nodes
        lectureNodes[12] = (TextView) findViewById(R.id.Thurs_Lec1);
        lectureNodes[13] = (TextView) findViewById(R.id.Thurs_Lec2);
        lectureNodes[14] = (TextView) findViewById(R.id.Thurs_Lec3);
        lectureNodes[15] = (TextView) findViewById(R.id.Thurs_Lec4);
        //setting the friday nodes
        lectureNodes[16] = (TextView) findViewById(R.id.Fri_Lec1);
        lectureNodes[17] = (TextView) findViewById(R.id.Fri_Lec2);
        lectureNodes[18] = (TextView) findViewById(R.id.Fri_Lec3);
        lectureNodes[19] = (TextView) findViewById(R.id.Fri_Lec4);
    }

    public void setTimeTableText(String[] lectures) {
        for(int i = 0; i < lectureNodes.length; i++){
            if(lectures[i].equalsIgnoreCase("NONE")){
                lectureNodes[i].setText("");
            }else{
                lectureNodes[i].setText(lectures[i]);
                lectureNodes[i].setTextColor(getResources().getColor(R.color.white));
                lectureNodes[i].setBackgroundColor(getResources().getColor(R.color.purple_500));
            }
        }
    }
}