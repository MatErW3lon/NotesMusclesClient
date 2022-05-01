package com.notesmuscles;

import static com.notesmuscles.NetworkProtocol.NetWorkProtocol.serverIP;
import static com.notesmuscles.NetworkProtocol.NetWorkProtocol.serverPort;

import android.content.Intent;
import android.os.Bundle;
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.notesmuscles.NetworkProtocol.*;

public class LoginActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int result = activityResult.getResultCode();
                            Intent data = activityResult.getData();
                            Toast.makeText(LoginActivity.this, "LOGGED OUT", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

    public static DataInputStream dataInputStream;
    public static DataOutputStream dataOutputStream;

    private EditText usernameText, passwordText;
    private Button loginButton;
    private LoginActivity loginActivity;
    final boolean[] buttonClicked = {false}; //accessing from an inner class hence effectively final

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getSupportActionBar().hide();
        loginActivity = this; //needed to avoid inner class reference this

        usernameText = (EditText) findViewById(R.id.UsernameEditText);
        passwordText = (EditText) findViewById(R.id.PasswordEditText);


        loginButton = (Button) findViewById(R.id.Loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!buttonClicked[0]){
                    String username = usernameText.getText().toString();
                    username = username.trim();
                    String password = passwordText.getText().toString();
                    password = password.trim();
                    new LoginToServer(loginActivity, username, password);
                    buttonClicked[0] = true;
                }
            }
        });
    }

    public void launchUserMenu(){
        setButtonToUnclicked();
        Intent intent = new Intent(getApplicationContext(), UserMenuActivity.class);
        intent.putExtra("username" ,getUserName());
        activityResultLauncher.launch(intent);
    }

    public void setButtonToUnclicked(){
        buttonClicked[0] = false;
    }


    public String getUserName(){
        return usernameText.getText().toString().trim();
    }
}

class LoginToServer{

    private Socket socket;
    private LoginActivity _loginActivity;

    public LoginToServer(LoginActivity _loginActivity, String username, String password){
        this._loginActivity = _loginActivity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket(serverIP, serverPort);
                    LoginActivity.dataInputStream = new DataInputStream(socket.getInputStream());
                    LoginActivity.dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    //for successful connection debug only
                    //Log.i("MESSAGE", connectionConfirmation);
                    String loginString = NetWorkProtocol.User_LogIn + NetWorkProtocol.dataDelimiter + username + NetWorkProtocol.dataDelimiter + password;
                    LoginActivity.dataOutputStream.writeUTF( loginString);
                    LoginActivity.dataOutputStream.flush();
                    //wait for server response
                    String serverResponse = LoginActivity.dataInputStream.readUTF();

                    //FOR DEBUG PURPOSES ONLY
                    Log.i("SERVER RESPONSE: ", serverResponse);
                    if(serverResponse.equals(NetWorkProtocol.SuccessFull_LOGIN)){
                        _loginActivity.launchUserMenu();
                    }else{
                        _loginActivity.setButtonToUnclicked();
                        _loginActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(_loginActivity.getApplicationContext(), "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch (Exception e){
                    _loginActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(_loginActivity.getApplicationContext(), "SERVER NOT RESPONDING", Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }}).start();
    }
}
