package citruscircuits.scout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;

import java.util.Arrays;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout.utils.AutoDialog;
import citruscircuits.scout.utils.TimerUtil;

import static citruscircuits.scout.Managers.InputManager.mRealTimeMatchData;
import static java.lang.String.valueOf;

//Written by the Daemon himself ~ Calvin
//testing
public class A1A extends DialogMaker implements View.OnClickListener {

    public ImageView iv_field;

    public String field_orientation;

    public boolean incapChecked = false;
    public boolean startTimer = true;
    public boolean noShape = false;
    public boolean tele = false;
    public boolean startedWCube = false;
    public boolean liftSelfAttempt;
    public boolean liftSelfActual;
    public boolean climbInputted = false;
    public boolean shapeCheck = false;
    public boolean isMapLeft=false;

    public Integer numRobotsAttemptedToLift = 0;
    public Integer numRobotsDidLift = 0;
    public Integer climbAttemptCounter=0;
    public Integer climbActualCounter=0;

    public Float ftbStartTime;
    public Float ftbEndTime;

    public List<String> climbAttemptKeys = Arrays.asList("self", "robot1", "robot2");
    public List<String> climbActualKeys = Arrays.asList("self", "robot1", "robot2");
    public List<String> climbKeys = Arrays.asList("type", "time", "attempted", "actual");

    public List<Object> climbAttemptValues = new ArrayList<>();
    public List<Object> climbActualValues = new ArrayList<>();
    public List<Object> climbValues = new ArrayList<>();

    JSONObject climbAttemptData = new JSONObject();
    JSONObject climbActualData = new JSONObject();
    JSONObject climbFinalData = new JSONObject();

    public TextView tv_team;
    public TextView tv_starting_position_warning;

    public Button btn_startTimer;
    public Button btn_drop;
    public Button btn_spill;
    public Button btn_foul;
    public Button btn_undo;
    public Button btn_edit;
    public Button btn_ftb;
    public Button btn_arrow;

    public Button zeroI;
    public Button oneI;
    public Button twoAI;
    public Button threeI;
    public Button twoBI;

    public Button zeroII;
    public Button oneII;
    public Button twoAII;
    public Button threeII;
    public Button twoBII;
    public Button spaceOneI;
    public Button spaceTwoI;
    public Button spaceThreeI;
    public Button spaceOneII;
    public Button spaceTwoII;
    public Button spaceThreeII;


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
    public RelativeLayout dialogLayout;

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
    public Map<Integer, List<Object>> actionDic;
    public int actionCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //InputManager.initMatchKey();

        if (AppCc.getSp("mapOrientation", 99) != 99) {
            if (AppCc.getSp("mapOrientation", 99) == 0) {
                mapOrientation(R.layout.activity_map_rb, R.id.fieldrb, "rb", true, false);
            } else {
                mapOrientation(R.layout.activity_map_br, R.id.fieldbr, "br", false, true);
            }
        } else {
            mapOrientation(R.layout.activity_map_rb, R.id.fieldrb, "rb", true, false);
        }
        //public void mapOrientation(int map, int fieldOrientation, String field_orientation_given, Boolean field1, Boolean field2) {
        //        AppCc.setSp("mapOrientation", 0);
        //        setContentView(map);
        //        field_orientation = field_orientation_given;
        //        overallLayout = findViewById(fieldOrientation);
        //        if (InputManager.mAllianceColor.equals("red")) {
        //            isMapLeft = field1;
        //        } else if (InputManager.mAllianceColor.equals("blue")) {
        //            isMapLeft = field2;
        //        }
        //    }

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

        tb_incap = findViewById(R.id.tgbtn_incap);

        iv = new ImageView(getApplicationContext());
        iv2 = new ImageView(getApplicationContext());
        actionCount = 0;
        actionDic = new HashMap<Integer, List<Object>>();
        actionList = new ArrayList<Object>();

        TimerUtil.mTimerView = findViewById(R.id.tv_timer);
        TimerUtil.mActivityView = findViewById(R.id.tv_activity);

        Fragment fragment = new AutoDialog();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (InputManager.mAllianceColor.equals("red")) {
            transaction.add(R.id.red_auto, fragment, "FRAGMENT");
            for (int i = 0; i < rg_blue_starting_position.getChildCount(); i++) {
                rg_blue_starting_position.getChildAt(i).setEnabled(false);
            }
        } else if (InputManager.mAllianceColor.equals("blue")) {
            transaction.add(R.id.blue_auto, fragment, "FRAGMENT");
            for (int i = 0; i < rg_red_starting_position.getChildCount(); i++) {
                rg_red_starting_position.getChildAt(i).setEnabled(false);
            }
        }

        transaction.commit();

        tv_team.setText(valueOf(InputManager.mTeamNum));

        if (TimerUtil.matchTimer != null) {
            TimerUtil.matchTimer.cancel();
            TimerUtil.matchTimer = null;
            TimerUtil.timestamp = 0f;
            TimerUtil.mTimerView.setText("15");
            TimerUtil.mActivityView.setText("AUTO");
            startTimer = true;
        }

        mRealTimeMatchData = new JSONArray();
        InputManager.mOneTimeMatchData = new JSONObject();
        InputManager.numSpill = 0;
        InputManager.numFoul = 0;

        btn_drop.setEnabled(false);
        btn_undo.setEnabled(false);

        addTouchListener();

        btn_spill.setOnLongClickListener((new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (!startTimer && !incapChecked && InputManager.numSpill>0) {
                    int index = -1;
                    for(int i=0;i<mRealTimeMatchData.length();i++){
                        try {
                            String test = mRealTimeMatchData.getString(i);
                            if(test.contains("spill")) {
                                index = i;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mRealTimeMatchData.remove(index);
                    InputManager.numSpill--;
                    btn_spill.setText("SPILL - " + InputManager.numSpill);
                }
                Log.e("LONG CLICK? SPILL", mRealTimeMatchData.toString());
                return true;
            }
        }));

        btn_foul.setOnLongClickListener((new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (!startTimer && !incapChecked && InputManager.numFoul>0) {
                    int index = -1;
                    for(int i=0;i<mRealTimeMatchData.length();i++){
                        try {
                            String test = mRealTimeMatchData.getString(i);
                            if(test.contains("scaleFoul")) {
                                index = i;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mRealTimeMatchData.remove(index);
                    InputManager.numFoul--;
                    btn_foul.setText("FOUL - " + InputManager.numFoul);
                }
                Log.e("LONG CLICK? FOUL", mRealTimeMatchData.toString());
                return true;
            }
        }));
    }

    @Override
    public void onClick(View v) {

    }

    public void onClickStartingPosition(View view) {
        tv_starting_position_warning = findViewById(R.id.tv_starting_position_warning);
        tv_starting_position_warning.setVisibility(View.INVISIBLE);
    }

    public void onClickTeleop(View view) {
//        actionList.clear();
////        actionList.add("invalid");
////        actionList.add("invalid");
////        actionList.add("invalid");
////        actionList.add("teleop");
////        actionList.add("rb");
////        actionDic.put(actionCount, actionList);
////        actionCount++;
        if (!startTimer) {
            tele = true;
            for (int i = 0; i < rg_blue_starting_position.getChildCount(); i++) {
                rg_blue_starting_position.getChildAt(i).setEnabled(false);
                rg_red_starting_position.getChildAt(i).setEnabled(false);
            }
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAGMENT");
            if (fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        try {
            InputManager.mOneTimeMatchData.put("allianceColor", InputManager.mAllianceColor);
            InputManager.mOneTimeMatchData.put("startingPosition", InputManager.mStartingPosition);
            InputManager.mOneTimeMatchData.put("startedWCube", startedWCube);
            InputManager.mOneTimeMatchData.put("scoutName", InputManager.mScoutName);
            InputManager.mOneTimeMatchData.put("autoLineCrossed", InputManager.autoLineCrossed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClickStartTimer(View v) {
        handler.removeCallbacks(runnable);
        handler.removeCallbacksAndMessages(null);
        TimerUtil.MatchTimerThread timerUtil = new TimerUtil.MatchTimerThread();
        btn_startTimer = findViewById(R.id.btn_timer);
        btn_drop = findViewById(R.id.btn_dropped);
        tb_auto_run = findViewById(R.id.tgbtn_auto_run);
        btn_arrow.setEnabled(false);
        btn_arrow.setVisibility(View.INVISIBLE);
        if (startTimer) {
            handler.postDelayed(runnable, 150000);
            timerUtil.initTimer();
            btn_startTimer.setText("RESET TIMER");
            startTimer = false;
            tb_auto_run.setEnabled(true);
            if (InputManager.mAllianceColor.equals("red")) {
                btn_startTimer.setBackgroundResource(R.drawable.auto_reset_red_selector);
            } else if (InputManager.mAllianceColor.equals("blue")) {
                btn_startTimer.setBackgroundResource(R.drawable.auto_reset_blue_selector);
            }
            if(startedWCube) {
                btn_drop.setEnabled(true);
            }
        } else if (!startTimer) {
            InputManager.numSpill = 0;
            InputManager.numFoul = 0;
            tb_start_cube = findViewById(R.id.tgbtn_start_with_cube);
            tb_start_cube.setEnabled(true);
            tb_start_cube.setChecked(false);
            tb_auto_run.setEnabled(false);
            tb_auto_run.setChecked(false);
            btn_drop.setEnabled(false);
            btn_undo.setEnabled(false);
            handler.removeCallbacks(runnable);
            handler.removeCallbacksAndMessages(null);
            TimerUtil.matchTimer.cancel();
            TimerUtil.matchTimer = null;
            TimerUtil.timestamp = 0f;
            TimerUtil.mTimerView.setText("15");
            TimerUtil.mActivityView.setText("AUTO");
            btn_startTimer.setText("START TIMER");
            if (shapeCheck) {
                overallLayout.removeView(iv);
            } else if (!shapeCheck) {
                overallLayout.removeView(iv2);
            }
            startTimer = true;
            shapeCheck = false;
            startedWCube = false;
            if (InputManager.mAllianceColor.equals("red")) {
                btn_startTimer.setBackgroundResource(R.drawable.auto_red_selector);
            } else if (InputManager.mAllianceColor.equals("blue")) {
                btn_startTimer.setBackgroundResource(R.drawable.auto_blue_selector);
            }
            mapChange();
        }
    }

    public void onClickAutoLineCrossed(View v) {
        if (!InputManager.autoLineCrossed) {
            InputManager.autoLineCrossed = true;
        } else if (InputManager.autoLineCrossed) {
            InputManager.autoLineCrossed = false;
        }
    }

    public void onClickBeginWithCube(View v) {
        if (!startedWCube) {
            shapeCheck = true;
            if(!startTimer) {
                btn_drop.setEnabled(true);
            }
            startedWCube = true;
            mapChange();
        } else if (startedWCube) {
            shapeCheck = false;
            btn_drop.setEnabled(false);
            startedWCube = false;
            mapChange();
        }
    }

    public void onClickDrop(View v) {
        if(!tele) {
            tb_start_cube = findViewById(R.id.tgbtn_start_with_cube);
            tb_start_cube.setEnabled(false);
        }
        btn_drop.setEnabled(false);
        btn_undo.setEnabled(true);
        timestamp("drop");
        shapeCheck = false;
        overallLayout.removeView(iv);
        actionList.clear();
        actionList.add("drop");
        actionList.add("x not matter");
        actionList.add("y not matter");
        actionList.add(TimerUtil.timestamp);
        actionDic.put(actionCount, actionList);
        actionCount++;
        mapChange();
    }

    public void onClickSpill(View v) throws JSONException {
        if (!startTimer && !incapChecked) {
            timestamp("spill");
            InputManager.numSpill++;
            btn_spill.setText("SPILL - " + InputManager.numSpill);
        }
    }

    public void onClickFoul(View v) throws JSONException {
        if (!startTimer && !incapChecked) {
            timestamp("scaleFoul");
            InputManager.numFoul++;
            btn_foul.setText("FOUL - " + InputManager.numFoul);
        }
    }

    public void onClickUndo(View v) {
        Log.e("jkgg", valueOf(actionCount));
        if (actionCount > 0) {
            actionCount = actionCount - 1;
            Log.e("actiondic?!", actionDic.toString());
            actionDic.remove(actionCount + 1);
            Log.e("wok", actionDic.get(actionCount).get(3).toString());
            // Log.e("PLZ",actionDic.get(actionCount).get(1).toString() );
            if (actionDic.get(actionCount).get(3).equals("triangle")) {
                Log.e("Why does this work", "WHYYYY");
                overallLayout.removeView(iv);
                shapeCheck = false;
                btn_drop.setEnabled(false);
                noShape = true;
                mapChange();
            } else if (actionDic.get(actionCount).get(3).equals("circle")) {
                Log.e("FUN", "check");
                shapeCheck = true;
                btn_drop.setEnabled(true);
                Log.e("Hello", "Work");
                overallLayout.removeView(iv2);
                mapChange();
            } else if (actionDic.get(actionCount).get(0).equals("drop")) {
                shapeCheck = true;
                btn_drop.setEnabled(true);
                mapChange();
            }
//            else if(actionDic.get(actionCount).get(3).equals("teleop")){
//                tele = false;
//                //could fail
//                Fragment fragment = new AutoDialog();
//                btn_startTimer = findViewById(R.id.btn_timer);
//                startTimer=false;
//
//                //stop fail
//
//                FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction transaction = fm.beginTransaction();
//                if (InputManager.mAllianceColor.equals("red")) {
//                    transaction.add(R.id.red_auto, fragment, "FRAGMENT");
//                    for (int i = 0; i < rg_blue_starting_position.getChildCount(); i++) {
//                        rg_blue_starting_position.getChildAt(i).setEnabled(false);
//                    }
//                } else if (InputManager.mAllianceColor.equals("blue")) {
//                    transaction.add(R.id.blue_auto, fragment, "FRAGMENT");
//                    for (int i = 0; i < rg_blue_starting_position.getChildCount(); i++) {
//                        rg_red_starting_position.getChildAt(i).setEnabled(false);
//                    }
//                }
//                transaction.commit();
//
//            }
        }
    }

    public void onClickEdit(View v) {
        if (actionCount > 0) {

        }
    }



    public void onClickClimb(View v) {
        final Float climbStartTime=TimerUtil.timestamp;
        final Dialog climbDialog = new Dialog(this);
        climbDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (isMapLeft){
            dialogLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.climb_dialog_l, null);
        }
        else {
            dialogLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.climb_dialog_r, null);
        }
       climbDialog.setCanceledOnTouchOutside(false);
       climbDialog.setCancelable(false);

        zeroI= (Button) dialogLayout.findViewById(R.id.zero);
        oneI= (Button) dialogLayout.findViewById(R.id.one);
        twoAI= (Button) dialogLayout.findViewById(R.id.twoa);
        threeI= (Button) dialogLayout.findViewById(R.id.three);
        twoBI= (Button) dialogLayout.findViewById(R.id.twob);
        zeroII= (Button) dialogLayout.findViewById(R.id.zeroII);
        oneII= (Button) dialogLayout.findViewById(R.id.oneII);
        twoAII= (Button) dialogLayout.findViewById(R.id.twoaII);
        threeII= (Button) dialogLayout.findViewById(R.id.threeII);
        twoBII= (Button) dialogLayout.findViewById(R.id.twobII);
        spaceOneI= (Button) dialogLayout.findViewById(R.id.SpaceOne);
        spaceTwoI= (Button) dialogLayout.findViewById(R.id.SpaceTwo);
        spaceThreeI= (Button) dialogLayout.findViewById(R.id.SpaceThree);
        spaceOneII= (Button) dialogLayout.findViewById(R.id.SpaceOneII);
        spaceTwoII= (Button) dialogLayout.findViewById(R.id.SpaceTwoII);
        spaceThreeII= (Button) dialogLayout.findViewById(R.id.SpaceThreeII);
        final Button cancel= (Button) dialogLayout.findViewById(R.id.cancelButton);
        final Button done= (Button) dialogLayout.findViewById(R.id.doneButton);
        spaceChanger(spaceTwoI, spaceThreeI, spaceOneI);
        spaceChanger(spaceTwoII, spaceThreeII, spaceOneII);

        if (climbInputted){
            spaceOneI.setText(climbAttemptValues.get(0).toString());
            spaceTwoI.setText(climbAttemptValues.get(1).toString());
            spaceThreeI.setText(climbAttemptValues.get(2).toString());
            spaceOneII.setText(climbActualValues.get(0).toString());
            spaceTwoII.setText(climbActualValues.get(1).toString());
            spaceThreeII.setText(climbActualValues.get(2).toString());
        }

        //this code works if we want to preset values
//        onClicknoneI(dialogLayout);
//        onClickoneI(dialogLayout);
//        onClicktwoAI(dialogLayout);
//        onClickthreeI(dialogLayout);
//        onClicktwoBI(dialogLayout);
//        onClicknoneII(dialogLayout);
//        onClickoneII(dialogLayout);
//        onClicktwoAII(dialogLayout);
//        onClickthreeII(dialogLayout);
//        onClicktwoBII(dialogLayout);
//
//        onClickspaceOneI(dialogLayout);
//        onClickspaceTwoI(dialogLayout);
//        onClickspaceThreeI(dialogLayout);
//
//        onClickspaceOneII(dialogLayout);
//        onClickspaceTwoII(dialogLayout);
//        onClickspaceThreeII(dialogLayout);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                climbActualCounter=0;
                climbAttemptCounter=0;
                climbDialog.dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spaceOneI.getText().toString().equals(" ") || spaceTwoI.getText().toString().equals(" ") || spaceThreeI.getText().toString().equals(" ") || spaceOneII.getText().toString().equals(" ") || spaceTwoII.getText().toString().equals(" ") || spaceThreeII.getText().toString().equals(" ")){
                    Toast.makeText(getBaseContext(), "Make Sure You Filled Out All Of The Information!",
                            Toast.LENGTH_SHORT).show();

                }else{
                    if(!climbInputted){
                        climbAttemptValues.add(spaceOneI.getText().toString());
                        climbAttemptValues.add(spaceTwoI.getText().toString());
                        climbAttemptValues.add(spaceThreeI.getText().toString());
                        climbActualValues.add(spaceOneII.getText().toString());
                        climbActualValues.add(spaceTwoII.getText().toString());
                        climbActualValues.add(spaceThreeII.getText().toString());
                    }else{
                        climbAttemptValues.set(0, spaceOneI.getText().toString());
                        climbAttemptValues.set(1, spaceTwoI.getText().toString());
                        climbAttemptValues.set(2, spaceThreeI.getText().toString());
                        climbActualValues.set(0, spaceOneII.getText().toString());
                        climbActualValues.set(1, spaceTwoII.getText().toString());
                        climbActualValues.set(2, spaceThreeII.getText().toString());
                    }
                    Log.e("wok", climbAttemptValues.toString());
                    Log.e("wok2", climbActualValues.toString());

                    recordClimb(climbStartTime);
                    climbActualCounter=0;
                    climbAttemptCounter=0;
                    climbDialog.dismiss();
                }
            }
        });

        climbDialog.setContentView(dialogLayout);
        climbDialog.show();

    }
    public void onClicknoneI(View v) {
        climbValuesAttempt("None", climbAttemptCounter);
        climbAttemptCounter++;
    }
    public void onClickoneI(View v) {
        climbValuesAttempt("1", climbAttemptCounter);
        climbAttemptCounter++;
    }
    public void onClicktwoAI(View v) {
        climbValuesAttempt("2", climbAttemptCounter);
        climbAttemptCounter++;
    }
    public void onClickthreeI(View v) {
        climbValuesAttempt("3", climbAttemptCounter);
        climbAttemptCounter++;

    }
    public void onClicktwoBI(View v) {
        climbValuesAttempt("2", climbAttemptCounter);
        climbAttemptCounter++;

    }
    public void onClicknoneII(View v) {
        climbValuesActual("None", climbActualCounter);
        climbActualCounter++;

    }
    public void onClickoneII(View v) {
        climbValuesActual("1", climbActualCounter);
        climbActualCounter++;

    }
    public void onClicktwoAII(View v) {
        climbValuesActual("2", climbActualCounter);
        climbActualCounter++;
    }
    public void onClickthreeII(View v) {
        climbValuesActual("3", climbActualCounter);
        climbActualCounter++;

    }
    public void onClicktwoBII(View v) {
        climbValuesActual("2", climbActualCounter);
        climbActualCounter++;
    }

    public void onClickspaceOneI(View v){
        climbAttemptCounter=0;
        spaceChanger(spaceTwoI, spaceThreeI, spaceOneI);
    }
    public void onClickspaceTwoI(View v){
        climbAttemptCounter=1;
        spaceChanger(spaceOneI, spaceThreeI, spaceTwoI);

    }
    public void onClickspaceThreeI(View v){
        climbAttemptCounter=2;
        spaceChanger(spaceOneI, spaceTwoI, spaceThreeI);

    }
    public void onClickspaceOneII(View v){
        climbActualCounter=0;
        spaceChanger(spaceTwoII, spaceThreeII, spaceOneII);
    }
    public void onClickspaceTwoII(View v){
        climbActualCounter=1;
        spaceChanger(spaceOneII, spaceThreeII, spaceTwoII);

    }
    public void onClickspaceThreeII(View v){
        climbActualCounter=2;
        spaceChanger(spaceOneII, spaceTwoII, spaceThreeII);
    }
    public void spaceChanger(Button whiteA, Button whiteB, Button Yellow){
        whiteA.setBackgroundColor(Color.WHITE);
        whiteB.setBackgroundColor(Color.WHITE);
        Yellow.setBackgroundColor(Color.YELLOW);

    }

    public void climbValuesAttempt(String buttonNumber, Integer climbintA){
        if (climbintA%3==0 ){
            spaceOneI.setText(buttonNumber);
            spaceChanger(spaceOneI, spaceThreeI, spaceTwoI);

        }else if(climbintA%3==1){
            spaceTwoI.setText(buttonNumber);
            spaceChanger(spaceOneI, spaceTwoI, spaceThreeI);

        }else if (climbintA%3==2){
            spaceThreeI.setText(buttonNumber);
            spaceChanger(spaceTwoI, spaceThreeI, spaceOneI);
        }

    }
    public void climbValuesActual(String buttonNumber, Integer climbintB){
        if(climbintB%3==0){
            spaceOneII.setText(buttonNumber);
            spaceChanger(spaceOneII, spaceThreeII, spaceTwoII);
        }else if(climbintB%3==1){
            spaceTwoII.setText(buttonNumber);
            spaceChanger(spaceOneII, spaceTwoII, spaceThreeII);
        }else if(climbintB%3==2){
            spaceThreeII.setText(buttonNumber);
            spaceChanger(spaceTwoII, spaceThreeII, spaceOneII);
        }
    }


    public void onClickIncap(View v) throws JSONException {
        if (!incapChecked) {
            tb_incap.setChecked(true);

            btn_drop.setEnabled(false);
            btn_undo.setEnabled(false);
            timestamp("beganIncap");

            incapChecked = true;
        } else if (incapChecked) {
            tb_incap.setChecked(false);
            if (shapeCheck) {
                btn_drop.setEnabled(true);
            }
            timestamp("endIncap");
            incapChecked = false;
        }
    }

    public void onClickDataCheck(View v) {
        if (rb_blue_right.isChecked() || rb_red_right.isChecked()) {
            InputManager.mStartingPosition = "right";
        } else if (rb_blue_center.isChecked() || rb_red_center.isChecked()) {
            InputManager.mStartingPosition = "center";
        } else if (rb_blue_left.isChecked() || rb_red_left.isChecked()) {
            InputManager.mStartingPosition = "left";
        }
        open(A2A.class, null, false, true);
    }

    public void onBackPressed() {
        final Activity activity = this;
        new AlertDialog.Builder(this)
                .setTitle("WARNING")
                .setMessage("GOING BACK WILL CAUSE LOSS OF DATA")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTouchListener() {
        overallLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                float x= (int) motionEvent.getX();
//                float y= (int) motionEvent.getY();
//                String message = String.format("Coordinates:(%.2f,%.2f)",x,y);
//                Log.d("hello", message);
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !startTimer && !incapChecked) {
                    int x = (int) motionEvent.getX();
                    int y = (int) motionEvent.getY();
                    if (x <= 1700 && y <= 1000 && InputManager.mScoutId <= 6 || x <= 1110 && y <= 610 && InputManager.mScoutId > 6 && InputManager.mScoutId < 12|| x<=845 && y<=490 && InputManager.mScoutId >=12 ) {
                        if ((x >= 400 && x <= 590 && y >= 90 && y <= 330 && InputManager.mScoutId <= 6 || x >= 270 && x <= 400 && y >= 80 && y <= 225 && InputManager.mScoutId > 6 && InputManager.mScoutId < 12 || x >= 200 && x <= 300 && y >= 70 && y <= 200 && InputManager.mScoutId >=12) && (tele || (isMapLeft && !tele))) {
                            if(shapeCheck) {
                                Log.d("locationOutput", "Top Left switch");
                                initShape(view, "score4", "score1", x, y, "circle", iv2, iv, false);
                            }
                        }
                        else if ((x >= 400 && x <= 590 && y >= 620 && y <= 860 && InputManager.mScoutId <= 6 || x >= 270 && x <= 400 && y >= 410 && y <= 560 && InputManager.mScoutId > 6 && InputManager.mScoutId < 12 || x >= 200 && x <= 300 && y >= 326 && y <= 450 && InputManager.mScoutId >=12) && (tele || (isMapLeft && !tele))) {
                            if(shapeCheck) {
                                Log.d("locationOutput", "Bottom Left switch");
                                initShape(view, "score3", "score6", x, y, "circle", iv2, iv, false);
                            }
                        } else if ((x >= 1110 && x <= 1300 && y >= 90 && y <= 330 && InputManager.mScoutId <= 6 || x >= 750 && x <= 860 && y >= 80 && y <= 225 && InputManager.mScoutId > 6 && InputManager.mScoutId < 12 || x >= 550 && x <= 654 && y >= 70 && y <= 200 && InputManager.mScoutId >=12) && (tele || (!isMapLeft && !tele))) {
                            if(shapeCheck) {
                                Log.d("locationOutput", "Top Right switch");
                                initShape(view, "score6", "score3", x, y, "circle", iv2, iv, false);
                            }
                        } else if ((x >= 1110 && x <= 1300 && y >= 620 && y <= 860 && InputManager.mScoutId <= 6 || x >= 750 && x <= 860 && y >= 410 && y <= 560 && InputManager.mScoutId > 6 && InputManager.mScoutId < 12 || x >= 550 && x <= 654 && y >= 326 && y <= 450 && InputManager.mScoutId >=12) && (tele || (!isMapLeft && !tele))) {
                            if(shapeCheck) {
                                Log.d("locationOutput", "Bottom Right switch");
                                initShape(view, "score1", "score4", x, y, "circle", iv2, iv, false);
                            }
                        } else if (x >= 760 && x <= 930 && y >= 60 && y <= 300 && InputManager.mScoutId <= 6 || x >= 510 && x <= 625 && y >= 55 && y <= 200 && InputManager.mScoutId > 6 && InputManager.mScoutId < 12 || x >= 375 && x <= 475 && y >= 50 && y <= 180 && InputManager.mScoutId >=12) {
                            if(shapeCheck) {
                                Log.d("locationOutput", "Scale Top");
                                initShape(view, "score5", "score2", x, y, "circle", iv2, iv, false);
                            }
                        } else if (x >= 760 && x <= 930 && y >= 650 && y <= 900 && InputManager.mScoutId <= 6 || x >= 510 && x <= 625 && y >= 440 && y <= 600 && InputManager.mScoutId > 6 && InputManager.mScoutId < 12 || x >= 375 && x <= 480 && y >= 350 && y <= 470 && InputManager.mScoutId >=12) {
                            if(shapeCheck) {
                                Log.d("locationOutput", "Scale Bottom");
                                initShape(view, "score2", "score5", x, y, "circle", iv2, iv, false);
                            }
                        } else if (((((x <= 160 && y >= 250 && y <= 430 && InputManager.mScoutId <= 6)
                                || (x <= 120 && y >= 170 && y <= 300 && InputManager.mScoutId > 6 && InputManager.mScoutId <12)
                                || (x<=90 && y>=150 && y<=250 && InputManager.mScoutId >=12))
                                && (((field_orientation.equals("rb") && InputManager.mAllianceColor.equals("red"))
                                || (field_orientation.equals("br") && InputManager.mAllianceColor.equals("blue")))))
                                || (((x >= 1530 && y >= 485 && y <= 695 && InputManager.mScoutId <= 6)
                                || (x >= 1030 && y >= 315 && y <= 455 && InputManager.mScoutId > 6 && InputManager.mScoutId <12)
                                || (x>=760 && x<=845 && y>=270 && y<=370 && InputManager.mScoutId >=12))
                                && (((field_orientation.equals("rb") && InputManager.mAllianceColor.equals("blue"))
                                || (field_orientation.equals("br") && InputManager.mAllianceColor.equals("red"))))))
                                && tele) {
                            if(shapeCheck) {
                                Log.d("locationOutput", "Exchange");
                                initShape(view, "exchangeScore", "exchangeScore", x, y, "circle", iv2, iv, false);
                            }
                        } else if (((x > 110 && x <= 500 && InputManager.mScoutId <= 6) || (x > 80 && x <= 320 && InputManager.mScoutId > 6 && InputManager.mScoutId <12) || (x<=250 && x>=60 && InputManager.mScoutId >=12)) && (tele || (isMapLeft && !tele))) {
                            if(!shapeCheck) {
                                Log.d("locationInput", "1");
                                initShape(view, "intake4", "intake1", x, y, "triangle", iv, iv2, true);
                            }
                        } else if (((x > 500 && x <= 845 && InputManager.mScoutId <= 6 || x > 320 && x <= 550 && InputManager.mScoutId > 6 && InputManager.mScoutId <12)|| (x<=430 && x>250 && InputManager.mScoutId >=12)) && (tele || (isMapLeft && !tele))) {
                            if(!shapeCheck) {
                                Log.d("locationInput", "2");
                                initShape(view, "intake3", "intake2", x, y, "triangle", iv, iv2, true);
                            }
                        } else if (((x > 845 && x <= 1200 && InputManager.mScoutId <= 6 || x > 550 && x <= 800 && InputManager.mScoutId > 6 && InputManager.mScoutId <12)|| (x<=600 && x>430 && InputManager.mScoutId >=12)) && (tele || (!isMapLeft && !tele))) {
                            if(!shapeCheck) {
                                Log.d("locationInput", "3");
                                initShape(view, "intake2", "intake3", x, y, "triangle", iv, iv2, true);
                            }
                        } else if (((x > 1200 && x <= 1580 && InputManager.mScoutId <= 6 || x > 800 && x <= 1040 && InputManager.mScoutId > 6 && InputManager.mScoutId <12)|| (x<=850 && x>600 && InputManager.mScoutId >=12)) && (tele || (!isMapLeft && !tele))) {
                            if(!shapeCheck) {
                                Log.d("locationInput", "4");
                                initShape(view, "intake1", "intake4", x, y, "triangle", iv, iv2, true);
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    public void initShape(View view, String position, String position2, int x, int y, String shape, ImageView add, ImageView remove, boolean check) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                50,
                50);
        lp.setMargins(x - 25, y - 40, 0, 0);
        add.setLayoutParams(lp);
        if (add == iv) {
            add.setImageDrawable(getResources().getDrawable(R.drawable.triangle_image));
        } else if (add == iv2) {
            add.setImageDrawable(getResources().getDrawable(R.drawable.blackcircle));
        }
        shapeCheck = check;
        btn_undo.setEnabled(true);
        if (!tele) {
            tb_start_cube = findViewById(R.id.tgbtn_start_with_cube);
            tb_start_cube.setEnabled(false);
        }
        if (!shapeCheck) {
            btn_drop.setEnabled(false);
        } else if (shapeCheck) {
            btn_drop.setEnabled(true);
        }
        overallLayout.removeView(remove);
        mapChange();
        if (field_orientation.equals("rb")) {
            actionList.clear();
            actionList.add(position);
            actionList.add(x);
            actionList.add(y);
            actionList.add(shape);
            actionList.add("rb");
            actionDic.put(actionCount, actionList);
            actionCount++;
            timestamp(position);
            Log.d("TIMESTAMP", String.valueOf(mRealTimeMatchData));
        } else if (field_orientation.equals("br")) {
            actionList.clear();
            actionList.add(position2);
            actionList.add(x);
            actionList.add(y);
            actionList.add(shape);
            actionList.add("br");
            actionDic.put(actionCount, actionList);
            actionCount++;
            timestamp(position2);
            Log.d("TIMESTAMP", String.valueOf(mRealTimeMatchData));
        }
        ((ViewGroup) view).addView(add);
    }

    //TODO Make Undo a function

    public void timestamp(String datapoint) {
        try {
            if (TimerUtil.timestamp <= 135 && !tele) {
                mRealTimeMatchData.put(new JSONObject().put(datapoint, 135));
            } else if (TimerUtil.timestamp > 135 && tele) {
                mRealTimeMatchData.put(new JSONObject().put(datapoint, 134.9));
            } else {
                mRealTimeMatchData.put(new JSONObject().put(datapoint, Float.valueOf(String.format("%.2f", TimerUtil.timestamp))));
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    public void mapOrientation(int map, int fieldOrientation, String field_orientation_given, Boolean field1, Boolean field2) {
        AppCc.setSp("mapOrientation", 0);
        setContentView(map);
        field_orientation = field_orientation_given;
        overallLayout = findViewById(fieldOrientation);
        if (InputManager.mAllianceColor.equals("red")) {
            isMapLeft = field1;
        } else if (InputManager.mAllianceColor.equals("blue")) {
            isMapLeft = field2;
        }
    }

    public void mapChange() {
        if(shapeCheck) {
            if (field_orientation.equals("rb")) {
                iv_field.setImageResource(R.drawable.field_yellow_rb);
            } else if (field_orientation.equals("br")) {
                iv_field.setImageResource(R.drawable.field_yellow_br);
            }
        }
        if(!shapeCheck) {
            if (field_orientation.equals("rb")) {
                iv_field.setImageResource(R.drawable.field_rb);
            } else if (field_orientation.equals("br")) {
                iv_field.setImageResource(R.drawable.field_br);
            }
        }
    }

    public void recordClimb(float time) {
        try {
            climbAttemptData.put(climbAttemptKeys.get(0), climbAttemptValues.get(0));
            climbAttemptData.put(climbAttemptKeys.get(1), climbAttemptValues.get(1));
            climbAttemptData.put(climbAttemptKeys.get(2), climbAttemptValues.get(2));
            climbActualData.put(climbActualKeys.get(0), climbActualValues.get(0));
            climbActualData.put(climbActualKeys.get(1), climbActualValues.get(1));
            climbActualData.put(climbActualKeys.get(2), climbActualValues.get(2));
            climbFinalData.put(climbKeys.get(0), "climb");
            climbFinalData.put(climbKeys.get(1), time);
            climbFinalData.put(climbKeys.get(2), climbAttemptData);
            climbFinalData.put(climbKeys.get(3), climbActualData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        climbInputted = true;
        Log.e("Climb", climbFinalData.toString());
    }
}
