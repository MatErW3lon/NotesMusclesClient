package com.notesmuscles.TimeTable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.NetworkProtocol.NetWorkProtocol;
import com.notesmuscles.R;
import com.notesmuscles.UserMenuActivity;

import java.util.HashMap;
import java.util.Map;

public class TimeTableViewActivity extends AppCompatActivity {

    private Button[] lectureNodes ;
    String bilkentID; //package access only
    private GetTimeTable timeTableRetriever;
    private Button returnButton;
    private Map<Integer,String> lectureColAndRows;
    private EditLectureNodeThread editLectureNodeThread;

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) { }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_timetable_activity);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        bilkentID = intent.getStringExtra("bilkentID");
        editLectureNodeThread = new EditLectureNodeThread();
        setLectureNodes();
        initLectureColAndRows();

        timeTableRetriever = new GetTimeTable(this);
        returnButton = (Button) findViewById(R.id.returnToUserMenuButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initLectureColAndRows(){
        lectureColAndRows = new HashMap<>();
        lectureColAndRows.put(0, bilkentID + "M" + NetWorkProtocol.DATA_DELIMITER + "monday" + NetWorkProtocol.DATA_DELIMITER + "Lecture1");
        lectureColAndRows.put(1, bilkentID + "M" + NetWorkProtocol.DATA_DELIMITER + "monday" + NetWorkProtocol.DATA_DELIMITER + "Lecture2");
        lectureColAndRows.put(2, bilkentID + "M" + NetWorkProtocol.DATA_DELIMITER + "monday" + NetWorkProtocol.DATA_DELIMITER + "Lecture3");
        lectureColAndRows.put(3, bilkentID + "M" + NetWorkProtocol.DATA_DELIMITER + "monday" + NetWorkProtocol.DATA_DELIMITER + "Lecture4");

        lectureColAndRows.put(4, bilkentID + "T" + NetWorkProtocol.DATA_DELIMITER + "tuesday" + NetWorkProtocol.DATA_DELIMITER + "Lecture1");
        lectureColAndRows.put(5, bilkentID + "T" + NetWorkProtocol.DATA_DELIMITER + "tuesday" + NetWorkProtocol.DATA_DELIMITER + "Lecture2");
        lectureColAndRows.put(6, bilkentID + "T" + NetWorkProtocol.DATA_DELIMITER + "tuesday" + NetWorkProtocol.DATA_DELIMITER + "Lecture3");
        lectureColAndRows.put(7, bilkentID + "T" + NetWorkProtocol.DATA_DELIMITER + "tuesday" + NetWorkProtocol.DATA_DELIMITER + "Lecture4");

        lectureColAndRows.put(8, bilkentID + "W" + NetWorkProtocol.DATA_DELIMITER + "wednesday" + NetWorkProtocol.DATA_DELIMITER + "Lecture1");
        lectureColAndRows.put(9, bilkentID + "W" + NetWorkProtocol.DATA_DELIMITER + "wednesday" + NetWorkProtocol.DATA_DELIMITER + "Lecture2");
        lectureColAndRows.put(10, bilkentID + "W" + NetWorkProtocol.DATA_DELIMITER + "wednesday" + NetWorkProtocol.DATA_DELIMITER + "Lecture3");
        lectureColAndRows.put(11, bilkentID + "W" + NetWorkProtocol.DATA_DELIMITER + "wednesday" + NetWorkProtocol.DATA_DELIMITER + "Lecture4");

        lectureColAndRows.put(12, bilkentID + "Th" + NetWorkProtocol.DATA_DELIMITER + "thursday" + NetWorkProtocol.DATA_DELIMITER + "Lecture1");
        lectureColAndRows.put(13, bilkentID + "Th" + NetWorkProtocol.DATA_DELIMITER + "thursday" + NetWorkProtocol.DATA_DELIMITER + "Lecture2");
        lectureColAndRows.put(14, bilkentID + "Th" + NetWorkProtocol.DATA_DELIMITER + "thursday" + NetWorkProtocol.DATA_DELIMITER + "Lecture3");
        lectureColAndRows.put(15, bilkentID + "Th" + NetWorkProtocol.DATA_DELIMITER + "thursday" + NetWorkProtocol.DATA_DELIMITER + "Lecture4");

        lectureColAndRows.put(16, bilkentID + "F" + NetWorkProtocol.DATA_DELIMITER + "friday" + NetWorkProtocol.DATA_DELIMITER + "Lecture1");
        lectureColAndRows.put(17, bilkentID + "F" + NetWorkProtocol.DATA_DELIMITER + "friday" + NetWorkProtocol.DATA_DELIMITER + "Lecture2");
        lectureColAndRows.put(18, bilkentID + "F" + NetWorkProtocol.DATA_DELIMITER + "friday" + NetWorkProtocol.DATA_DELIMITER + "Lecture3");
        lectureColAndRows.put(19, bilkentID + "F" + NetWorkProtocol.DATA_DELIMITER + "friday" + NetWorkProtocol.DATA_DELIMITER + "Lecture4");

    }

    private void setLectureNodes(){
        lectureNodes = new Button[20];
        //setting the monday nodes
        lectureNodes[0] = (Button) findViewById(R.id.Mon_Lec1);
        lectureNodes[1] = (Button) findViewById(R.id.Mon_Lec2);
        lectureNodes[2] = (Button) findViewById(R.id.Mon_Lec3);
        lectureNodes[3] = (Button) findViewById(R.id.Mon_Lec4);
        //setting the tuesday nodes
        lectureNodes[4] = (Button) findViewById(R.id.Tue_Lec1);
        lectureNodes[5] = (Button) findViewById(R.id.Tue_Lec2);
        lectureNodes[6] = (Button) findViewById(R.id.Tue_Lec3);
        lectureNodes[7] = (Button) findViewById(R.id.Tue_Lec4);
        //setting the wednesday nodes
        lectureNodes[8] = (Button) findViewById(R.id.Wed_Lec1);
        lectureNodes[9] = (Button) findViewById(R.id.Wed_Lec2);
        lectureNodes[10] = (Button) findViewById(R.id.Wed_Lec3);
        lectureNodes[11] = (Button) findViewById(R.id.Wed_Lec4);
        //setting the thursday nodes
        lectureNodes[12] = (Button) findViewById(R.id.Thurs_Lec1);
        lectureNodes[13] = (Button) findViewById(R.id.Thurs_Lec2);
        lectureNodes[14] = (Button) findViewById(R.id.Thurs_Lec3);
        lectureNodes[15] = (Button) findViewById(R.id.Thurs_Lec4);
        //setting the friday nodes
        lectureNodes[16] = (Button) findViewById(R.id.Fri_Lec1);
        lectureNodes[17] = (Button) findViewById(R.id.Fri_Lec2);
        lectureNodes[18] = (Button) findViewById(R.id.Fri_Lec3);
        lectureNodes[19] = (Button) findViewById(R.id.Fri_Lec4);
    }

    public synchronized void setTimeTableText(String[] lectures) {
        //Log.i("TIMETABLE", "HERE");
        for(int i = 0; i < lectureNodes.length; i++){
            if(lectures[i].equalsIgnoreCase("NONE")){
                lectureNodes[i].setText("");
            }else{
                lectureNodes[i].setText(lectures[i]);
            }
        }
    }

    public synchronized void setOnClickListeners(){
        for(int i = 0; i < lectureNodes.length; i++){
            int finalI = i;
            lectureNodes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), GetEditInputPopActivity.class);
                    activityResultLauncher.launch(intent);
                    editLectureNodeThread.setLectureToEdit(lectureColAndRows.get(finalI));
                    editLectureNodeThread.start();
                }
            });
        }
        }
}
