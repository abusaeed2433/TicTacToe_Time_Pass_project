package com.unknownn.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {


    public TextView TextViewTester,TextViewWinLoseDraw;
    private Button ButtonRetryTester;
    public boolean isgameovershowing=true;
    private ImageView ImageViewWin,ImageViewLose,ImageViewDraw;
    public static boolean goToMainPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFinishOnTouchOutside(false);
        ButtonRetryTester=findViewById(R.id.ButtonRetryTester);
        ImageViewWin=findViewById(R.id.ImageViewWin);
        ImageViewLose=findViewById(R.id.ImageViewLose);
        ImageViewDraw=findViewById(R.id.ImageViewDraw);
        TextViewWinLoseDraw = findViewById(R.id.TextViewWinLoseDraw);


        if (QuickMatchActivity.winner == 1) {
            ImageViewWin.setVisibility(View.VISIBLE);
            TextViewWinLoseDraw.setText("you won".toUpperCase());
            TextViewWinLoseDraw.setTextSize(50);
            TextViewWinLoseDraw.setTextColor(Color.parseColor("#00FF00"));
        } else if (QuickMatchActivity.winner == 2) {
            ImageViewLose.setVisibility(View.VISIBLE);
            TextViewWinLoseDraw.setText("you lost".toUpperCase());
            TextViewWinLoseDraw.setTextSize(50);
            TextViewWinLoseDraw.setTextColor(Color.parseColor("#FF0000"));
        } else {
            ImageViewDraw.setVisibility(View.VISIBLE);
            TextViewWinLoseDraw.setText("match draw".toUpperCase());
            TextViewWinLoseDraw.setTextSize(50);
            TextViewWinLoseDraw.setTextColor(Color.parseColor("#0000FF"));
        }
        ButtonRetryTester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuickMatchActivity.isRetryShowing = false;
                QuickMatchActivity.isFromGameOver = true;
                goToMainPage = false;
                finish();
            }
        });
        Button ButtonGoBack = findViewById(R.id.ButtonGoBack);
        ButtonGoBack.setOnClickListener(view -> {
            QuickMatchActivity.shouldIExit = true;
            goToMainPage = true;
            finish();
        });

    }

}