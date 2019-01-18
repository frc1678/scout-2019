package citruscircuits.scout;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;

import java.util.Arrays;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout.utils.AutoDialog;
import citruscircuits.scout.utils.TimerUtil;

import static citruscircuits.scout.Managers.InputManager.mAllianceColor;
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
    public boolean field = true;
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
    public Map<Integer, List<Object>> actionDic;
    public int actionCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //InputManager.initMatchKey();

        setContentView(R.layout.activity_map);

        iv_field = findViewById(R.id.imageView);

        if (AppCc.getSp("mapOrientation", 99) != 99) {
            if (AppCc.getSp("mapOrientation", 99) == 0) {
                if(mAllianceColor.equals("blue")) {
                    iv_field.setImageResource(R.drawable.field_intake_blue_left);
                    field_orientation = "blue_left";
                } else if(mAllianceColor.equals("red")) {
                    iv_field.setImageResource(R.drawable.field_intake_red_right);
                    field_orientation = "red_right";
                }
            } else {
                if(mAllianceColor.equals("blue")) {
                    iv_field.setImageResource(R.drawable.field_intake_blue_right);
                    field_orientation = "blue_right";
                } else if(mAllianceColor.equals("red")) {
                    iv_field.setImageResource(R.drawable.field_intake_red_left);
                    field_orientation = "red_left";
                }
            }
        } else {
            if(mAllianceColor.equals("blue")) {
                iv_field.setImageResource(R.drawable.field_intake_blue_right);
                field_orientation = "blue_right";
            } else if(mAllianceColor.equals("red")) {
                iv_field.setImageResource(R.drawable.field_intake_red_left);
                field_orientation = "red_left";
            }
        }

        tv_team = findViewById(R.id.tv_teamNum);

        btn_drop = findViewById(R.id.btn_dropped);
        btn_spill = findViewById(R.id.btn_spilled);
        btn_undo = findViewById(R.id.btn_undo);
        btn_arrow = findViewById(R.id.btn_arrow);

        tb_incap = findViewById(R.id.tbtn_incap);

        iv = new ImageView(getApplicationContext());
        iv2 = new ImageView(getApplicationContext());
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
                                    recordFTB(liftSelfAttempt, numRobotsAttemptedToLift, liftSelfActual, numRobotsDidLift);

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

                            recordFTB(false, 0, false, 0);
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
                        if ((x >= 400 && x <= 590 && y >= 90 && y <= 330 && InputManager.mScoutId <= 6 || x >= 270 && x <= 400 && y >= 80 && y <= 225 && InputManager.mScoutId > 6 && InputManager.mScoutId < 12 || x >= 200 && x <= 300 && y >= 70 && y <= 200 && InputManager.mScoutId >=12) && (tele || (field && !tele))) {
                            if(shapeCheck) {
                                Log.d("locationOutput", "Top Left switch");
                                initShape(view, "score4", "score1", x, y, "circle", iv2, iv, false);
                            }
                        }
                        else if ((x >= 400 && x <= 590 && y >= 620 && y <= 860 && InputManager.mScoutId <= 6 || x >= 270 && x <= 400 && y >= 410 && y <= 560 && InputManager.mScoutId > 6 && InputManager.mScoutId < 12 || x >= 200 && x <= 300 && y >= 326 && y <= 450 && InputManager.mScoutId >=12) && (tele || (field && !tele))) {
                            if(shapeCheck) {
                                Log.d("locationOutput", "Bottom Left switch");
                                initShape(view, "score3", "score6", x, y, "circle", iv2, iv, false);
                            }
                        } else if ((x >= 1110 && x <= 1300 && y >= 90 && y <= 330 && InputManager.mScoutId <= 6 || x >= 750 && x <= 860 && y >= 80 && y <= 225 && InputManager.mScoutId > 6 && InputManager.mScoutId < 12 || x >= 550 && x <= 654 && y >= 70 && y <= 200 && InputManager.mScoutId >=12) && (tele || (!field && !tele))) {
                            if(shapeCheck) {
                                Log.d("locationOutput", "Top Right switch");
                                initShape(view, "score6", "score3", x, y, "circle", iv2, iv, false);
                            }
                        } else if ((x >= 1110 && x <= 1300 && y >= 620 && y <= 860 && InputManager.mScoutId <= 6 || x >= 750 && x <= 860 && y >= 410 && y <= 560 && InputManager.mScoutId > 6 && InputManager.mScoutId < 12 || x >= 550 && x <= 654 && y >= 326 && y <= 450 && InputManager.mScoutId >=12) && (tele || (!field && !tele))) {
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
                        } else if (((x > 110 && x <= 500 && InputManager.mScoutId <= 6) || (x > 80 && x <= 320 && InputManager.mScoutId > 6 && InputManager.mScoutId <12) || (x<=250 && x>=60 && InputManager.mScoutId >=12)) && (tele || (field && !tele))) {
                            if(!shapeCheck) {
                                Log.d("locationInput", "1");
                                initShape(view, "intake4", "intake1", x, y, "triangle", iv, iv2, true);
                            }
                        } else if (((x > 500 && x <= 845 && InputManager.mScoutId <= 6 || x > 320 && x <= 550 && InputManager.mScoutId > 6 && InputManager.mScoutId <12)|| (x<=430 && x>250 && InputManager.mScoutId >=12)) && (tele || (field && !tele))) {
                            if(!shapeCheck) {
                                Log.d("locationInput", "2");
                                initShape(view, "intake3", "intake2", x, y, "triangle", iv, iv2, true);
                            }
                        } else if (((x > 845 && x <= 1200 && InputManager.mScoutId <= 6 || x > 550 && x <= 800 && InputManager.mScoutId > 6 && InputManager.mScoutId <12)|| (x<=600 && x>430 && InputManager.mScoutId >=12)) && (tele || (!field && !tele))) {
                            if(!shapeCheck) {
                                Log.d("locationInput", "3");
                                initShape(view, "intake2", "intake3", x, y, "triangle", iv, iv2, true);
                            }
                        } else if (((x > 1200 && x <= 1580 && InputManager.mScoutId <= 6 || x > 800 && x <= 1040 && InputManager.mScoutId > 6 && InputManager.mScoutId <12)|| (x<=850 && x>600 && InputManager.mScoutId >=12)) && (tele || (!field && !tele))) {
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
            add.setImageDrawable(getResources().getDrawable(R.drawable.lemon));
        } else if (add == iv2) {
            add.setImageDrawable(getResources().getDrawable(R.drawable.orange));
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

    public void mapChange() {
        if(shapeCheck) {
            if (field_orientation.equals("rb")) {
                iv_field.setImageResource(R.drawable.field_intake_blue_left);
            } else if (field_orientation.equals("br")) {
                iv_field.setImageResource(R.drawable.field_intake_blue_left);
            }
        }
        if(!shapeCheck) {
            if (field_orientation.equals("rb")) {
                iv_field.setImageResource(R.drawable.field_intake_blue_left);
            } else if (field_orientation.equals("br")) {
                iv_field.setImageResource(R.drawable.field_intake_blue_left);
            }
        }
    }

    public void recordFTB(Boolean attemptBool, Integer attemptNum, Boolean actualBool, Integer actualNum) {
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
}
