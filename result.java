package com.dive.chanuka.trycatchtheball;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public class result extends AppCompatActivity {

    private InterstitialAd interstitial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        interstitial = new InterstitialAd(this);
            //interstitial.setAdUnitId("ca-app-pub-9167408919955062/4676875091");
            interstitial.setAdUnitId("ca-app-pub-9167408919955062/1324491783");

            AdRequest adRequest = new AdRequest.Builder().build();


        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        TextView highScoreLabel = (TextView) findViewById(R.id.highScoreLabel);

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE",0);

        if(score > highScore){
            highScoreLabel.setText("Your High Score : " + score);

            //save
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        }else{
            highScoreLabel.setText("Your High Score : " + highScore);
        }

        if(score%15==0 && score != 0) {
            interstitial.loadAd(adRequest);

            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    displayInterstitial();
                }
            });
        }

    }

    public void callForInctructions(View view){
        startActivity(new Intent(getApplicationContext(), Instructions.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void displayInterstitial(){
        if(interstitial.isLoaded()){
            interstitial.show();
        }
    }

    public void tryAgain(View view){
        startActivity(new Intent(getApplicationContext(), main.class));
    }

}
