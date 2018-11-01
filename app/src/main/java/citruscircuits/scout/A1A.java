package citruscircuits.scout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout.utils.AutoDialog;
import citruscircuits.scout.utils.TimerUtil;

import static java.lang.String.valueOf;

//Written by the Daemon himself ~ Calvin
//testing
public class A1A extends DialogMaker implements View.OnClickListener{

    public ImageView iv_field;

    public String field_orientation;

    public boolean incapChecked = false;
    public boolean startTimer = true;
    public boolean noShape = false;
    public boolean tele = false;
    public boolean field = true;
    public boolean startedWCube = false;
    public boolean liftSelfAttempt;
    public boolean liftSelfActual;
    public boolean climbInputted = false;

    public String currentShape = "";

    public Integer numRobotsAttemptedToLift = 0;
    public Integer numRobotsDidLift = 0;

    public Float ftbStartTime;
    public Float ftbEndTime;

    public List<String> climbAttemptKeys = Arrays.asList("liftSelf", "otherRobotsLifted");
    public List<String> climbActualKeys = Arrays.asList("liftSelf", "otherRobotsLifted");
    public List<String> climbKeys = Arrays.asList("Attempt", "Actual", "startTime", "endTime");

    public List<Object> climbAttemptValues = new ArrayList<>();
    public List<Object> climbActualValues = new ArrayList<>();
    public List<Object> climbValues = new ArrayList<>();

    HashMap<String,Object> ftbAttemptData = new HashMap<String, Object>();
    HashMap<String,Object> ftbActualData = new HashMap<String, Object>();
    HashMap<String,Object> ftbFinalData = new HashMap<String, Object>();

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
    public List<Object> actionList;
    public Map<Integer,List<Object>> actionDic;
    public int actionCount;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        InputManager.initMatchKey();

        if(AppCc.getSp("mapOrientation",99) != 99){
            if(AppCc.getSp("mapOrientation", 99) == 0){
                setContentView(R.layout.activity_map_rb);

                overallLayout = findViewById(R.id.fieldrb);
                field_orientation="rb";
                if(InputManager.mAllianceColor.equals("red")){
                    field = true;
                }
                else if(InputManager.mAllianceColor.equals("blue")){
                    field = false;
                }
            }else {
                setContentView(R.layout.activity_map_br);
                overallLayout = findViewById(R.id.fieldbr);
                field_orientation="br";
                if(InputManager.mAllianceColor.equals("red")){
                    field = false;
                }
                else if(InputManager.mAllianceColor.equals("blue")){
                    field = true;
                }
            }
        }else{
            AppCc.setSp("mapOrientation", 0);
            setContentView(R.layout.activity_map_rb);

            overallLayout = findViewById(R.id.fieldrb);
            field_orientation="rb";
            if(InputManager.mAllianceColor.equals("red")){
                field = true;
            }
            else if(InputManager.mAllianceColor.equals("blue")){
                field = false;
            }
        }

        iv_field = findViewById(R.id.imageView);

        tv_team = findViewById(R.id.tv_teamNum);

        rg_blue_starting_position = findViewById(R.id.blue_starting_position);
        rg_red_starting_position = findViewById(R.id.red_starting_position);

        rb_blue_right = findViewById(R.id.blue_starting_position_right);
        rb_blue_center = findViewById(R.id.blue_starting_position_center);
        rb_blue_left = findViewById(R.id.blue_starting_position_left);
        rb_red_right = findViewById(R.id.red_starting_position_right);
        rb_red_center = findViewById(R.id.red_starting_position_center);
        rb_red_left = findViewById(R.id.red_starting_position_left);

        btn_drop = findViewById(R.id.btn_dropped);
        btn_spill = findViewById(R.id.btn_spilled);
        btn_foul = findViewById(R.id.btn_fouled);
        btn_undo = findViewById(R.id.btn_undo);
        btn_edit = findViewById(R.id.btn_edit_data);
        btn_ftb = findViewById(R.id.btn_ftb);
        btn_arrow = findViewById(R.id.btn_arrow);

        tb_auto_run = findViewById(R.id.tgbtn_auto_run);
        tb_start_cube = findViewById(R.id.tgbtn_start_with_cube);
        tb_incap = findViewById(R.id.tgbtn_incap);

        iv = new ImageView(getApplicationContext());
        iv2 = new ImageView(getApplicationContext());
        actionCount=0;
        actionDic=new HashMap<Integer, List<Object>>();
        actionList=new ArrayList<Object>();

        TimerUtil.mTimerView = findViewById(R.id.tv_timer);
        TimerUtil.mActivityView = findViewById(R.id.tv_activity);

        Fragment fragment = new AutoDialog();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if(InputManager.mAllianceColor.equals("red")) {
            transaction.add(R.id.red_auto, fragment, "FRAGMENT");
            for (int i = 0; i < rg_blue_starting_position.getChildCount(); i++) {
                rg_blue_starting_position.getChildAt(i).setEnabled(false);
            }
        }
        else if(InputManager.mAllianceColor.equals("blue")) {
            transaction.add(R.id.blue_auto, fragment, "FRAGMENT");
            for (int i = 0; i < rg_blue_starting_position.getChildCount(); i++) {
                rg_red_starting_position.getChildAt(i).setEnabled(false);
            }
        }

        transaction.commit();

        tv_team.setText(valueOf(InputManager.mTeamNum));

        if(TimerUtil.matchTimer != null) {
            TimerUtil.matchTimer.cancel();
            TimerUtil.matchTimer = null;
            TimerUtil.timestamp = 0f;
            TimerUtil.mTimerView.setText("15");
            TimerUtil.mActivityView.setText("AUTO");
            startTimer = true;
        }

        addTouchListener();
    }

    @Override
    public void onClick(View v){

    }

    public void onClickTeleop(View view) {
        if(!startTimer && (rb_blue_right.isChecked() || rb_red_right.isChecked() || rb_blue_center.isChecked() || rb_red_center.isChecked() || rb_blue_left.isChecked() || rb_red_left.isChecked())) {
            tele = true;
            for (int i = 0; i < rg_blue_starting_position.getChildCount(); i++) {
                rg_blue_starting_position.getChildAt(i).setEnabled(false);
                rg_red_starting_position.getChildAt(i).setEnabled(false);
            }
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAGMENT");
            if (fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        InputManager.mRealTimeMatchData = new JSONArray();
        try {
            InputManager.mRealTimeMatchData.put(new JSONObject().put("allianceColor", InputManager.mAllianceColor));
            InputManager.mRealTimeMatchData.put(new JSONObject().put("startingPosition", InputManager.mStartingPosition));
            InputManager.mRealTimeMatchData.put(new JSONObject().put("startedWCube", startedWCube));
            InputManager.mRealTimeMatchData.put(new JSONObject().put("autoLineCrossed", InputManager.autoLineCrossed));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            if(startedWCube) {
                currentShape = "triangle";
            }
            else if(!startedWCube) {
                currentShape = "circle";
            }
            if(InputManager.mAllianceColor.equals("red")) {
                btn_startTimer.setBackgroundResource(R.drawable.auto_reset_red_selector);
            }
            else if(InputManager.mAllianceColor.equals("blue")) {
                btn_startTimer.setBackgroundResource(R.drawable.auto_reset_blue_selector);
            }
        }
        else if(!startTimer) {
            InputManager.numSpill = 0;
            InputManager.numFoul = 0;
            tb_start_cube = findViewById(R.id.tgbtn_start_with_cube);
            tb_start_cube.setEnabled(true);
            tb_start_cube.setChecked(false);
            handler.removeCallbacks(runnable);
            handler.removeCallbacksAndMessages(null);
            TimerUtil.matchTimer.cancel();
            TimerUtil.matchTimer = null;
            TimerUtil.timestamp = 0f;
            TimerUtil.mTimerView.setText("15");
            TimerUtil.mActivityView.setText("AUTO");
            btn_startTimer.setText("START TIMER");
            if(currentShape.equals("triangle")) {
                overallLayout.removeView(iv);
            }
            else if(currentShape.equals("circle")) {
                overallLayout.removeView(iv2);
            }
            startTimer = true;
            currentShape = "none";
            startedWCube = false;
            if(InputManager.mAllianceColor.equals("red")) {
                btn_startTimer.setBackgroundResource(R.drawable.auto_red_selector);
            }
            else if(InputManager.mAllianceColor.equals("blue")) {
                btn_startTimer.setBackgroundResource(R.drawable.auto_blue_selector);
            }
            if(field_orientation.equals("rb")){
                iv_field.setImageResource(R.drawable.field_rb);
            }
            else if(field_orientation.equals("br")){
                iv_field.setImageResource(R.drawable.field_br);
            }
        }
    }

    public void onClickAutoLineCrossed (View v) {
        if(!InputManager.autoLineCrossed){
            InputManager.autoLineCrossed = true;
        }
        else if(InputManager.autoLineCrossed){
            InputManager.autoLineCrossed = false;
        }
    }

    public void onClickBeginWithCube (View v) {
        if(!startedWCube) {
            currentShape = "triangle";
            startedWCube=true;
            if(field_orientation.equals("rb")){
                iv_field.setImageResource(R.drawable.field_yellow_rb);
            }
            else if(field_orientation.equals("br")){
                iv_field.setImageResource(R.drawable.field_yellow_br);
            }
        }
        else if(startedWCube) {
            currentShape = "none";
            startedWCube=false;
            if(field_orientation.equals("rb")){
                iv_field.setImageResource(R.drawable.field_rb);
            }
            else if(field_orientation.equals("br")){
                iv_field.setImageResource(R.drawable.field_br);
            }
        }
    }

    public void onClickDrop (View v) throws JSONException {
        InputManager.mRealTimeMatchData.put("drop",TimerUtil.timestamp);
        shapeCheck = false;
        overallLayout.removeView(iv);
        actionList.clear();
        actionList.add("drop");
        actionList.add("x not matter");
        actionList.add("y not matter");
        actionList.add(TimerUtil.timestamp);
        actionDic.put(actionCount, actionList);
        actionCount++;
        if(field_orientation.equals("rb")){
            iv_field.setImageResource(R.drawable.field_rb);
        }
        else if(field_orientation.equals("br")){
            iv_field.setImageResource(R.drawable.field_br);

        if(currentShape.equals("triangle") && !startTimer && !incapChecked) {
            InputManager.mRealTimeMatchData.put(new JSONObject().put("drop", Float.valueOf(String.format("%.2f", TimerUtil.timestamp))));
            currentShape = "none";
            overallLayout.removeView(iv);
            if(field_orientation.equals("rb")){
                iv_field.setImageResource(R.drawable.field_rb);
            }
            else if(field_orientation.equals("br")){
                iv_field.setImageResource(R.drawable.field_br);
            }
        }
    }

    public void onClickSpill (View v) throws JSONException {
        if(!startTimer && !incapChecked) {
            InputManager.mRealTimeMatchData.put(new JSONObject().put("spill", Float.valueOf(String.format("%.2f", TimerUtil.timestamp))));
            InputManager.numSpill++;
            btn_spill.setText("SPILL : " + InputManager.numSpill);
        }
    }

    public void onClickFoul (View v) throws JSONException {
        if(!startTimer && !incapChecked) {
            InputManager.mRealTimeMatchData.put(new JSONObject().put("scaleFoul", Float.valueOf(String.format("%.2f", TimerUtil.timestamp))));
            InputManager.numFoul++;
            btn_foul.setText("FOUL : " + InputManager.numFoul);
        }
    }
    public void triangleParams(int x, int y ){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                50,
                50);
        lp.setMargins(x-25, y-40, 0, 0);
        iv.setLayoutParams(lp);
        iv.setImageDrawable(getResources().getDrawable(R.drawable.triangle_image));
    }

    public void circleParams(int x, int y){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                50,
                50);
        lp.setMargins(x-25, y-40, 0, 0);
        iv2.setLayoutParams(lp);
        iv2.setImageDrawable(getResources().getDrawable(
                R.drawable.blackcircle));
    }

    public void onClickUndo (View v) {
        Log.e("jkgg", valueOf(actionCount));
        if (actionCount>0) {
            actionCount = actionCount - 1;
            Log.e("actiondic?!", actionDic.toString());
            actionDic.remove(actionCount + 1);
            Log.e("wok",actionDic.get(actionCount).get(3).toString());
            // Log.e("PLZ",actionDic.get(actionCount).get(1).toString() );
            if (actionDic.get(actionCount).get(3).equals("triangle")) {
                Log.e("Why does this work", "WHYYYY");
                overallLayout.removeView(iv);
                shapeCheck=false;
                noShape=true;
                if(field_orientation.equals("rb")){
                    iv_field.setImageResource(R.drawable.field_rb);
                }
                else if(field_orientation.equals("br")){
                    iv_field.setImageResource(R.drawable.field_br);
                }
                triangleParams(Integer.valueOf(actionDic.get(actionCount).get(1).toString()),Integer.valueOf(actionDic.get(actionCount).get(2).toString()));
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                        50,
//                        50);
//                lp.setMargins(Integer.valueOf(actionDic.get(actionCount).get(1).toString()) - 25, Integer.valueOf(actionDic.get(actionCount).get(2).toString()) - 40, 0, 0);
//                iv.setLayoutParams(lp);
//                iv.setImageDrawable(getResources().getDrawable(R.drawable.triangle_image));
//                overallLayout.addView(iv);
//                ((ViewGroup) v).addView(iv2);


            } else if (actionDic.get(actionCount).get(3).equals("circle")) {
                shapeCheck=true;
                Log.e("Hello", "Work");
                overallLayout.removeView(iv2);
                if(field_orientation.equals("rb")){
                    iv_field.setImageResource(R.drawable.field_yellow_rb);
                }
                else if(field_orientation.equals("br")){
                    iv_field.setImageResource(R.drawable.field_yellow_br);
                }
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                        50,
//                        50);
//                lp.setMargins(Integer.valueOf(actionDic.get(actionCount).get(1).toString()) - 25, Integer.valueOf(actionDic.get(actionCount).get(2).toString()) - 40, 0, 0);
//                iv2.setLayoutParams(lp);
//                iv2.setImageDrawable(getResources().getDrawable(R.drawable.blackcircle));
                circleParams(Integer.valueOf(actionDic.get(actionCount).get(1).toString()),Integer.valueOf(actionDic.get(actionCount).get(2).toString()));
//               ((ViewGroup) v).addView(iv2);
//                overallLayout.addView(iv2);
            } else if (actionDic.get(actionCount).get(0).equals("drop")){
                shapeCheck=true;
                if(field_orientation.equals("rb")){
                    iv_field.setImageResource(R.drawable.field_yellow_rb);
                }
                else if(field_orientation.equals("br")){
                    iv_field.setImageResource(R.drawable.field_yellow_br);
                }


            }
        }
    }

    public void onClickEdit (View v) {
        if (actionCount>0){

        }
    }

    public void onClickFTB (View v) {

        if(tele && !startTimer && !incapChecked && !climbInputted) {
            ftbStartTime = Float.valueOf(String.format("%.2f", TimerUtil.timestamp));
            final Dialog ftbDialog = new Dialog(this);
            ftbDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            final RelativeLayout ftbDialogLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.ftb_dialog, null);

            final AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);

            final AlertDialog.Builder builder2;
            builder2 = new AlertDialog.Builder(this);

            builder.setMessage("Did the scouted robot lift itself or another robot with its own mechanism?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ftbEndTime = Float.valueOf(String.format("%.2f", TimerUtil.timestamp));

                            ftbDialog.setContentView(ftbDialogLayout);

                            final TextView tv_attempted = ftbDialogLayout.findViewById(R.id.tv_value_attempted);
                            final TextView tv_didLift = ftbDialogLayout.findViewById(R.id.tv_didLift_value);

                            Button btn_attemptedPlus = ftbDialogLayout.findViewById(R.id.btn_plus_attempted);
                            Button btn_attemptedMinus = ftbDialogLayout.findViewById(R.id.btn_minus_attempted);
                            Button btn_plus = ftbDialogLayout.findViewById(R.id.btn_didLift_plus);
                            Button btn_minus = ftbDialogLayout.findViewById(R.id.btn_didLift_minus);
                            Button btn_cancel = ftbDialogLayout.findViewById(R.id.cancelButton);
                            Button btn_done = ftbDialogLayout.findViewById(R.id.doneButton);

                            btn_attemptedPlus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(numRobotsAttemptedToLift < 2) {
                                        numRobotsAttemptedToLift ++;
                                        tv_attempted.setText(String.valueOf(numRobotsAttemptedToLift));
                                    }
                                }
                            });

                            btn_attemptedMinus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(numRobotsAttemptedToLift > 0) {
                                        numRobotsAttemptedToLift --;
                                        tv_attempted.setText(String.valueOf(numRobotsAttemptedToLift));
                                    }
                                }
                            });

                            btn_plus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(numRobotsDidLift < 2) {
                                        numRobotsDidLift ++;
                                        tv_didLift.setText(String.valueOf(numRobotsDidLift));
                                    }
                                }
                            });

                            btn_minus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(numRobotsDidLift > 0) {
                                        numRobotsDidLift --;
                                        tv_didLift.setText(String.valueOf(numRobotsDidLift));
                                    }
                                }
                            });

                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ftbDialog.cancel();
                                }
                            });

                            btn_done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    climbAttemptValues.add(liftSelfAttempt);
                                    climbAttemptValues.add(numRobotsAttemptedToLift);

                                    climbActualValues.add(liftSelfActual);
                                    climbActualValues.add(numRobotsDidLift);

                                    ftbAttemptData.put(climbAttemptKeys.get(0), climbAttemptValues.get(0));
                                    ftbAttemptData.put(climbAttemptKeys.get(1), climbAttemptValues.get(1));

                                    ftbActualData.put(climbActualKeys.get(0), climbActualValues.get(0));
                                    ftbActualData.put(climbActualKeys.get(1), climbActualValues.get(1));

                                    climbValues.add(ftbAttemptData);
                                    climbValues.add(ftbActualData);
                                    climbValues.add(ftbStartTime);
                                    climbValues.add(ftbEndTime);

                                    ftbFinalData.put(climbKeys.get(0), climbValues.get(0));
                                    ftbFinalData.put(climbKeys.get(1), climbValues.get(1));
                                    ftbFinalData.put(climbKeys.get(2), climbValues.get(2));
                                    ftbFinalData.put(climbKeys.get(3), climbValues.get(3));

                                    try {
                                        InputManager.mRealTimeMatchData.put(new JSONObject().put("climb", ftbFinalData));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    climbInputted = true;

                                    Log.i("FTB", ftbFinalData.toString());

                                    ftbDialog.dismiss();
                                }
                            });


                            builder2.setMessage("Did the scouted robot climb/lift itself?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            liftSelfAttempt = true;
                                            liftSelfActual = true;

                                            ftbDialog.show();
                                        }
                                    })
                                    .setNeutralButton("ATTEMPTED TO, BUT FAILED", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            liftSelfAttempt = true;
                                            liftSelfActual = false;

                                            ftbDialog.show();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            liftSelfAttempt = false;
                                            liftSelfActual = false;

                                            ftbDialog.show();
                                        }
                                    })
                                    .show();
                                }
                            })

                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ftbEndTime = Float.valueOf(String.format("%.2f", TimerUtil.timestamp));

                            climbAttemptValues.add(false);
                            climbAttemptValues.add(0);

                            climbActualValues.add(false);
                            climbActualValues.add(0);

                            ftbAttemptData.put(climbAttemptKeys.get(0), climbAttemptValues.get(0));
                            ftbAttemptData.put(climbAttemptKeys.get(1), climbAttemptValues.get(1));

                            ftbActualData.put(climbActualKeys.get(0), climbActualValues.get(0));
                            ftbActualData.put(climbActualKeys.get(1), climbActualValues.get(1));

                            climbValues.add(ftbAttemptData);
                            climbValues.add(ftbActualData);
                            climbValues.add(ftbStartTime);
                            climbValues.add(ftbEndTime);

                            ftbFinalData.put(climbKeys.get(0), climbValues.get(0));
                            ftbFinalData.put(climbKeys.get(1), climbValues.get(1));
                            ftbFinalData.put(climbKeys.get(2), climbValues.get(2));
                            ftbFinalData.put(climbKeys.get(3), climbValues.get(3));

                            Log.i("FTB", ftbFinalData.toString());

                            try {
                                InputManager.mRealTimeMatchData.put(new JSONObject().put("climb", ftbFinalData));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .show();
        }
    }

    public void onClickIncap (View v) throws JSONException {
        if(!incapChecked) {
            tb_incap.setChecked(true);
            InputManager.mRealTimeMatchData.put(new JSONObject().put("beganIncap", Float.valueOf(String.format("%.2f", TimerUtil.timestamp))));
            incapChecked = true;
        }
        else if(incapChecked) {
            tb_incap.setChecked(false);
            InputManager.mRealTimeMatchData.put(new JSONObject().put("endIncap", Float.valueOf(String.format("%.2f", TimerUtil.timestamp))));
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
    private void addTouchListener() {
        overallLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !startTimer && !incapChecked) {
                    int x = (int) motionEvent.getX();
                    int y = (int) motionEvent.getY();
                    if (x<=1700 && y<=930){
                        if (!shapeCheck){
                            overallLayout.removeView(iv2);
//                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                                    50,
//                                    50);
//                            //ImageView iv= new ImageView(getApplicationContext());
//                            lp.setMargins(x-25, y-40, 0, 0);
//                            iv.setLayoutParams(lp);
//                            iv.setImageDrawable(getResources().getDrawable(R.drawable.triangle_image));
                            triangleParams(x,y);
                            Log.d("TELE", valueOf(tele));
                            Log.d("FIELD", valueOf(field));
                            if (((x<=425 && y<=930 && InputManager.mScoutId <=6) || (x>75 && x<=285 && y<=575 && InputManager.mScoutId >6)) && (tele || (field && !tele))){
                                Log.d("locationInput","1");
                                actionList.clear();
                                actionList.add("loc_1");
                                actionList.add(x);
                                actionList.add(y);
                                actionList.add("triangle");
                                actionDic.put(actionCount, actionList);
                                actionCount++;
                                shapeCheck=true;
                                if(field_orientation.equals("rb")){
                                    iv_field.setImageResource(R.drawable.field_yellow_rb);
                                    try {
                                        InputManager.mRealTimeMatchData.put("intake4",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(field_orientation.equals("br")){
                                    iv_field.setImageResource(R.drawable.field_yellow_br);
                                    try {
                                        InputManager.mRealTimeMatchData.put("intake1",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ((ViewGroup) view).addView(iv);
                            }
                            else if (((x>425 && x<=850 && y<=930 && InputManager.mScoutId <=6 || x>285 && x<=575 && y<=575 && InputManager.mScoutId >6)) && (tele || (field && !tele))){
                                Log.d("locationInput","2");
                                actionList.clear();
                                actionList.add("loc_2");
                                actionList.add(x);
                                actionList.add(y);
                                actionList.add("triangle");
                                actionDic.put(actionCount, actionList);
                                actionCount++;
                                shapeCheck=true;
                                if(field_orientation.equals("rb")){
                                    iv_field.setImageResource(R.drawable.field_yellow_rb);
                                    try {
                                        InputManager.mRealTimeMatchData.put("intake3",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(field_orientation.equals("br")){
                                    iv_field.setImageResource(R.drawable.field_yellow_br);
                                    try {
                                        InputManager.mRealTimeMatchData.put("intake2",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ((ViewGroup) view).addView(iv);
                            }
                            else if (((x>850 && x<=1275 && y<=930 && InputManager.mScoutId <=6 || x>575 && x<=865 && y<=575 && InputManager.mScoutId >6)) && (tele || (!field && !tele))){
                                Log.d("locationInput","3");

                                actionList.clear();
                                actionList.add("loc_3");
                                actionList.add(x);
                                actionList.add(y);
                                actionList.add("triangle");
                                actionDic.put(actionCount, actionList);
                                actionCount++;
                                shapeCheck=true;
                                if(field_orientation.equals("rb")){
                                    iv_field.setImageResource(R.drawable.field_yellow_rb);
                                    try {
                                        InputManager.mRealTimeMatchData.put("intake2",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(field_orientation.equals("br")){
                                    iv_field.setImageResource(R.drawable.field_yellow_br);
                                    try {
                                        InputManager.mRealTimeMatchData.put("intake3",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ((ViewGroup) view).addView(iv);
                            }
                            else if (((x>1275 && x<=1700 && y<=930 && InputManager.mScoutId <=6 || x>865 && x<=1075 && y<=575 && InputManager.mScoutId >6)) && (tele || (!field && !tele))){
                                Log.d("locationInput","4");
                                shapeCheck=true;
                                actionList.clear();
                                actionList.add("loc_4");
                                actionList.add(x);
                                actionList.add(y);
                                actionList.add("triangle");
                                actionDic.put(actionCount, actionList);
                                actionCount++;
                                if(field_orientation.equals("rb")){
                                    iv_field.setImageResource(R.drawable.field_yellow_rb);
                                    try {
                                        InputManager.mRealTimeMatchData.put("intake1",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(field_orientation.equals("br")){
                                    iv_field.setImageResource(R.drawable.field_yellow_br);
                                    try {
                                        InputManager.mRealTimeMatchData.put("intake4",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ((ViewGroup) view).addView(iv);
                            }
                        }
                        else if (shapeCheck){
                            overallLayout.removeView(iv);
//                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                                    50,
//                                    50);
//                            lp.setMargins(x-25, y-40, 0, 0);
//                            iv2.setLayoutParams(lp);
//                            iv2.setImageDrawable(getResources().getDrawable(
//                                    R.drawable.blackcircle));
                            circleParams(x,y);
                            if ((x>=325 && x<=490 && y>=140 && y<=330 && InputManager.mScoutId <=6 || x>=230 && x<=310 && y>=90 && y<=210 && InputManager.mScoutId >6) && (tele || (field && !tele))){
                                Log.d("locationOutput","Top Left switch");
//                                overallLayout.removeView(iv);
                                shapeCheck=false;
                                noShape=true;
                                actionList.clear();
                                actionList.add("Top Left Switch");
                                actionList.add(x);
                                actionList.add(y);
                                actionList.add("circle");
                                actionDic.put(actionCount, actionList);
                                actionCount++;
                                if(field_orientation.equals("rb")){
                                    iv_field.setImageResource(R.drawable.field_rb);
                                    try {
                                        InputManager.mRealTimeMatchData.put("score4",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(field_orientation.equals("br")){
                                    iv_field.setImageResource(R.drawable.field_br);
                                    try {
                                        InputManager.mRealTimeMatchData.put("score1",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ((ViewGroup) view).addView(iv2);
                            }
                            return true;
                        }
                        else if ((x>=325 && x<=595 && y>=140 && y<=800 && InputManager.mScoutId <=6
                                || x>=230 && x<=310 && y>=400 && y<=530 && InputManager.mScoutId >6)
                                && (tele || (field && !tele))){
                            if(currentShape.equals("none") || currentShape.equals("triangle")){
                                Log.d("locationOutput","Bottom Left switch");
//                                overallLayout.removeView(iv);
                                shapeCheck=false;
                                noShape=true;
                                actionList.clear();
                                actionList.add("Bottom Left Switch");
                                actionList.add(x);
                                actionList.add(y);
                                actionList.add("circle");
                                actionDic.put(actionCount, actionList);
                                actionCount++;
                                if(field_orientation.equals("rb")){
                                    iv_field.setImageResource(R.drawable.field_rb);
                                    try {
                                        InputManager.mRealTimeMatchData.put("intake3",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(field_orientation.equals("br")){
                                    iv_field.setImageResource(R.drawable.field_br);
                                    try {
                                        InputManager.mRealTimeMatchData.put("intake6",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ((ViewGroup) view).addView(iv2);
                            }
                            return true;
                        }
                        else if ((x>=1250 && x<=1400 && y>=140 && y<=330 && InputManager.mScoutId <=6
                                || x>=780 && x<=900 && y>=100 && y<=210 && InputManager.mScoutId >6)
                                && (tele || (!field && !tele))){
                            if(currentShape.equals("none") || currentShape.equals("triangle")){
                                Log.d("locationOutput","Top Right switch");
//                                overallLayout.removeView(iv);
                                shapeCheck=false;
                                noShape=true;
                                actionList.clear();
                                actionList.add("Top Right Switch");
                                actionList.add(x);
                                actionList.add(y);
                                actionList.add("circle");
                                actionDic.put(actionCount, actionList);
                                actionCount++;
                                if(field_orientation.equals("rb")){
                                    iv_field.setImageResource(R.drawable.field_rb);
                                    try {
                                        InputManager.mRealTimeMatchData.put("score6",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(field_orientation.equals("br")){
                                    iv_field.setImageResource(R.drawable.field_br);
                                    try {
                                        InputManager.mRealTimeMatchData.put("score3",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ((ViewGroup) view).addView(iv2);
                            }
                            return true;
                        }
                        else if ((x>=1250 && x<=1400 && y>=600 && y<=800 && InputManager.mScoutId <=6
                                || x>=780 && x<=900 && y>=400 && y<=530 && InputManager.mScoutId >6)
                                && (tele || (!field && !tele))){
                            if(currentShape.equals("none") || currentShape.equals("triangle")){
                                Log.d("locationOutput","Bottom Right switch");
//                                overallLayout.removeView(iv);
                                shapeCheck=false;
                                noShape=true;
                                actionList.clear();

                                actionList.add("Bottom Right Switch");
                                actionList.add(x);
                                actionList.add(y);
                                actionList.add("circle");
                                actionDic.put(actionCount, actionList);
                                actionCount++;
                                if(field_orientation.equals("rb")){
                                    iv_field.setImageResource(R.drawable.field_rb);
                                    try {
                                        InputManager.mRealTimeMatchData.put("score1",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(field_orientation.equals("br")){
                                    iv_field.setImageResource(R.drawable.field_br);
                                    try {
                                        InputManager.mRealTimeMatchData.put("score4",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ((ViewGroup) view).addView(iv2);
                            }
                            return true;
                        }
                        else if (x>=800 && x<=940 && y>=60 && y<=240 && InputManager.mScoutId <=6
                                || x>=530 && x<=620 && y>=60 && y<=165 && InputManager.mScoutId >6){
                            if(currentShape.equals("none") || currentShape.equals("triangle")){
                                Log.d("locationOutput","Scale Top");
//                                overallLayout.removeView(iv);
                                shapeCheck=false;
                                noShape=true;
                                actionList.clear();
                                actionList.add("Scale Top");
                                actionList.add(x);
                                actionList.add(y);
                                actionList.add("circle");
                                actionDic.put(actionCount, actionList);
                                actionCount++;
                                if(field_orientation.equals("rb")){
                                    iv_field.setImageResource(R.drawable.field_rb);
                                    try {
                                        InputManager.mRealTimeMatchData.put("score5",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(field_orientation.equals("br")){
                                    iv_field.setImageResource(R.drawable.field_br);
                                    try {
                                        InputManager.mRealTimeMatchData.put("score2",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ((ViewGroup) view).addView(iv2);
                            }
                            return true;
                        }
                        else if (x>=790 && x<=940 && y>=700 && y<=870 && InputManager.mScoutId <=6
                                || x>=530 && x<=620 && y>=450 && y<=560 && InputManager.mScoutId >6){
                            if(currentShape.equals("none") || currentShape.equals("triangle")){
                                Log.d("locationOutput","Scale Bottom");
//                                overallLayout.removeView(iv);
                                shapeCheck=false;
                                noShape=true;
                                actionList.clear();
                                actionList.add("Scale Bottom");
                                actionList.add(x);
                                actionList.add(y);
                                actionList.add("circle");
                                actionDic.put(actionCount, actionList);
                                actionCount++;
                                if(field_orientation.equals("rb")){
                                    iv_field.setImageResource(R.drawable.field_rb);
                                    try {
                                        InputManager.mRealTimeMatchData.put("score2",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(field_orientation.equals("br")){
                                    iv_field.setImageResource(R.drawable.field_br);
                                    try {
                                        InputManager.mRealTimeMatchData.put("score5",TimerUtil.timestamp);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ((ViewGroup) view).addView(iv2);
                            }
                            else if ((x>=0 && x<=170 && y>=290 && y<=420 && InputManager.mScoutId <=6 || x>=1530 && x<=1700 && y>=490 && y<=630 && InputManager.mScoutId <=6 || x>=0 && x<=120 && y>=210 && y<=290 && InputManager.mScoutId>6 || x>=1000 && x<=1110 && y>=330 && y<=410 && InputManager.mScoutId>6) && tele){
                                Log.d("locationOutput","Exchange");
//                                overallLayout.removeView(iv);
                                shapeCheck=false;
                                noShape=true;
                                actionList.clear();
                                actionList.add("Exchange");
                                actionList.add(x);
                                actionList.add(y);
                                actionList.add("circle");
                                actionDic.put(actionCount, actionList);
                                actionCount++;
                                try {
                                    InputManager.mRealTimeMatchData.put("score2",TimerUtil.timestamp);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(field_orientation.equals("rb")){
                                    InputManager.mRealTimeMatchData.put(new JSONObject().put("intake2", Float.valueOf(String.format("%.2f", TimerUtil.timestamp))));
                                }else if(field_orientation.equals("br")){
                                    InputManager.mRealTimeMatchData.put(new JSONObject().put("intake3", Float.valueOf(String.format("%.2f", TimerUtil.timestamp))));
                                }else if(field_orientation.equals("br")){
                                    iv_field.setImageResource(R.drawable.field_br);
                                }
                            }catch(JSONException je){   je.printStackTrace(); }
                        }
                    }
                    //purpose: to locate which quadrant
                }
                return false;
            }
        });
    }

    public void initCircle(View view){
        if(!tele) {
            tb_start_cube = findViewById(R.id.tgbtn_start_with_cube);
            tb_start_cube.setEnabled(false);
        }
        overallLayout.removeView(iv);
        currentShape = "circle";
        noShape=true;
        if(field_orientation.equals("rb")){
            iv_field.setImageResource(R.drawable.field_rb);
        }
        else if(field_orientation.equals("br")){
            iv_field.setImageResource(R.drawable.field_br);
        }
        ((ViewGroup) view).addView(iv2);
    }

    public void initTriangle(View view){
        if(!tele) {
            tb_start_cube = findViewById(R.id.tgbtn_start_with_cube);
            tb_start_cube.setEnabled(false);
        }
        currentShape = "triangle";
        if(field_orientation.equals("rb")){
            iv_field.setImageResource(R.drawable.field_yellow_rb);
        }
        else if(field_orientation.equals("br")){
            iv_field.setImageResource(R.drawable.field_yellow_br);
        }
        ((ViewGroup) view).addView(iv);
    }
}
