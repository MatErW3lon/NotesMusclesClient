package com.notesmuscles.Notes;

import android.util.Log;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;

import java.io.IOException;

class Server_Connection_Thread extends Thread{

    private Notes_Courses_Activity notes_courses_activity;

    public Server_Connection_Thread(Notes_Courses_Activity notes_courses_activity){
        this.notes_courses_activity = notes_courses_activity;
        final int[] courses_count = new int[1];
        Thread getCourseCountThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginActivity.dataOutputStream.writeUTF(NetWorkProtocol.RETRIEVE_NOTES_REQUEST + NetWorkProtocol.dataDelimiter + Notes_Courses_Activity.bilkentID);
                    LoginActivity.dataOutputStream.flush();
                    //wait for the number of courses
                    courses_count[0] = LoginActivity.dataInputStream.readInt();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        getCourseCountThread.start();
        while(getCourseCountThread.isAlive()){}
        this.notes_courses_activity.setCoursesCount(courses_count[0]);
    }
}
