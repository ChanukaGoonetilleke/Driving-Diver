package com.dive.chanuka.trycatchtheball;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class rr extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decarView = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decarView.setSystemUiVisibility(uiOption);

        setContentView(R.layout.activity_rr);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent homeIntent = new Intent(rr.this, start.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
