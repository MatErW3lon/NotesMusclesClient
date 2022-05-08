package com.notesmuscles.RecordLecture;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class PhotoSend{

    private byte[] imageData;
    private DataOutputStream os;
    private DataInputStream is;

    public PhotoSend(){
        os = LoginActivity.dataOutputStream;
        is = LoginActivity.dataInputStream;
    }

    public void send(){
        int imageDataLength = this.imageData.length;
        String[] splitDateData = Calendar.getInstance().getTime().toString().split(" ");
        String buildDate = "";
        for(int i = 0; i < splitDateData.length; i++){
            buildDate += splitDateData[i] + " ";
        }
        String finalBuildDate = buildDate;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //here we write the date as well
                    os.writeUTF(NetWorkProtocol.Image_Send + NetWorkProtocol.DATA_DELIMITER + imageDataLength + NetWorkProtocol.DATA_DELIMITER + finalBuildDate);
                    os.flush();
                    Thread.sleep(500);
                    os.write(imageData, 0, imageDataLength);
                    os.flush();
                    //}
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    closeEveryThing( os, is);
                } catch(InterruptedException interruptedException){
                    interruptedException.printStackTrace();
                    closeEveryThing( os, is);
                }
            }
        }).start();
    }

    public void setByteArray(byte[] bytes){
        imageData = bytes;
    }

    public void closeEveryThing(DataOutputStream os, DataInputStream is) {
        try {
            if(os != null){
                os.close();
            }
            if( is != null){
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

