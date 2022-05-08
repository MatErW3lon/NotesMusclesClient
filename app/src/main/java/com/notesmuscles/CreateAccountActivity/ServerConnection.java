package com.notesmuscles.CreateAccountActivity;

import android.util.Log;

import com.notesmuscles.NetworkProtocol.NetWorkProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
        try{
            String accountInfoBuilder = buildAccountInfo();
            byte[] accountInfoBytes = accountInfoBuilder.getBytes(StandardCharsets.UTF_8);
            dataOutputStream.writeUTF(NetWorkProtocol.Acc_Info_Ready + NetWorkProtocol.DATA_DELIMITER + accountInfoBytes.length); //account info ready and byte array length
            dataOutputStream.flush();
            String serverResponse = dataInputStream.readUTF();
            dataOutputStream.write(accountInfoBytes, 0, accountInfoBytes.length);
            dataOutputStream.flush();
        }catch(IOException ioException){}
    }


    //the info string is of the following form
    /*
        firstname/lastname/bilkentid/dayID/lec1/lec2/lec3/lec4/dayID/lec1/lec2/lec3/lec4/dayID/lec1/lec2/lec3/lec4/dayID/lec1/lec2/lec3/lec4/dayID/lec1/lec2/lec3/lec4/username/password
     */

    private String buildAccountInfo() {
        String returnString = AccountInfoBuffer.FirstName + NetWorkProtocol.DATA_DELIMITER + AccountInfoBuffer.LastName + NetWorkProtocol.DATA_DELIMITER
                + AccountInfoBuffer.BilkentID + NetWorkProtocol.DATA_DELIMITER + AccountInfoBuffer.Monday.getStringRep() + NetWorkProtocol.DATA_DELIMITER +
                AccountInfoBuffer.Tuesday.getStringRep() + NetWorkProtocol.DATA_DELIMITER + AccountInfoBuffer.Wednesday.getStringRep() + NetWorkProtocol.DATA_DELIMITER +
                AccountInfoBuffer.Thursday.getStringRep() + NetWorkProtocol.DATA_DELIMITER + AccountInfoBuffer.Friday.getStringRep() + NetWorkProtocol.DATA_DELIMITER +
                AccountInfoBuffer.username + NetWorkProtocol.DATA_DELIMITER + AccountInfoBuffer.password;
        return returnString;

    }
    public boolean checkBilkentIDUniqueness(String bilkentID){
        //Log.i("RESPONSE", bilkentID);
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

    public void CancelAccountCreation(){
        Thread sendRequest = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    dataOutputStream.writeUTF(NetWorkProtocol.Cancel_Acc_Request);
                    dataOutputStream.flush();

                }catch(IOException ioException){}
            }
        });
        sendRequest.start();
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
