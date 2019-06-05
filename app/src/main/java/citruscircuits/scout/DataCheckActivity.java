package citruscircuits.scout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout._superDataClasses.Cst;
import citruscircuits.scout.utils.AppUtils;

import static java.lang.String.valueOf;

public class DataCheckActivity extends DialogMaker {

    EditText et_matchNum;
    EditText et_teamNum;

    Spinner name_spinner;

    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacheck);
        context = this;

        et_matchNum = findViewById(R.id.matchET);
        et_teamNum = findViewById(R.id.teamET);

        et_matchNum.setText(valueOf(InputManager.mMatchNum));
        et_teamNum.setText(valueOf(InputManager.mTeamNum));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.datacheck_dropdown_name, Cst.SCOUT_NAMES);
        name_spinner = (Spinner) findViewById(R.id.spinner_name);

        name_spinner.setAdapter(spinnerAdapter);
        name_spinner.setSelection(((ArrayAdapter<String>)name_spinner.getAdapter()).getPosition(InputManager.mScoutName));

        name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                InputManager.mScoutName = name_spinner.getSelectedItem().toString();
                AppCc.setSp("scoutName", InputManager.mScoutName);
            }
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing, but necessary for spinner
            }
        });
    }

    public void onClickToQR(View view) {
        if(et_teamNum.getText().toString().equals("") || Integer.valueOf(et_teamNum.getText().toString()) == 0 || et_matchNum.getText().toString().equals("") || Integer.valueOf(et_matchNum.getText().toString()) == 0) {
            Toast.makeText(getBaseContext(), "There is null information!", Toast.LENGTH_SHORT).show();
        } else {
            InputManager.mTeamNum = AppUtils.StringToInt(et_teamNum.getText().toString());
            InputManager.mMatchNum = AppUtils.StringToInt(et_matchNum.getText().toString());

            //Put all OneTimeMatchData in mOneTimeMatchData to compress into header.
            try {
                InputManager.mOneTimeMatchData.put("startingLevel", InputManager.mHabStartingPositionLevel);
                InputManager.mOneTimeMatchData.put("crossedHabLine", InputManager.mCrossedHabLine);
                InputManager.mOneTimeMatchData.put("startingLocation", InputManager.mHabStartingPositionOrientation);
                InputManager.mOneTimeMatchData.put("preload", InputManager.mPreload);
                InputManager.mOneTimeMatchData.put("isNoShow", InputManager.isNoShow);
                InputManager.mOneTimeMatchData.put("timerStarted", InputManager.mTimerStarted);
                InputManager.mOneTimeMatchData.put("currentCycle", InputManager.mCycleNum);
                InputManager.mOneTimeMatchData.put("scoutName", InputManager.mScoutName);
                InputManager.mOneTimeMatchData.put("scoutID", InputManager.mScoutId);
                InputManager.mOneTimeMatchData.put("appVersion", InputManager.mAppVersion);
                InputManager.mOneTimeMatchData.put("assignmentMode", InputManager.mAssignmentMode);
                InputManager.mOneTimeMatchData.put("assignmentFileTimestamp", InputManager.mAssignmentFileTimestamp);
                InputManager.mOneTimeMatchData.put("sandstormEndPosition", InputManager.mSandstormEndPosition);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            open(QRDisplayActivity.class, null, false, false);
        }
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