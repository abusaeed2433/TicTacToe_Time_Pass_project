package com.unknownn.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class OfflineActivity extends AppCompatActivity implements View.OnClickListener{
    private final Handler mHandler = new Handler();
    private final Button[][] buttons=new Button[3][3];
    private TextView tvMessageMine,tvMessageOpponent;
    private ImageButton buttonRestartMine, buttonRestartOpponent;
    boolean player1=true,extra=true, specialVar =true;
    public static String winner;
    public static boolean isOpeningFromActivity3 =false, isRetryShowing =false, shouldIExit =false, isFromGameOver = false;
    private boolean isFromRestart = false;
    private int player1points=0,getPlayer2points=0,moveCounter=0;
    private final String[][] field = new String[3][3];
    private Toast mToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                String ButtonId="box"+i+j;
                int resId=getResources().getIdentifier(ButtonId,"id",getPackageName());
                buttons[i][j]=findViewById(resId);
                buttons[i][j].setOnClickListener(this);
            }
        }

        tvMessageMine = findViewById(R.id.tv_message_mine);
        tvMessageOpponent = findViewById(R.id.tv_message_opponent);
        buttonRestartMine = findViewById(R.id.button_restart_mine);
        buttonRestartOpponent = findViewById(R.id.button_restart_opponent);

        setListener();
    }

    private void setListener(){
        buttonRestartMine.setOnClickListener(v -> {
            isFromRestart = true;
            mHandler.postDelayed(runnable,500);
        });

        buttonRestartOpponent.setOnClickListener(v -> {
            isFromRestart = true;
            mHandler.postDelayed(runnable,500);
        });
    }

    private void showSafeToast(String message){
        try {
            if(mToast != null) mToast.cancel();
            mToast = Toast.makeText(this,message,Toast.LENGTH_LONG);
            mToast.show();
        }catch (Exception ignored){}
    }

    public void onResume(){

        if(GameOverActivity.goToMainPage){
            GameOverActivity.goToMainPage = false;
            finishAffinity();
        }

        if(isFromGameOver && !isRetryShowing){
            isFromRestart = true;
            setMessage(true);
            mHandler.postDelayed(runnable,500);
            isFromGameOver = false;
        }
        if(shouldIExit){
            shouldIExit = false;
            finish();
        }
        super.onResume();
    }

    public void FieldCreatorAndChecker(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        if (Winner(field)!=-1) {
            if (moveCounter % 2 == 1) {
                Player1Wins(Winner(field));
                specialVar =false;
            } else {
                Player2Wins(Winner(field));
                specialVar =false;
            }
        }
        else {
            if (moveCounter == 9) {
                Draw();
                specialVar =false;
            }
        }
    }

    public void onClick(View v){
        if (!((Button)v).getText().toString().equals("")) {
            showSafeToast(getString(R.string.unavailable_move));
        }
        else {
            if(specialVar) {
                if (player1) {
                    setMessage(false);
                    ((Button)v).setText("X");
                    ((Button)v).setTextColor(getResources().getColor(R.color.color2));
                    moveCounter++;
                } else {
                    setMessage(true);
                    ((Button)v).setText("O");
                    ((Button)v).setTextColor(getResources().getColor(R.color.white));
                    moveCounter++;
                }
                player1 = !player1;
            }
            else{
                showSafeToast(getString(R.string.cannot_move_now).toUpperCase());
            }
            FieldCreatorAndChecker();
        }
    }

    public void Player1Wins(int var){
        winner="PLAYER 1 WON";
        ShowAnime(var);
        isOpeningFromActivity3 =true;
        waitAndShow();
    }

    public void Player2Wins(int var){
        winner="PLAYER 2 WON";
        ShowAnime(var);
        isOpeningFromActivity3 =true;
        waitAndShow();
    }

    public void Draw(){
        winner="MATCH DRAW";
        isOpeningFromActivity3 =true;
        waitAndShow();
    }



    private void waitAndShow(){
        new Handler(Looper.getMainLooper()).postDelayed(() -> showEndingDialog(),800);
    }

    public void showEndingDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_layout);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        Button buttonRestart = dialog.findViewById(R.id.button_restart);
        Button buttonGoBack = dialog.findViewById(R.id.button_go_back);
        TextView textView = dialog.findViewById(R.id.tv_dialog);

        textView.setText(winner);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                isFromRestart = true;
                mHandler.postDelayed(runnable,500);
            }
        });

        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    private final Runnable runnable =new Runnable(){ // IT IS RESET BUTTON THAT WORKS AFTER 1S DELAY
        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setText("");
                    buttons[i][j].setBackgroundResource(R.drawable.button_ripple_2);
                }
            }
            setMessage(true);
            moveCounter = 0;
            player1 = true;
            specialVar =true;
            isOpeningFromActivity3 =false;
        }
    };

    private void setMessage(boolean myMove){
        if(myMove) {
            tvMessageMine.setText(getString(R.string.my_move));
            tvMessageOpponent.setText(getString(R.string.opponent_move));
        }
        else{
            tvMessageMine.setText(getString(R.string.opponent_move));
            tvMessageOpponent.setText(getString(R.string.my_move));
        }
    }

    private int Winner(String[][] field) {
        for(int i=0;i<3;i++){
            if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2])&& !field[i][0].equals("")){
                if(i==0) return 1;
                else if(i==1) return 2;
                else return 3;
            }
            if(field[0][i].equals(field[1][i]) &&field[2][i].equals(field[0][i])&&!field[0][i].equals("")){
                if(i==0) return 4;
                else if(i==1) return 5;
                else return 6;
            }
        }
        if(field[0][0].equals(field[1][1])&&field[0][0].equals(field[2][2])&&!field[0][0].equals("")){
            return 7;
        }
        if(field[0][2].equals(field[1][1])&&field[0][2].equals(field[2][0])&&!field[0][2].equals("")){
            return 8;
        }
        return -1;
    }

    //EXITING ANIMATION BELOW
    @Override
    public void finish(){
        super.finish();
    }


    public Runnable JustDelay = new Runnable(){
        @Override
        public void run(){
        }
    };

    public void ShowAnimeLoop(int i){
        for(int j=0;j<3;j++){
            buttons[i][j].setBackgroundColor(Color.parseColor("#FF0000"));
            mHandler.postDelayed(JustDelay,500);
        }
    }
    public void ShowAnimeLoop2(int i){
        for(int j=0;j<3;j++){
            buttons[j][i].setBackgroundColor(Color.parseColor("#FF0000"));
            mHandler.postDelayed(JustDelay,500);
        }
    }

    int i;

    public void ShowAnime(int var){
        if(var>=1 && var<=3) {
            if (var == 1) {
                i = 0;
            } else if (var == 2) {
                i = 1;
            } else {
                i = 2;
            }
            ShowAnimeLoop(i);
        }
        else if(var>=4 && var<=6){
            if(var==4) i=0;
            else if(var==5) i=1;
            else i=2;
            ShowAnimeLoop2(i);
        }
        else if(var==7){
            buttons[0][0].setBackgroundColor(Color.parseColor("#FF0000"));
            mHandler.postDelayed(JustDelay,500);
            buttons[1][1].setBackgroundColor(Color.parseColor("#FF0000"));
            mHandler.postDelayed(JustDelay,500);
            buttons[2][2].setBackgroundColor(Color.parseColor("#FF0000"));
            mHandler.postDelayed(JustDelay,500);
        }
        else{
            buttons[0][2].setBackgroundColor(Color.parseColor("#FF0000"));
            mHandler.postDelayed(JustDelay,500);
            buttons[1][1].setBackgroundColor(Color.parseColor("#FF0000"));
            mHandler.postDelayed(JustDelay,500);
            buttons[2][0].setBackgroundColor(Color.parseColor("#FF0000"));
            mHandler.postDelayed(JustDelay,500);
        }
    }
}
