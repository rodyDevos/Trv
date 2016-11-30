
package com.igames2go.t4f.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.igames2go.t4f.R;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spashscreen);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        new Timer(5000, 1000).start();
    }

    private class Timer extends CountDownTimer {

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onFinish() {

            startActivity(new Intent(getApplicationContext(), LoginPage.class));
            finish();

        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub

        }

    }
}
