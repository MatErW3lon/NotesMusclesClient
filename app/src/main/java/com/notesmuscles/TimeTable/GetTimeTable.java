package com.notesmuscles.TimeTable;

import android.util.Log;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;
import com.notesmuscles.UserMenuActivity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

class GetTimeTable extends Thread{

    private String[] lectures;
    private TimeTableViewActivity myTimeTableAct;

    //the server sends the following string as bytes
    //lecM1/lecM2/lecM3/lecM4/lecT1/lecT2/lecT3/lecT4/lecW1/lecW2/lecW3/lecW4/lecTH1/lecTH2/lecTH3/lecTH4/LecF1/lecF2/lecF3/lecF4
    public GetTimeTable(TimeTableViewActivity myTimeTableAct){
        this.myTimeTableAct = myTimeTableAct;
        this.start();
    }

    @Override
    public void run(){
        //this is where we retrieve the data
        try {
            LoginActivity.dataOutputStream.writeUTF(NetWorkProtocol.RETRIEVE_TIMETABLE_REQUEST + NetWorkProtocol.dataDelimiter + myTimeTableAct.bilkentID);
            LoginActivity.dataOutputStream.flush();
            //Log.i("DEBUG", "HERE");
            int byteLength = LoginActivity.dataInputStream.readInt();
            //Log.i("DEBUG", byteLength + "");
            byte[] timetableBytes = new byte[byteLength];
            LoginActivity.dataOutputStream.writeInt(1);
            LoginActivity.dataInputStream.readFully(timetableBytes);
            //Log.i("DEBUG", "READ BYTES");
            String timetableStr = new String(timetableBytes, StandardCharsets.UTF_8);
            //Log.i("DEBUG", timetableStr);
            UserMenuActivity.timetable = timetableStr;
            lectures = timetableStr.split(NetWorkProtocol.dataDelimiter);
            myTimeTableAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myTimeTableAct.setTimeTableText(lectures);
                }
            });
        } catch (IOException ioException) {
            // Log.i("DEBUG", "EXCEPTION");
            ioException.printStackTrace();
        }
    }
}
