package com.notesmuscles.Notes;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.notesmuscles.R;

public class Notes_Courses_Activity extends AppCompatActivity {

    private Button returnButton;
    private TextView courseCountView;
    private int courses_count;
    public static String bilkentID;
    private Server_Connection_Thread server_connection_thread;

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
                finish();
            }
        });

        server_connection_thread = new Server_Connection_Thread(this);

        courseCountView = (TextView) findViewById(R.id.CourseCountView);
        courseCountView.setText("" + courses_count);
        setCoursesButton();
    }

    private void setCoursesButton() {
        LinearLayout linear = (LinearLayout) findViewById(R.id.courseButtonsLinearLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        linear.setOrientation(LinearLayout.VERTICAL);
        for (int i = 1; i <= courses_count; i++) {
            Button btn = new Button(this);
            btn.setId(i);
            final int id_ = btn.getId();
            btn.setText("button " + id_);
            btn.setBackgroundResource(R.drawable.roundedbutton);
            linear.addView(btn, params);
        }
    }

    public synchronized void setCoursesCount(int courses_count){
        this.courses_count = courses_count;
    }
}
