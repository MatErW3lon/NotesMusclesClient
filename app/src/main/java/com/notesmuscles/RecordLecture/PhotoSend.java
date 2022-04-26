package com.notesmuscles.RecordLecture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.notesmuscles.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class PhotoSend implements Runnable{

    private byte[] imageData;
    private DataOutputStream os;
    private DataInputStream is;

    public PhotoSend(){
        os = LoginActivity.dataOutputStream;
        is = LoginActivity.dataInputStream;
    }

    public void run(){
        int imageDataLength = this.imageData.length;
        try {
            os.writeInt(imageDataLength);
            os.flush();
            //block the photo sending until confirmation
            int confirmation = is.readInt();
            if(confirmation != 1){
                throw new IOException();
            }else{
                //start sending the bytes after a sleep
                Thread.sleep(10);
                os.write(imageData, 0, imageDataLength);
                os.flush();
            }
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

