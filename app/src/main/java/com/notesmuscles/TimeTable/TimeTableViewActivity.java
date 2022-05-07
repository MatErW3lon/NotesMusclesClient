package com.notesmuscles.TimeTable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.NetworkProtocol.NetWorkProtocol;
import com.notesmuscles.R;
import com.notesmuscles.UserMenuActivity;

public class TimeTableViewActivity extends AppCompatActivity {

    private TextView[] lectureNodes ;
    String bilkentID; //package access only
    private GetTimeTable timeTableRetriever;
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_timetable_activity);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        bilkentID = intent.getStringExtra("bilkentID");
        setLectureNodes();
        if(UserMenuActivity.timetable == null){
            Toast.makeText(getApplicationContext(), "GET TIMETABLE", Toast.LENGTH_SHORT).show();
            timeTableRetriever = new GetTimeTable(this);
        }else{
            setTimeTableText(UserMenuActivity.timetable.split(NetWorkProtocol.dataDelimiter));
        }
        returnButton = (Button) findViewById(R.id.returnToUserMenuButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setLectureNodes(){
        lectureNodes = new TextView[20];
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

    public synchronized void setTimeTableText(String[] lectures) {
        //Log.i("TIMETABLE", "HERE");
        for(int i = 0; i < lectureNodes.length; i++){
            if(lectures[i].equalsIgnoreCase("NONE")){
                lectureNodes[i].setText("");
            }else{
                lectureNodes[i].setText(lectures[i]);
                //lectureNodes[i].setTextColor(getResources().getColor(R.color.white));
                //lectureNodes[i].setBackgroundColor(getResources().getColor(R.color.purple_500)));
            }
        }
    }
}