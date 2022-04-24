package com.notesmuscles.NetworkProtocol;

/*
    THE NETWORK PROTOCOL INTERFACE IS SHARED BETWEEN THE CLIENT AND
    THE SERVER. HENCE SOME COMMANDS MAY OR MAY NOT BE USED ON EITHER
    SIDE
 */

public interface NetWorkProtocol {
    String LOGIN_FAILED = "ERROR";

    String connectionEstablished = "CONNECTED";

    String dataDelimiter = "/";

    String User_LogOut = "LOGOUT";
    String Invalid_LogOut = null;

    String User_LogIn = "LOGIN"; //note that this will be extend by username and password
    //example command to log in a user LOGIN/MatErW3lon/uglyday@14

    String SuccessFull_LOGIN = "SUCCESS";
}

