package citruscircuits.scout.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import citruscircuits.scout.R;

public class TimerUtil {

    public static CountDownTimer matchTimer = null;

    public static TextView mTimerView;

    public static float timestamp;

    public static String displayTime;

    public static class MatchTimerThread extends Thread{

        @Override
        //Set timer parameters when run
        public void run() {
            matchTimer = new CountDownTimer(150000, 10) {
                public void onTick(long millisUntilFinished) {
                    float tempTime = millisUntilFinished/1000f;
                    timestamp = Float.parseFloat(String.format("%.1f", tempTime));
                    //Adjust time depending on if time is greater than or less than 135 seconds
                    if (tempTime > 135.5f) {
                        displayTime = String.valueOf(Math.round(tempTime) - 135);
                    }
                    else if (tempTime < 134.5f) {
                        displayTime = String.valueOf(Math.round(tempTime));
                    }
                    mTimerView.setText(displayTime);
                }

                public void onFinish() {

                }
            }.start();
        }

        //Start timer
        public void initTimer(){
            run();
        }

    }

}