package com.notesmuscles.Notes;

import android.content.Intent;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.NetworkProtocol.NetWorkProtocol;
import com.notesmuscles.R;
import com.notesmuscles.UserMenuActivity;

import java.io.IOException;

public class Share_Notes_Popup_Activity extends AppCompatActivity {

    private TextView promptForInputTextView;
    private Button confirmBtn, cancelBtn;
    private EditText receiverBilkentIDEditText;

    private final String prompt = "<h2>ENTER USER BILKENT ID</h2>";

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) { }
                    }
            );

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_lecture_edit_activity);
        getSupportActionBar().hide();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.6));

        promptForInputTextView = (TextView) findViewById(R.id.PromptForEditPopText);
        promptForInputTextView.setText(Html.fromHtml(prompt, Html.FROM_HTML_MODE_COMPACT));

        receiverBilkentIDEditText = (EditText) findViewById(R.id.InputEditText);
        receiverBilkentIDEditText.setHint("ENTER ID");

        confirmBtn = (Button) findViewById(R.id.confirm_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent[] intent = new Intent[1];
                String bilkentIDReceiver = receiverBilkentIDEditText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            LoginActivity.dataOutputStream.writeUTF(bilkentIDReceiver);
                            LoginActivity.dataOutputStream.flush();

                            //wait for response
                            String response = LoginActivity.dataInputStream.readUTF();
                            if(response.equals(NetWorkProtocol.SHARE_NOTES_CONFIRMATION)){
                                intent[0] = new Intent(getApplicationContext(), UserMenuActivity.class);
                                activityResultLauncher.launch(intent[0]);
                            }else if(response.equals(NetWorkProtocol.SHARE_NOTES_ERROR_STATUS_NOUSER)){
                                //launch relevant error message
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), bilkentIDReceiver + " NOT REGISTERED", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else if(response.equals(NetWorkProtocol.SHARE_NOTES_ERROR_STATUS_NOLECTURE)){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), bilkentIDReceiver + " DOES NOT TAKE THE LECTURE", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }catch(IOException ioException){
                            ioException.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        cancelBtn =  (Button) findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            LoginActivity.dataOutputStream.writeUTF("CANCEL");
                            LoginActivity.dataOutputStream.flush();

                        }catch(IOException ioException){
                            ioException.printStackTrace();
                        }
                    }
                }).start();
                Intent intent = new Intent(getApplicationContext(), UserMenuActivity.class);
                activityResultLauncher.launch(intent);
            }
        });
    }
}
