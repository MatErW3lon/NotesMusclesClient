package com.notesmuscles.TimeTable;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

class GetTimeTable extends Thread{

    private String[] lectures;
    private TimeTableViewActivity myTimeTableAct;

    //the server sends the following string as bytes
    //lecM1/lecM2/lecM3/lecM4/lecT1/lecT2/lecT3/lecT4/lecW1/lecW2/lecW3/lecW4/lecTH1/lecTH2/lecTH3/lecTH4/LecF1/lecF2/lecF3/lecF4
    public GetTimeTable(TimeTableViewActivity myTimeTableAct){
        this.myTimeTableAct = myTimeTableAct;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginActivity.dataOutputStream.writeUTF(NetWorkProtocol.RETRIEVE_TIMETABLE_REQUEST + NetWorkProtocol.dataDelimiter + myTimeTableAct.bilkentID);
                    LoginActivity.dataOutputStream.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void run(){
        //this is where we retrieve the dta as bytes
        int byteLength = 0;
        try {
            byteLength = LoginActivity.dataInputStream.readInt();
            byte[] timetableBytes = new byte[byteLength];
            LoginActivity.dataInputStream.readFully(timetableBytes);
            String timetableStr = new String(timetableBytes, StandardCharsets.UTF_8);
            lectures = timetableStr.split(NetWorkProtocol.dataDelimiter);
            myTimeTableAct.setTimeTableText(lectures);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
