package com.unknownn.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.TimerTask;

public class QuickMatchActivity extends AppCompatActivity implements View.OnClickListener{

    private final Handler mHandler = new Handler();
    public Button[][] buttons=new Button[3][3];
    private Button buttonZero, buttonCross, buttonReset;
    private TextView tvMessage;
    private View RetryButtonShow;
    private boolean player1=true, specialVar =true,extra=true,isMax=true,moveCounterChecker=true;
    public boolean isGameOverShowing =false, isRandomChosen =false, hasSomeoneWin =false;
    public static boolean isRetryShowing =false, shouldIExit =false, isButtonCrossSelected =false,
            isFromGameOver = false, isOpeningFromActivity2 =false;
    private boolean isFromRestart = false;
    public static int winner=-1;
    public int moveCounter=0,ButtonZeroCLickedCounter, randomNum;
    String[][] field = new String[3][3];
    String s1,s2,player,opponent,move, moveVal;
    public int row1,col1;

    private Toast mToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_match);

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                String sid="box"+i+j;
                int resId = getResources().getIdentifier(sid,"id",getPackageName());
                buttons[i][j] = findViewById(resId);
                buttons[i][j].setOnClickListener(this);
            }
        }

        buttonCross = findViewById(R.id.button_cross);
        buttonZero = findViewById(R.id.button_zero);
        buttonReset = findViewById(R.id.button_reset);
        tvMessage = findViewById(R.id.tv_message);
        setListener();
    }

    public void onResume() {

        if(GameOverActivity.goToMainPage){
            GameOverActivity.goToMainPage = false;
            finish();
        }

        if(isFromGameOver && !isRetryShowing){
            isFromRestart = true;
            tvMessage.setText(getString(R.string.select_your_character_first));
            mHandler.postDelayed(runnable,1000);
            isFromGameOver = false;
        }
        if(shouldIExit){
            shouldIExit = false;
            finish();
        }
        super.onResume();
    }

    private void setListener(){
        buttonCross.setOnClickListener(v -> buttonCrossCLicked());
        buttonZero.setOnClickListener(v -> buttonZeroClicked());
        buttonReset.setOnClickListener(v -> {
            isFromRestart = true;
            mHandler.postDelayed(runnable,500);
        });
    }

    public void buttonCrossCLicked(){
        if(!isRetryShowing) {
            if (moveCounter == 0) {
                randomNum =new Random().nextInt(4);
                randomNum = (randomNum * 2) + 1;
                moveCounterChecker = false;
                tvMessage = findViewById(R.id.tv_message);
                tvMessage.setText(R.string.game_is_running);
                showSafeToast(getString(R.string.x_selected));
                s1 = "X";
                s2 = "O";
                player = "O";
                opponent = "X";
                buttonCross.setVisibility(View.GONE);
                buttonZero.setVisibility(View.GONE);
                isButtonCrossSelected =true;
            }
            else {
                showSafeToast(getString(R.string.message));
                extra = !extra;
            }
        }
    }

    private void showSafeToast(String message){
        try {
            if(mToast != null) mToast.cancel();
            mToast = Toast.makeText(this,message,Toast.LENGTH_LONG);
            mToast.show();
        }catch (Exception ignored){}
    }

    public void buttonZeroClicked(){
        if(!isRetryShowing) {
            if (moveCounter == 0) {
                moveCounterChecker = false;
                tvMessage.setText(getString(R.string.game_is_running));
                showSafeToast(getString(R.string.o_selected));
                randomNum =new Random().nextInt(4)+1;
                randomNum *=2;
                s1 = "O";
                s2 = "X";
                player = "X";
                opponent = "O";
                ButtonZeroCLickedCounter++;
                if (ButtonZeroCLickedCounter > 100) ButtonZeroCLickedCounter = 0;
                int row=new Random().nextInt(3);
                int col=new Random().nextInt(3);
                buttons[row][col].setText("X");
                buttons[row][col].setTextColor(getResources().getColor(R.color.white));
                moveCounter++;
                buttonCross.setVisibility(View.GONE);
                buttonZero.setVisibility(View.GONE);
            } else {
                showSafeToast(getString(R.string.message));
                extra = !extra;
            }
        }
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
            } else {
                Player2Wins(Winner(field));
            }
        }
        else {
            if (moveCounter == 9) {
                Draw();
            }
        }
    } //CHECK FOR WIN LOSE OR DRAW OR ELSE

    public void onClick(View v){
        if(moveCounterChecker){
            showSafeToast(getString(R.string.select_your_character_first));
            extra=!extra;
        }
        else {
            if (!((Button) v).getText().toString().equals("")) {
                showSafeToast(getString(R.string.unavailable_move));
            }
            else {
                if (player1) {
                    if(specialVar) {
                        ((Button) v).setText(s1);
                        ((Button) v).setTextColor(Color.parseColor("#00ff00"));
                        moveCounter++;
                        specialVar =false;
                        FieldCreatorAndChecker();
                        if(moveCounter!=9 && !hasSomeoneWin) {
                            if(moveCounter == randomNum){
                                int[] arr=new int[EmptyCounter()];
                                arr=CreateList(arr);
                                int randompos=new Random().nextInt(arr.length);
                                moveCounter++;
                                if(arr[randompos]==1){ row1=0;col1=0;}
                                else if(arr[randompos]==2){ row1=0;col1=1;}
                                else if(arr[randompos]==3){ row1=0;col1=2;}
                                else if(arr[randompos]==4){ row1=1;col1=0;}
                                else if(arr[randompos]==5){ row1=1;col1=1;}
                                else if(arr[randompos]==6){ row1=1;col1=2;}
                                else if(arr[randompos]==7){ row1=2;col1=0;}
                                else if(arr[randompos]==8){ row1=2;col1=1;}
                                else if(arr[randompos]==9){ row1=2;col1=2;}
                                mHandler.postDelayed(MakeDelay2,1000);
                                specialVar =true;
                            }
                            else {
                                mHandler.postDelayed(MakeDelay, 1000);
                            }
                        }
                    }
                    else{
                        showSafeToast(getString(R.string.cannot_move_now));
                    }
                }
            }
        }
    }

    public int[] CreateList(int[] arr){
        int k=0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(buttons[i][j].getText().toString().equals("")){
                    arr[k]=3*i+j+1;
                    k++;
                }
            }
        }
        return arr;
    }
    public int EmptyCounter(){
        int count=0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(buttons[i][j].getText().toString().equals("")) count++;
            }
        }
        return count;
    } // COUNT EMPTY ELEMENT


    private final Runnable MakeDelay=new Runnable() {
        @Override
        public void run() {
            moveCounter++;
            moveVal =FindBestMove(field);
            int row=Integer.parseInt(String.valueOf(moveVal.charAt(0)));
            int col=Integer.parseInt(String.valueOf(moveVal.charAt(1)));
            buttons[row][col].setText(s2);
            buttons[row][col].setTextColor(getResources().getColor(R.color.white));
            FieldCreatorAndChecker();
            specialVar =true;
        }
    };  // IT WILL MAKE A DELAY OF 500MS

    public Runnable MakeDelay2 = new Runnable(){
        @Override
        public void run() {
            buttons[row1][col1].setText(s2);
            buttons[row1][col1].setTextColor(getResources().getColor(R.color.white));
            FieldCreatorAndChecker();
        }
    };

    public void Player1Wins(int var){
        specialVar =false;
        ShowAnime(var);
        mHandler.postDelayed(justDelay,1000);
        hasSomeoneWin =true;
        if(isButtonCrossSelected) winner=1;
        else winner=2;
        isOpeningFromActivity2 =true;
        OpenGameOverScreen();
    }
    public void Player2Wins(int var){
        specialVar =false;
        ShowAnime(var);
        mHandler.postDelayed(justDelay,1000);
        if(isButtonCrossSelected) winner=2;
        else winner=1;
        isOpeningFromActivity2 =true;
        hasSomeoneWin =true;
        OpenGameOverScreen();
    };
    public void Draw(){
        specialVar =false;
        mHandler.postDelayed(justDelay,1000);
        isOpeningFromActivity2 =true;
        hasSomeoneWin =true;
        OpenGameOverScreen();
    }

    public void OpenGameOverScreen(){
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent=new Intent(QuickMatchActivity.this,GameOverActivity.class);
            startActivity(intent);
        },800);
    }

    public Runnable justDelay = () -> { }; //just for a delay

    private final Runnable runnable = new Runnable(){
        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setText("");
                    buttons[i][j].setBackgroundResource(R.drawable.button_ripple);
                }
            }
            buttonCross.setVisibility(View.VISIBLE);
            buttonZero.setVisibility(View.VISIBLE);

            if(isFromRestart){
                isFromRestart = false;
            }
            else{
                showSafeToast(getString(R.string.select_your_character_first));
            }

            moveCounter = 0;
            moveCounterChecker = true;
            player1 = true;
            specialVar = true;
            isGameOverShowing =false;
            shouldIExit =false;
            winner=-1;
            isButtonCrossSelected =false;
            isOpeningFromActivity2 =false;
            hasSomeoneWin =false;
        }
    };

    public void ResetFromAnotherActivity(){
        Toast.makeText(this, "Toast", Toast.LENGTH_SHORT).show();
    }
    // THIS METHOD WILL BE CALLED FROM GAME OVER SCREEN
    public void ResetFromAnotherActivity1(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String ButtonId="Box"+i+j;
                int resId=getResources().getIdentifier(ButtonId,"id",getPackageName());
                buttons[i][j]=findViewById(resId);
                buttons[i][j].setText("");
                buttons[i][j].setBackgroundColor(Color.parseColor("#BAE8E1"));
            }
        }
        buttonCross.setVisibility(View.VISIBLE);
        buttonZero.setVisibility(View.VISIBLE);
        showSafeToast(getString(R.string.select_your_character_first));
        moveCounter = 0;
        moveCounterChecker = true;
        player1 = true;
        specialVar = true;
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


    public Boolean isMovesLeft(String[][] field){ //
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (field[i][j].equals(""))
                    return true;
        return false;
    }

    public int Evaluate(String b[][]){
        for (int row = 0; row < 3; row++){
            if (b[row][0] == b[row][1] &&
                    b[row][1] == b[row][2])
            {
                if (b[row][0] == player)
                    return +10;                           //+10 means AI wins and -10 means User wins.
                else if (b[row][0] == opponent)
                    return -10;
            }
        } //CHECKING ROWWISE

        for (int col = 0; col < 3; col++){
            if (b[0][col] == b[1][col] &&
                    b[1][col] == b[2][col])
            {
                if (b[0][col] == player)
                    return +10;

                else if (b[0][col] == opponent)
                    return -10;
            }
        }  //CHECKING COLUMNWISE

        if (b[0][0] == b[1][1] && b[1][1] == b[2][2]){
            if (b[0][0] == player)
                return +10;
            else if (b[0][0] == opponent)
                return -10;
        }  //CHECKING DIAGONALLY

        if (b[0][2] == b[1][1] && b[1][1] == b[2][0]){
            if (b[0][2] == player)
                return +10;
            else if (b[0][2] == opponent)
                return -10;
        }  //CHECKING DIAGONALLYs

        return 0;  //IF NONE WINS RETURN 0
    }  //CHECKING FOR WINNER

    public int Minimax(String[][] field, int depth, Boolean isMax){
        int score = Evaluate(field);
        if (score==10)
            return score;
        if (score == -10)
            return score;
        if (!isMovesLeft(field))
            return 0;
        if(isMax){
            int best = -1000;
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (field[i][j].equals(""))
                    {
                        field[i][j] = player;
                        best = Math.max(best, Minimax(field,
                                depth+1,!isMax));

                        field[i][j]="";
                    }
                }
            }
            return best;
        }
        else{
            int best = 1000;
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (field[i][j].equals(""))
                    {
                        field[i][j]=opponent;
                        best = Math.min(best, Minimax(field,
                                depth+1, !isMax));

                        field[i][j]="";
                    }
                }
            }
            return best;
        }
    }

    public String FindBestMove(String[][] field){
        int bestVal = -1000,row=-1,col=-1;
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (field[i][j].equals(""))
                {
                    field[i][j]=player;
                    int moveVal = Minimax(field, 0, false);
                    field[i][j]="";
                    if (moveVal>bestVal)
                    {
                        row = i;
                        col=j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        move=Integer.toString(row)+Integer.toString(col);   //making string and returning value
        return move;
    }
    //AUTOMATIC MOVE FINDER ABOVE


    //BELOW PART WILL SHOW SIMPLE ANIMATION
    public void ShowAnimeLoop(int i){
        for(int j=0;j<3;j++){
            mHandler.postDelayed(justDelay,500);
            buttons[i][j].setBackgroundColor(getResources().getColor(R.color.light_red));
        }
    }

    public void ShowAnimeLoop2(int i){
        for(int j=0;j<3;j++){
            mHandler.postDelayed(justDelay,500);
            buttons[j][i].setBackgroundColor(getResources().getColor(R.color.light_red));
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
            buttons[0][0].setBackgroundColor(getResources().getColor(R.color.light_red));
            mHandler.postDelayed(justDelay,500);
            buttons[1][1].setBackgroundColor(getResources().getColor(R.color.light_red));
            mHandler.postDelayed(justDelay,500);
            buttons[2][2].setBackgroundColor(getResources().getColor(R.color.light_red));
            mHandler.postDelayed(justDelay,500);
        }
        else{
            mHandler.postDelayed(justDelay,500);
            buttons[0][2].setBackgroundColor(getResources().getColor(R.color.light_red));
            mHandler.postDelayed(justDelay,500);
            buttons[1][1].setBackgroundColor(getResources().getColor(R.color.light_red));
            mHandler.postDelayed(justDelay,500);
            buttons[2][0].setBackgroundColor(getResources().getColor(R.color.light_red));
        }
    }

}
