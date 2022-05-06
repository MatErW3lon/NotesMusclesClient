package com.notesmuscles.Notes;

import android.util.Log;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

class Server_Connection_Thread extends Thread{

    private Notes_Courses_Activity notes_courses_activity;
    private String[] message;

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
                    String message_temp = LoginActivity.dataInputStream.readUTF();
                    message = message_temp.split(NetWorkProtocol.dataDelimiter);
                    courses_count[0] = Integer.parseInt(message[0]);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        getCourseCountThread.start();
        while(getCourseCountThread.isAlive()){}
        this.notes_courses_activity.setCoursesCount(courses_count[0]);
    }

    public String[] getMessage() {
        return this.message;
    }

    public String sendSelectedCourseCode(String course_selected) {
        final String[] textFilesData = new String[1];
        Thread tempThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    LoginActivity.dataOutputStream.writeUTF(course_selected);
                    LoginActivity.dataOutputStream.flush();
                    //we wait for the server response
                    textFilesData[0] = LoginActivity.dataInputStream.readUTF();
                }catch(IOException ioException){
                    ioException.printStackTrace();
                }
            }
        });
        tempThread.start();
        while(tempThread.isAlive()){}
        Log.i("DEBUG", textFilesData[0]);
        return textFilesData[0];
    }

    public String getNotesData(String textFileName) {
        final String[] data = new String[1];
        Thread tempThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginActivity.dataOutputStream.writeUTF(textFileName);
                    int bytesLength = LoginActivity.dataInputStream.readInt();
                    byte[] dataBytes = new byte[bytesLength];
                    LoginActivity.dataInputStream.readFully(dataBytes);
                    data[0] = new String(dataBytes, StandardCharsets.UTF_8);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        tempThread.start();
        while(tempThread.isAlive()){}
        return data[0];
    }

    @Override
    public void run(){
        try {
            LoginActivity.dataOutputStream.writeUTF(NetWorkProtocol.CANCEL_NOTES);
            LoginActivity.dataOutputStream.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
