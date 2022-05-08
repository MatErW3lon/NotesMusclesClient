package com.notesmuscles.ProfileActivity;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class Delete_Acc_Thread extends Thread{

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public Delete_Acc_Thread(){
        dataInputStream = LoginActivity.dataInputStream;
        dataOutputStream = LoginActivity.dataOutputStream;
    }

    @Override
    public void run(){
        try {
            dataOutputStream.writeUTF(NetWorkProtocol.DELETE_ACCOUNT_REQUEST + NetWorkProtocol.DATA_DELIMITER);
            dataOutputStream.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
