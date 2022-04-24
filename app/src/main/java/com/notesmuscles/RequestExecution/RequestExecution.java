package com.notesmuscles.RequestExecution;

import android.util.Log;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;
import com.notesmuscles.UserMenuActivity;

import java.io.IOException;


public class RequestExecution {

    private UserMenuActivity myUser;

    public RequestExecution(UserMenuActivity userMenuActivity){
        myUser = userMenuActivity;
    }

    public void executeCommand(String command){
        if(command.equals(NetWorkProtocol.User_LogOut)){
            myUser.finish();
            try {
                LoginActivity.dataInputStream.close();
                LoginActivity.dataOutputStream.close();
            } catch (IOException ioException) {
                Log.e("IO EXCEPTION", ioException.toString());
            }
            LoginActivity.dataOutputStream = null;
            LoginActivity.dataInputStream = null;
        }
    }

}
