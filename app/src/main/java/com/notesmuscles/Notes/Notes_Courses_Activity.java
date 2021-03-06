package com.notesmuscles.Notes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.notesmuscles.R;
import com.notesmuscles.UserMenuActivity;

public class Notes_Courses_Activity extends AppCompatActivity {

    private Button returnButton;
    private int courses_count;

    public static String bilkentID;
    static Server_Connection_Thread server_connection_thread;

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
        setContentView(R.layout.notes_courses_activity);
        getSupportActionBar().hide();
        Notes_Courses_Activity.bilkentID = getIntent().getStringExtra("bilkentID");

        returnButton = (Button) findViewById(R.id.returnToUserMenuButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                server_connection_thread.start();
                Intent intent = new Intent(getApplicationContext(), UserMenuActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        server_connection_thread = new Server_Connection_Thread(this);
        setCoursesButton();
    }

    private void setCoursesButton() {
        String[] courseNames = server_connection_thread.getMessage();
        LinearLayout linear = (LinearLayout) findViewById(R.id.courseButtonsLinearLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.setMargins(0,24,0,24);
        linear.setOrientation(LinearLayout.VERTICAL);

        Intent intent = new Intent(getApplicationContext(), Select_Text_Activity.class);

        for (int i = 1; i <= courses_count; i++) {
            Button btn = new Button(this);
            btn.setId(i);
            btn.setText(courseNames[i]);
            btn.setBackgroundResource(R.drawable.roundedbutton);
            int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("course", courseNames[finalI]);
                    activityResultLauncher.launch(intent);
                }
            });
            linear.addView(btn, params);
        }
    }

    public synchronized void setCoursesCount(int courses_count){
        this.courses_count = courses_count;
    }
}
