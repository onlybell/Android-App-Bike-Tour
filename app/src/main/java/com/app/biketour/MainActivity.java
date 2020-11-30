package com.app.biketour;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    /**
     * Description:
     *      Loading Page
     * Function:
     *      onCreate()
     */

    //Loading time
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);

                //Animation for transition
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                finish();
            }
        },SPLASH_TIME_OUT);
    }

}
