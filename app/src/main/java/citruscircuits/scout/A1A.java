package citruscircuits.scout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout.utils.AutoDialog;
import citruscircuits.scout.utils.TimerUtil;

//Written by the Daemon himself ~ Calvin
public class A1A extends DialogMaker implements View.OnClickListener{

    public boolean startTimer = true;

    public Button btn_topLeftSwitch;
    public Button btn_startTimer;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(AppCc.getSp("mapOrientation",99) != 99){
            if(AppCc.getSp("mapOrientation", 99) == 0){
                setContentView(R.layout.activity_map_new_rb);
            }else {
                setContentView(R.layout.activity_map_new_br);
            }
        }else{
            AppCc.setSp("mapOrientation", 0);
            setContentView(R.layout.activity_map_new_rb);
        }

        Fragment fragment = new AutoDialog();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if(InputManager.mAllianceColor.equals("Red")) {
            transaction.add(R.id.red_auto, fragment, "FRAGMENT");
        }
        else if(InputManager.mAllianceColor.equals("Blue")) {
            transaction.add(R.id.blue_auto, fragment, "FRAGMENT");
        }

        transaction.commit();

        if(TimerUtil.matchTimer != null) {
            TimerUtil.matchTimer.cancel();
            TimerUtil.matchTimer = null;
            TimerUtil.timestamp = 0f;
            TimerUtil.mTimerView.setText("15");
            TimerUtil.mActivityView.setText("AUTO");
            startTimer = true;
        }

//        btn_topLeftSwitch = findViewById(R.id.topleft_switch);
//        btn_topLeftSwitch.setOnClickListener(A1A.this);

        TimerUtil.mTimerView = findViewById(R.id.tv_timer);
        TimerUtil.mActivityView = findViewById(R.id.tv_activity);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.topleft_switch:
                initSeesawDialog(this, false, false);
                break;
        }
    }

    public void onClickTeleop(View view) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAGMENT");
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public void onClickStartTimer(View v) {
        TimerUtil.MatchTimerThread timerUtil = new TimerUtil.MatchTimerThread();
        btn_startTimer = findViewById(R.id.btn_timer);
        if(startTimer) {
            timerUtil.initTimer();
            btn_startTimer.setText("RESET TIMER");
            startTimer = false;
            if(InputManager.mAllianceColor.equals("Red")) {
                btn_startTimer.setBackgroundResource(R.drawable.auto_reset_red_selector);
            }
            else if(InputManager.mAllianceColor.equals("Blue")) {
                btn_startTimer.setBackgroundResource(R.drawable.auto_reset_blue_selector);
            }
        }
        else if(!startTimer) {
            Log.i("TIME", String.valueOf(TimerUtil.timestamp));
            TimerUtil.matchTimer.cancel();
            TimerUtil.matchTimer = null;
            TimerUtil.timestamp = 0f;
            TimerUtil.mTimerView.setText("15");
            TimerUtil.mActivityView.setText("AUTO");
            btn_startTimer.setText("START TIMER");
            startTimer = true;
            if(InputManager.mAllianceColor.equals("Red")) {
                btn_startTimer.setBackgroundResource(R.drawable.auto_red_selector);
            }
            else if(InputManager.mAllianceColor.equals("Blue")) {
                btn_startTimer.setBackgroundResource(R.drawable.auto_blue_selector);
            }
        }
    }

}
