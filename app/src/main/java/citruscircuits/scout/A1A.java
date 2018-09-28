package citruscircuits.scout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
    public boolean shapeCheck = false;
    public boolean noShape = false;

    public TextView tv_team;

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

    public RadioButton rb_blue_right;
    public RadioButton rb_blue_center;
    public RadioButton rb_blue_left;
    public RadioButton rb_red_right;
    public RadioButton rb_red_center;
    public RadioButton rb_red_left;

    public RelativeLayout overallLayout;

    public ImageView iv;
    public ImageView iv2;

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
                setContentView(R.layout.activity_map_rb);
                overallLayout = findViewById(R.id.fieldrb);
            }else {
                setContentView(R.layout.activity_map_br);
                overallLayout = findViewById(R.id.fieldbr);
            }
        }else{
            AppCc.setSp("mapOrientation", 0);
            setContentView(R.layout.activity_map_rb);
            overallLayout = findViewById(R.id.fieldrb);
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

        rb_blue_right = findViewById(R.id.blue_starting_position_right);
        rb_blue_center = findViewById(R.id.blue_starting_position_center);
        rb_blue_left = findViewById(R.id.blue_starting_position_left);
        rb_red_right = findViewById(R.id.red_starting_position_right);
        rb_red_center = findViewById(R.id.red_starting_position_center);
        rb_red_left = findViewById(R.id.red_starting_position_left);

        iv = new ImageView(getApplicationContext());
        iv2 = new ImageView(getApplicationContext());

        TimerUtil.mTimerView = findViewById(R.id.tv_timer);
        TimerUtil.mActivityView = findViewById(R.id.tv_activity);

        tv_team.setText(valueOf(InputManager.mTeamNum));

        addTouchListener();
    }

    @Override
    public void onClick(View v){

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
        if(rb_blue_right.isChecked() || rb_red_right.isChecked()) {
            InputManager.mStartingPosition = "right";
        }
        else if(rb_blue_center.isChecked() || rb_red_center.isChecked()) {
            InputManager.mStartingPosition = "center";
        }
        else if(rb_blue_left.isChecked() || rb_red_left.isChecked()) {
            InputManager.mStartingPosition = "left";
        }
        open(A2A.class, null, false, true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTouchListener(){
        overallLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                float x= (int) motionEvent.getX();
//                float y= (int) motionEvent.getY();
//                String message = String.format("Coordinates:(%.2f,%.2f)",x,y);
//                Log.d("hello", message);
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) motionEvent.getX();
                    int y = (int) motionEvent.getY();
                    if (x<=1700 && y<=930){
                        if (!shapeCheck){
                            overallLayout.removeView(iv2);
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                    50,
                                    50);
                            //ImageView iv= new ImageView(getApplicationContext());
                            lp.setMargins(x-25, y-40, 0, 0);
                            iv.setLayoutParams(lp);
                            iv.setImageDrawable(getResources().getDrawable(
                                    R.drawable.triangle_image));
                            ((ViewGroup) view).addView(iv);
                            shapeCheck=true;
                            // overallLayout.removeView(iv);
                            if (x<=425 && y<=930 && InputManager.mScoutId <=6 || x>75 && x<=285 && y<=575 && InputManager.mScoutId >6){
                                Log.d("locationInput","1");
                            }
                            else if (x>425 && x<=850 && y<=930 && InputManager.mScoutId <=6 || x>285 && x<=575 && y<=575 && InputManager.mScoutId >6){
                                Log.d("locationInput","2");
                            }
                            else if (x>850 && x<=1275 && y<=930 && InputManager.mScoutId <=6 || x>575 && x<=865 && y<=575 && InputManager.mScoutId >6){
                                Log.d("locationInput","3");
                            }
                            else if (x<=1700 && y<=930 && InputManager.mScoutId <=6 || x<=1075 && y<=575 && InputManager.mScoutId >6){
                                Log.d("locationInput","4");
                            }
                        }
                        else if (shapeCheck){
                            overallLayout.removeView(iv);
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                    50,
                                    50);
                            shapeCheck=false;
                            noShape=true;
                            if (x>=325 && x<=490 && y>=140 && y<=330 && InputManager.mScoutId <=6 || x>=230 && x<=310 && y>=90 && y<=210 && InputManager.mScoutId >6){
                                Log.d("locationOutput","Top Left switch");
                                //ImageView iv = new ImageView(getApplicationContext());
                                lp.setMargins(x-25, y-40, 0, 0);
                                iv2.setLayoutParams(lp);
                                iv2.setImageDrawable(getResources().getDrawable(
                                        R.drawable.blackcircle));
                                ((ViewGroup) view).addView(iv2);
                            }
                            else if (x>=325 && x<=595 && y>=140 && y<=800 && InputManager.mScoutId <=6 || x>=230 && x<=310 && y>=400 && y<=530 && InputManager.mScoutId >6){
                                Log.d("locationOutput","Bottom Left switch");
                                //ImageView iv = new ImageView(getApplicationContext());
                                lp.setMargins(x-25, y-40, 0, 0);
                                iv2.setLayoutParams(lp);
                                iv2.setImageDrawable(getResources().getDrawable(
                                        R.drawable.blackcircle));
                                ((ViewGroup) view).addView(iv2);
                            }
                            else if (x>=1250 && x<=1400 && y>=140 && y<=330 && InputManager.mScoutId <=6 || x>=780 && x<=900 && y>=100 && y<=210 && InputManager.mScoutId >6){
                                Log.d("locationOutput","Top Right switch");
                                //ImageView iv = new ImageView(getApplicationContext());
                                lp.setMargins(x-25, y-40, 0, 0);
                                iv2.setLayoutParams(lp);
                                iv2.setImageDrawable(getResources().getDrawable(
                                        R.drawable.blackcircle));
                                ((ViewGroup) view).addView(iv2);
                            }
                            else if (x>=1250 && x<=1400 && y>=600 && y<=800 && InputManager.mScoutId <=6 || x>=780 && x<=900 && y>=400 && y<=530 && InputManager.mScoutId >6){
                                Log.d("locationOutput","Bottom Right switch");
                                //ImageView iv = new ImageView(getApplicationContext());
                                lp.setMargins(x-25, y-40, 0, 0);
                                iv2.setLayoutParams(lp);
                                iv2.setImageDrawable(getResources().getDrawable(
                                        R.drawable.blackcircle));
                                ((ViewGroup) view).addView(iv2);
                            }
                            else if (x>=800 && x<=940 && y>=60 && y<=240 && InputManager.mScoutId <=6 || x>=530 && x<=620 && y>=60 && y<=165 && InputManager.mScoutId >6){
                                Log.d("locationOutput","Scale Top");
                                //ImageView iv = new ImageView(getApplicationContext());
                                lp.setMargins(x-25, y-40, 0, 0);
                                iv2.setLayoutParams(lp);
                                iv2.setImageDrawable(getResources().getDrawable(
                                        R.drawable.blackcircle));
                                ((ViewGroup) view).addView(iv2);
                            }
                            else if (x>=790 && x<=940 && y>=700 && y<=870 && InputManager.mScoutId <=6 || x>=530 && x<=620 && y>=450 && y<=560 && InputManager.mScoutId >6){
                                Log.d("locationOutput","Scale Bottom");
                                //ImageView iv = new ImageView(getApplicationContext());
                                lp.setMargins(x-25, y-40, 0, 0);
                                iv2.setLayoutParams(lp);
                                iv2.setImageDrawable(getResources().getDrawable(
                                        R.drawable.blackcircle));
                                ((ViewGroup) view).addView(iv2);
                            }
                            else if (x>=0 && x<=170 && y>=290 && y<=420 && InputManager.mScoutId <=6 || x>=1530 && x<=1700 && y>=490 && y<=630 && InputManager.mScoutId <=6 || x>=0 && x<=120 && y>=210 && y<=290 && InputManager.mScoutId>6 || x>=1000 && x<=1110 && y>=330 && y<=410 && InputManager.mScoutId>6){
                                Log.d("locationOutput","Exchange");
                                //ImageView iv = new ImageView(getApplicationContext());
                                lp.setMargins(x-25, y-40, 0, 0);
                                iv2.setLayoutParams(lp);
                                iv2.setImageDrawable(getResources().getDrawable(
                                        R.drawable.blackcircle));
                                ((ViewGroup) view).addView(iv2);
                            }
//                            else if (x>=790 && x<=940 && y>=700 && y<=870) {
//                                Log.d("locationOutput", "Left Portal");
//                            }
//                            else if (x>=790 && x<=940 && y>=700 && y<=870) {
//                                Log.d("locationOutput", "Right Portal");
//                            }
                        }
//
                    }
                    //purpose: to locate which quadrant
                }
                return false;
            }
        });
    }
}
