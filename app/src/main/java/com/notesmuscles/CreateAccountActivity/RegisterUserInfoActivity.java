package com.notesmuscles.CreateAccountActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.R;

import java.io.IOException;

public class RegisterUserInfoActivity extends AppCompatActivity {

    private EditText firstnameEditText, lastnameEditText, bilkentIDEditText;
    private Button returnButton, nextButton;
    public static ServerConnection serverConnection;
    static{
        serverConnection = new ServerConnection();
    }


    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) { }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.register_user_info);
        getSupportActionBar().hide();

        getViews();
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CancelPopActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch next slide
                //validate inputs
                String firstName = firstnameEditText.getText().toString().trim();
                String lastName = lastnameEditText.getText().toString().trim();
                String bilkentID = bilkentIDEditText.getText().toString().trim();

                setInfoBuffer(firstName,lastName,bilkentID);

                if(!InputValidation.validateUserInfo(RegisterUserInfoActivity.this)){
                    firstnameEditText.setText("FIRST NAME");
                    lastnameEditText.setText("LAST NAME");
                    bilkentIDEditText.setText("BILKENT ID");
                }else{
                    if(serverConnection.checkBilkentIDUniqueness(bilkentID)){
                        //check for launch activity
                        Log.i("RESPONSE", "ready to launch activity");
                        Intent intent = new Intent(getApplicationContext(), CreateTimeTableActivity.class);
                        intent.putExtra("first_name", firstName);
                        activityResultLauncher.launch(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "BILKENT ID ALREADY EXISTS", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void getViews(){
        firstnameEditText = (EditText) findViewById(R.id.firstnameEditText);
        lastnameEditText = (EditText) findViewById(R.id.lastnameEditText);
        bilkentIDEditText = (EditText) findViewById(R.id.bilkentIDEditText);
        returnButton = (Button) findViewById(R.id.returnToWelcomeButton);
        nextButton = (Button) findViewById(R.id.nextInfobutton);
    }

    private void setInfoBuffer(String firstName, String lastName, String bilkentID){
        AccountInfoBuffer.FirstName = firstName;
        AccountInfoBuffer.LastName = lastName;
        AccountInfoBuffer.BilkentID = bilkentID;
    }
}
