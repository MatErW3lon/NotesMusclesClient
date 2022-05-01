package com.notesmuscles.CreateAccountActivity;

import android.util.Log;

import com.notesmuscles.NetworkProtocol.NetWorkProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ServerConnection extends Thread{

    private Socket clientSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ServerConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clientSocket = new Socket(NetWorkProtocol.serverIP, NetWorkProtocol.serverPort);
                    dataInputStream =  new DataInputStream(clientSocket.getInputStream());
                    dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                    Thread.sleep(10);
                    dataOutputStream.writeUTF(NetWorkProtocol.Create_Account_Request);
                    dataOutputStream.flush();
                } catch (IOException ioException) {
                    try {
                        closeEverything();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    ioException.printStackTrace();
                }catch (InterruptedException interruptedException){
                    try {
                        closeEverything();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    interruptedException.printStackTrace();
                }

            }
        }).start();
    }

    //we only start this thread when we have accumulated all the user information
    @Override
    public void run(){

    }

    public boolean checkBilkentIDUniqueness(String bilkentID){
        Log.i("RESPONSE", bilkentID);
        final boolean[] returnValue = {true};
        Thread requestForUniquenessThread =  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataOutputStream.writeUTF(bilkentID);
                    dataOutputStream.flush();
                    String response = dataInputStream.readUTF();
                    Log.i("RESPONSE" , String.valueOf(response));
                    if(response.equals(NetWorkProtocol.ACCOUNT_CONTINUE)){
                        returnValue[0] = true;
                    }else{
                        returnValue[0] = false;
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        requestForUniquenessThread.start();
        while(requestForUniquenessThread.isAlive()){} //stop here before returning
        Log.i("RESPONSE", "READY TO RETURN");
        return returnValue[0];
    }

    private void closeEverything() throws Exception {
        if(clientSocket != null){
            clientSocket.close();
        }
        if(dataInputStream != null){
            dataInputStream.close();
        }
        if(dataOutputStream != null){
            dataOutputStream.close();
        }

    }




}
