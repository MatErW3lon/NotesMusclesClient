package com.notesmuscles.ProfileActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.CreateAccountActivity.RegisterUserInfoActivity;
import com.notesmuscles.R;
import com.notesmuscles.WelcomeActivity;

public class DeleteConfirmPopActivity extends AppCompatActivity {

    private TextView warningMessage;
    private Button confirmButton, cancelButton;
    private Delete_Acc_Thread delete_acc_thread;

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) { }
                    }
            );

    private final String warning = "<h2>WARNING</h2><br>" +
            "<p>This action can not be undone. Do you wish to continue?</p>";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancelacc_pop_activity);
        getSupportActionBar().hide();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.6), (int)(height*.4));

        warningMessage = (TextView) findViewById(R.id.CreateAccPopText);
        warningMessage.setText(Html.fromHtml(warning, Html.FROM_HTML_MODE_COMPACT));

        confirmButton = (Button) findViewById(R.id.confirm_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_acc_thread = new Delete_Acc_Thread();
                delete_acc_thread.start();
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
