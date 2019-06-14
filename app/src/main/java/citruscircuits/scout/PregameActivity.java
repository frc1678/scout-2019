package citruscircuits.scout;

import citruscircuits.scout._superActivities.DialogMaker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import org.json.JSONObject;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superDataClasses.AppCc;

import static citruscircuits.scout.Managers.InputManager.mAllianceColor;
//import static citruscircuits.scout.Managers.InputManager.mHabStartingPositionLevel;
import static java.lang.String.valueOf;


public class PregameActivity extends DialogMaker {
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

    public TextView teamNumTextView1;
    public TextView teamNumTextView2;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrowNext= findViewById(R.id.btn_arrow);
        InputManager.mOneTimeMatchData = new JSONObject();

        //Declare UI of pregame map depending on map orientation and alliance color
        if ( AppCc.getSp("mapOrientation", 99) != 0){
            if(mAllianceColor.equals("red")){
                setContentView(R.layout.activity_pregame_right);
                teamNumTextView1 = (TextView) findViewById(R.id.teamTextView1);
                teamNumTextView1.setText(String.valueOf(InputManager.mTeamNum));
                defineVariables();
                iv_hab.setImageResource(R.drawable.pregame_hab_red_right);
                layerOneA.setBackgroundResource(R.drawable.pregame_starting_position_red_selector);
                layerOneB.setBackgroundResource(R.drawable.pregame_starting_position_red_selector);
                layerOneC.setBackgroundResource(R.drawable.pregame_starting_position_red_selector);
                layerTwoA.setBackgroundResource(R.drawable.pregame_starting_position_red_selector);
                layerTwoB.setBackgroundResource(R.drawable.pregame_starting_position_red_selector);
                layerTwoA.setBackgroundResource(R.drawable.pregame_starting_position_red_selector);
            } else if (mAllianceColor.equals("blue")){
                setContentView(R.layout.activity_pregame_left);
                teamNumTextView2 = (TextView) findViewById(R.id.teamTextView2);
                teamNumTextView2.setText(String.valueOf(InputManager.mTeamNum));
                defineVariables();

                iv_hab.setImageResource(R.drawable.pregame_hab_blue_left);
                layerOneA.setBackgroundResource(R.drawable.pregame_starting_position_blue_selector);
                layerOneB.setBackgroundResource(R.drawable.pregame_starting_position_blue_selector);
                layerOneC.setBackgroundResource(R.drawable.pregame_starting_position_blue_selector);
                layerTwoA.setBackgroundResource(R.drawable.pregame_starting_position_blue_selector);
                layerTwoB.setBackgroundResource(R.drawable.pregame_starting_position_blue_selector);
                layerTwoA.setBackgroundResource(R.drawable.pregame_starting_position_blue_selector);
            }
        }else if ( AppCc.getSp("mapOrientation", 99) == 0){
            if(mAllianceColor.equals("red")){
                setContentView(R.layout.activity_pregame_left);
                teamNumTextView2 = (TextView) findViewById(R.id.teamTextView2);
                teamNumTextView2.setText(String.valueOf(InputManager.mTeamNum));
                defineVariables();
                iv_hab.setImageResource(R.drawable.pregame_hab_red_left);
                layerOneA.setBackgroundResource(R.drawable.pregame_starting_position_red_selector);
                layerOneB.setBackgroundResource(R.drawable.pregame_starting_position_red_selector);
                layerOneC.setBackgroundResource(R.drawable.pregame_starting_position_red_selector);
                layerTwoA.setBackgroundResource(R.drawable.pregame_starting_position_red_selector);
                layerTwoB.setBackgroundResource(R.drawable.pregame_starting_position_red_selector);
                layerTwoA.setBackgroundResource(R.drawable.pregame_starting_position_red_selector);

            } else if (mAllianceColor.equals("blue")){
                setContentView(R.layout.activity_pregame_right);
                teamNumTextView1 = (TextView) findViewById(R.id.teamTextView1);
                teamNumTextView1.setText(String.valueOf(InputManager.mTeamNum));
                defineVariables();

                iv_hab.setImageResource(R.drawable.pregame_hab_blue_right);
                layerOneA.setBackgroundResource(R.drawable.pregame_starting_position_blue_selector);
                layerOneB.setBackgroundResource(R.drawable.pregame_starting_position_blue_selector);
                layerOneC.setBackgroundResource(R.drawable.pregame_starting_position_blue_selector);
                layerTwoA.setBackgroundResource(R.drawable.pregame_starting_position_blue_selector);
                layerTwoB.setBackgroundResource(R.drawable.pregame_starting_position_blue_selector);
                layerTwoA.setBackgroundResource(R.drawable.pregame_starting_position_blue_selector);
            }
        }
          preloadNone.setChecked(true);
    }
//Records if a robot showed up
public void onClickShowUp(View view){
        //If the show up button is already checked, disables map based buttons
        if (showUp.isChecked()){
            layerOneA.setEnabled(false);
            layerOneB.setEnabled(false);
            layerOneC.setEnabled(false);
            layerTwoA.setEnabled(false);
            layerTwoB.setEnabled(false);
            preloadCargo.setEnabled(false);
            preloadPanel.setEnabled(false);
            preloadNone.setEnabled(false);
        }else{
            layerOneA.setEnabled(true);
            layerOneB.setEnabled(true);
            layerOneC.setEnabled(true);
            layerTwoA.setEnabled(true);
            layerTwoB.setEnabled(true);
            preloadCargo.setEnabled(true);
            preloadPanel.setEnabled(true);
            preloadNone.setEnabled(true);

        }

}
//Defines views and buttons in the pregame screen
public void defineVariables(){
    iv_hab = findViewById(R.id.imageView);
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
    //Records which level the robot begins at
    public void onClickHabOneA(View v){
        habSub2.clearCheck();
    }
    
    public void onClickHabOneB(View v){
        habSub2.clearCheck();
    }
    
    public void onClickHabOneC(View v){
        habSub2.clearCheck();
    }
    
    public void onClickHabTwoA(View v){
        habSub1.clearCheck();
    }
    
    public void onClickHabTwoB(View v){
        habSub1.clearCheck();
    }
    //Saves what starting position and level robot started at. Moves to either map activity or final data activity depending on if robot showed up.
    public void onClickDataCheck(View v) {
        if (!showUp.isChecked()){
            if((!layerOneA.isChecked() && !layerOneB.isChecked() && !layerOneC.isChecked()&& !layerTwoA.isChecked()
                        && !layerTwoB.isChecked())
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
                if(preloadCargo.isChecked()){
                    InputManager.mPreload = "cargo";

                }else if (preloadPanel.isChecked()){
                    InputManager.mPreload = "panel";

                }else if (preloadNone.isChecked()){
                    InputManager.mPreload = "none";
                }
                InputManager.isNoShow=false;

                open(MapActivity.class, null, false, true);

            }
        }
        //Go to next activity
        else if(showUp.isChecked()){
            InputManager.isNoShow=true;

            open(DataCheckActivity.class, null, false, true);
        }
        MapActivity.startTimer=true;
        MapActivity.timerCheck=false;
    }

}
