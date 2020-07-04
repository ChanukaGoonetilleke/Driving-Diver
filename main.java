package com.dive.chanuka.trycatchtheball;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class main extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView pink;
    private ImageView black;
    private ImageView backgorund;

    //size
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    private Button pauseBtn;

    private boolean pause_flg = false;

    // Position
    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;
    private int backgorundX;

    //Speed
    private int boxSpeed;
    private int orangeSpeed;
    private int pinkSpeed;
    private int blackSpeed;

    //Score
    private int score = 0;

    // Initialize Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer sound;


    // status Check
    private boolean action_flg = false;
    private boolean start_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound = new SoundPlayer(this);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        box = (ImageView) findViewById(R.id.box);


        orange = (ImageView) findViewById(R.id.orange);
        pink = (ImageView) findViewById(R.id.pink);
        black = (ImageView) findViewById(R.id.black);
        pauseBtn = (Button) findViewById(R.id.pauseBtn);
        //backgorund = (ImageView) findViewById(R.id.backgorund);

        //get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;


        boxSpeed = Math.round(screenHeight / 60F);
        orangeSpeed = Math.round(screenWidth / 60F);
        pinkSpeed = Math.round(screenWidth / 36F);
        blackSpeed = Math.round(screenWidth / 45F);

        //Log.v("SPEED_BOX", boxSpeed+"");
     //   Log.v("SPEED_ORANGE", orangeSpeed+"");
      //  Log.v("SPEED_PINK", pinkSpeed+"");
       // Log.v("SPEED_BLACK", blackSpeed+"");



        // Move to out of screen
        orange.setX(-80);
        orange.setY(-80);
        pink.setX(-80);
        pink.setY(-80);
        black.setX(-80);
        black.setY(-80);

        scoreLabel.setText("Score: 0");

    }


    public void changePos() {

        hitCheck();

        //background moving
        /*backgorundX -= orangeSpeed;
        if(backgorundX < -screenWidth){
            backgorundX = (screenWidth+3) + 10;
        }
        backgorund.setX(backgorundX);
        */



        //Orange
        orangeX -= orangeSpeed; //speed
        if(orangeX < 0){
            orangeX = screenWidth + 20;
            orangeSpeed *=1.01;
            orangeY = (int) Math.floor(Math.random()*(frameHeight-orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);


        //Black
        blackX -= blackSpeed; //speed
        if(blackX < 0){

            blackX = screenWidth + 30;
            blackSpeed *=1.01;
            blackY = (int) Math.floor(Math.random()*(frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        // Pink
        pinkX -= pinkSpeed; //speed
        if(pinkX <0){
            pinkX = screenWidth + 6000;
            orangeSpeed *=1.0001;
            pinkY = (int) Math.floor(Math.random()*(frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);


        // Move Box up and down
        if(action_flg == true){
            //Touching
            boxY -= boxSpeed;
        }else {
            //releasing
            boxY += boxSpeed;
        }

        // check box position.
        if(boxY<0)boxY = 0;

        if(boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;

        box.setY(boxY);

        scoreLabel.setText("Score: " + score);
    }

    public void hitCheck()
    {

        //If the center of the ball is in the box, it counts as a hit

        //orange
        int orangeCenterX = orangeX + orange.getWidth()/2;
        int orangeCenterY = orangeY + orange.getHeight()/2;

        //0 <= orangeCenterX <= boxwidth
        // boxY <= orangecenterY <= boxY + boxHeight

        if(0 <= orangeCenterX && orangeCenterX <= boxSize && boxY <= orangeCenterY && orangeCenterY <= boxY + boxSize){
            score += 10;
            orangeX = -10;
            sound.playHitSound();
        }



        //Pink
        int pinkCenterX = pinkX + pink.getWidth()/2;
        int pinkCenterY = pinkY + pink.getHeight()/2;
        if(0 <= pinkCenterX && pinkCenterX <= boxSize && boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize){
            score += 30;
            pinkX = -10;
            sound.playHit2Sound();
        }

        //Black
        int blackCenterX = blackX + black.getWidth()/2;
        int blackCenterY = blackY + black.getHeight()/2;
        if(0 <= blackCenterX && blackCenterX <= boxSize && boxY <= blackCenterY && blackCenterY <= boxY + boxSize){

            //sound.playOverSound();
            //stop timer!
            timer.cancel();
            timer = null;

            sound.playOverSound();
            //show results.
            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }

    }

    public boolean onTouchEvent(MotionEvent me){

        if(start_flg == false){
            // this is how to aminate like a gif
            box.setImageResource(R.drawable.runningcat);
            AnimationDrawable runningCat = (AnimationDrawable)box.getDrawable();
            runningCat.start();

            orange.setImageResource(R.drawable.runningcat2);
            AnimationDrawable runningCat2 = (AnimationDrawable)orange.getDrawable();
            runningCat2.start();

            black.setImageResource(R.drawable.runningcat3);
            AnimationDrawable runningCat3 = (AnimationDrawable)black.getDrawable();
            runningCat3.start();

        /*    backgorund.setImageResource(R.drawable.runningcat2);
            AnimationDrawable runningCat2 = (AnimationDrawable)backgorund.getDrawable();
            runningCat2.start();

            */

            start_flg = true;

            //why get frame height and box height here?
            //because the UI has not been set on the screen in OnCreate()!!

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int) box.getY();

            // The box is a square.(height and width are the same.)
            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);
            timer.schedule(new TimerTask() {
                public void run(){
                    handler.post(new Runnable(){
                        public void run(){
                            changePos();
                        }
                    });
                }
            },0,20);

        }else{
            if(me.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;
            }else if (me.getAction() == MotionEvent.ACTION_UP){
                action_flg = false;
            }
        }



        return true;
    }

    public void pausePush(View view){

        if(pause_flg == false){

            pause_flg = true;

            timer.cancel();
            timer = null;

            pauseBtn.setText("START");
        }else{
            pause_flg = false;

            pauseBtn.setText("PAUSE");

            timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run(){
                    handler.post(new Runnable(){
                        public void run(){
                            changePos();
                        }
                    });
                }
            },0,20);

        }

    }



}
