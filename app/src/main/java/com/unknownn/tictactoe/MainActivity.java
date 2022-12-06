package com.unknownn.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;

    private Button buttonOffline, buttonQuick;
    private ImageButton ibExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOffline = findViewById(R.id.button_offline);
        buttonQuick = findViewById(R.id.button_quick_match);
        ibExit = findViewById(R.id.button_exit);

        buttonOffline.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this, OfflineActivity.class);
            startActivity(intent);
        });

        buttonQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, QuickMatchActivity.class);
                startActivity(intent);
            }
        });

        ibExit.setOnClickListener(v -> finishAffinity());

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        }
        else {
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }

}
