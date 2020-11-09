package com.traidev.masterCoder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.traidev.masterCoder.Editor.CoderActivity;
import com.traidev.masterCoder.Youtube.VideosPlaylist;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //firebaseAuth = FirebaseAuth.getInstance();
     /*   SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        */
        /*if(sharedPrefManager.getsUser().getName() != null)
        {
            Intent loginIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(loginIntent);
        }
        else
        {
            Intent loginIntent = new Intent(SplashActivity.this, Home.class);
            startActivity(loginIntent);
        }*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), CoderActivity.class);
                startActivity(intent);
                finish();
            }
        },1500);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.splash));
            }

    }


}

