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
import com.notesmuscles.WelcomeActivity;

public class UserNamePasswordActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, confirmPasswordEditText;
    private Button returnButton, createAccountButton;

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
        setContentView(R.layout.username_password_activity);
        getSupportActionBar().hide();

        getViews();
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CancelPopActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                setInfoBuffer(username, password, confirmPassword);

                if(!InputValidation.validateUserNamePassword(UserNamePasswordActivity.this)){
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    confirmPasswordEditText.setText("");
                }else{
                    RegisterUserInfoActivity.serverConnection.start();
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    activityResultLauncher.launch(intent);
                }
            }
        });
    }

    private void setInfoBuffer(String username, String password,String confirmPassword) {
        AccountInfoBuffer.username = username;
        AccountInfoBuffer.password = password;
        AccountInfoBuffer.confirmPassword = confirmPassword;
    }

    private void getViews(){
        usernameEditText = (EditText) findViewById(R.id.UsernameEditText);
        passwordEditText = (EditText) findViewById(R.id.PasswordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.ConfirmPasswordEditText);
        returnButton = (Button) findViewById(R.id.returnToWelcomeButton);
        createAccountButton = (Button) findViewById(R.id.finalizeInfobutton);
    }
}
