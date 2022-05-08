package com.notesmuscles.Notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.R;
import com.notesmuscles.UserMenuActivity;

import java.io.IOException;

public class Edit_Text_Activity extends AppCompatActivity {

    private EditText notesTextEdittable;
    private Button cancelBtn, confirmBtn;

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
        setContentView(R.layout.edit_notes_activity);
        getSupportActionBar().hide();

        notesTextEdittable = (EditText) findViewById(R.id.notesEditTextView);
        notesTextEdittable.setText(getIntent().getStringExtra("text"));

        cancelBtn = (Button) findViewById(R.id.returnToUserMenuButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notes_Courses_Activity.server_connection_thread.start();
                Intent intent = new Intent(getApplicationContext(), UserMenuActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        confirmBtn = (Button) findViewById(R.id.EditNotesButton);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edittedText = notesTextEdittable.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            LoginActivity.dataOutputStream.writeUTF(edittedText);
                            LoginActivity.dataOutputStream.flush();
                            Intent intent = new Intent(getApplicationContext(), UserMenuActivity.class);

                            activityResultLauncher.launch(intent);
                        }catch(IOException ioException){
                            ioException.printStackTrace();
                        }
                    }
                }).start();
            }
        });


    }
}
