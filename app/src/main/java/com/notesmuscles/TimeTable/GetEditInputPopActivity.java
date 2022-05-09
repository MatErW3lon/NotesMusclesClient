package com.notesmuscles.TimeTable;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.R;
import com.notesmuscles.UserMenuActivity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetEditInputPopActivity extends AppCompatActivity {

    private TextView promptForInput;
    private final String prompt = "<h2>ENTER SET TO</h2><br><p>Enter None to drop course. Warning! over-writing a course will drop that course</p>";
    private EditText inputEdit;
    private Button confirmBtn, cancelBtn;

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) { }
                    }
            );

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_lecture_edit_activity);
        getSupportActionBar().hide();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.6));

        promptForInput = (TextView) findViewById(R.id.PromptForEditPopText);
        promptForInput.setText(Html.fromHtml(prompt,Html.FROM_HTML_MODE_COMPACT));

        cancelBtn = (Button) findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            LoginActivity.dataOutputStream.writeUTF("CANCEL");
                            LoginActivity.dataOutputStream.flush();
                        }catch(IOException ioException){
                            ioException.printStackTrace();
                        }
                    }
                }).start();
                launchUserMenu();
            }
        });

        inputEdit = (EditText) findViewById(R.id.InputEditText);

        confirmBtn = (Button) findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = inputEdit.getText().toString();
                if(validateCoursesInfo(input)){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{

                                LoginActivity.dataOutputStream.writeUTF(input);
                                LoginActivity.dataOutputStream.flush();
                            }catch(IOException ioException){
                                ioException.printStackTrace();
                            }
                        }
                    }).start();
                    launchUserMenu();
                }
            }
        });
    }

    private void launchUserMenu(){
        Intent intent = new Intent(getApplicationContext(), UserMenuActivity.class);
        activityResultLauncher.launch(intent);
    }

    private boolean validateCoursesInfo(String course){
        Pattern pattern = Pattern.compile("[A-Z]+-[0-9]+-[0-9]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(course);
        if(!matcher.find()){
            if(course.equalsIgnoreCase("NONE")){
                return true;
            }
            String buildWarningStr = course + " IS NOT A VALID INPUT";
            Toast.makeText(getApplicationContext(), buildWarningStr, Toast.LENGTH_SHORT).show();
            return false;
            }
        return true;
    }
}
