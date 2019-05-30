package citruscircuits.scout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout.Managers.OutputManager;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout._superDataClasses.Cst;
import citruscircuits.scout.utils.AppUtils;

import static java.lang.String.valueOf;

public class A2A extends DialogMaker {

    EditText et_matchNum;
    EditText et_teamNum;

    Spinner name_spinner;

    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacheck);
        context = this;
        //Declare team and match variables
        et_matchNum = findViewById(R.id.matchET);
        et_teamNum = findViewById(R.id.teamET);

        et_matchNum.setText(valueOf(InputManager.mMatchNum));
        et_teamNum.setText(valueOf(InputManager.mTeamNum));

        //Declare name spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, Cst.SCOUT_NAMES);
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
    //Records match data and saves it as a QR
    public void onClickToQR(View view) {
        //If anything is empty in the activity, it will prevent the user from entering the QR activity. Otherwise, save data and go to QR activity
        if(et_teamNum.getText().toString().equals("") || Integer.valueOf(et_teamNum.getText().toString()) == 0 || et_matchNum.getText().toString().equals("") || Integer.valueOf(et_matchNum.getText().toString()) == 0) {
            Toast.makeText(getBaseContext(), "There is null information!", Toast.LENGTH_SHORT).show();
        } else {
            InputManager.mTeamNum = AppUtils.StringToInt(et_teamNum.getText().toString());
            InputManager.mMatchNum = AppUtils.StringToInt(et_matchNum.getText().toString());

            //Records all match data. Put all OneTimeMatchData in mOneTimeMatchData to compress into header.
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

            open(A3A.class, null, false, false);
        }
    }
    //If android back button is pressed, warns the user that they will lose information.
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