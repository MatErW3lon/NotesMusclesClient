package com.notesmuscles.RecordLecture;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
        try {
            os.writeUTF(NetWorkProtocol.Image_Send + NetWorkProtocol.dataDelimiter + imageDataLength);
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

