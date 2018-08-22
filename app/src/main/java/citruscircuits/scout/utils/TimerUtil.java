package citruscircuits.scout.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import citruscircuits.scout.R;

public class TimerUtil {

    public static CountDownTimer matchTimer = null;

    public static TextView mTimerView;
    public static TextView mActivityView;

    public static float timestamp;

    public static String displayTime;

    public static class MatchTimerThread extends Thread{

        @Override
        public void run() {
            matchTimer = new CountDownTimer(150000, 10) {
                public void onTick(long millisUntilFinished) {
                    float tempTime = millisUntilFinished/1000f;
                    timestamp = Float.parseFloat(String.format("%.1f", tempTime));
                    if (tempTime > 135.5f) {
                        mActivityView.setText("AUTO");
                        displayTime = String.valueOf(Math.round(tempTime) - 135);
                    }
                    else if (tempTime >= 134.5f && tempTime <= 135.5f) {
                        mActivityView.setText("TELE");
                        displayTime = "135";
                    }
                    else if (tempTime < 134.5f) {
                        mActivityView.setText("TELE");
                        displayTime = String.valueOf(Math.round(tempTime));
                    }
                    mTimerView.setText(displayTime);
                }

                public void onFinish() {

                }
            }.start();
        }

        public void initTimer(){
            run();
        }

    }

}