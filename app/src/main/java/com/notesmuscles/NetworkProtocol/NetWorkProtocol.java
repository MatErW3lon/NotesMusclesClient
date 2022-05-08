package com.notesmuscles.NetworkProtocol;

/*
    THE NETWORK PROTOCOL INTERFACE IS SHARED BETWEEN THE CLIENT AND
    THE SERVER. HENCE SOME COMMANDS MAY OR MAY NOT BE USED ON EITHER
    SIDE
 */

public interface NetWorkProtocol {

    String serverIP = "139.179.197.131";
    int serverPort = 4444;

    String LOGIN_FAILED = "ERROR";

    String DATA_DELIMITER = "/";

    String User_LogOut = "LOGOUT";
    String Invalid_LogOut = null;

    String User_LogIn = "LOGIN"; //note that this will be extend by username and password
    //example command to log in a user LOGIN/MatErW3lon/uglyday@14

    String SuccessFull_LOGIN = "SUCCESS";

    String Image_Send = "IMAGE";
    Integer Image_Received_Confirmation = 1;
    String Image_Stop = "STOP_IMAGE";

    //create account commands
    String Create_Account_Request = "CREATE_ACCOUNT";
    String ACCOUNT_EXISTS_ERROR = "-1";
    String ACCOUNT_CONTINUE = "1";
    String Cancel_Acc_Request = "CANCEL_NEW_ACC";
    String Acc_Info_Ready = "READY_TO_CREATE";
    //after sending the request the account info will be sent as a byte

    //retrieve timetable
    String RETRIEVE_TIMETABLE_REQUEST = "TIMETABLE";
    String EDIT_TIMETABLE_REQUEST = "EDIT_TIMETABLE";

    String RETRIEVE_NOTES_REQUEST = "NOTES";
    String CANCEL_NOTES = "CANCEL_NOTES";
    String EDIT_REQUEST = "EDIT_NOTES";

    //Account deletion
    String DELETE_ACCOUNT_REQUEST = "DELETE_ACC";


}

