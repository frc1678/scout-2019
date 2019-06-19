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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;

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

import citruscircuits.scout.utils.StormDialog;

import static citruscircuits.scout.utils.StormDialog.btn_startTimer;
import static citruscircuits.scout.utils.StormDialog.preloadCargo2;
import static citruscircuits.scout.utils.StormDialog.preloadNone2;
import static citruscircuits.scout.utils.StormDialog.preloadPanel2;
import static citruscircuits.scout.utils.StormDialog.tb_hab_run;

import static citruscircuits.scout.Managers.InputManager.mTabletType;
import static citruscircuits.scout.utils.StormDialog.teleButton;
import static java.lang.String.valueOf;

public class MapActivity extends DialogMaker implements View.OnClickListener {

    final Activity activity = this;

    public LayoutInflater layoutInflater;

    public ImageView iv_field;

    public String field_orientation;

    public boolean modeIsIntake = true;
    public static boolean startTimer = true;
    public boolean tele = false;
    public boolean startedWObject = false;
    public boolean climbInputted = false;
    public static boolean timerCheck = false;
    public boolean pw = true;
    public boolean isMapLeft = false;
    public boolean dropClick = false;
    public boolean placementDialogOpen = false;
    public boolean isPopupOpen = false;

    public boolean didSucceed;
    public boolean wasDefended;
    public boolean shotOutOfField;
    public boolean didUndoOnce = true;
    public Integer climbAttemptCounter = 0;
    public Integer climbActualCounter = 0;
    public Integer level;
    public Integer undoX;
    public Integer undoY;

    public Float time;

    public List<String> climbAttemptKeys = Arrays.asList("D", "E", "F");
    public List<String> climbActualKeys = Arrays.asList("D", "E", "F");

    public List<Integer> climbAttemptValues = new ArrayList<>();
    public List<Integer> climbActualValues = new ArrayList<>();

    public JSONObject compressionDic;

    String climbAttemptData;
    String climbActualData;

    public TextView tv_team;

    //    public Button btn_startTimer;
    public static Button btn_drop;
    public static Button btn_foul;
    public static Button btn_cyclesDefended;
    public static Button btn_undo;
    public static Button btn_climb;
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
    public static ToggleButton tb_incap;
    public static ToggleButton tb_defense;

    public RelativeLayout dialogLayout;
    public RelativeLayout overallLayout;
    public RelativeLayout placementDialogLayout;

    public RadioButton fail;
    public RadioButton success;

    public RadioButton level1;
    public RadioButton level2;
    public RadioButton level3;

    public ToggleButton tb_wasDefended;
    public ToggleButton tb_shotOutOfField;

    public PopupWindow popup = new PopupWindow();
    public PopupWindow popup_fail_success = new PopupWindow();
    public PopupWindow popup_drop_defense = new PopupWindow();

    public ImageView field;
    public ImageView iv_game_element;

    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        public void run() {
            tv_team.setVisibility(View.INVISIBLE);
            btn_arrow.setEnabled(true);
            btn_arrow.setVisibility(View.VISIBLE);
        }
    };
    //Alert dialog pop up when 10 seconds into teleop
    public Handler teleWarningHandler = new Handler();
    public Runnable teleWarningRunnable = new Runnable() {
        public void run() {
            if (!tele) {
                new AlertDialog.Builder(activity)
                        .setTitle("YOU ARE 10 SECONDS INTO TELEOP!!")
                        .setPositiveButton("GO TO TELEOP", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                popup.dismiss();
                                popup_fail_success.dismiss();
                                if (placementDialogOpen) {
                                    placementDialog.dismiss();
                                }
                                pw = true;
                                toTeleop();
                            }
                        })
                        .setNegativeButton("STAY IN SANDSTORM", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    };

    public List<Object> actionList;
    public Map<Integer, List<Object>> actionDic;
    public int actionCount;
    public int x;
    public int y;

    public static String mode = "intake";
    public String element = "";
    public String zone = "";

    public String structure;
    public String side;

    public Dialog placementDialog;

    public Fragment fragment;
    public FragmentTransaction transaction;
    public FragmentManager fm;
    public Fragment fragmentRecreate;
    public FragmentTransaction transactionRecreate;
    public FragmentManager fmRecreate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        handler.removeCallbacks(runnable);
        handler.removeCallbacksAndMessages(null);

        teleWarningHandler.removeCallbacks(teleWarningRunnable);
        teleWarningHandler.removeCallbacksAndMessages(null);

        iv_field = findViewById(R.id.imageView);

//Set map color and orientation according to pre-selected alliance color and field view
        if (AppCc.getSp("mapOrientation", 99) != 99) {
            if (AppCc.getSp("mapOrientation", 99) == 0) {
                if (mAllianceColor.equals("blue")) {
                    iv_field.setImageResource(R.drawable.map_field_intake_blue_right);
                    field_orientation = "blue_right";
                    isMapLeft = false;
                } else if (mAllianceColor.equals("red")) {
                    iv_field.setImageResource(R.drawable.map_field_intake_red_left);
                    field_orientation = "red_left";
                    isMapLeft = true;
                }
            } else {
                if (mAllianceColor.equals("blue")) {
                    iv_field.setImageResource(R.drawable.map_field_intake_blue_left);
                    field_orientation = "blue_left";
                    isMapLeft = true;
                } else if (mAllianceColor.equals("red")) {
                    iv_field.setImageResource(R.drawable.map_field_intake_red_right);
                    field_orientation = "red_right";
                    isMapLeft = false;
                }
            }
        } else {
            if (mAllianceColor.equals("blue")) {
                iv_field.setImageResource(R.drawable.map_field_intake_blue_left);
                field_orientation = "blue_left";
                isMapLeft = true;
            } else if (mAllianceColor.equals("red")) {
                iv_field.setImageResource(R.drawable.map_field_intake_red_right);
                field_orientation = "red_right";
                isMapLeft = false;
            }
        }

        layoutInflater = (LayoutInflater) MapActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

//Set how big popups are for each tablet type
        if (mTabletType.equals("green")) {
            popup = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.map_popup_intake, null), 620, 450, false);
        } else if (mTabletType.equals("black")) {
            popup = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.map_popup_intake, null), 410, 300, false);
        } else if (mTabletType.equals("fire")) {
            popup = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.map_popup_intake, null), 310, 250, false);
        }
        popup.setOutsideTouchable(false);
        popup.setFocusable(false);

        if (mTabletType.equals("green")) {
            popup_fail_success = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.map_popup_fail_success_loading_station, null), 620, 450, false);
        } else if (mTabletType.equals("black")) {
            popup_fail_success = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.map_popup_fail_success_loading_station, null), 410, 300, false);
        } else if (mTabletType.equals("fire")) {
            popup_fail_success = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.map_popup_fail_success_loading_station, null), 310, 250, false);
        }
        popup_fail_success.setOutsideTouchable(false);
        popup_fail_success.setFocusable(false);

        if (mTabletType.equals("green")) {
            popup_drop_defense = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.map_popup_drop_defense, null), 620, 450, false);
        } else if (mTabletType.equals("black")) {
            popup_drop_defense = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.map_popup_drop_defense, null), 410, 300, false);
        } else if (mTabletType.equals("fire")) {
            popup_drop_defense = new PopupWindow((RelativeLayout) layoutInflater.inflate(R.layout.map_popup_drop_defense, null), 310, 250, false);
        }
        popup_drop_defense.setOutsideTouchable(false);
        popup_drop_defense.setFocusable(false);

//Declaration of buttons on map
        tv_team = findViewById(R.id.tv_teamNum);

        btn_drop = findViewById(R.id.btn_dropped);
        btn_foul = findViewById(R.id.btn_foul);
        btn_cyclesDefended = findViewById(R.id.btn_cyclesDefended);
        btn_undo = findViewById(R.id.btn_undo);
        btn_arrow = findViewById(R.id.btn_arrow);
        btn_climb = findViewById(R.id.btn_climb);
        tb_defense = (ToggleButton) findViewById(R.id.tbtn_defense);


        tb_incap = findViewById(R.id.tbtn_incap);
        tb_defense = findViewById(R.id.tbtn_defense);

        iv_game_element = new ImageView(getApplicationContext());
        actionCount = 0;
        actionDic = new HashMap<Integer, List<Object>>();
        actionList = new ArrayList<Object>();

        overallLayout = findViewById(R.id.field);

//Set location of sandstorm fragment
        TimerUtil.mTimerView = findViewById(R.id.tv_timer);
        preload();
        fragment = new StormDialog();
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        if (AppCc.getSp("mapOrientation", 99) != 99) {
            if (AppCc.getSp("mapOrientation", 99) != 0) {
                if (InputManager.mAllianceColor.equals("red")) {
                    transaction.add(R.id.left_storm, fragment, "FRAGMENT");
                } else if (InputManager.mAllianceColor.equals("blue")) {
                    transaction.add(R.id.right_storm, fragment, "FRAGMENT");
                }

            } else if (AppCc.getSp("mapOrientation", 99) == 0) {
                if (InputManager.mAllianceColor.equals("red")) {
                    transaction.add(R.id.right_storm, fragment, "FRAGMENT");

                } else if (InputManager.mAllianceColor.equals("blue")) {
                    transaction.add(R.id.left_storm, fragment, "FRAGMENT");
                }
            }

        }
        transaction.commit();

        tv_team.setText(valueOf(InputManager.mTeamNum));

//Set timer value if the timer exists
        if (TimerUtil.matchTimer != null) {
            TimerUtil.matchTimer.cancel();
            TimerUtil.matchTimer = null;
            TimerUtil.timestamp = 0f;
            TimerUtil.mTimerView.setText("15");
            startTimer = true;
        }

//Set data (fouls and cycles defended) as empty
        mRealTimeMatchData = new JSONArray();
        InputManager.mOneTimeMatchData = new JSONObject();
        InputManager.numFoul = 0;
        InputManager.cyclesDefended = 0;

        btn_drop.setEnabled(false);
        btn_undo.setEnabled(false);
        addTouchListener();

        //Deincrement Fouls counter upon long click
        btn_foul.setOnLongClickListener((new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (InputManager.numFoul > 0) {
                    int index = -1;
                    for (int i = 0; i < mRealTimeMatchData.length(); i++) {
                        try {
                            String test = mRealTimeMatchData.getString(i);
                            if (test.contains("foul")) {
                                index = i;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mRealTimeMatchData.remove(index);
                    InputManager.numFoul--;
                    btn_foul.setText("PIN FOUL - " + InputManager.numFoul);
                }
                return true;
            }
        }));

        //Deincrement Cycles Defended counter upon long click.
        btn_cyclesDefended.setOnLongClickListener((new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (InputManager.cyclesDefended > 0) {
                    int index = -1;
                    //Increment through mRealTimeMatchData to identify the last cyclesDefended to remove.
                    for (int i = 0; i < mRealTimeMatchData.length(); i++) {
                        try {
                            String test = mRealTimeMatchData.getString(i);
                            if (test.contains("cyclesDefended")) {
                                index = i;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mRealTimeMatchData.remove(index);
                    InputManager.cyclesDefended--;
                    btn_cyclesDefended.setText("FAILED PLACEMENTS/DROPS CAUSED - " + InputManager.cyclesDefended);
                }
                return true;
            }
        }));
    }


    @Override
    public void onClick(View v) {

    }

    // onClick methods for preload in sandstorm.
    public void onClickPreloadPanel(View view) {
        setPreloadType("panel");
    }

    public void onClickPreloadCargo(View view) {
        setPreloadType("cargo");
    }

    public void onClickPreloadNone(View view) {
        setPreloadType("none");
    }

    public void onClickTeleop(View view) {
        toTeleop();
    }

    // Method sets mPreload and calls preload and dismissPopups
    public void setPreloadType(String PVal) {
        InputManager.mPreload = PVal;
        preload();
        dismissPopups();
        if (!tb_incap.isChecked()) {
            pw = true;
        }
    }

    //Method to initialize the teleop map
    public void toTeleop() {
        if (!startTimer) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAGMENT");
            if (fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        dismissPopups();
        //Allows scout to touch screen if a popup used to be open
        if (isPopupOpen && !tb_incap.isChecked()) {
            pw = true;
        }
        tele = true;

        btn_undo.setEnabled(false);

        //If the timer is on and incap isn't checked, make buttons clickable

        if (timerCheck && !tb_incap.isChecked()) {
            btn_climb.setEnabled(true);
            tb_defense.setEnabled(true);
        }
        if (modeIsIntake) {
            mode = "intake";
        } else if (!modeIsIntake) {
            mode = "placement";
        }
        mapChange();
        InputManager.mCrossedHabLine = tb_hab_run.isChecked();
    }

    //When start timer is clicked, adjust timer based on being clicked on and off
    public void onClickStartTimer(View v) {
        handler.removeCallbacks(runnable);
        handler.removeCallbacksAndMessages(null);

        teleWarningHandler.removeCallbacks(teleWarningRunnable);
        teleWarningHandler.removeCallbacksAndMessages(null);

        TimerUtil.MatchTimerThread timerUtil = new TimerUtil.MatchTimerThread();

        btn_drop = findViewById(R.id.btn_dropped);

        if (startTimer) {
            pw = true;
            handler.postDelayed(runnable, 150000);
            teleWarningHandler.postDelayed(teleWarningRunnable, 25000);
            timerUtil.initTimer();
            btn_startTimer.setText("RESET TIMER");
            timerCheck = true;
            startTimer = false;
            btn_undo.setEnabled(false);
            teleButton.setEnabled(true);
            tb_hab_run.setEnabled(true);
            tb_incap.setEnabled(true);
            btn_foul.setEnabled(true);
            if (InputManager.mPreload.equals("cargo") || InputManager.mPreload.equals("panel")) {
                btn_drop.setEnabled(true);
            }
            InputManager.mTimerStarted = (int) (System.currentTimeMillis() / 1000);
            if (InputManager.mAllianceColor.equals("red")) {
                btn_startTimer.setBackgroundResource(R.drawable.map_storm_reset_red_selector);
            } else if (InputManager.mAllianceColor.equals("blue")) {
                btn_startTimer.setBackgroundResource(R.drawable.pregame_storm_reset_blue_selector);
            }

        } else if (!startTimer) {
            pw = false;
            InputManager.numFoul = 0;
            tb_incap.setEnabled(false);
            tb_incap.setChecked(false);
            btn_foul.setEnabled(false);
            tb_hab_run.setEnabled(false);
            tb_hab_run.setChecked(false);
            teleButton.setEnabled(false);
            btn_undo.setEnabled(false);
            btn_drop.setEnabled(false);
            actionDic.clear();
            TimerUtil.matchTimer.cancel();
            TimerUtil.matchTimer = null;
            TimerUtil.timestamp = 0f;
            TimerUtil.mTimerView.setText("15");
            btn_startTimer.setText("START TIMER");
            overallLayout.removeView(iv_game_element);
            dismissPopups();
            startTimer = true;
            timerCheck = false;
            isPopupOpen = false;
            preload();
            InputManager.numFoul = 0;
            actionCount = 0;
            btn_foul.setText("PIN FOUL - " + InputManager.numFoul);
            mRealTimeMatchData = new JSONArray();

            // Make preload enabled when you reset the timer.
            preloadEnabled(true);

            if (InputManager.mAllianceColor.equals("red")) {
                btn_startTimer.setBackgroundResource(R.drawable.map_storm_btn_red_selector);
            } else if (InputManager.mAllianceColor.equals("blue")) {
                btn_startTimer.setBackgroundResource(R.drawable.map_storm_btn_blue_selector);
            }
            mapChange();
        }
    }

    // Method sets preload buttons as enabled or disabled.
    public void preloadEnabled(Boolean preVal) {
        preloadCargo2.setEnabled(preVal);
        preloadPanel2.setEnabled(preVal);
        preloadNone2.setEnabled(preVal);
    }

    public void onClickDrop(View v) {
        //Sets an exact location for drop to occur

        if (mTabletType.equals("black")) {
            x = 230;
            y = 600;
        } else if (mTabletType.equals("green")) {
            x = 280;
            y = 820;
        } else {
            x = 150;
            y = 650;
        }
        dropClick = true;

        if (tele) {
            initPopup(popup_drop_defense);

            final Button defendedButton = (Button) layoutInflater.inflate(R.layout.map_popup_drop_defense, null).findViewById(R.id.dropDefended);

            if (element.equals("panel")) {
                defendedButton.setBackgroundColor(Color.parseColor("#fffa00"));
            } else {
                defendedButton.setBackgroundColor(Color.parseColor("#ffab4c"));
            }
        } else if (!tele) {
            recordDrop(false);
        }
    }

    //Record different types of drops
    public void onClickDropDefended(View v) {
        recordDrop(true);
        dropClick = false;
    }

    public void onClickDropNotDefended(View v) {
        recordDrop(false);
        dropClick = false;
    }

    public void onClickCancelDrop(View v) {
        popup_drop_defense.dismiss();
        if (!tb_defense.isChecked()) {
            pw = true;
        }

        dropClick = false;
    }

    public void recordDrop(Boolean dropWasDefended) {
        //Allow user to input next intake.
        mode = "intake";
        modeIsIntake = true;
        pw = true;

        btn_drop.setEnabled(false);

        popup_drop_defense.dismiss();

        // Make preload disabled after you've dropped.
        if (!tele) {
            preloadEnabled(false);
        }

        overallLayout.removeView(iv_game_element);

        mapChange();

        undoDicAdder(x, y, "drop");


        //Record drop action in mRealTimeMatchData to be compressed.
        compressionDic = new JSONObject();

        try {
            compressionDic.put("type", "drop");
            timestamp(TimerUtil.timestamp);
            compressionDic.put("wasDefended", dropWasDefended);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRealTimeMatchData.put(compressionDic);
    }

    //Record fouls and make foul counter go up
    public void onClickFoul(View v) throws JSONException {
        InputManager.numFoul++;
        btn_foul.setText("PIN FOUL - " + InputManager.numFoul);
        compressionDic = new JSONObject();
        try {
            compressionDic.put("type", "foul");
            timestamp(TimerUtil.timestamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRealTimeMatchData.put(compressionDic);
    }

    public void onClickUndo(View v) {
        // Re-enable preload when you undo your first action.
        if (!tele && actionCount <= 1) {
            preloadEnabled(true);
        }

        dismissPopups();
        pw = true;
        int index = -1;
        for (int i = 0; i < mRealTimeMatchData.length(); i++) {
            try {
                String hf = mRealTimeMatchData.getString(i);
                if (hf.contains("intake") || hf.contains("placement") || hf.contains("drop") || hf.contains("incap") || hf.contains("unincap") || hf.contains("startDefense") || hf.contains("endDefense")) {
                    index = i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.e("indexVals", String.valueOf(index));
        Log.e("mRealTimeMatchDataVals", mRealTimeMatchData.toString());

        //Remove previous object from mRealTimeMatchData
        mRealTimeMatchData.remove(index);
        if (actionCount > 0) {

            actionCount = actionCount - 1;

            if (actionDic.get(actionCount).get(3).equals("cargo")) {
                element = String.valueOf(actionDic.get(actionCount).get(3));
                if (actionDic.get(actionCount).get(2).equals("intake")) {
                    undoGeneric(true, false, "placement");

                } else if (actionDic.get(actionCount).get(2).equals("placement")) {
                    undoGeneric(false, true, "intake");
                }

            } else if (actionDic.get(actionCount).get(3).equals("panel")) {
                element = String.valueOf(actionDic.get(actionCount).get(3));
                if (actionDic.get(actionCount).get(2).equals("intake")) {
                    undoGeneric(true, false, "placement");

                } else if (actionDic.get(actionCount).get(2).equals("placement")) {
                    undoGeneric(false, true, "intake");
                }
            } else if (actionDic.get(actionCount).get(3).equals("drop")) {
                btn_drop.setEnabled(true);
                modeIsIntake = false;
                mode = "placement";
            } else if (actionDic.get(actionCount).get(3).equals("incap")) {
                pw = true;
                if (mode.equals("placement")) {
                    btn_drop.setEnabled(true);
                }

                if (!tele) {
                    tb_defense.setEnabled(false);
                    tb_hab_run.setEnabled(true);
                    btn_climb.setEnabled(false);
                } else if (tele) {
                    tb_defense.setEnabled(true);
                    tb_defense.bringToFront();
                    if (!tb_defense.isChecked()) {
                        btn_foul.setEnabled(true);
                        btn_climb.setEnabled(true);
                    }
                }
                tb_incap.setChecked(false);
            } else if (actionDic.get(actionCount).get(3).equals("unincap")) {
                btn_climb.setEnabled(false);
                btn_drop.setEnabled(false);
                btn_foul.setEnabled(false);
                tb_defense.setEnabled(false);
                tb_incap.setChecked(true);

                pw = false;

            } else if (actionDic.get(actionCount).get(3).equals("defense")) {
                btn_climb.setEnabled(true);
                pw = true;
                tb_defense.setChecked(false);

                //Remove Cycles Defended tracker from UI.
                btn_cyclesDefended.setEnabled(false);
                btn_cyclesDefended.setVisibility(View.INVISIBLE);

            } else if (actionDic.get(actionCount).get(3).equals("undefense")) {
                dismissPopups();
                pw = true;
                btn_climb.setEnabled(false);
                tb_defense.setChecked(true);

                //Show Cycles Defended tracker in UI and keep previous Cycles Defended.
                btn_cyclesDefended.setEnabled(true);
                btn_cyclesDefended.setVisibility(View.VISIBLE);
                btn_cyclesDefended.bringToFront();
            }
            actionDic.remove(actionCount);
            mapChange();
        } else if (actionCount == 0) {
            actionDic.remove(actionCount);
            preload();
        }
        btn_undo.setEnabled(false);
        didUndoOnce = true;
    }

    //Method that changes intake status, game mode, and the previous game element
    public void undoGeneric(Boolean btndrop, Boolean intakeVal, String modeGeneric) {
        btn_drop.setEnabled(btndrop);
        modeIsIntake = intakeVal;
        mode = modeGeneric;
        overallLayout.removeView(iv_game_element);
    }

    //Set climb attempted value to whatever is selected by user
    public void climbAttemptEdit(Button spaceValue, Integer space) {
        if (climbInputted) {
            if (climbAttemptValues.get(space) == 0) {
                spaceValue.setText("None");
            } else {
                spaceValue.setText(climbAttemptValues.get(space).toString());
            }
        }
    }

    //Set climb actual value to whatever is selected by user
    public void climbActualEdit(Button spaceValue, Integer space) {
        if (climbInputted) {
            if (climbActualValues.get(space) == 0) {
                spaceValue.setText("None");
            } else {
                spaceValue.setText(climbActualValues.get(space).toString());
            }
        }
    }


    public void onClickClimb(View v) {
        time = TimerUtil.timestamp;
        dismissPopups();
        pw = true;

        final Float climbStartTime = TimerUtil.timestamp;
        //Open climb dialog
        final Dialog climbDialog = new Dialog(this);
        climbDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (isMapLeft) {
            dialogLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.map_dialog_climb_left, null);
        } else {
            dialogLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.main_dialog_climb_right, null);
        }
        climbDialog.setCanceledOnTouchOutside(false);
        climbDialog.setCancelable(false);

        zeroI = (Button) dialogLayout.findViewById(R.id.zero);
        oneI = (Button) dialogLayout.findViewById(R.id.one);
        twoAI = (Button) dialogLayout.findViewById(R.id.twoa);
        threeI = (Button) dialogLayout.findViewById(R.id.three);
        twoBI = (Button) dialogLayout.findViewById(R.id.twob);
        zeroII = (Button) dialogLayout.findViewById(R.id.zeroII);
        oneII = (Button) dialogLayout.findViewById(R.id.oneII);
        twoAII = (Button) dialogLayout.findViewById(R.id.twoaII);
        threeII = (Button) dialogLayout.findViewById(R.id.threeII);
        twoBII = (Button) dialogLayout.findViewById(R.id.twobII);
        spaceOneI = (Button) dialogLayout.findViewById(R.id.SpaceOne);
        spaceTwoI = (Button) dialogLayout.findViewById(R.id.SpaceTwo);
        spaceThreeI = (Button) dialogLayout.findViewById(R.id.SpaceThree);
        spaceOneII = (Button) dialogLayout.findViewById(R.id.SpaceOneII);
        spaceTwoII = (Button) dialogLayout.findViewById(R.id.SpaceTwoII);
        spaceThreeII = (Button) dialogLayout.findViewById(R.id.SpaceThreeII);
        final Button cancel = (Button) dialogLayout.findViewById(R.id.cancelButton);
        final Button done = (Button) dialogLayout.findViewById(R.id.doneButton);
        spaceChanger(spaceTwoI, spaceThreeI, spaceOneI);
        spaceChanger(spaceTwoII, spaceThreeII, spaceOneII);

        climbAttemptEdit(spaceOneI, 0);
        climbAttemptEdit(spaceTwoI, 1);
        climbAttemptEdit(spaceThreeI, 2);
        climbActualEdit(spaceOneII, 0);
        climbActualEdit(spaceTwoII, 1);
        climbActualEdit(spaceThreeII, 2);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                climbActualCounter = 0;
                climbAttemptCounter = 0;
                climbDialog.dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spaceOneI.getText().toString().equals(" ") || spaceTwoI.getText().toString().equals(" ") || spaceThreeI.getText().toString().equals(" ") || spaceOneII.getText().toString().equals(" ") || spaceTwoII.getText().toString().equals(" ") || spaceThreeII.getText().toString().equals(" ")) {
                    Toast.makeText(getBaseContext(), "Make Sure You Filled Out All Of The Information!",
                            Toast.LENGTH_SHORT).show();
                } else if (spaceOneI.getText().toString().equals("None") && spaceTwoI.getText().toString().equals("None") && spaceThreeI.getText().toString().equals("None") && spaceOneII.getText().toString().equals("None") && spaceTwoII.getText().toString().equals("None") && spaceThreeII.getText().toString().equals("None")) {
                    climbInputted = false;

                    //Remove previous climb input if scout inputs all "None"
                    for (int i = 0; i < mRealTimeMatchData.length(); i++) {
                        try {
                            String matchDataIndexString = mRealTimeMatchData.getString(i);
                            if (matchDataIndexString.contains("climb")) {
                                mRealTimeMatchData.remove(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    climbDialog.dismiss();
                } else {
                    //Record Climb data
                    dataListSendAttempt(spaceOneI.getText().toString(), spaceOneI, 0);
                    dataListSendAttempt(spaceTwoI.getText().toString(), spaceTwoI, 1);
                    dataListSendAttempt(spaceThreeI.getText().toString(), spaceThreeI, 2);
                    dataListSendActual(spaceOneII.getText().toString(), spaceOneII, 0);
                    dataListSendActual(spaceTwoII.getText().toString(), spaceTwoII, 1);
                    dataListSendActual(spaceThreeII.getText().toString(), spaceThreeII, 2);
                    Log.e("cimbAttemptVals", climbAttemptValues.toString());
                    Log.e("climbActualVals", climbActualValues.toString());

                    if (climbInputted) {
                        int index = -1;
                        for (int i = 0; i < mRealTimeMatchData.length(); i++) {
                            try {
                                String hf = mRealTimeMatchData.getString(i);
                                if (hf.contains("climb")) {
                                    index = i;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        mRealTimeMatchData.remove(index);

                    }

                    recordClimb(climbStartTime);
                    climbActualCounter = 0;
                    climbAttemptCounter = 0;
                    climbDialog.dismiss();
                }
            }
        });

        climbDialog.setContentView(dialogLayout);
        climbDialog.show();

    }

    //Record different climb values
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

    public void onClickspaceOneI(View v) {
        climbAttemptCounter = 0;
        spaceChanger(spaceTwoI, spaceThreeI, spaceOneI);
    }

    public void onClickspaceTwoI(View v) {
        climbAttemptCounter = 1;
        spaceChanger(spaceOneI, spaceThreeI, spaceTwoI);

    }

    public void onClickspaceThreeI(View v) {
        climbAttemptCounter = 2;
        spaceChanger(spaceOneI, spaceTwoI, spaceThreeI);

    }

    public void onClickspaceOneII(View v) {
        climbActualCounter = 0;
        spaceChanger(spaceTwoII, spaceThreeII, spaceOneII);
    }

    public void onClickspaceTwoII(View v) {
        climbActualCounter = 1;
        spaceChanger(spaceOneII, spaceThreeII, spaceTwoII);

    }

    public void onClickspaceThreeII(View v) {
        climbActualCounter = 2;
        spaceChanger(spaceOneII, spaceTwoII, spaceThreeII);
    }

    public void spaceChanger(Button whiteA, Button whiteB, Button Yellow) {
        whiteA.setBackgroundColor(Color.WHITE);
        whiteB.setBackgroundColor(Color.WHITE);
        Yellow.setBackgroundColor(Color.parseColor("#f6ff6a"));
    }

    public void climbValuesAttempt(String buttonNumber, Integer climbintA) {
        if (climbintA % 3 == 0) {
            spaceOneI.setText(buttonNumber);
            spaceChanger(spaceOneI, spaceThreeI, spaceTwoI);

        } else if (climbintA % 3 == 1) {
            spaceTwoI.setText(buttonNumber);
            spaceChanger(spaceOneI, spaceTwoI, spaceThreeI);

        } else if (climbintA % 3 == 2) {
            spaceThreeI.setText(buttonNumber);
            spaceChanger(spaceTwoI, spaceThreeI, spaceOneI);
        }
    }

    public void climbValuesActual(String buttonNumber, Integer climbintB) {
        if (climbintB % 3 == 0) {
            spaceOneII.setText(buttonNumber);
            spaceChanger(spaceOneII, spaceThreeII, spaceTwoII);
        } else if (climbintB % 3 == 1) {
            spaceTwoII.setText(buttonNumber);
            spaceChanger(spaceOneII, spaceTwoII, spaceThreeII);
        } else if (climbintB % 3 == 2) {
            spaceThreeII.setText(buttonNumber);
            spaceChanger(spaceTwoII, spaceThreeII, spaceOneII);
        }
    }

    //Save climb attempt values
    public void dataListSendAttempt(String buttonvalue, Button spaceValueAttempt, Integer listID) {
        if (!climbInputted) {
            if (buttonvalue.equals("None")) {
                climbAttemptValues.add(0);
            } else {
                climbAttemptValues.add(Integer.valueOf(spaceValueAttempt.getText().toString()));
            }
        } else {
            if (buttonvalue.equals("None")) {
                climbAttemptValues.set(listID, 0);
            } else {
                climbAttemptValues.set(listID, Integer.valueOf(spaceValueAttempt.getText().toString()));
            }
        }
    }

    //Save climb actual values
    public void dataListSendActual(String buttonvalue, Button spaceValueActual, Integer listID) {
        if (!climbInputted) {
            if (buttonvalue.equals("None")) {
                climbActualValues.add(0);
            } else {
                climbActualValues.add(Integer.valueOf(spaceValueActual.getText().toString()));
            }
        } else {
            if (buttonvalue.equals("None")) {
                climbActualValues.set(listID, 0);
            } else {
                climbActualValues.set(listID, Integer.valueOf(spaceValueActual.getText().toString()));
            }
        }
    }

    public void onClickIncap(View v) {

        //If incap is checked, disable buttons
        if (tb_incap.isChecked()) {
            dismissPopups();

            btn_climb.setEnabled(false);
            btn_drop.setEnabled(false);
            btn_foul.setEnabled(false);
            tb_defense.setEnabled(false);

            if (!tele) {
                tb_hab_run.setEnabled(false);
            }

            pw = false;

            compressionDic = new JSONObject();

            try {
                compressionDic.put("type", "incap");
                timestamp(TimerUtil.timestamp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mRealTimeMatchData.put(compressionDic);

            undoDicAdder("NA", "NA", "incap");

        }

        //If incap isn't checked, re-enable buttons
        else if (!tb_incap.isChecked()) {
            undoDicAdder("NA", "NA", "unincap");
            pw = true;

            btn_foul.setEnabled(true);

            if (!tele) {
                tb_hab_run.setEnabled(true);
            } else {
                if (!tb_defense.isChecked()) {
                    btn_climb.setEnabled(true);
                }
                tb_defense.setEnabled(true);
            }

            compressionDic = new JSONObject();

            try {
                compressionDic.put("type", "unincap");
                timestamp(TimerUtil.timestamp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mRealTimeMatchData.put(compressionDic);

            if (mode.equals("placement")) {
                btn_drop.setEnabled(true);
            }
        }
        mapChange();
        Log.i("mRealTimeMatchDataVal", mRealTimeMatchData.toString());
    }

    public void onClickDefense(View v) {
        btn_climb = (Button) findViewById(R.id.btn_climb);
        if (tb_defense.isChecked()) {
            dismissPopups();
            pw = true;
            btn_climb.setEnabled(false);

            //Show Cycles Defended tracker in UI.
            InputManager.cyclesDefended = 0;
            btn_cyclesDefended.setEnabled(true);
            btn_cyclesDefended.setVisibility(View.VISIBLE);
            btn_cyclesDefended.bringToFront();
            btn_cyclesDefended.setText("FAILED PLACEMENTS/DROPS CAUSED - 0");

            compressionDic = new JSONObject();

            try {
                compressionDic.put("type", "startDefense");
                timestamp(TimerUtil.timestamp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mRealTimeMatchData.put(compressionDic);

            mapChange();
            undoDicAdder("NA", "NA", "defense");
        } else if (!tb_defense.isChecked()) {
            btn_climb.setEnabled(true);

            //Remove Cycles Defended tracker from UI.
            btn_cyclesDefended.setEnabled(false);
            btn_cyclesDefended.setVisibility(View.INVISIBLE);

            pw = true;

            compressionDic = new JSONObject();

            try {
                compressionDic.put("type", "endDefense");
                compressionDic.put("cyclesDefended", InputManager.cyclesDefended);
                timestamp(TimerUtil.timestamp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mRealTimeMatchData.put(compressionDic);

            mapChange();
            undoDicAdder("NA", "NA", "undefense");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTouchListener() {
        overallLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (pw && timerCheck) {
                        x = (int) motionEvent.getX();
                        y = (int) motionEvent.getY();
                        Log.e("Xcoordinate", String.valueOf(x));
                        Log.e("Ycoordinate", String.valueOf(y));

                        //Set coordinates of map based on tablet type
                        if (!((((x > 1700 || y > 985) && mTabletType.equals("green")) || ((x > 1130 || y > 660) && mTabletType.equals("black")) || ((x > 850 || y > 490) && mTabletType.equals("fire")))
                                || ((((field_orientation.contains("left") && x < 225) || (field_orientation.contains("right") && x > 1440)) && y > 415 && y < 615 && mTabletType.equals("green"))
                                || ((field_orientation.contains("left") && x < 175) || (field_orientation.contains("right") && x > 955)) && y > 280 && y < 410 && mTabletType.equals("black")))
                                || ((field_orientation.contains("left") && x < 130) || (field_orientation.contains("right") && x > 720)) && y > 210 && y < 305 && mTabletType.equals("fire")) {
                            //Set screen as unclickable during intake mode and initialize popup
                            if (mode.equals("intake") && !tb_defense.isChecked()) {
                                pw = false;
                                modeIsIntake = true;
                                initPopup(popup);
                            } else if (mode.equals("intake") && tb_defense.isChecked()) {
                                pw = true;
                                initPopup(popup);
                            }
                            //Set coordinates of rocket and cargo ship depending on tablet type
                            else if (mode.equals("placement") && !tb_defense.isChecked() && ((y > -4.5 * x + 4457.5 && y > 4.5 * x - 4182.5 && y > 700 && field_orientation.contains("right") && mTabletType.equals("green"))
                                    || (y < 4.5 * x - 3405 && y < -4.5 * x + 5212.5 && y < 330 && field_orientation.contains("right") && mTabletType.equals("green"))
                                    || (y > -4.5 * x + 3467.5 && y > 4.5 * x - 3585 && y > 700 && field_orientation.contains("left") && mTabletType.equals("green"))
                                    || (y < 4.5 * x - 2437.5 && y < -4.5 * x + 4245 && y < 330 && field_orientation.contains("left") && mTabletType.equals("green"))
                                    || (y > -4.625 * x + 3031.875 && y > 4.625 * x - 2865 && y > 465 && field_orientation.contains("right") && mTabletType.equals("black"))
                                    || (y < 4.625 * x - 2346.875 && y < -4.625 * x + 3550 && y < 220 && field_orientation.contains("right") && mTabletType.equals("black"))
                                    || (y > -4.625 * x + 2361.25 && y > 4.625 * x - 2217.5 && y > 465 && field_orientation.contains("left") && mTabletType.equals("black"))
                                    || (y < 4.625 * x - 1671.25 && y < -4.625 * x + 2907.5 && y < 220 && field_orientation.contains("left") && mTabletType.equals("black"))
                                    || (y > -4.5 * x + 2217.5 && y > 4.5 * x - 2093.5 && y > 350 && field_orientation.contains("right") && mTabletType.equals("fire"))
                                    || (y < 4.5 * x - 1702.5 && y < -4.5 * x + 2608.5 && y < 165 && field_orientation.contains("right") && mTabletType.equals("fire"))
                                    || (y > -4.5 * x + 1722.5 && y > 4.5 * x - 1607.5 && y > 350 && field_orientation.contains("left") && mTabletType.equals("fire"))
                                    || (y < 4.5 * x - 1207.5 && y < -4.5 * x + 2122.5 && y < 165 && field_orientation.contains("left") && mTabletType.equals("fire"))
                                    || (((field_orientation.contains("left") && x > 950 && x < 1445) || (field_orientation.contains("right") && x > 255 && x < 760)) && y > 335 && y < 700 && mTabletType.equals("green"))
                                    || (((field_orientation.contains("left") && x > 625 && x < 960) || (field_orientation.contains("right") && x > 170 && x < 505)) && y > 225 && y < 465 && mTabletType.equals("black"))
                                    || (((field_orientation.contains("left") && x > 470 && x < 720) || (field_orientation.contains("right") && x > 130 && x < 380)) && y > 165 && y < 350 && mTabletType.equals("fire")))) {
                                pw = false;
                                initPlacement();
                                modeIsIntake = false;
                                placementDialogOpen = true;
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    public void onClickCargo(View view) {
        initIntake("cargo");
        // Make preload disabled after intaking
        if (!tele) {
            preloadEnabled(false);
        }
    }

    public void onClickPanel(View view) {
        initIntake("panel");
        // Make preload disabled after intaking
        if (!tele) {
            preloadEnabled(false);
        }
    }

    //Dismiss popups and set screen as clickable when cancel is clicked
    public void onClickCancel(View view) {
        popup.dismiss();
        pw = true;
    }

    //When fail is clicked, set mode to intake and set screen as clickable
    public void onClickFail(View view) {
        overallLayout.removeView(iv_game_element);

        mode = "intake";
        modeIsIntake = true;

        pw = true;
        recordLoadingStation(false);

        mapChange();

        popup_fail_success.dismiss();
    }

    //When an intake is successful, enable and disable certain buttons and set mode to placement
    public void onClickSuccess(View view) {
        if (mode.equals("intake")) {
            mode = "placement";
            modeIsIntake = false;
            btn_drop.setEnabled(true);
        } else if (mode.equals("placement")) {
            mode = "intake";
            modeIsIntake = true;
            btn_drop.setEnabled(false);
        }
        recordLoadingStation(true);

        initShape();

        popup_fail_success.dismiss();
    }

    public void onClickCancelFS(View view) {
        popup_fail_success.dismiss();
        pw = true;
    }

    //Add timestamp to objects in mRealTimeMatchData
    public void timestamp(Float givenTime) {
        if ((givenTime <= 135 && !tele) || (givenTime > 135 && tele)) {
            try {
                compressionDic.put("time", String.format("%.1f", givenTime) + "*");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                compressionDic.put("time", String.format("%.1f", givenTime));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void recordLoadingStation(boolean didSucceed) {
        compressionDic = new JSONObject();
        //Add loading station intakes to mRealTimeMatchData
        try {
            compressionDic.put("type", "intake");
            timestamp(time);
            compressionDic.put("piece", element);
            compressionDic.put("zone", zone);
            compressionDic.put("didSucceed", didSucceed);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRealTimeMatchData.put(compressionDic);

    }

    public void recordPlacement() {
        if (fail.isChecked()) {
            didSucceed = false;
        } else if (success.isChecked()) {
            didSucceed = true;
        }

        wasDefended = tb_wasDefended.isChecked();

        compressionDic = new JSONObject();

        try {
            compressionDic.put("type", "placement");
            timestamp(time);
            compressionDic.put("piece", element);
            compressionDic.put("didSucceed", didSucceed);
            compressionDic.put("wasDefended", wasDefended);
            compressionDic.put("structure", structure);

            if ((structure.contains("Rocket") && element.equals("panel")) || structure.equals("cargoShip")) {
                compressionDic.put("side", side);
            }
            //Record different levels of rocket
            if (structure.contains("Rocket")) {
                if (level1.isChecked()) {
                    level = 1;
                } else if (level2.isChecked()) {
                    level = 2;
                } else if (level3.isChecked()) {
                    level = 3;
                }
                if (!didSucceed && (tb_shotOutOfField.isEnabled())) {
                    shotOutOfField = tb_shotOutOfField.isChecked();
                    compressionDic.put("shotOutOfField", shotOutOfField);
                }
                compressionDic.put("level", level);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRealTimeMatchData.put(compressionDic);

        Log.i("mRealTimeMatchDataVals?", mRealTimeMatchData.toString());
    }

    //Set map drawable based user mode
    public void mapChange() {
        if (element.equals("cargo") && !tb_incap.isChecked() && !tb_defense.isChecked()) {
            iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_cargo));
            if (mode.equals("placement")) {
                iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_cargo));

                if (field_orientation.contains("left")) {
                    iv_field.setImageResource(R.drawable.map_field_placement_cargo_left);
                } else if (field_orientation.contains("right")) {
                    iv_field.setImageResource(R.drawable.map_field_placement_cargo_right);
                }
            }
        } else if (element.equals("panel") && !tb_incap.isChecked() && !tb_defense.isChecked()) {
            iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_panel));
            if (mode.equals("placement")) {
                if (field_orientation.contains("left")) {
                    iv_field.setImageResource(R.drawable.map_field_placement_panel_left);
                } else if (field_orientation.contains("right")) {
                    iv_field.setImageResource(R.drawable.map_field_placement_panel_right);
                }
            }
        }
        if (mode.equals("intake") && !tb_incap.isChecked() && !tb_defense.isChecked()) {
            btn_drop.setEnabled(false);
            if (field_orientation.equals("blue_left")) {
                iv_field.setImageResource(R.drawable.map_field_intake_blue_left);
            } else if (field_orientation.equals("blue_right")) {
                iv_field.setImageResource(R.drawable.map_field_intake_blue_right);
            } else if (field_orientation.equals("red_left")) {
                iv_field.setImageResource(R.drawable.map_field_intake_red_left);
            } else if (field_orientation.equals("red_right")) {
                iv_field.setImageResource(R.drawable.map_field_intake_red_right);
            }
        }
        if (tb_incap.isChecked()) {
            if (field_orientation.contains("right")) {
                iv_field.setImageResource(R.drawable.map_field_incap_right);
            } else if (field_orientation.contains("left")) {
                iv_field.setImageResource(R.drawable.map_field_incap_left);
            }
        } else if (tb_defense.isChecked()) {
            if (mode.equals("intake")) {
                if (field_orientation.equals("blue_left")) {
                    iv_field.setImageResource(R.drawable.map_field_defense_blue_left);
                } else if (field_orientation.equals("blue_right")) {
                    iv_field.setImageResource(R.drawable.map_field_defense_blue_right);
                } else if (field_orientation.equals("red_left")) {
                    iv_field.setImageResource(R.drawable.map_field_defense_red_left);
                } else if (field_orientation.equals("red_right")) {
                    iv_field.setImageResource(R.drawable.map_field_defense_red_right);
                }
            }
            if (element.equals("cargo")) {
                iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_cargo));
                if (mode.equals("placement")) {
                    iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_cargo));
                    if (field_orientation.contains("left")) {
                        iv_field.setImageResource(R.drawable.map_field_defense_placement_cargo_left);
                    } else if (field_orientation.contains("right")) {
                        iv_field.setImageResource(R.drawable.map_field_defense_placement_cargo_right);
                    }
                }
            }
            if (element.equals("panel")) {
                iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_panel));
                if (mode.equals("placement")) {
                    if (field_orientation.contains("left")) {
                        iv_field.setImageResource(R.drawable.map_field_defense_placement_panel_left);
                    } else if (field_orientation.contains("right")) {
                        iv_field.setImageResource(R.drawable.map_field_defense_placement_panel_right);
                    }
                }
            }
        }
    }

    public void initShape() {
        pw = true;
        overallLayout.removeView(iv_game_element);

        //Set cargo image on screen if element is cargo
        if (element.equals("cargo")) {
            iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_cargo));
            undoDicAdder(x, y, "cargo");

            //Set hatch panel image on screen if element is panel
        } else if (element.equals("panel")) {
            iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.map_indicator_panel));
            undoDicAdder(x, y, "panel");

        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                100,
                100);
        //Set location of game element based on coordinates and tablet type
        if ((y > 900 && mTabletType.equals("green")) || (y > 550 && mTabletType.equals("black"))) {
            if ((x < 40 && mTabletType.equals("green")) || (x < 25 && mTabletType.equals("black"))) {
                lp.setMargins(x + 20, y - 90, 0, 0);
            } else if ((x > 1650 && mTabletType.equals("green")) || (x > 1090 && mTabletType.equals("black"))) {
                lp.setMargins(x - 100, y - 90, 0, 0);
            } else {
                lp.setMargins(x - 25, y - 90, 0, 0);
            }
        } else if ((y < 85 && mTabletType.equals("green")) || (y < 55 && mTabletType.equals("black"))) {
            if ((x < 40 && mTabletType.equals("green")) || (x < 25 && mTabletType.equals("black"))) {
                lp.setMargins(x + 20, y + 5, 0, 0);
            } else if ((x > 1650 && mTabletType.equals("green")) || (x > 1090 && mTabletType.equals("black"))) {
                lp.setMargins(x - 100, y + 5, 0, 0);
            } else {
                lp.setMargins(x - 25, y + 5, 0, 0);
            }
        } else if ((x < 40 && mTabletType.equals("green")) || (x < 25 && mTabletType.equals("black"))) {
            lp.setMargins(x + 20, y - 40, 0, 0);
        } else if ((x > 1650 && mTabletType.equals("green")) || (x > 1090 && mTabletType.equals("black"))) {
            lp.setMargins(x - 100, y - 40, 0, 0);
        } else {
            lp.setMargins(x - 25, y - 40, 0, 0);
        }
        iv_game_element.setLayoutParams(lp);
        ((ViewGroup) overallLayout).addView(iv_game_element);

        mapChange();
    }

    public void initIntake(String givenElement) {
        zone = "";

        time = TimerUtil.timestamp;
        dismissPopups();
        element = givenElement;

        //Set coordinates for loading stations based on tablet types and field orientation
        if (((((field_orientation.contains("left") && x < 225) || (field_orientation.contains("right") && x > 1440)) && mTabletType.equals("green"))
                || (((field_orientation.contains("left") && x < 175) || (field_orientation.contains("right") && x > 955)) && mTabletType.equals("black"))
                || (((field_orientation.contains("left") && x < 130) || (field_orientation.contains("right") && x > 720)) && mTabletType.equals("fire")))) {
            if ((((field_orientation.contains("left") && y <= 415) || (field_orientation.contains("right") && y >= 615)) && mTabletType.equals("green"))
                    || (((field_orientation.contains("left") && y <= 280) || (field_orientation.contains("right") && y >= 410)) && mTabletType.equals("black"))
                    || (((field_orientation.contains("left") && y <= 220) || (field_orientation.contains("right") && y >= 280)) && mTabletType.equals("fire"))) {
                zone = "leftLoadingStation";
            } else if ((((field_orientation.contains("right") && y <= 415) || (field_orientation.contains("left") && y >= 615)) && mTabletType.equals("green"))
                    || (((field_orientation.contains("right") && y <= 280) || (field_orientation.contains("left") && y >= 410)) && mTabletType.equals("black"))
                    || (((field_orientation.contains("right") && y <= 220) || (field_orientation.contains("left") && y >= 280)) && mTabletType.equals("fire"))) {
                zone = "rightLoadingStation";
            }
            initPopup(popup_fail_success);
            //Set coordinates for zones based on tablet types
        } else {
            if ((y > 517 && mTabletType.equals("green") && field_orientation.contains("left")) || (y <= 517 && mTabletType.equals("green") && field_orientation.contains("right"))
                    || (y > 345 && mTabletType.equals("black") && field_orientation.contains("left")) || (y <= 345 && mTabletType.equals("black") && field_orientation.contains("right"))
                    || (y > 260 && mTabletType.equals("fire") && field_orientation.contains("left")) || (y <= 260 && mTabletType.equals("fire") && field_orientation.contains("right"))) {
                if ((((field_orientation.contains("left") && x <= 540 && x >= 225) || (field_orientation.contains("right") && x >= 1160 && x <= 1440)) && mTabletType.equals("green"))
                        || (((field_orientation.contains("left") && x <= 360 && x >= 175) || (field_orientation.contains("right") && x >= 955 && x <= 955)) && mTabletType.equals("black"))
                        || (((field_orientation.contains("left") && x <= 270 && x >= 130) || (field_orientation.contains("right") && x >= 580 && x <= 720)) && mTabletType.equals("fire"))) {
                    zone = "zone1Right";
                } else if ((((field_orientation.contains("left") && x <= 940 && x > 540) || (field_orientation.contains("right") && x >= 760 && x < 1160)) && mTabletType.equals("green"))
                        || (((field_orientation.contains("left") && x <= 625 && x > 360) || (field_orientation.contains("right") && x >= 500 && x < 955)) && mTabletType.equals("black"))
                        || (((field_orientation.contains("left") && x <= 470 && x > 270) || (field_orientation.contains("right") && x >= 380 && x < 580)) && mTabletType.equals("fire"))) {
                    zone = "zone2Right";
                } else if ((((field_orientation.contains("left") && x <= 1445 && x > 940) || (field_orientation.contains("right") && x >= 255 && x < 760)) && mTabletType.equals("green"))
                        || (((field_orientation.contains("left") && x <= 960 && x > 625) || (field_orientation.contains("right") && x >= 170 && x < 500)) && mTabletType.equals("black"))
                        || (((field_orientation.contains("left") && x <= 720 && x > 270) || (field_orientation.contains("right") && x >= 125 && x < 380)) && mTabletType.equals("fire"))) {
                    zone = "zone3Right";
                } else if ((((field_orientation.contains("left") && x > 1445) || (field_orientation.contains("right") && x < 255)) && mTabletType.equals("green"))
                        || (((field_orientation.contains("left") && x > 960) || (field_orientation.contains("right") && x < 170)) && mTabletType.equals("black"))
                        || (((field_orientation.contains("left") && x > 720) || (field_orientation.contains("right") && x < 125)) && mTabletType.equals("fire"))) {
                    zone = "zone4Right";
                }
            } else {
                if ((((field_orientation.contains("left") && x <= 540 && x >= 225) || (field_orientation.contains("right") && x >= 1160 && x <= 1440)) && mTabletType.equals("green"))
                        || (((field_orientation.contains("left") && x <= 360 && x >= 175) || (field_orientation.contains("right") && x >= 955 && x <= 955)) && mTabletType.equals("black"))
                        || (((field_orientation.contains("left") && x <= 270 && x >= 130) || (field_orientation.contains("right") && x >= 580 && x <= 720)) && mTabletType.equals("fire"))) {
                    zone = "zone1Left";
                } else if ((((field_orientation.contains("left") && x <= 940 && x > 540) || (field_orientation.contains("right") && x >= 760 && x < 1160)) && mTabletType.equals("green"))
                        || (((field_orientation.contains("left") && x <= 625 && x > 360) || (field_orientation.contains("right") && x >= 500 && x < 955)) && mTabletType.equals("black"))
                        || (((field_orientation.contains("left") && x <= 470 && x > 270) || (field_orientation.contains("right") && x >= 380 && x < 580)) && mTabletType.equals("fire"))) {
                    zone = "zone2Left";
                } else if ((((field_orientation.contains("left") && x <= 1445 && x > 940) || (field_orientation.contains("right") && x >= 255 && x < 760)) && mTabletType.equals("green"))
                        || (((field_orientation.contains("left") && x <= 960 && x > 625) || (field_orientation.contains("right") && x >= 170 && x < 500)) && mTabletType.equals("black"))
                        || (((field_orientation.contains("left") && x <= 720 && x > 270) || (field_orientation.contains("right") && x >= 125 && x < 380)) && mTabletType.equals("fire"))) {
                    zone = "zone3Left";
                } else if ((((field_orientation.contains("left") && x > 1445) || (field_orientation.contains("right") && x < 255)) && mTabletType.equals("green"))
                        || (((field_orientation.contains("left") && x > 960) || (field_orientation.contains("right") && x < 170)) && mTabletType.equals("black"))
                        || (((field_orientation.contains("left") && x > 720) || (field_orientation.contains("right") && x < 125)) && mTabletType.equals("fire"))) {
                    zone = "zone4Left";
                }
            }

            if (givenElement.equals("cargo") || givenElement.equals("panel")) {
                mode = "placement";
                modeIsIntake = false;
                btn_drop.setEnabled(true);

                compressionDic = new JSONObject();

                try {
                    compressionDic.put("type", "intake");
                    timestamp(time);
                    compressionDic.put("piece", element);
                    compressionDic.put("zone", zone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mRealTimeMatchData.put(compressionDic);

                Log.i("mRealTimeMatchDataVals", mRealTimeMatchData.toString());

                initShape();
            }
        }
    }

    public void preload() {

        if (InputManager.mPreload.equals("cargo") || InputManager.mPreload.equals("panel")) {

            //Set whether drop is enabled based on whether or not incap is checked

            if (!startTimer) {
                if (tb_incap.isChecked()) {
                    btn_drop.setEnabled(false);
                } else {
                    btn_drop.setEnabled(true);
                }
            }
            if (InputManager.mPreload.equals("cargo")) {
                element = "cargo";
            } else if (InputManager.mPreload.equals("panel")) {
                element = "panel";
            }
            mode = "placement";
            modeIsIntake = false;
            startedWObject = true;
            mapChange();
            startedWObject = false;
        } else if (InputManager.mPreload.equals("none")) {
            mode = "intake";
            modeIsIntake = true;
            btn_drop.setEnabled(false);
            element = "none";
            startedWObject = false;
            mapChange();
        }
    }

    public void initPlacement() {
        placementDialog = new Dialog(this);

        placementDialog.setCanceledOnTouchOutside(false);
        placementDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //ROCKET LEFT (bottom, right, green): y > -4.5 * x + 4457.5 && y > 4.5 * x - 4182.5 && y > 700 && field_orientation.contains("right") && mTabletType.equals("green")
        //ROCKET RIGHT (top, right, green): y < 4.5 * x - 3405 && y < -4.5 * x + 5212.5 && y < 330 && field_orientation.contains("right") && mTabletType.equals("green")

        //ROCKET RIGHT (bottom, left, green): y > -4.5 * x + 3467.5 && y > 4.5 * x - 3585 && y > 700 && field_orientation.contains("left") && mTabletType.equals("green")
        //ROCKET LEFT (top, left, green): y < 4.5 * x - 2437.5 && y < -4.5 * x + 4245 && y < 330 && field_orientation.contains("left") && mTabletType.equals("green")

        //ROCKET LEFT (bottom, right, black): y > -4.625 * x + 3031.875 && y > 4.625 * x - 2865 && y > 465 && field_orientation.contains("right") && mTabletType.equals("black")
        //ROCKET RIGHT (top, right, black): y < 4.625 * x - 2346.875 && y < -4.625 * x + 3550 && y < 220 && field_orientation.contains("right") && mTabletType.equals("black")

        //ROCKET RIGHT (bottom, left, black): y > -4.625 * x + 2361.25 && y > 4.625 * x - 2217.5 && y > 465 && field_orientation.contains("left") && mTabletType.equals("black")
        //ROCKET LEFT (top, left, black): y < 4.625 * x - 1671.25 && y < -4.625 * x + 2907.5 && y < 220 && field_orientation.contains("left") && mTabletType.equals("black")

        //ROCKET LEFT (bottom, right, fire): y > -4.5 * x + 2217.5 && y > 4.5 * x - 2093.5 && y > 350 && field_orientation.contains("right") && mTabletType.equals("fire")
        //ROCKET RIGHT (top, right, fire): y < 4.5 * x - 1702.5 && y < -4.5 * x + 2608.5 && y < 165 && field_orientation.contains("right") && mTabletType.equals("fire")

        //ROCKET RIGHT (bottom, left, fire): y > -4.5 * x + 1722.5 && y > 4.5 * x - 1607.5 && y > 350 && field_orientation.contains("left") && mTabletType.equals("fire")
        //ROCKET LEFT (top, left, fire): y < 4.5 * x - 1207.5 && y < -4.5 * x + 2122.5 && y < 165 && field_orientation.contains("left") && mTabletType.equals("fire")


        //Set locations of rockets based on tablet types
        if ((y > -4.5 * x + 4457.5 && y > 4.5 * x - 4182.5 && y > 700 && field_orientation.contains("right") && mTabletType.equals("green"))
                || (y < 4.5 * x - 3405 && y < -4.5 * x + 5212.5 && y < 330 && field_orientation.contains("right") && mTabletType.equals("green"))
                || (y > -4.5 * x + 3467.5 && y > 4.5 * x - 3585 && y > 700 && field_orientation.contains("left") && mTabletType.equals("green"))
                || (y < 4.5 * x - 2437.5 && y < -4.5 * x + 4245 && y < 330 && field_orientation.contains("left") && mTabletType.equals("green"))
                || (y > -4.625 * x + 3031.875 && y > 4.625 * x - 2865 && y > 465 && field_orientation.contains("right") && mTabletType.equals("black"))
                || (y < 4.625 * x - 2346.875 && y < -4.625 * x + 3550 && y < 220 && field_orientation.contains("right") && mTabletType.equals("black"))
                || (y > -4.625 * x + 2361.25 && y > 4.625 * x - 2217.5 && y > 465 && field_orientation.contains("left") && mTabletType.equals("black"))
                || (y < 4.625 * x - 1671.25 && y < -4.625 * x + 2907.5 && y < 220 && field_orientation.contains("left") && mTabletType.equals("black"))
                || (y > -4.5 * x + 2217.5 && y > 4.5 * x - 2093.5 && y > 350 && field_orientation.contains("right") && mTabletType.equals("fire"))
                || (y < 4.5 * x - 1702.5 && y < -4.5 * x + 2608.5 && y < 165 && field_orientation.contains("right") && mTabletType.equals("fire"))
                || (y > -4.5 * x + 1722.5 && y > 4.5 * x - 1607.5 && y > 350 && field_orientation.contains("left") && mTabletType.equals("fire"))
                || (y < 4.5 * x - 1207.5 && y < -4.5 * x + 2122.5 && y < 165 && field_orientation.contains("left") && mTabletType.equals("fire"))) {
            if ((y < 4.5 * x - 3405 && y < -4.5 * x + 5212.5 && y < 330 && field_orientation.contains("right") && mTabletType.equals("green"))
                    || (y > -4.5 * x + 3467.5 && y > 4.5 * x - 3585 && y > 700 && field_orientation.contains("left") && mTabletType.equals("green"))
                    || (y < 4.625 * x - 2346.875 && y < -4.625 * x + 3550 && y < 220 && field_orientation.contains("right") && mTabletType.equals("black"))
                    || (y > -4.625 * x + 2361.25 && y > 4.625 * x - 2217.5 && y > 465 && field_orientation.contains("left") && mTabletType.equals("black"))
                    || (y < 4.5 * x - 1702.5 && y < -4.5 * x + 2608.5 && y < 165 && field_orientation.contains("right") && mTabletType.equals("fire"))
                    || (y > -4.5 * x + 1722.5 && y > 4.5 * x - 1607.5 && y > 350 && field_orientation.contains("left") && mTabletType.equals("fire"))) {
                structure = "rightRocket";
            } else if ((y > -4.5 * x + 4457.5 && y > 4.5 * x - 4182.5 && y > 700 && field_orientation.contains("right") && mTabletType.equals("green"))
                    || (y < 4.5 * x - 2437.5 && y < -4.5 * x + 4245 && y < 330 && field_orientation.contains("left") && mTabletType.equals("green"))
                    || (y > -4.625 * x + 3031.875 && y > 4.625 * x - 2865 && y > 465 && field_orientation.contains("right") && mTabletType.equals("black"))
                    || (y < 4.625 * x - 1671.25 && y < -4.625 * x + 2907.5 && y < 220 && field_orientation.contains("left") && mTabletType.equals("black"))
                    || (y > -4.5 * x + 2217.5 && y > 4.5 * x - 2093.5 && y > 350 && field_orientation.contains("right") && mTabletType.equals("fire"))
                    || (y < 4.5 * x - 1207.5 && y < -4.5 * x + 2122.5 && y < 165 && field_orientation.contains("left") && mTabletType.equals("fire"))) {
                structure = "leftRocket";
            }
            //If element is panel, add different coordinates for different sides of each rocket
            if (element.equals("panel")) {
                if ((((x >= 740 && field_orientation.contains("left")) || (x <= 960 && field_orientation.contains("right"))) && mTabletType.equals("green"))
                        || (((x >= 493 && field_orientation.contains("left")) || (x <= 640 && field_orientation.contains("right"))) && mTabletType.equals("black"))
                        || (((x >= 370 && field_orientation.contains("left")) || (x <= 480 && field_orientation.contains("right"))) && mTabletType.equals("fire"))) {
                    side = "far";
                } else if ((((x < 740 && field_orientation.contains("left")) || (x > 960 && field_orientation.contains("right"))) && mTabletType.equals("green"))
                        || (((x < 493 && field_orientation.contains("left")) || (x > 640 && field_orientation.contains("right"))) && mTabletType.equals("black"))
                        || (((x < 480 && field_orientation.contains("left")) || (x > 370 && field_orientation.contains("right"))) && mTabletType.equals("fire"))) {
                    side = "near";
                }
            }
            placementDialogLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.map_placement_dialog_rocket, null);

            tb_shotOutOfField = placementDialogLayout.findViewById(R.id.shotOutOfField);

            level1 = placementDialogLayout.findViewById(R.id.radio_3);
            level2 = placementDialogLayout.findViewById(R.id.radio_2);
            level3 = placementDialogLayout.findViewById(R.id.radio_1);

            if (element.equals("panel")) {
                tb_shotOutOfField.setVisibility(View.INVISIBLE);

                level1.setBackgroundResource(R.drawable.map_placement_level_selector_panel);
                level2.setBackgroundResource(R.drawable.map_placement_level_selector_panel);
                level3.setBackgroundResource(R.drawable.map_placement_level_selector_panel);
            } else if (element.equals("cargo")) {
                tb_shotOutOfField.setBackgroundResource(R.drawable.map_placement_cargo_toggle_selector);

                level1.setBackgroundResource(R.drawable.map_placement_level_selector_cargo);
                level2.setBackgroundResource(R.drawable.map_placement_level_selector_cargo);
                level3.setBackgroundResource(R.drawable.map_placement_level_selector_cargo);
            }
            //Set coordinates of cargo ship
        } else if ((((field_orientation.contains("left") && x > 950 && x < 1445) || (field_orientation.contains("right") && x > 255 && x < 760)) && y > 335 && y < 700 && mTabletType.equals("green"))
                || (((field_orientation.contains("left") && x > 625 && x < 960) || (field_orientation.contains("right") && x > 170 && x < 505)) && y > 225 && y < 565 && mTabletType.equals("black"))
                || (((field_orientation.contains("left") && x > 470 && x < 720) || (field_orientation.contains("right") && x > 130 && x < 380)) && y > 165 && y < 350 && mTabletType.equals("fire"))) {
            structure = "cargoShip";
            if ((((field_orientation.contains("left") && x < 1130) || (field_orientation.contains("right") && x > 570)) && mTabletType.equals("green"))
                    || (((field_orientation.contains("left") && x < 750) || (field_orientation.contains("right") && x > 380)) && mTabletType.equals("black"))
                    || (((field_orientation.contains("left") && x < 565) || (field_orientation.contains("right") && x > 285)) && mTabletType.equals("fire"))) {
                side = "near";
            } else if ((((field_orientation.contains("left") && y < 517) || (field_orientation.contains("right") && y > 517)) && mTabletType.equals("green"))
                    || (((field_orientation.contains("left") && y < 345) || (field_orientation.contains("right") && y > 345)) && mTabletType.equals("black"))
                    || (((field_orientation.contains("left") && y < 260) || (field_orientation.contains("right") && y > 260)) && mTabletType.equals("fire"))) {
                side = "left";
            } else if ((((field_orientation.contains("left") && y >= 517) || (field_orientation.contains("right") && y <= 517)) && mTabletType.equals("green"))
                    || (((field_orientation.contains("left") && y >= 345) || (field_orientation.contains("right") && y <= 345)) && mTabletType.equals("black"))
                    || (((field_orientation.contains("left") && y >= 260) || (field_orientation.contains("right") && y <= 260)) && mTabletType.equals("fire"))) {
                side = "right";
            }
            placementDialogLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.map_placement_dialog_cargo_ship, null);
        }
        fail = placementDialogLayout.findViewById(R.id.fail);
        success = placementDialogLayout.findViewById(R.id.success);

        tb_wasDefended = placementDialogLayout.findViewById(R.id.wasDefended);

        if (element.equals("panel")) {
            tb_wasDefended.setBackgroundResource(R.drawable.map_placement_panel_toggle_selector);
        } else if (element.equals("cargo")) {
            tb_wasDefended.setBackgroundResource(R.drawable.map_placement_cargo_toggle_selector);
        }
        if (tele) {
            tb_wasDefended.setEnabled(true);
        } else if (!tele) {
            tb_wasDefended.setEnabled(false);
        }

        placementDialog.setContentView(placementDialogLayout);
        placementDialog.show();
    }

    public void onClickPlacementFail(View view) {
        time = TimerUtil.timestamp;
        if (structure.contains("Rocket")) {
            tb_shotOutOfField.setEnabled(true);
        }
    }

    public void onClickPlacementSuccess(View view) {
        time = TimerUtil.timestamp;
        if (structure.contains("Rocket")) {
            tb_shotOutOfField.setEnabled(false);
            tb_shotOutOfField.setChecked(false);
        }
    }

    public void onClickCancelCS(View view) {
        pw = true;
        placementDialogOpen = false;
        placementDialog.dismiss();
    }

    public void onClickDoneCS(View view) {
        // Make preload disabled after placement in CS.
        if (!tele) {
            preloadEnabled(false);
        }
        if (fail.isChecked() || success.isChecked()) {
            recordPlacement();
            mode = "intake";
            modeIsIntake = true;
            if (success.isChecked()) {
                initShape();
            } else if (fail.isChecked()) {
                overallLayout.removeView(iv_game_element);
                pw = true;
                mapChange();

                if (element.equals("cargo")) {
                    undoDicAdder(x, y, "cargo");
                } else if (element.equals("panel")) {
                    undoDicAdder(x, y, "panel");

                }
            }
            placementDialogOpen = false;
            placementDialog.dismiss();
        } else {
            Toast.makeText(getBaseContext(), "Please input fail/success!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickCancelRocket(View view) {
        pw = true;
        placementDialogOpen = false;
        placementDialog.dismiss();
    }

    public void onClickDoneRocket(View View) {
        // Make preload disabled after placement in rocket.
        if (!tele) {
            preloadEnabled(false);
        }

        if ((fail.isChecked() || success.isChecked())
                && (level1.isChecked() || level2.isChecked() || level3.isChecked())) {
            recordPlacement();
            mode = "intake";
            modeIsIntake = true;
            if (success.isChecked()) {
                initShape();
            } else if (fail.isChecked()) {
                overallLayout.removeView(iv_game_element);
                pw = true;
                mapChange();

                if (element.equals("cargo")) {
                    undoDicAdder(x, y, "cargo");

                } else if (element.equals("panel")) {
                    undoDicAdder(x, y, "panel");

                }
            }
            placementDialogOpen = false;
            placementDialog.dismiss();
        } else {
            Toast.makeText(getBaseContext(), "Please input fail/success and/or level!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void initPopup(PopupWindow pw2) {
        if (timerCheck) {
            isPopupOpen = true;
            //Set coordinates and size of popup based on tablet type
            if (tb_defense.isChecked() && pw && ((((field_orientation.contains("left") && x >= 1445) || (field_orientation.contains("right") && x <= 255)) && mTabletType.equals("green"))
                    || (((field_orientation.contains("left") && x >= 960) || (field_orientation.contains("right") && x <= 170)) && mTabletType.equals("black"))
                    || (((field_orientation.contains("left") && x >= 720) || (field_orientation.contains("right") && x <= 130)) && mTabletType.equals("fire")))) {
                if (mTabletType.equals("fire")) {
                    pw2.showAtLocation(overallLayout, Gravity.NO_GRAVITY, x - 150, y - 100);
                } else if (mTabletType.equals("green")) {
                    pw2.showAtLocation(overallLayout, Gravity.NO_GRAVITY, x - 350, y - 100);
                } else if (mTabletType.equals("black")) {
                    pw2.showAtLocation(overallLayout, Gravity.NO_GRAVITY, x - 230, y - 150);
                }
                pw = false;
            } else if (tb_defense.isChecked() && dropClick) {
                if (mTabletType.equals("fire")) {
                    pw2.showAtLocation(overallLayout, Gravity.NO_GRAVITY, x - 150, y - 100);
                } else if (mTabletType.equals("green")) {
                    pw2.showAtLocation(overallLayout, Gravity.NO_GRAVITY, x - 350, y - 100);
                } else if (mTabletType.equals("black")) {
                    pw2.showAtLocation(overallLayout, Gravity.NO_GRAVITY, x - 230, y - 150);
                }
                pw = false;
            } else if (((!tele) && (!tb_defense.isChecked()) && ((((field_orientation.contains("left") && x <= 1445) || (field_orientation.contains("right") && x >= 255)) && mTabletType.equals("green"))
                    || (((field_orientation.contains("left") && x <= 960) || (field_orientation.contains("right") && x >= 170)) && mTabletType.equals("black"))
                    || (((field_orientation.contains("left") && x <= 720) || (field_orientation.contains("right") && x >= 130)) && mTabletType.equals("fire")))) || (tele && !tb_defense.isChecked())) {
                if (mTabletType.equals("fire")) {
                    pw2.showAtLocation(overallLayout, Gravity.NO_GRAVITY, x - 150, y - 100);
                } else if (mTabletType.equals("green")) {
                    pw2.showAtLocation(overallLayout, Gravity.NO_GRAVITY, x - 350, y - 100);
                } else if (mTabletType.equals("black")) {
                    pw2.showAtLocation(overallLayout, Gravity.NO_GRAVITY, x - 230, y - 150);
                }
                pw = false;
            } else {
                pw = true;
            }
        }
    }

    //Method to dismiss all popups
    public void dismissPopups() {
        popup.dismiss();
        popup_fail_success.dismiss();
        popup_drop_defense.dismiss();
    }

    //Method to add action to a dictionary
    public void undoDicAdder(Object xCoordinate, Object yCoordinate, String type) {
        actionList = new ArrayList<Object>();
        actionList.add(xCoordinate);
        actionList.add(yCoordinate);
        actionList.add(mode);
        actionList.add(type);
        actionList.add(TimerUtil.timestamp);
        actionDic.put(actionCount, actionList);
        actionCount++;
        didUndoOnce = false;
        btn_undo.setEnabled(true);
    }

    //Add all lists of climb data into a dictionary and put them in mRealTimeMatchData
    public void recordClimb(float time) {
        compressionDic = new JSONObject();
        try {
            climbAttemptData = "{" + climbAttemptKeys.get(0) + climbAttemptValues.get(0) + ";" + climbAttemptKeys.get(1) + climbAttemptValues.get(1) + ";" + climbAttemptKeys.get(2) + climbAttemptValues.get(2) + "}";
            climbActualData = "{" + climbActualKeys.get(0) + climbActualValues.get(0) + ";" + climbActualKeys.get(1) + climbActualValues.get(1) + ";" + climbActualKeys.get(2) + climbActualValues.get(2) + "}";

            compressionDic.put("type", "climb");
            timestamp(time);
            compressionDic.put("attempted", climbAttemptData);
            compressionDic.put("actual", climbActualData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRealTimeMatchData.put(compressionDic);

        climbInputted = true;
        Log.i("mRealTimeMatchDataVals", mRealTimeMatchData.toString());
    }

    //Increment when pressed.
    public void onClickCyclesDefended(View v) {
        InputManager.cyclesDefended++;
        btn_cyclesDefended.setText("FAILED PLACEMENTS/DROPS CAUSED - " + InputManager.cyclesDefended);
    }

    //Move to next activity and saves defense data
    public void onClickDataCheck(View v) {
        if (tb_defense.isChecked()) {
            compressionDic = new JSONObject();

            try {
                compressionDic.put("type", "endDefense");
                compressionDic.put("cyclesDefended", InputManager.cyclesDefended);
                timestamp(TimerUtil.timestamp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mRealTimeMatchData.put(compressionDic);
        }

        open(DataCheckActivity.class, null, false, true);
    }

    //If user presses android back button, warn them that they will lose data
    public void onBackPressed() {
        final Activity activity = this;
        new AlertDialog.Builder(this)
                .setTitle("WARNING")
                .setMessage("GOING BACK WILL CAUSE LOSS OF DATA")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        handler.removeCallbacks(runnable);
                        handler.removeCallbacksAndMessages(null);

                        teleWarningHandler.removeCallbacks(teleWarningRunnable);
                        teleWarningHandler.removeCallbacksAndMessages(null);

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
