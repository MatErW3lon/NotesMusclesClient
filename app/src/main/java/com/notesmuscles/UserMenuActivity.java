package com.notesmuscles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.NetworkProtocol.NetWorkProtocol;
import com.notesmuscles.RecordLecture.CameraActivity;
import com.notesmuscles.RequestExecution.RequestExecution;

import java.io.IOException;

public class UserMenuActivity extends AppCompatActivity implements Runnable {

    private String username;
    private TextView welcomeUserTextView;
    private Button logoutButton, recordButton;
    private RequestExecution requestExecution;
    private Thread readServerResponseThread;

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int result = activityResult.getResultCode();
                            Intent data = activityResult.getData();
                            Toast.makeText(getApplicationContext(), "RETURNED TO MENU", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_menu_activity);
        getSupportActionBar().hide();

        welcomeUserTextView = (TextView) findViewById(R.id.UserWelcomeTextView);
        //display username
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        welcomeUserTextView.setText("WELCOME, " + username);
        setLogoutButton();
        requestExecution = new RequestExecution(this);
        readServerResponseThread = new Thread(this);
        readServerResponseThread.start();
        setRecordButton();
    }

    private void setLogoutButton(){
        logoutButton = (Button) findViewById(R.id.Logoutbutton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LoginActivity.dataOutputStream.writeUTF(NetWorkProtocol.User_LogOut);
                            LoginActivity.dataOutputStream.flush();
                        } catch (IOException ioException) {
                            Log.i("IO EXCEPTION", ioException.toString());
                        }
                    }
                }).start();
            }
        });
    }

    private void setRecordButton(){
        recordButton = (Button) findViewById(R.id.Recordbutton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "RECORDING" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                activityResultLauncher.launch(intent);
            }
        });
    }

    @Override
    public void run() {
        try {
            String serverResponse = LoginActivity.dataInputStream.readUTF();
            Log.i("SERVER RESPONSE", serverResponse);
            requestExecution.executeCommand(serverResponse);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
