package com.notesmuscles.GlobalChat;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class Server_Pull_Chat extends Thread{

    private volatile boolean end_receiving_thread;
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
        end_receiving_thread = false;
    }

    @Override
    public void run(){
        try{
            while(!end_receiving_thread) {
                dataOutputStream.writeUTF(NetWorkProtocol.PULL_GLOBAL_CHAT + NetWorkProtocol.DATA_DELIMITER);
                dataOutputStream.flush();
                //now we read the messages
                messages = dataInputStream.readUTF();
                myGlobalChatActivity.runOnUiThread(message_editor);
            }
            dataOutputStream.writeUTF(NetWorkProtocol.END_GLOBAL_CHAT);
            dataOutputStream.flush();
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

    public void setEndReceivingThread(){
        this.end_receiving_thread = true;
    }

}
