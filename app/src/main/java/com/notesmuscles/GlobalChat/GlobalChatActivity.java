package com.notesmuscles.GlobalChat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.NetworkProtocol.NetWorkProtocol;
import com.notesmuscles.R;

public class GlobalChatActivity extends AppCompatActivity {

    private Button returnBtn, sendBtn;
    private TextView messages_view;
    private EditText messages_to_send;
    private Server_Pull_Chat server_pull_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_chat_activity);
        getSupportActionBar().hide();

        messages_view = (TextView) findViewById(R.id.MessagesTextView);
        messages_to_send  = (EditText) findViewById(R.id.MessageEditText);

        server_pull_chat = new Server_Pull_Chat(this);
        server_pull_chat.start();


        returnBtn = (Button) findViewById(R.id.returnToUserMenuButton);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                server_pull_chat.sendMessage(NetWorkProtocol.END_GLOBAL_CHAT);
                finish();
            }
        });

        sendBtn = (Button) findViewById(R.id.sendMessagebutton);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getMessage = messages_to_send.getText().toString();
                server_pull_chat.sendMessage(getMessage);
                messages_to_send.setText("");
            }
        });
    }

    public void setTextView(String messages) {
        messages_view.append(messages);
    }
}
