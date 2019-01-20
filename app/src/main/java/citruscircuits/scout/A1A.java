package citruscircuits.scout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

    JSONObject ftbAttemptData = new JSONObject();
    JSONObject ftbActualData = new JSONObject();
    JSONObject ftbFinalData = new JSONObject();

    public TextView tv_team;
    public TextView tv_starting_position_warning;

    public Button btn_startTimer;
    public Button btn_drop;
    public Button btn_spill;
    public Button btn_undo;
    public Button btn_climb;
    public Button btn_arrow;

    public ToggleButton tb_incap;
    public ToggleButton tb_auto_run;
    public ToggleButton tb_start_cube;

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
                } else if(mAllianceColor.equals("red")) {
                    iv_field.setImageResource(R.drawable.field_intake_red_left);
                    field_orientation = "red_left";
                }
            } else {
                if(mAllianceColor.equals("blue")) {
                    iv_field.setImageResource(R.drawable.field_intake_blue_left);
                    field_orientation = "blue_right";
                } else if(mAllianceColor.equals("red")) {
                    iv_field.setImageResource(R.drawable.field_intake_red_right);
                    field_orientation = "red_right";
                }
            }
        } else {
            if(mAllianceColor.equals("blue")) {
                iv_field.setImageResource(R.drawable.field_intake_blue_left);
                field_orientation = "blue_left";
            } else if(mAllianceColor.equals("red")) {
                iv_field.setImageResource(R.drawable.field_intake_red_right);
                field_orientation = "red_right";
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

    public void onClickDrop(View v) {
        if(!tele) {
            tb_start_cube = findViewById(R.id.tgbtn_start_with_cube);
            tb_start_cube.setEnabled(false);
        }
        btn_drop.setEnabled(false);
        btn_undo.setEnabled(true);
        timestamp();
        shapeCheck = false;
        overallLayout.removeView(iv_game_element);
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
            timestamp();
            InputManager.numSpill++;
            btn_spill.setText("SPILL - " + InputManager.numSpill);
        }
    }

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

    public void onClickClimb(View v) {
        if (tele && !startTimer && !incapChecked && !climbInputted) {
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
                                    if (numRobotsAttemptedToLift < 2) {
                                        numRobotsAttemptedToLift++;
                                        tv_attempted.setText(String.valueOf(numRobotsAttemptedToLift));
                                    }
                                }
                            });

                            btn_attemptedMinus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (numRobotsAttemptedToLift > 0) {
                                        numRobotsAttemptedToLift--;
                                        tv_attempted.setText(String.valueOf(numRobotsAttemptedToLift));
                                    }
                                }
                            });

                            btn_plus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (numRobotsDidLift < 2) {
                                        numRobotsDidLift++;
                                        tv_didLift.setText(String.valueOf(numRobotsDidLift));
                                    }
                                }
                            });

                            btn_minus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (numRobotsDidLift > 0) {
                                        numRobotsDidLift--;
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
                                    recordClimb(liftSelfAttempt, numRobotsAttemptedToLift, liftSelfActual, numRobotsDidLift);

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

                            recordClimb(false, 0, false, 0);
                        }
                    })
                    .show();
        }
    }

    public void onClickIncap(View v) throws JSONException {
        if (!incapChecked) {
            tb_incap.setChecked(true);

            btn_drop.setEnabled(false);
            btn_undo.setEnabled(false);
            timestamp();

            incapChecked = true;
        } else if (incapChecked) {
            tb_incap.setChecked(false);
            if (shapeCheck) {
                btn_drop.setEnabled(true);
            }
            timestamp();
            incapChecked = false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addTouchListener() {
        overallLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    x = (int) motionEvent.getX();
                    y = (int) motionEvent.getY();

                    if(mode.equals("intake")) {
                        if((((field_orientation.contains("left") && x>275 && x<545) || (field_orientation.contains("right") && x>1145 && x<1425)) && y>340 && y<690 && mScoutId < 9) || (((field_orientation.contains("left") && x>175 && x<360) || (field_orientation.contains("right") && x>775 && x<955)) && y>225 && y<460 && mScoutId >= 9)) {
                            mode = "placement";
                            element = "orange";
                            if ((((y < 510 && field_orientation.contains("right")) || (y>=510 && field_orientation.contains("left"))) && mScoutId < 9) || (((y < 345 && field_orientation.contains("right")) || (y>=345 && field_orientation.contains("left"))) && mScoutId >= 9)) {
                                zone = "rightDepot";
                            } else if ((((y < 510 && field_orientation.contains("left")) || (y>=510 && field_orientation.contains("right"))) && mScoutId < 9)  || (((y < 345 && field_orientation.contains("left")) || (y>=345 && field_orientation.contains("right"))) && mScoutId >= 9)) {
                                zone = "leftDepot";
                            }
                            initShape();
                        } else {
                            initPopup(popup);
                        }
                    } else if(mode.equals("placement")) { //TODO add placement coordinates
                        initPopup(popup_fail_success);
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
    }

    public void onClickFail(View view) {
        overallLayout.removeView(iv_game_element);

        mode = "intake";

        if(zone.contains("LoadingStation")) {
            recordLoadingStation(false);
        }

//        if((x <= 1125 && x >= 870 && y <= 275 && y >= 50) || (x <= 1125 && x >= 870 && y <= 980 && y >= 760)) {
//            initPopup(popup_rocket);
//        }

        mapChange();

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
        }

        initShape();

//        if((x <= 1125 && x >= 870 && y <= 275 && y >= 50) || (x <= 1125 && x >= 870 && y <= 980 && y >= 760)) {
//            initPopup(popup_rocket);
//        }

        popup_fail_success.dismiss();
    }

    public void onClickCancelFS(View view) {
        popup_fail_success.dismiss();
    }

    public void onClickDone(View view) {
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
        overallLayout.removeView(iv_game_element);

        if(element.equals("orange")) {
            iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.orange));
        } else if(element.equals("lemon")) {
            iv_game_element.setImageDrawable(getResources().getDrawable(R.drawable.lemon));
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                100,
                100);
        lp.setMargins(x - 25, y - 40, 0, 0);
        iv_game_element.setLayoutParams(lp);

        ((ViewGroup) overallLayout).addView(iv_game_element);

        mapChange();
    }

    public void initIntake(String givenElement) {
        popup.dismiss();
        element = givenElement;

        if((((field_orientation.contains("left") && x < 225) || (field_orientation.contains("right") && x > 1440)) && mScoutId < 9) || (((field_orientation.contains("left") && x < 175) || (field_orientation.contains("right") && x > 955)) && mScoutId >= 9)) {
            if((((field_orientation.contains("left") && y<=415) || (field_orientation.contains("right") && y>=615)) && mScoutId < 9) || (((field_orientation.contains("left") && y<=280) || (field_orientation.contains("right") && y>=410)) && mScoutId >= 9)) {
                zone = "leftLoadingStation";
            } else if((((field_orientation.contains("right") && y<=415) || (field_orientation.contains("left") && y>=615)) && mScoutId < 9) || (((field_orientation.contains("right") && y<=280) || (field_orientation.contains("left") && y>=410)) && mScoutId >= 9)) {
                zone = "rightLoadingStation";
            }
            initPopup(popup_fail_success);
        } else {
            mode = "placement";
            if((y>517 && mScoutId < 9) || (y>345 && mScoutId >= 9)) {
                if ((((field_orientation.contains("left") && x <= 540) || (field_orientation.contains("right") && x >= 1160)) && mScoutId < 9) || (((field_orientation.contains("left") && x <= 360) || (field_orientation.contains("right") && x >= 955)) && mScoutId >= 9)) {
                    zone = "zone1Right";
                } else if ((((field_orientation.contains("left") && x <= 940) || (field_orientation.contains("right") && x >= 760)) && mScoutId < 9) || (((field_orientation.contains("left") && x <= 625) || (field_orientation.contains("right") && x >= 500)) && mScoutId >= 9)) {
                    zone = "zone2Right";
                } else if ((((field_orientation.contains("left") && x <= 1445) || (field_orientation.contains("right") && x >= 255)) && mScoutId < 9) || (((field_orientation.contains("left") && x <= 960) || (field_orientation.contains("right") && x >= 170)) && mScoutId >= 9)) {
                    zone = "zone3Right";
                } else {
                    zone = "zone4Right";
                }
            } else {
                if ((((field_orientation.contains("left") && x <= 540) || (field_orientation.contains("right") && x >= 1160)) && mScoutId < 9) || (((field_orientation.contains("left") && x <= 360) || (field_orientation.contains("right") && x >= 955)) && mScoutId >= 9)) {
                    zone = "zone1Left";
                } else if ((((field_orientation.contains("left") && x <= 940) || (field_orientation.contains("right") && x >= 760)) && mScoutId < 9) || (((field_orientation.contains("left") && x <= 625) || (field_orientation.contains("right") && x >= 500)) && mScoutId >= 9)) {
                    zone = "zone2Left";
                } else if ((((field_orientation.contains("left") && x <= 1445) || (field_orientation.contains("right") && x >= 255)) && mScoutId < 9) || (((field_orientation.contains("left") && x <= 960) || (field_orientation.contains("right") && x >= 170)) && mScoutId >= 9)) {
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

    public void initPopup(PopupWindow pw) {
        pw.showAtLocation(overallLayout, Gravity.NO_GRAVITY, x - 350, y - 100);
    }

    public void recordClimb(Boolean attemptBool, Integer attemptNum, Boolean actualBool, Integer actualNum) {
        climbAttemptValues.add(attemptBool);
        climbAttemptValues.add(attemptNum);

        climbActualValues.add(actualBool);
        climbActualValues.add(actualNum);

        try {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        climbInputted = true;

        Log.i("FTB", ftbFinalData.toString());
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
