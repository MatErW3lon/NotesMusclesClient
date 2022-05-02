package com.notesmuscles.CreateAccountActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.notesmuscles.LoginActivity;
import com.notesmuscles.R;
import com.notesmuscles.WelcomeActivity;

public class CancelPopActivity extends AppCompatActivity {

    private TextView warningMessage;
    private Button confirmButton, cancelButton;

    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) { }
                    }
            );


    private final String warning = "<h2>WARNING</h2><br>" +
            "<p>if you return, all data entered will be lost. Confirm?</p>";

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

        getWindow().setLayout((int)(width*.8), (int)(height*.6));

        warningMessage = (TextView) findViewById(R.id.CreateAccPopText);
        warningMessage.setText(Html.fromHtml(warning, Html.FROM_HTML_MODE_COMPACT));

        confirmButton = (Button) findViewById(R.id.confirm_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUserInfoActivity.serverConnection.CancelAccountCreation();
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
