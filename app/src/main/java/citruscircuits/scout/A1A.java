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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import citruscircuits.scout.utils.TimerUtil;

import static citruscircuits.scout.Managers.InputManager.mAllianceColor;
import static citruscircuits.scout.Managers.InputManager.mRealTimeMatchData;
import static citruscircuits.scout.Managers.InputManager.mScoutId;
import static java.lang.String.valueOf;

//Written by the Daemon himself ~ Calvin
//testing
public class A1A extends DialogMaker implements View.OnClickListener {

    public LayoutInflater layoutInflater;

    public ImageView iv_field;

    public String field_orientation;

    public boolean incapChecked = false;
    public boolean startTimer = true;
    public boolean tele = false;
    public boolean startedWCube = false;
    public boolean liftSelfAttempt;
    public boolean liftSelfActual;
    public boolean climbInputted = false;
    public boolean shapeCheck = false;
    public boolean pw = true;
    public boolean isMapLeft=false;
    public boolean didSucceed;
    public boolean wasDefended;

    public Integer numRobotsAttemptedToLift = 0;
    public Integer numRobotsDidLift = 0;
    public Integer climbAttemptCounter=0;
    public Integer climbActualCounter=0;
    public Integer level;

    public Float ftbStartTime;
    public Float ftbEndTime;

    public List<String> climbAttemptKeys = Arrays.asList("self", "robot1", "robot2");
    public List<String> climbActualKeys = Arrays.asList("self", "robot1", "robot2");
    public List<String> climbKeys = Arrays.asList("type", "time", "attempted", "actual");

    public List<Integer> climbAttemptValues = new ArrayList<>();
    public List<Integer> climbActualValues = new ArrayList<>();
    public List<Object> climbValues = new ArrayList<>();

    JSONObject climbAttemptData = new JSONObject();
    JSONObject climbActualData = new JSONObject();
    JSONObject climbFinalData = new JSONObject();

    public TextView tv_team;
    public TextView tv_starting_position_warning;

    public Button btn_startTimer;
    public Button btn_drop;
    public Button btn_spill;
    public Button btn_undo;
    public Button btn_climb;
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

    public RelativeLayout dialogLayout;
    public RelativeLayout overallLayout;

    public PopupWindow popup = new PopupWindow();
    public PopupWindow popup_fail_success = new PopupWindow();
    public PopupWindow popup_rocket = new PopupWindow();

    public ImageView field;
    public ImageView iv_game_element;

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

    public int x;
    public int y;

    public String mode = "intake";
    public String element;
    public String zone;
    public String structure;
    public String side;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        iv_field = findViewById(R.id.imageView);

        if (AppCc.getSp("mapOrientation", 99) != 99) {
            if (AppCc.getSp("mapOrientation", 99) == 0) {
                if(mAllianceColor.equals("blue")) {
                    iv_field.setImageResource(R.drawable.field_intake_blue_right);
                    field_orientation = "blue_right";
                    isMapLeft = false;
                } else if(mAllianceColor.equals("red")) {
                    iv_field.setImageResource(R.drawable.field_intake_red_left);
                    field_orientation = "red_left";
                    isMapLeft = true;
                }
            } else {
                if(mAllianceColor.equals("blue")) {
                    iv_field.setImageResource(R.drawable.field_intake_blue_left);
                    field_orientation = "blue_left";
                    isMapLeft = true;
                } else if(mAllianceColor.equals("red")) {
                    iv_field.setImageResource(R.drawable.field_intake_red_right);
                    field_orientation = "red_right";
                    isMapLeft = false;
                }
            }
        } else {
            if(mAllianceColor.equals("blue")) {
                iv_field.setImageResource(R.drawable.field_intake_blue_left);
                field_orientation = "blue_left";
                isMapLeft = true;
            } else if(mAllianceColor.equals("red")) {
                iv_field.setImageResource(R.drawable.field_intake_red_right);
                field_orientation = "red_right";
                isMapLeft = false;
            }
        }

        layoutInflater = (LayoutInflater) A1A.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        if(mScoutId < 9) {
            popup = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.pw_intake, null), 620, 450, false);
        } else {
            popup = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.pw_intake, null), 410, 300, false);
        }
        popup.setOutsideTouchable(false);
        popup.setFocusable(false);

        if(mScoutId < 9) {
            popup_fail_success = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.pw_fail_success, null), 620, 450, false);
        } else {
            popup_fail_success = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.pw_fail_success, null), 410, 300, false);
        }
        popup_fail_success.setOutsideTouchable(false);
        popup_fail_success.setFocusable(false);

        popup_rocket = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.pw_rocket, null), 620, 650, false);
        popup_rocket.setOutsideTouchable(false);
        popup_rocket.setFocusable(false);

        tv_team = findViewById(R.id.tv_teamNum);

        btn_drop = findViewById(R.id.btn_dropped);
        btn_spill = findViewById(R.id.btn_spilled);
        btn_undo = findViewById(R.id.btn_undo);
        btn_arrow = findViewById(R.id.btn_arrow);

        tb_incap = findViewById(R.id.tbtn_incap);

        iv_game_element = new ImageView(getApplicationContext());
        actionCount = 0;
        actionDic = new HashMap<Integer, List<Object>>();
        actionList = new ArrayList<Object>();

        overallLayout = findViewById(R.id.field);

        TimerUtil.mTimerView = findViewById(R.id.tv_timer);
        TimerUtil.mActivityView = findViewById(R.id.tv_activity);

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
    }

    @Override
    public void onClick(View v) {

    }

    public void onClickTeleop(View view) {
        if (!startTimer) {
            tele = true;
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
            overallLayout.removeView(iv_game_element);
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

//    public void onClickDrop(View v) {
//        if(!tele) {
//            tb_start_cube = findViewById(R.id.tgbtn_start_with_cube);
//            tb_start_cube.setEnabled(false);
//        }
//        btn_drop.setEnabled(false);
//        btn_undo.setEnabled(true);
//        timestamp();
//        shapeCheck = false;
//        overallLayout.removeView(iv_game_element);
//        actionList.clear();
//        actionList.add("drop");
//        actionList.add("x not matter");
//        actionList.add("y not matter");
//        actionList.add(TimerUtil.timestamp);
//        actionDic.put(actionCount, actionList);
//        actionCount++;
//        mapChange();
//    }

//    public void onClickSpill(View v) throws JSONException {
//        if (!startTimer && !incapChecked) {
//            timestamp();
//            InputManager.numSpill++;
//            btn_spill.setText("SPILL - " + InputManager.numSpill);
//        }
//    }

//    public void onClickUndo(View v) {
//        Log.e("jkgg", valueOf(actionCount));
//        if (actionCount > 0) {
//            actionCount = actionCount - 1;
//            Log.e("actiondic?!", actionDic.toString());
//            actionDic.remove(actionCount + 1);
//            Log.e("wok", actionDic.get(actionCount).get(3).toString());
//            // Log.e("PLZ",actionDic.get(actionCount).get(1).toString() );
//            if (actionDic.get(actionCount).get(3).equals("triangle")) {
//                Log.e("Why does this work", "WHYYYY");
//                overallLayout.removeView(iv);
//                shapeCheck = false;
//                btn_drop.setEnabled(false);
//                noShape = true;
//                mapChange();
//            } else if (actionDic.get(actionCount).get(3).equals("circle")) {
//                Log.e("FUN", "check");
//                shapeCheck = true;
//                btn_drop.setEnabled(true);
//                Log.e("Hello", "Work");
//                overallLayout.removeView(iv2);
//                mapChange();
//            } else if (actionDic.get(actionCount).get(0).equals("drop")) {
//                shapeCheck = true;
//                btn_drop.setEnabled(true);
//                mapChange();
//            }
//        }
//    }

    public void climbAttemptEdit(Button spaceValue, Integer space){
        if(climbInputted){
            if (climbAttemptValues.get(space)==0){
                spaceValue.setText("None");
            }else{
                spaceValue.setText(climbAttemptValues.get(space).toString());
            }
        }
    }
    public void climbActualEdit(Button spaceValue, Integer space){
        if(climbInputted){
            if (climbActualValues.get(space)==0){
                spaceValue.setText("None");
            }else{
                spaceValue.setText(climbActualValues.get(space).toString());
            }
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

        climbAttemptEdit(spaceOneI,0);
        climbAttemptEdit(spaceTwoI,1);
        climbAttemptEdit(spaceThreeI,2);
        climbActualEdit(spaceOneII,0);
        climbActualEdit(spaceTwoII,1);
        climbActualEdit(spaceThreeII,2);

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
                    dataListSendAttempt(spaceOneI.getText().toString(), spaceOneI, 0);
                    dataListSendAttempt(spaceTwoI.getText().toString(), spaceTwoI, 1);
                    dataListSendAttempt(spaceThreeI.getText().toString(), spaceThreeI, 2);
                    dataListSendActual(spaceOneII.getText().toString(), spaceOneII, 0);
                    dataListSendActual(spaceTwoII.getText().toString(), spaceTwoII, 1);
                    dataListSendActual(spaceThreeII.getText().toString(), spaceThreeII, 2);
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
        Yellow.setBackgroundColor(Color.parseColor("#f6ff6a"));
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
    public void dataListSendAttempt(String buttonvalue, Button spaceValueAttempt, Integer listID){
        if(!climbInputted){
            if (buttonvalue.equals("None")){
                climbAttemptValues.add(0);
            }else{
                climbAttemptValues.add(Integer.valueOf(spaceValueAttempt.getText().toString()));
            }
        }else {
            if (buttonvalue.equals("None")){
                climbAttemptValues.set(listID, 0);
            }else{
                climbAttemptValues.set(listID, Integer.valueOf(spaceValueAttempt.getText().toString()));
            }
        }
    }
    public void dataListSendActual(String buttonvalue, Button spaceValueActual, Integer listID){
        if(!climbInputted){
            if (buttonvalue.equals("None")){
                climbActualValues.add(0);
            }else{
                climbActualValues.add(Integer.valueOf(spaceValueActual.getText().toString()));
            }
        }else {
            if (buttonvalue.equals("None")){
                climbActualValues.set(listID, 0);
            }else{
                climbActualValues.set(listID, Integer.valueOf(spaceValueActual.getText().toString()));
            }
        }
    }


//    public void onClickIncap(View v) throws JSONException {
//        if (!incapChecked) {
//            tb_incap.setChecked(true);
//
//            btn_drop.setEnabled(false);
//            btn_undo.setEnabled(false);
//            timestamp();
//
//            incapChecked = true;
//        } else if (incapChecked) {
//            tb_incap.setChecked(false);
//            if (shapeCheck) {
//                btn_drop.setEnabled(true);
//            }
//            timestamp();
//            incapChecked = false;
//        }
//    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTouchListener() {
        overallLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if(pw) {
                        x = (int) motionEvent.getX();
                        y = (int) motionEvent.getY();

                        if(!(((x > 1700 || y > 985) && mScoutId < 9) || ((x > 1130 || y > 660) && mScoutId >= 9))) {
                            if(mode.equals("intake")) {
                                pw = false;
                                initPopup(popup);
                            } else if(mode.equals("placement") && (((y > -4.5 * x + 4457.5 && y > 4.5 * x - 4182.5 && y > 700 && field_orientation.contains("right") && mScoutId < 9)
                                    || (y < 4.5 * x - 3405 && y < -4.5 * x + 5212.5 && y < 330 && field_orientation.contains("right") && mScoutId < 9)
                                    || (y > -4.5 * x + 3467.5 && y > 4.5 * x - 3585 && y > 700 && field_orientation.contains("left") && mScoutId < 9)
                                    || (y < 4.5 * x - 2437.5 && y < -4.5 * x + 4245 && y < 330 && field_orientation.contains("left") && mScoutId < 9)
                                    || (y > -4.625 * x + 3031.875 && y > 4.625 * x - 2865 && y > 465 && field_orientation.contains("right") && mScoutId >= 9)
                                    || (y < 4.625 * x - 2346.875 && y < -4.625 * x + 3550 && y < 220 && field_orientation.contains("right") && mScoutId >= 9)
                                    || (y > -4.625 * x + 2361.25 && y > 4.625 * x - 2217.5 && y > 465 && field_orientation.contains("left") && mScoutId >= 9)
                                    || (y < 4.625 * x - 1671.25 && y < -4.625 * x + 2907.5 && y < 220 && field_orientation.contains("left") && mScoutId >= 9)
                                    || (((field_orientation.contains("left") && x > 950 && x < 1445) || (field_orientation.contains("right") && x > 255 && x < 760)) && y > 335 && y < 700 && mScoutId < 9))
                                    || (((field_orientation.contains("left") && x > 625 && x < 960) || (field_orientation.contains("right") && x > 170 && x < 505)) && y > 225 && y < 565 && mScoutId >= 9))){
                                pw = false;
                                initPopup(popup_fail_success);
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    public void onClickOrange(View view) {
        initIntake("orange");
    }

    public void onClickLemon(View view) {
        initIntake("lemon");
    }

    public void onClickCancel(View view) {
        popup.dismiss();
        pw = true;
    }

    public void onClickFail(View view) {
        overallLayout.removeView(iv_game_element);

        mode = "intake";

        if(zone.contains("LoadingStation")) {
            pw = true;
            recordLoadingStation(false);

            mapChange();
        } else {
            initPlacement();
        }

        popup_fail_success.dismiss();
    }

    public void onClickSuccess(View view) {
        if(mode.equals("intake")) {
            mode = "placement";
        } else if(mode.equals("placement")) {
            mode = "intake";
        }

        if(zone.contains("LoadingStation")) {
            recordLoadingStation(true);

            initShape();
        } else {
            initPlacement();
        }

        popup_fail_success.dismiss();
    }

    public void onClickCancelFS(View view) {
        popup_fail_success.dismiss();
        pw = true;
    }

    public void onClickDone(View view) {
        initShape();
        popup_rocket.dismiss();
    }

    public void timestamp() {
        try {
            if ((TimerUtil.timestamp <= 135 && !tele) || (TimerUtil.timestamp > 135 && tele)) {
                mRealTimeMatchData.put(new JSONObject().put("time", String.format("%.2f", TimerUtil.timestamp) + "*"));
            } else {
                mRealTimeMatchData.put(new JSONObject().put("time", String.format("%.2f", TimerUtil.timestamp)));
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    public void recordLoadingStation(boolean didSucceed) {
        try {
            mRealTimeMatchData.put(new JSONObject().put("type", "intake"));
            timestamp();
            mRealTimeMatchData.put(new JSONObject().put("piece", element));
            mRealTimeMatchData.put(new JSONObject().put("zone", zone));
            mRealTimeMatchData.put(new JSONObject().put("didSucceed", didSucceed));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("RECORDING?", mRealTimeMatchData.toString());
    }

    public void recordPlacement(Boolean didSucceed, Boolean wasDefended, String structure, String side, Integer level) { //TODO REDO HOW didSucceed is recorded
        try {
            mRealTimeMatchData.put(new JSONObject().put("type", "placement"));
            timestamp();
            mRealTimeMatchData.put(new JSONObject().put("piece", element));
            mRealTimeMatchData.put(new JSONObject().put("didSucceed", didSucceed));
            mRealTimeMatchData.put(new JSONObject().put("wasDefended", wasDefended));
            mRealTimeMatchData.put(new JSONObject().put("structure", structure));
            mRealTimeMatchData.put(new JSONObject().put("side", side));
            if(structure.contains("Rocket")) {
                mRealTimeMatchData.put(new JSONObject().put("level", level));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("RECORDING?", mRealTimeMatchData.toString());
    }

    public void mapChange() {
        if(element.equals("orange")) {
            iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.orange));
            if(mode.equals("placement")) {
                iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.orange));
                if (field_orientation.contains("left")) {
                    iv_field.setImageResource(R.drawable.field_placement_orange_left);
                } else if (field_orientation.contains("right")) {
                    iv_field.setImageResource(R.drawable.field_placement_orange_right);
                }
            }
        } else if(element.equals("lemon")) {
            iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.lemon));
            if(mode.equals("placement")) {
                if (field_orientation.contains("left")) {
                    iv_field.setImageResource(R.drawable.field_placement_lemon_left);
                } else if (field_orientation.contains("right")) {
                    iv_field.setImageResource(R.drawable.field_placement_lemon_right);
                }
            }
        } if(mode.equals("intake")) {
            if(field_orientation.equals("blue_left")) {
                iv_field.setImageResource(R.drawable.field_intake_blue_left);
            } else if(field_orientation.equals("blue_right")) {
                iv_field.setImageResource(R.drawable.field_intake_blue_right);
            } else if(field_orientation.equals("red_left")) {
                iv_field.setImageResource(R.drawable.field_intake_red_left);
            } else if(field_orientation.equals("red_right")) {
                iv_field.setImageResource(R.drawable.field_intake_red_right);
            }
        }
    }

    public void initShape() {
        pw = true;
        overallLayout.removeView(iv_game_element);

        if(element.equals("orange")) {
            iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.orange));
        } else if(element.equals("lemon")) {
            iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.lemon));
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                100,
                100);
        if((y > 900 && mScoutId < 9) || (y > 550 && mScoutId >= 9)) {
            if((x < 40 && mScoutId < 9) || (x < 25 && mScoutId >= 9)) {
                lp.setMargins(x + 20, y - 90, 0, 0);
            } else if((x > 1650 && mScoutId < 9) || (x > 1090 && mScoutId >= 9)) {
                lp.setMargins(x - 100, y - 90, 0, 0);
            } else {
                lp.setMargins(x - 25, y - 90, 0, 0);
            }
        } else if((y < 85 && mScoutId < 9) || (y < 55 && mScoutId >= 9)) {
            if((x < 40 && mScoutId < 9) || (x < 25 && mScoutId >= 9)) {
                lp.setMargins(x + 20, y + 5, 0, 0);
            } else if((x > 1650 && mScoutId < 9) || (x > 1090 && mScoutId >= 9)) {
                lp.setMargins(x - 100, y + 5, 0, 0);
            } else {
                lp.setMargins(x - 25, y + 5, 0, 0);
            }
        } else if((x < 40 && mScoutId < 9) || (x < 25 && mScoutId >= 9)) {
            lp.setMargins(x + 20, y - 40, 0, 0);
        } else if((x > 1650 && mScoutId < 9) || (x > 1090 && mScoutId >= 9)) {
            lp.setMargins(x - 100, y - 40, 0, 0);
        } else {
            lp.setMargins(x - 25, y - 40, 0, 0);
        }
        iv_game_element.setLayoutParams(lp);

        ((ViewGroup) overallLayout).addView(iv_game_element);

        mapChange();
    }

    public void initIntake(String givenElement) {
        popup.dismiss();
        element = givenElement;

        if((((field_orientation.contains("left") && x < 225) || (field_orientation.contains("right") && x > 1440)) && mScoutId < 9)
                || (((field_orientation.contains("left") && x < 175) || (field_orientation.contains("right") && x > 955)) && mScoutId >= 9)) {
            if((((field_orientation.contains("left") && y<=415) || (field_orientation.contains("right") && y>=615)) && mScoutId < 9)
                    || (((field_orientation.contains("left") && y<=280) || (field_orientation.contains("right") && y>=410)) && mScoutId >= 9)) {
                zone = "leftLoadingStation";
            } else if((((field_orientation.contains("right") && y<=415) || (field_orientation.contains("left") && y>=615)) && mScoutId < 9)
                    || (((field_orientation.contains("right") && y<=280) || (field_orientation.contains("left") && y>=410)) && mScoutId >= 9)) {
                zone = "rightLoadingStation";
            }
            initPopup(popup_fail_success);
        } else {
            mode = "placement";
            if((y>517 && mScoutId < 9) || (y>345 && mScoutId >= 9)) {
                if ((((field_orientation.contains("left") && x <= 540) || (field_orientation.contains("right") && x >= 1160)) && mScoutId < 9)
                        || (((field_orientation.contains("left") && x <= 360) || (field_orientation.contains("right") && x >= 955)) && mScoutId >= 9)) {
                    zone = "zone1Right";
                } else if ((((field_orientation.contains("left") && x <= 940) || (field_orientation.contains("right") && x >= 760)) && mScoutId < 9)
                        || (((field_orientation.contains("left") && x <= 625) || (field_orientation.contains("right") && x >= 500)) && mScoutId >= 9)) {
                    zone = "zone2Right";
                } else if ((((field_orientation.contains("left") && x <= 1445) || (field_orientation.contains("right") && x >= 255)) && mScoutId < 9)
                        || (((field_orientation.contains("left") && x <= 960) || (field_orientation.contains("right") && x >= 170)) && mScoutId >= 9)) {
                    zone = "zone3Right";
                } else {
                    zone = "zone4Right";
                }
            } else {
                if ((((field_orientation.contains("left") && x <= 540) || (field_orientation.contains("right") && x >= 1160)) && mScoutId < 9)
                        || (((field_orientation.contains("left") && x <= 360) || (field_orientation.contains("right") && x >= 955)) && mScoutId >= 9)) {
                    zone = "zone1Left";
                } else if ((((field_orientation.contains("left") && x <= 940) || (field_orientation.contains("right") && x >= 760)) && mScoutId < 9)
                        || (((field_orientation.contains("left") && x <= 625) || (field_orientation.contains("right") && x >= 500)) && mScoutId >= 9)) {
                    zone = "zone2Left";
                } else if ((((field_orientation.contains("left") && x <= 1445) || (field_orientation.contains("right") && x >= 255)) && mScoutId < 9)
                        || (((field_orientation.contains("left") && x <= 960) || (field_orientation.contains("right") && x >= 170)) && mScoutId >= 9)) {
                    zone = "zone3Left";
                } else {
                    zone = "zone4Left";
                }
            }

            try {
                mRealTimeMatchData.put(new JSONObject().put("type", "intake"));
                timestamp();
                mRealTimeMatchData.put(new JSONObject().put("piece", element));
                mRealTimeMatchData.put(new JSONObject().put("zone", zone));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("RECORDING?", mRealTimeMatchData.toString());
            initShape();
        }
    }

    public void initPlacement() {
        popup_fail_success.dismiss();

        //ROCKET LEFT (bottom, right, green): y > -4.5 * x + 4457.5 && y > 4.5 * x - 4182.5 && y > 700 && field_orientation.contains("right") && mScoutId < 9
        //ROCKET RIGHT (top, right, green): y < 4.5 * x - 3405 && y < -4.5 * x + 5212.5 && y < 330 && field_orientation.contains("right") && mScoutId < 9

        //ROCKET RIGHT (bottom, left, green): y > -4.5 * x + 3467.5 && y > 4.5 * x - 3585 && y > 700 && field_orientation.contains("left") && mScoutId < 9
        //ROCKET LEFT (top, left, green): y < 4.5 * x - 2437.5 && y < -4.5 * x + 4245 && y < 330 && field_orientation.contains("left") && mScoutId < 9

        //ROCKET LEFT (bottom, right, black): y > -4.625 * x + 3031.875 && y > 4.625 * x - 2865 && y > 465 && field_orientation.contains("right") && mScoutId >= 9
        //ROCKET RIGHT (top, right, black): y < 4.625 * x - 2346.875 && y < -4.625 * x + 3550 && y < 220 && field_orientation.contains("right") && mScoutId >= 9

        //ROCKET RIGHT (bottom, left, black): y > -4.625 * x + 2361.25 && y > 4.625 * x - 2217.5 && y > 465 && field_orientation.contains("left") && mScoutId >= 9
        //ROCKET LEFT (bottom, left, black): y < 4.625 * x - 1671.25 && y < -4.625 * x + 2907.5 && y < 220 && field_orientation.contains("left") && mScoutId >= 9


        if((y > -4.5 * x + 4457.5 && y > 4.5 * x - 4182.5 && y > 700 && field_orientation.contains("right") && mScoutId < 9)
                || (y < 4.5 * x - 3405 && y < -4.5 * x + 5212.5 && y < 330 && field_orientation.contains("right") && mScoutId < 9)
                || (y > -4.5 * x + 3467.5 && y > 4.5 * x - 3585 && y > 700 && field_orientation.contains("left") && mScoutId < 9)
                || (y < 4.5 * x - 2437.5 && y < -4.5 * x + 4245 && y < 330 && field_orientation.contains("left") && mScoutId < 9)
                || (y > -4.625 * x + 3031.875 && y > 4.625 * x - 2865 && y > 465 && field_orientation.contains("right") && mScoutId >= 9)
                || (y < 4.625 * x - 2346.875 && y < -4.625 * x + 3550 && y < 220 && field_orientation.contains("right") && mScoutId >= 9)
                || (y > -4.625 * x + 2361.25 && y > 4.625 * x - 2217.5 && y > 465 && field_orientation.contains("left") && mScoutId >= 9)
                || (y < 4.625 * x - 1671.25 && y < -4.625 * x + 2907.5 && y < 220 && field_orientation.contains("left") && mScoutId >= 9)) {
            if((y < 4.5 * x - 3405 && y < -4.5 * x + 5212.5 && y < 330 && field_orientation.contains("right") && mScoutId < 9)
                    || (y > -4.5 * x + 3467.5 && y > 4.5 * x - 3585 && y > 700 && field_orientation.contains("left") && mScoutId < 9)
                    || (y < 4.625 * x - 2346.875 && y < -4.625 * x + 3550 && y < 220 && field_orientation.contains("right") && mScoutId >= 9)
                    || (y > -4.625 * x + 2361.25 && y > 4.625 * x - 2217.5 && y > 465 && field_orientation.contains("left") && mScoutId >= 9)) {
                structure = "rightRocket";
            } else if((y > -4.5 * x + 4457.5 && y > 4.5 * x - 4182.5 && y > 700 && field_orientation.contains("right") && mScoutId < 9)
                    || (y < 4.5 * x - 2437.5 && y < -4.5 * x + 4245 && y < 330 && field_orientation.contains("left") && mScoutId < 9)
                    || (y > -4.625 * x + 3031.875 && y > 4.625 * x - 2865 && y > 465 && field_orientation.contains("right") && mScoutId >= 9)
                    || (y < 4.625 * x - 1671.25 && y < -4.625 * x + 2907.5 && y < 220 && field_orientation.contains("left") && mScoutId >= 9)) {
                structure = "leftRocket";
            }
            if(element.equals("lemon")) {
                if((((x >= 740 && field_orientation.contains("left")) || (x <= 960 && field_orientation.contains("right"))) && mScoutId < 9) || (((x >= 740 && field_orientation.contains("left")) || (x <= 960 && field_orientation.contains("right"))) && mScoutId >= 9)) {
                    side = "far";
                } else if((((x < 740 && field_orientation.contains("left")) || (x > 960 && field_orientation.contains("right"))) && mScoutId < 9) || (((x < 740 && field_orientation.contains("left")) || (x > 960 && field_orientation.contains("right"))) && mScoutId >= 9)) {
                    side = "near";
                }
            }
            initPopup(popup_rocket);
        } else if((((field_orientation.contains("left") && x > 950 && x < 1445) || (field_orientation.contains("right") && x > 255 && x < 760)) && y > 335 && y < 700 && mScoutId < 9)
                || ((field_orientation.contains("left") && x > 625 && x < 960) || (field_orientation.contains("right") && x > 170 && x < 505)) && y > 225 && y < 565 && mScoutId >= 9) {
            structure = "cargoShip";
            if((((field_orientation.contains("left") && x < 1130) || (field_orientation.contains("right") && x > 570)) && mScoutId < 9)
                    || ((field_orientation.contains("left") && x < 750) || (field_orientation.contains("right") && x > 380)) && mScoutId >= 9) {
                side = "near";
            } else if((((field_orientation.contains("left") &&  y < 517) || (field_orientation.contains("right") && y > 517)) && mScoutId < 9)
                    || ((field_orientation.contains("left") &&  y < 345) || (field_orientation.contains("right") && y > 345)) && mScoutId >= 9) {
                side = "left";
            } else if((((field_orientation.contains("left") &&  y >= 517) || (field_orientation.contains("right") && y <= 517)) && mScoutId < 9)
                    || ((field_orientation.contains("left") &&  y >= 345) || (field_orientation.contains("right") && y <= 345)) && mScoutId >= 9) {
                side = "right";
            }
            initShape();
        }
        Log.i("ROCKET!!", structure + side);
    }

    public void initPopup(PopupWindow pw) {
        pw.showAtLocation(overallLayout, Gravity.NO_GRAVITY, x - 350, y - 100);
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

    public void onClickDataCheck(View v) {
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
}
