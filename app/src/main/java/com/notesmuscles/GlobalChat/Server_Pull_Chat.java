package com.notesmuscles.GlobalChat;

import android.util.Log;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class Server_Pull_Chat extends Thread{

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String messages;
    private GlobalChatActivity myGlobalChatActivity;
    private final Runnable message_editor = new Runnable() {
        @Override
        public void run() {
            myGlobalChatActivity.setTextView(messages);
        }
    };

    public Server_Pull_Chat(GlobalChatActivity myGlobalChatActivity){
        dataInputStream = LoginActivity.dataInputStream;
        dataOutputStream = LoginActivity.dataOutputStream;
        messages = null;
        this.myGlobalChatActivity = myGlobalChatActivity;
    }

    @Override
    public void run(){
        try{
            dataOutputStream.writeUTF(NetWorkProtocol.PULL_GLOBAL_CHAT + NetWorkProtocol.DATA_DELIMITER);
            dataOutputStream.flush();
            Log.i("DEBUG", "READING THREAD STARTED");
            messages = dataInputStream.readUTF();
            Log.i("DEBUG", "MESSAGE ->" + messages);
            while(!messages.equalsIgnoreCase(NetWorkProtocol.END_GLOBAL_CHAT)) {
                //now we read the messages
                myGlobalChatActivity.runOnUiThread(message_editor);
                messages = dataInputStream.readUTF();
                Log.i("DEBUG", "MESSAGE" + messages);
            }
            Log.i("DEBUG", "READING THREAD DEAD");
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    public void sendMessage(String toSend){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    dataOutputStream.writeUTF(toSend);
                    dataOutputStream.flush();
                }catch(IOException ioException){
                    ioException.printStackTrace();
                }
            }
        }).start();
    }

}
