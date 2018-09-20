package citruscircuits.scout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONException;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout.utils.AutoDialog;
import citruscircuits.scout.utils.TimerUtil;

import static java.lang.String.valueOf;

//Written by the Daemon himself ~ Calvin
public class A1A extends DialogMaker implements View.OnClickListener{

    public boolean incapChecked = false;
    public boolean startTimer = true;

    public TextView tv_team;

    public Button btn_topLeftSwitch;
    public Button btn_startTimer;
    public Button btn_drop;
    public Button btn_spill;
    public Button btn_foul;
    public Button btn_undo;
    public Button btn_edit;
    public Button btn_ftb;
    public Button btn_arrow;

    public ToggleButton tb_incap;
    public ToggleButton tb_auto_run;
    public ToggleButton tb_start_cube;

    public RadioGroup rg_blue_starting_position;
    public RadioGroup rg_red_starting_position;

    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        public void run() {
            btn_arrow.setEnabled(true);
            btn_arrow.setVisibility(View.VISIBLE);
        }
    };

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

        tv_team = findViewById(R.id.tv_teamNum);

        btn_drop = findViewById(R.id.btn_dropped);
        btn_spill = findViewById(R.id.btn_spilled);
        btn_foul = findViewById(R.id.btn_fouled);
        btn_undo = findViewById(R.id.btn_undo);
        btn_edit = findViewById(R.id.btn_edit_data);
        btn_ftb = findViewById(R.id.btn_ftb);
        btn_arrow = findViewById(R.id.btn_arrow);

        tb_incap = findViewById(R.id.tgbtn_incap);
        tb_auto_run = findViewById(R.id.tgbtn_auto_run);
        tb_start_cube = findViewById(R.id.tgbtn_start_with_cube);

        rg_blue_starting_position = findViewById(R.id.blue_starting_position);
        rg_red_starting_position = findViewById(R.id.red_starting_position);

        TimerUtil.mTimerView = findViewById(R.id.tv_timer);
        TimerUtil.mActivityView = findViewById(R.id.tv_activity);

        tv_team.setText(valueOf(InputManager.mTeamNum));
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
        for (int i = 0; i < rg_blue_starting_position.getChildCount(); i++) {
            rg_blue_starting_position.getChildAt(i).setEnabled(false);
            rg_red_starting_position.getChildAt(i).setEnabled(false);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAGMENT");
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public void onClickStartTimer(View v) {
        handler.removeCallbacks(runnable);
        handler.removeCallbacksAndMessages(null);
        TimerUtil.MatchTimerThread timerUtil = new TimerUtil.MatchTimerThread();
        btn_startTimer = findViewById(R.id.btn_timer);
        btn_arrow.setEnabled(false);
        btn_arrow.setVisibility(View.INVISIBLE);
        if(startTimer) {
            handler.postDelayed(runnable, 150000);
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
            handler.removeCallbacks(runnable);
            handler.removeCallbacksAndMessages(null);
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

    public void onClickDrop (View v) throws JSONException {
        InputManager.dropTimes.put(TimerUtil.timestamp);
    }

    public void onClickSpill (View v) {
        InputManager.numSpill +=1;
        btn_spill.setText("SPILL - " + InputManager.numSpill);
    }

    public void onClickFoul (View v) {
        InputManager.numFoul +=1;
        btn_foul.setText("FOUL - " + InputManager.numFoul);
    }

    public void onClickUndo (View v) {

    }

    public void onClickEdit (View v) {

    }

    public void onClickFTB (View v) {

    }

    public void onClickIncap (View v) throws JSONException {
        if(!incapChecked) {
            tb_incap.setChecked(true);
            InputManager.incapTimes.put(TimerUtil.timestamp);
            incapChecked = true;
        }
        else if(incapChecked) {
            tb_incap.setChecked(false);
            InputManager.unincapTimes.put(TimerUtil.timestamp);
            incapChecked = false;
        }
    }

    public void onClickDataCheck (View v) {
        open(A2A.class, null, false, true);
    }
}
