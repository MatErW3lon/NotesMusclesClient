package com.notesmuscles;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText, passwordText;
    private Button loginButton;
    private LoginActivity loginActivity;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getSupportActionBar().hide();
        loginActivity = this;

        usernameText = (EditText) findViewById(R.id.UsernameEditText);
        passwordText = (EditText) findViewById(R.id.PasswordEditText);

        final boolean[] buttonClicked = {false}; //accessing from an inner class hence effectively final
        loginButton = (Button) findViewById(R.id.Loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!buttonClicked[0]){
                    String username = usernameText.getText().toString();
                    String password = passwordText.getText().toString();
                    new LoginToServer(loginActivity, username, password);
                    Toast.makeText(LoginActivity.this, "LOGGING IN", Toast.LENGTH_LONG).show();
                    buttonClicked[0] = true;
                }else{
                    Toast.makeText(LoginActivity.this, "ALREADY LOGGING IN", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

class LoginToServer{

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private LoginActivity loginActivity;

    final String serverIP = "139.179.197.194";
    final int NotesMusclePort = 4444;

    public LoginToServer(LoginActivity loginActivity, String username, String password){
        this.loginActivity = loginActivity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    socket = new Socket(serverIP, NotesMusclePort);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    String connectionConfirmation = dataInputStream.readUTF();
                    Log.i("MESSAGE", connectionConfirmation);
                    if(connectionConfirmation.equals("CONNECTED")){
                        dataOutputStream.writeUTF("/LOGIN/" + username + "/"  + password + "/");
                        dataOutputStream.flush();
                    }
                }catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }}).start();
    }
}
