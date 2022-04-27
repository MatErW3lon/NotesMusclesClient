package com.notesmuscles;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                        }
                    }
            );

    private ImageView animation;

    @Override
    protected void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        getSupportActionBar().hide();

        animation = (ImageView) findViewById(R.id.imageView);

        Handler animation_handler = new Handler();



        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++){
                    try{
                        Thread.sleep(300);
                    }catch(InterruptedException interruptedException){}
                    if(i % 2 == 0 )
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                animation.setImageResource(R.drawable.logostartscreen2);
                            }
                        });
                    else
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                animation.setImageResource(R.drawable.logostartscreen1);
                            }
                        });
                }
            }
        }).start();

        Handler intent_handler = new Handler();
        intent_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                activityResultLauncher.launch(intent);
            }
        }, 2000);
    }
}
