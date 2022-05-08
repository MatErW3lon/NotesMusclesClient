package com.notesmuscles.CreateAccountActivity;

import com.notesmuscles.NetworkProtocol.NetWorkProtocol;

class AccountInfoBuffer {

    //register user buffer
    static String FirstName;
    static String LastName;
    static String BilkentID;

    //timetable buffer
    static class Monday{
        static String dayID;
        static String lecture1;
        static String lecture2;
        static String lecture3;
        static String lecture4;
        static String getStringRep(){
            return dayID + NetWorkProtocol.DATA_DELIMITER + lecture1 + NetWorkProtocol.DATA_DELIMITER + lecture2 + NetWorkProtocol.DATA_DELIMITER
                    + lecture3 + NetWorkProtocol.DATA_DELIMITER + lecture4;
        }
    }
    static class Tuesday{
        static String dayID;
        static String lecture1;
        static String lecture2;
        static String lecture3;
        static String lecture4;
        static String getStringRep(){
            return dayID + NetWorkProtocol.DATA_DELIMITER + lecture1 + NetWorkProtocol.DATA_DELIMITER + lecture2 + NetWorkProtocol.DATA_DELIMITER
                    + lecture3 + NetWorkProtocol.DATA_DELIMITER + lecture4;
        }
    }
    static class Wednesday{
        static String dayID;
        static String lecture1;
        static String lecture2;
        static String lecture3;
        static String lecture4;
        static String getStringRep(){
            return dayID + NetWorkProtocol.DATA_DELIMITER + lecture1 + NetWorkProtocol.DATA_DELIMITER + lecture2 + NetWorkProtocol.DATA_DELIMITER
                    + lecture3 + NetWorkProtocol.DATA_DELIMITER + lecture4;
        }
    }
    static class Thursday{
        static String dayID;
        static String lecture1;
        static String lecture2;
        static String lecture3;
        static String lecture4;
        static String getStringRep(){
            return dayID + NetWorkProtocol.DATA_DELIMITER + lecture1 + NetWorkProtocol.DATA_DELIMITER + lecture2 + NetWorkProtocol.DATA_DELIMITER
                    + lecture3 + NetWorkProtocol.DATA_DELIMITER + lecture4;
        }
    }
    static class Friday{
        static String dayID;
        static String lecture1;
        static String lecture2;
        static String lecture3;
        static String lecture4;
        static String getStringRep(){
            return dayID + NetWorkProtocol.DATA_DELIMITER + lecture1 + NetWorkProtocol.DATA_DELIMITER + lecture2 + NetWorkProtocol.DATA_DELIMITER
                    + lecture3 + NetWorkProtocol.DATA_DELIMITER + lecture4;
        }
    }


    //username password buffer;
    static String username;
    static String password;
    static String confirmPassword;

}
