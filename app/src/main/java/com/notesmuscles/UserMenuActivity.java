package com.notesmuscles;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.NetworkProtocol.NetWorkProtocol;
import com.notesmuscles.ProfileActivity.ProfileViewActivity;
import com.notesmuscles.RecordLecture.CameraActivity;
import com.notesmuscles.RecordLecture.NoLecturePopActivity;
import com.notesmuscles.TimeTable.TimeTableViewActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class UserMenuActivity extends AppCompatActivity{

    private String username;
    private TextView welcomeUserTextView, data_time_textview;
    private Button logoutButton, recordButton, profileButton, timetableButton;
    private String firstname, lastname, bilkentID;
    public static String timetable;
    private Date currentTime;
    private Handler handler;
    private Runnable alterDateRunnable = new Runnable() {
        @Override
        public void run() {
            alterDateTextView();
            handler.postDelayed(alterDateRunnable, 1000);
        }
    };

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {

                            Toast.makeText(getApplicationContext(), "MENU", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_menu_activity);
        getSupportActionBar().hide();
        UserMenuActivity.timetable = null;

        welcomeUserTextView = (TextView) findViewById(R.id.UserWelcomeTextView);
        //display username
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        firstname = intent.getStringExtra("firstname");
        lastname = intent.getStringExtra("lastname");
        bilkentID = intent.getStringExtra("bilkentID");
        data_time_textview = (TextView) findViewById(R.id.DateTimeTextView);
        welcomeUserTextView.setText("WELCOME, " + username);
        alterDateTextView();
        setLogoutButton();
        setRecordButton();
        setProfileButton();
        setTimetableButton();
        handler = new Handler();
        alterDateRunnable.run();
    }

    private void setLogoutButton(){
        logoutButton = (Button) findViewById(R.id.Logoutbutton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LoginActivity.dataOutputStream.writeUTF(NetWorkProtocol.User_LogOut);
                            LoginActivity.dataOutputStream.flush();
                            finish();
                        } catch (IOException ioException) {
                            Log.i("IO EXCEPTION", ioException.toString());
                        }
                    }
                }).start();
            }
        });
    }

    private void setRecordButton(){
        recordButton = (Button) findViewById(R.id.Recordbutton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean[] dateAndTimeCheck = validateLectureRecordingTime();
                Log.i("DEBUG", "CHECKED DATE AND TIME VALIDATION");
                Intent intent;
                if(dateAndTimeCheck[2]){
                    intent = new Intent(getApplicationContext(), NoLecturePopActivity.class);
                    intent.putExtra("message", "It is a weekend today. Are you sure you have a lecture today?");
                    activityResultLauncher.launch(intent);
                }else if(dateAndTimeCheck[1]){
                    intent = new Intent(getApplicationContext(), NoLecturePopActivity.class);
                    intent.putExtra("message", "<h2>ERROR</h2><br><p>The classes for the day have not started</p>");
                    activityResultLauncher.launch(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "RECORDING" , Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), CameraActivity.class);
                    activityResultLauncher.launch(intent);
                }
            }
        });
    }

    private void setProfileButton(){
        profileButton = (Button) findViewById(R.id.Profilebutton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileViewActivity.class);
                intent.putExtra("firstname", firstname);
                intent.putExtra("lastname", lastname);
                intent.putExtra("bilkentID", bilkentID);
                activityResultLauncher.launch(intent);
            }
        });
    }

    private void setTimetableButton(){
        timetableButton = (Button) findViewById(R.id.Timetablebutton);
        timetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TimeTableViewActivity.class);
                intent.putExtra("bilkentID", bilkentID);
                activityResultLauncher.launch(intent);
            }
        });
    }

    public String getFirstname(){
        return firstname;
    }

    public String getLastname(){
        return lastname;
    }

    public String getBilkentID(){
        return bilkentID;
    }

    private void alterDateTextView(){
        currentTime = Calendar.getInstance().getTime();
        String[] splitDateData = currentTime.toString().split(" ");
        String buildDateDisplay = "";
        for(int i = 0; i <= 3; i++){
            buildDateDisplay += splitDateData[i] + " ";
        }
        data_time_textview.setText(buildDateDisplay);
    }

    private boolean[] validateLectureRecordingTime(){
        Log.i("DEBUG", "VALIDATING DATE AND TIME");
        boolean[] allChecks = new boolean[]{false, false, false};
                                        //can record, time not between 0830-1730, weekends
        //first check for weekends
        if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            allChecks[2] = true;
            return allChecks;
        }
        //need to check for hours between 0830-1730
        //check for hours first
        //Calender.hour returns hour as a 24-hour format
        if(Calendar.getInstance().get(Calendar.HOUR) < 8 || Calendar.getInstance().get(Calendar.HOUR) > 17){
            allChecks[1] = true;
            return  allChecks;
        }else if(Calendar.getInstance().get(Calendar.HOUR) == 8 && Calendar.getInstance().get(Calendar.MINUTE)  < 30){
            allChecks[1] = true;
            return allChecks;
        }else if(Calendar.getInstance().get(Calendar.HOUR) == 17 && Calendar.getInstance().get(Calendar.MINUTE)  > 30){
            allChecks[1] = true;
            return  allChecks;
        }
        allChecks[0] = true;
        return  allChecks;
    }
}
