package com.notesmuscles.TimeTable;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;
import com.notesmuscles.Notes.Edit_Text_Activity;

import java.io.DataOutputStream;
import java.io.IOException;

class EditLectureNodeThread extends Thread{

    private DataOutputStream dataOutputStream;
    private volatile String lectureToEdit;

    public EditLectureNodeThread(){
        dataOutputStream = LoginActivity.dataOutputStream;
    }

    public void setLectureToEdit(String setTo){
        lectureToEdit = setTo;
    }

    @Override
    public void run() {
        try {
            dataOutputStream.writeUTF(NetWorkProtocol.EDIT_TIMETABLE_REQUEST + NetWorkProtocol.DATA_DELIMITER + lectureToEdit);
            dataOutputStream.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
