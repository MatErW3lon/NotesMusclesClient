package com.notesmuscles.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.NetworkProtocol.NetWorkProtocol;
import com.notesmuscles.R;
import com.notesmuscles.UserMenuActivity;

public class Select_Text_Activity extends AppCompatActivity {

    private String course_selected, textFileData;
    private Button returnButton;
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
        setContentView(R.layout.select_notes_activity);
        getSupportActionBar().hide();

        course_selected = getIntent().getStringExtra("course");
        textFileData = Notes_Courses_Activity.server_connection_thread.sendSelectedCourseCode(course_selected);
        setButtonsLayout();


        returnButton = (Button) findViewById(R.id.returnToNotesMenuButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notes_Courses_Activity.server_connection_thread.start();
                Intent intent = new Intent(getApplicationContext(), UserMenuActivity.class);
                activityResultLauncher.launch(intent);
            }
        });
    }

    private void setButtonsLayout() {
        String[] textFileData_split = textFileData.split(NetWorkProtocol.DATA_DELIMITER);
        int textFilesCount = Integer.parseInt(textFileData_split[0]);
        LinearLayout linear = (LinearLayout) findViewById(R.id.notesButtonsLinearLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.setMargins(0,24,0,24);
        linear.setOrientation(LinearLayout.VERTICAL);
        Intent intent = new Intent(getApplicationContext(), Text_View_Activity.class);

        for(int i = 1; i < textFileData_split.length; i++){
            Log.i("DEBUG", "HERE");
            Button btn = new Button(this);
            btn.setId(i);
            btn.setText(textFileData_split[i]);
            btn.setBackgroundResource(R.drawable.roundedbutton);
            int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("textFileName", textFileData_split[finalI]);
                    activityResultLauncher.launch(intent);
                }
            });
            linear.addView(btn, params);
        }

    }
}
