package com.notesmuscles.CreateAccountActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class RegisterUserInfoActivity extends AppCompatActivity {

    private EditText firstnameEditText, lastnameEditText, bilkentIDEditText;
    private Button returnButton, nextButton;

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
                Toast.makeText(getApplicationContext(), "NEXT ACTIVITY TO BE IMPLEMENTED", Toast.LENGTH_LONG).show();
                //launch next slide
                //validate inputs
                String firstName = firstnameEditText.getText().toString().trim();
                String lastName = lastnameEditText.getText().toString().trim();
                String bilkentID = bilkentIDEditText.getText().toString().trim();

                setInfoBuffer(firstName,lastName,bilkentID);
                InputValidation.validateUserInfo();
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
