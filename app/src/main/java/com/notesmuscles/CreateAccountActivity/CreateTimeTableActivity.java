package com.notesmuscles.CreateAccountActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.R;

public class CreateTimeTableActivity extends AppCompatActivity {

    private TextView userNameTextView, dayTextView;
    private Button returnButton, nextButton;
    private EditText lecture1, lecture2, lecture3, lecture4;
    public final String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};
    public int dayIndex;
    private String firstName;

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
        setContentView(R.layout.create_timetable_activity);
        getSupportActionBar().hide();

        userNameTextView = (TextView) findViewById(R.id.welcomeNewUserTitle);
        Intent intent = getIntent();
        firstName = intent.getStringExtra("first_name");
        dayIndex = intent.getIntExtra("DAY_INDEX", 0);
        userNameTextView.setText("WELCOME, " + firstName + Html.fromHtml("<br><p>please complete your Bilkent Course Info</p>"));

        setView();

        returnButton = (Button) findViewById(R.id.returnToWelcomeButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CancelPopActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        nextButton = (Button) findViewById(R.id.nextInfobutton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch next slide
                //validate inputs
                String[] courseInfoStr = new String[4];
                courseInfoStr[0] = lecture1.getText().toString().trim();
                courseInfoStr[1] = lecture2.getText().toString().trim();
                courseInfoStr[2] = lecture3.getText().toString().trim();
                courseInfoStr[3] = lecture4.getText().toString().trim();

                setInfoBuffer(courseInfoStr);

                if(!InputValidation.validateCoursesInfo(CreateTimeTableActivity.this ,courseInfoStr)){
                    lecture1.setText("");
                    lecture2.setText("");
                    lecture3.setText("");
                    lecture4.setText("");
                }else{
                    Intent intent;
                    if(dayIndex > 3){
                        intent = new Intent(getApplicationContext(), UserNamePasswordActivity.class);
                    }else{
                        intent = new Intent(getApplicationContext(), CreateTimeTableActivity.class);
                        dayIndex++;
                        intent.putExtra("DAY_INDEX", dayIndex);
                        intent.putExtra("first_name", firstName);
                    }
                    activityResultLauncher.launch(intent);
                }
            }
        });
    }

    private void setInfoBuffer(String[] courses){
        switch(dayIndex){
            case 0: //Monday
                AccountInfoBuffer.Monday.dayID = AccountInfoBuffer.BilkentID + "M";
                AccountInfoBuffer.Monday.lecture1 = courses[0].toUpperCase();
                AccountInfoBuffer.Monday.lecture2 = courses[1].toUpperCase();
                AccountInfoBuffer.Monday.lecture3 = courses[2].toUpperCase();
                AccountInfoBuffer.Monday.lecture4 = courses[3].toUpperCase();
                break;
            case 1: //Tuesday
                AccountInfoBuffer.Tuesday.dayID = AccountInfoBuffer.BilkentID + "T";
                AccountInfoBuffer.Tuesday.lecture1 = courses[0].toUpperCase();
                AccountInfoBuffer.Tuesday.lecture2 = courses[1].toUpperCase();
                AccountInfoBuffer.Tuesday.lecture3 = courses[2].toUpperCase();
                AccountInfoBuffer.Tuesday.lecture4 = courses[3].toUpperCase();
                break;
            case 2: //Wednesday
                AccountInfoBuffer.Wednesday.dayID = AccountInfoBuffer.BilkentID + "W";
                AccountInfoBuffer.Wednesday.lecture1 = courses[0].toUpperCase();
                AccountInfoBuffer.Wednesday.lecture2 = courses[1].toUpperCase();
                AccountInfoBuffer.Wednesday.lecture3 = courses[2].toUpperCase();
                AccountInfoBuffer.Wednesday.lecture4 = courses[3].toUpperCase();
                break;
            case 3: //Thursday
                AccountInfoBuffer.Thursday.dayID = AccountInfoBuffer.BilkentID + "Th";
                AccountInfoBuffer.Thursday.lecture1 = courses[0].toUpperCase();
                AccountInfoBuffer.Thursday.lecture2 = courses[1].toUpperCase();
                AccountInfoBuffer.Thursday.lecture3 = courses[2].toUpperCase();
                AccountInfoBuffer.Thursday.lecture4 = courses[3].toUpperCase();
                break;
            case 4: //Friday
                AccountInfoBuffer.Friday.dayID = AccountInfoBuffer.BilkentID + "F";
                AccountInfoBuffer.Friday.lecture1 = courses[0].toUpperCase();
                AccountInfoBuffer.Friday.lecture2 = courses[1].toUpperCase();
                AccountInfoBuffer.Friday.lecture3 = courses[2].toUpperCase();
                AccountInfoBuffer.Friday.lecture4 = courses[3].toUpperCase();
                break;
            default:
                break;
        }
    }

    private void setView() {
        dayTextView = (TextView) findViewById(R.id.DayID);
        dayTextView.setText(days[dayIndex]);
        lecture1 = (EditText) findViewById(R.id.Lec1EditText);
        lecture2 = (EditText) findViewById(R.id.Lec2EditText);
        lecture3 = (EditText) findViewById(R.id.Lec3EditText);
        lecture4 = (EditText) findViewById(R.id.Lec4EditText);
    }
}
