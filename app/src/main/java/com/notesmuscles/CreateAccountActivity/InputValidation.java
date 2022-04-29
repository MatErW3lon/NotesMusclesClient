package com.notesmuscles.CreateAccountActivity;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

class InputValidation {

    private static Pattern pattern;
    private static Matcher matcher;

    static boolean validateUserInfo(AppCompatActivity userInfoActivity) {
        pattern = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);
        String[] userInfo = new String[]{AccountInfoBuffer.FirstName, AccountInfoBuffer.LastName, AccountInfoBuffer.BilkentID};
        //the first and last name should not contain any character other than  a digit
        for (int i = 0; i < userInfo.length - 1; i++) {
            for (int j = 0; j < userInfo[i].length(); j++) {
                matcher = pattern.matcher(userInfo[i].charAt(j) + "");
                if (!matcher.find()) {
                    int finalI = i;
                    userInfoActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String buildWarningStr = userInfo[finalI] + " IS NOT A VALID INPUT";
                            Toast.makeText(userInfoActivity.getApplicationContext(),buildWarningStr , Toast.LENGTH_LONG).show();
                        }
                    });
                    return  false;
                }
            }
        }

        //if the first and last name are all correct, check the bilkent id
        pattern = Pattern.compile("[0-9]");
        if(userInfo[2].length() != 8){
            userInfoActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(userInfoActivity.getApplicationContext(), "BILKENT ID LENGTH INVALID", Toast.LENGTH_SHORT).show();
                }
            });
            return false;
        }

        //check each digit
        for(int i = 0 ; i < userInfo[2].length(); i++){
            matcher = pattern.matcher(userInfo[2].charAt(i) + "");
            if(!matcher.find()){
                userInfoActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(userInfoActivity.getApplicationContext(), "BILKENT ID MUST BE ALL DIGITS", Toast.LENGTH_SHORT).show();
                    }
                });
            return false;
            }
        }
        //all data validated
        return true;
    }



}


