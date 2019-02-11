package citruscircuits.scout;

import citruscircuits.scout._superActivities.DialogMaker;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;


import org.json.JSONException;
import org.json.JSONObject;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superDataClasses.AppCc;

import static citruscircuits.scout.Managers.InputManager.mAllianceColor;
//import static citruscircuits.scout.Managers.InputManager.mHabStartingPositionLevel;
import static java.lang.String.valueOf;


public class A0B extends DialogMaker {
    public RadioGroup driverStart;
    public RadioButton driverLeft;
    public RadioButton driverCenter;
    public RadioButton driverRight;
    public RadioGroup hab;
    public RadioGroup habSub1;
    public RadioGroup habSub2;
    public RadioGroup preloadGroup;
    public RadioButton layerTwoA;
    public RadioButton layerTwoB;
    public RadioButton layerOneA;
    public RadioButton layerOneB;
    public RadioButton layerOneC;
    public RadioButton preloadCargo;
    public RadioButton preloadPanel;
    public RadioButton preloadNone;
    public ToggleButton showUp;

    public Button arrowNext;
    public ImageView iv_hab;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrowNext= findViewById(R.id.btn_arrow);
        InputManager.mOneTimeMatchData = new JSONObject();

        if ( AppCc.getSp("mapOrientation", 99) != 0){
            Log.e("wok", "prer");
            if(mAllianceColor.equals("red")){
                setContentView(R.layout.activity_pregame_r);
                defineVariables();
                iv_hab.setImageResource(R.drawable.red_hab_r);
                driverRight.setText("R3");
                driverCenter.setText("R2");
                driverLeft.setText("R1");
                layerOneA.setBackgroundResource(R.drawable.starting_position_red_selector);
                layerOneB.setBackgroundResource(R.drawable.starting_position_red_selector);
                layerOneC.setBackgroundResource(R.drawable.starting_position_red_selector);
                layerTwoA.setBackgroundResource(R.drawable.starting_position_red_selector);
                layerTwoB.setBackgroundResource(R.drawable.starting_position_red_selector);
                layerTwoA.setBackgroundResource(R.drawable.starting_position_red_selector);
                driverRight.setBackgroundResource(R.drawable.driver_red);
                driverCenter.setBackgroundResource(R.drawable.driver_red);
                driverLeft.setBackgroundResource(R.drawable.driver_red);
            } else if (mAllianceColor.equals("blue")){
                setContentView(R.layout.activity_pregame_l);
                defineVariables();
                Log.e("wok", "bluer");
                iv_hab.setImageResource(R.drawable.blue_hab_l);
                driverRight.setText("B3");
                driverCenter.setText("B2");
                driverLeft.setText("B1");
                driverRight.setBackgroundResource(R.drawable.driver_blue);
                driverCenter.setBackgroundResource(R.drawable.driver_blue);
                driverLeft.setBackgroundResource(R.drawable.driver_blue);
                layerOneA.setBackgroundResource(R.drawable.starting_position_blue_selector);
                layerOneB.setBackgroundResource(R.drawable.starting_position_blue_selector);
                layerOneC.setBackgroundResource(R.drawable.starting_position_blue_selector);
                layerTwoA.setBackgroundResource(R.drawable.starting_position_blue_selector);
                layerTwoB.setBackgroundResource(R.drawable.starting_position_blue_selector);
                layerTwoA.setBackgroundResource(R.drawable.starting_position_blue_selector);
            }
        }else if ( AppCc.getSp("mapOrientation", 99) == 0){
            Log.e("wok", "prel");
            if(mAllianceColor.equals("red")){
                setContentView(R.layout.activity_pregame_l);
                defineVariables();
                Log.e("wok", "redl");
                iv_hab.setImageResource(R.drawable.red_hab_l);
                driverRight.setText("R3");
                driverCenter.setText("R2");
                driverLeft.setText("R1");

                driverRight.setBackgroundResource(R.drawable.driver_red);
                driverCenter.setBackgroundResource(R.drawable.driver_red);
                driverLeft.setBackgroundResource(R.drawable.driver_red);
                layerOneA.setBackgroundResource(R.drawable.starting_position_red_selector);
                layerOneB.setBackgroundResource(R.drawable.starting_position_red_selector);
                layerOneC.setBackgroundResource(R.drawable.starting_position_red_selector);
                layerTwoA.setBackgroundResource(R.drawable.starting_position_red_selector);
                layerTwoB.setBackgroundResource(R.drawable.starting_position_red_selector);
                layerTwoA.setBackgroundResource(R.drawable.starting_position_red_selector);
            } else if (mAllianceColor.equals("blue")){
                setContentView(R.layout.activity_pregame_r);
                defineVariables();

                Log.e("wok", "bluel");
                iv_hab.setImageResource(R.drawable.blue_hab_r);
                driverRight.setText("B3");
                driverCenter.setText("B2");
                driverLeft.setText("B1");

                driverRight.setBackgroundResource(R.drawable.driver_blue);
                driverCenter.setBackgroundResource(R.drawable.driver_blue);
                driverLeft.setBackgroundResource(R.drawable.driver_blue);
                layerOneA.setBackgroundResource(R.drawable.starting_position_blue_selector);
                layerOneB.setBackgroundResource(R.drawable.starting_position_blue_selector);
                layerOneC.setBackgroundResource(R.drawable.starting_position_blue_selector);
                layerTwoA.setBackgroundResource(R.drawable.starting_position_blue_selector);
                layerTwoB.setBackgroundResource(R.drawable.starting_position_blue_selector);
                layerTwoA.setBackgroundResource(R.drawable.starting_position_blue_selector);
            }
        }
          preloadNone.setChecked(true);
    }

public void onClickShowUp(View view){
        if (showUp.isChecked()){
            layerOneA.setEnabled(false);
            layerOneB.setEnabled(false);
            layerOneC.setEnabled(false);
            layerTwoA.setEnabled(false);
            layerTwoB.setEnabled(false);
            driverRight.setEnabled(false);
            driverLeft.setEnabled(false);
            driverCenter.setEnabled(false);
            preloadCargo.setEnabled(false);
            preloadPanel.setEnabled(false);
            preloadNone.setEnabled(false);
        }else{
            layerOneA.setEnabled(true);
            layerOneB.setEnabled(true);
            layerOneC.setEnabled(true);
            layerTwoA.setEnabled(true);
            layerTwoB.setEnabled(true);
            driverRight.setEnabled(true);
            driverLeft.setEnabled(true);
            driverCenter.setEnabled(true);
            preloadCargo.setEnabled(true);
            preloadPanel.setEnabled(true);
            preloadNone.setEnabled(true);

        }

}
public void defineVariables(){
    iv_hab = findViewById(R.id.imageView);
    driverStart=findViewById(R.id.driver_starting_position);
    driverCenter=findViewById(R.id.driver_center);
    driverLeft=findViewById(R.id.driver_left);
    driverRight=findViewById(R.id.driver_right);
    hab= findViewById(R.id.hab);
    habSub1=findViewById(R.id.innerhab1);
    habSub2=findViewById(R.id.innerhab2);
    layerTwoA= findViewById(R.id.LayerTwoA);
    layerTwoB= findViewById(R.id.LayerTwoB);
    layerOneA= findViewById(R.id.LayerOneA);
    layerOneB= findViewById(R.id.LayerOneB);
    layerOneC= findViewById(R.id.LayerOneC);
    preloadGroup=findViewById(R.id.preload);
    preloadCargo= findViewById(R.id.preloadCargo);
    preloadPanel= findViewById(R.id.preloadPanel);
    preloadNone= findViewById(R.id.preloadNone);
    showUp= findViewById(R.id.showedUp);
    }
    public void onClickHabOneA(View v){
        Log.e("wokkkkkk", "layer1");
        habSub2.clearCheck();

    }
    public void onClickHabOneB(View v){
        Log.e("wokkkkkk", "layer1");
        habSub2.clearCheck();

    }
    public void onClickHabOneC(View v){
        Log.e("wokkkkkk", "layer1");
        habSub2.clearCheck();

    }
    public void onClickHabTwoA(View v){
        Log.e("wokkkkkk", "layer2");
        habSub1.clearCheck();

    }
    public void onClickHabTwoB(View v){
        Log.e("wokkkkkk", "layer2");
        habSub1.clearCheck();
    }



    public void onClickDataCheck(View v) {
        //record data

        if (!showUp.isChecked()){
            if((!layerOneA.isChecked() && !layerOneB.isChecked() && !layerOneC.isChecked()&& !layerTwoA.isChecked()
                        && !layerTwoB.isChecked()) || (!driverRight.isChecked() && !driverCenter.isChecked() && !driverLeft.isChecked())
                        || ( !preloadCargo.isChecked()&& !preloadPanel.isChecked() && !preloadNone.isChecked())){
                    Toast.makeText(getBaseContext(), "Make Sure You Filled Out All Of The Information!",
                        Toast.LENGTH_SHORT).show();
            }else {
                if (layerTwoA.isChecked()) {
                    InputManager.mHabStartingPositionLevel = 2;
                    InputManager.mHabStartingPositionOrientation="left";
                } else if (layerTwoB.isChecked()) {
                    InputManager.mHabStartingPositionLevel = 2;
                    InputManager.mHabStartingPositionOrientation="right";

                }else if (layerOneA.isChecked()) {
                    InputManager.mHabStartingPositionLevel = 1;
                    InputManager.mHabStartingPositionOrientation="left";

                }else if (layerOneB.isChecked()) {
                    InputManager.mHabStartingPositionLevel = 1;
                    InputManager.mHabStartingPositionOrientation="mid";

                }else if (layerOneC.isChecked()) {
                    InputManager.mHabStartingPositionLevel = 1;
                    InputManager.mHabStartingPositionOrientation="right";
                }
                if (driverLeft.isChecked()) {
                    InputManager.mDriverStartingPosition = 1;
                } else if (driverCenter.isChecked()) {
                    InputManager.mDriverStartingPosition = 2;
                } else if (driverRight.isChecked()) {
                    InputManager.mDriverStartingPosition = 3;
                }
                if(preloadCargo.isChecked()){
                    InputManager.mPreload = "orange";

                }else if (preloadPanel.isChecked()){
                    InputManager.mPreload = "lemon";

                }else if (preloadNone.isChecked()){
                    InputManager.mPreload = "none";
                }
                InputManager.isNoShow=false;

                open(A1A.class, null, false, true);

            }
        }
        //go to next activity
        else if(showUp.isChecked()){
            InputManager.isNoShow=true;
            try {
                InputManager.mOneTimeMatchData.put("isNoShow", InputManager.isNoShow);
                InputManager.mOneTimeMatchData.put("currentCycle", InputManager.mCycleNum);
                InputManager.mOneTimeMatchData.put("scoutName", InputManager.mScoutName);
                InputManager.mOneTimeMatchData.put("scoutID", InputManager.mScoutId);
                InputManager.mOneTimeMatchData.put("appVersion", InputManager.mAppVersion);
                InputManager.mOneTimeMatchData.put("assignmentMode", InputManager.mAssignmentMode);
                InputManager.mOneTimeMatchData.put("assignmentFileTimestamp", InputManager.mAssignmentFileTimestamp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            open(A2A.class, null, false, true);
        }
        A1A.cancelStormChecker=false;
        A1A.startTimer=true;
        A1A.timerCheck=false;
    }

}
