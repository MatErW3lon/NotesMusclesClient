package com.notesmuscles.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.R;
import com.notesmuscles.UserMenuActivity;

public class Text_View_Activity extends AppCompatActivity {

    private Button returnButton;
    private String textFileName;
    private TextView notesText;

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
        setContentView(R.layout.notes_text_activity);
        getSupportActionBar().hide();

        Log.i("DEBUG", "HERE IN NOTES VIEW");
        returnButton = (Button) findViewById(R.id.returnToUserMenuButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserMenuActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        textFileName = getIntent().getStringExtra("textFileName");
        notesText = (TextView) findViewById(R.id.notesTextView);
        String notesData = Notes_Courses_Activity.server_connection_thread.getNotesData(textFileName);
        notesText.setText(notesData);
    }
}
