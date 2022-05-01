package com.notesmuscles.CreateAccountActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.R;

public class CreateTimeTableActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private Button returnButton, nextButton;
    private EditText course1, course2, course3, course4, course5;

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
        String firstName = intent.getStringExtra("first_name");
        userNameTextView.setText("WELCOME, " + firstName + Html.fromHtml("<br><p>please complete your Bilkent Course Info</p>"));
        setCourseEditTexts();

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
                String[] courseInfoStr = new String[5];
                courseInfoStr[0] = course1.getText().toString().trim();
                courseInfoStr[1] = course2.getText().toString().trim();
                courseInfoStr[2] = course3.getText().toString().trim();
                courseInfoStr[3] = course4.getText().toString().trim();
                courseInfoStr[4] = course5.getText().toString().trim();

                setInfoBuffer(courseInfoStr);

                if(!InputValidation.validateCoursesInfo(CreateTimeTableActivity.this)){
                    course1.setText("COURSE 1");
                    course2.setText("COURSE 2");
                    course3.setText("COURSE 3");
                    course4.setText("COURSE 4");
                    course5.setText("COURSE 5");
                }else{
                    //Toast.makeText(getApplicationContext(), "NEXT ACTIVITY TO BE IMPLEMENTED", Toast.LENGTH_SHORT).show();
                    //launch new activity
                    Intent intent = new Intent(getApplicationContext(), UserNamePasswordActivity.class);
                    activityResultLauncher.launch(intent);
                }
            }
        });
    }

    private void setInfoBuffer(String[] courses){
        AccountInfoBuffer.course1 = courses[0];
        AccountInfoBuffer.course2 = courses[1];
        AccountInfoBuffer.course3 = courses[2];
        AccountInfoBuffer.course4 = courses[3];
        AccountInfoBuffer.course5 = courses[4];
    }

    private void setCourseEditTexts() {
        course1 =  (EditText) findViewById(R.id.Course1EditText);
        course2 = (EditText) findViewById(R.id.Course2EditText);
        course3 = (EditText) findViewById(R.id.Course3EditText);
        course4 = (EditText) findViewById(R.id.Course4EditText);
        course5 = (EditText) findViewById(R.id.Course5EditText);
    }

}
