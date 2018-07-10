package citruscircuits.scout._superActivities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.net.ParseException;

import org.json.JSONObject;

import citruscircuits.scout.A0A;
import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout.R;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout.utils.AppUtils;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

//Written by the Daemon himself ~ Calvin
public class DialogMaker extends AppTc{

    public void initSeesawDialog(Activity a, boolean isOpponentSeesaw, boolean isScale){
        SeesawDialog mSeesawDialog = new SeesawDialog(a, isOpponentSeesaw, isScale);
        mSeesawDialog.show();
    }

    public void initOverrideDialog(Activity a){
        OverrideDialog mOverrideDialog = new OverrideDialog(a);
        mOverrideDialog.show();
    }

    public class OverrideDialog extends Dialog {

        private Activity context;

        private EditText et_overrideTeamNum;
        private ToggleButton tbtn_AllianceColor;

        private String tAllianceColor;

        public OverrideDialog(Activity a){
            super(a);
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_override);

            OverrideDialog.this.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    try{
                        InputManager.mTeamNum = AppUtils.StringToInt(et_overrideTeamNum.getText().toString());
                    }catch(Exception e){}

                    if(tbtn_AllianceColor.isChecked()){
                        InputManager.mAllianceColor = "Red";
                    }else{
                        InputManager.mAllianceColor = "Blue";
                    }

                    InputManager.storeUserData();
                    A0A.updateUserData();
                }
            });

            initViews();
        }

        public void initViews(){
            et_overrideTeamNum = findViewById(R.id.et_overrideTeamNum);

            tbtn_AllianceColor = findViewById(R.id.tbtn_alliance_color);
        }
    }

    public class SeesawDialog extends Dialog implements View.OnClickListener {

        private Activity context;

        private boolean isOpponentSeesaw, isScale;

        private TextView tv_DialogTitle;
        private ToggleButton tbtn_Outcome;
        private RadioButton rbtn_OpponentOwned, rbtn_Balanced,
                    rbtn_Layer1, rbtn_Layer2, rbtn_Layer3;

        public JSONObject mSeesawInfo;

        private SeesawDialog(Activity a, boolean isOpponentSeesaw, boolean isScale){
            super(a);
            SeesawDialog.this.context = a;

            SeesawDialog.this.isOpponentSeesaw = isOpponentSeesaw;
            SeesawDialog.this.isScale = isScale;
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_seesaws);

            initViews();
        }

        public void onClick(View v){
            switch (v.getId()) {
                case R.id.tbtn_outcome:
                    break;
                case R.id.rbtn_opponentOwned:
                    break;
                case R.id.rbtn_balanced:
                    break;
                case R.id.rbtn_layer1:
                    break;
                case R.id.rbtn_layer2:
                    break;
                case R.id.rbtn_layer3:
                    break;

            }
        }

        private void initViews(){
            tv_DialogTitle = (TextView) findViewById(R.id.dialogTitle);
            if(isOpponentSeesaw && isScale){        tv_DialogTitle.setText(R.string.tv_titleIsOpponentScale);}
            else if(!isOpponentSeesaw && isScale){      tv_DialogTitle.setText(R.string.tv_titleIsAllianceScale);}
            else if(isOpponentSeesaw && !isScale){      tv_DialogTitle.setText(R.string.tv_titleIsOpponentSwitch);}
            else if(!isOpponentSeesaw && !isScale){      tv_DialogTitle.setText(R.string.tv_titleIsAllianceSwitch);}

            tbtn_Outcome = (ToggleButton) findViewById(R.id.tbtn_outcome);
            tbtn_Outcome.setOnClickListener(SeesawDialog.this);

            rbtn_OpponentOwned = (RadioButton) findViewById(R.id.rbtn_opponentOwned);
            rbtn_Balanced = (RadioButton) findViewById(R.id.rbtn_balanced);
            rbtn_OpponentOwned.setOnClickListener(SeesawDialog.this);
            rbtn_Balanced.setOnClickListener(SeesawDialog.this);

            rbtn_Layer1 = (RadioButton) findViewById(R.id.rbtn_layer1);
            rbtn_Layer2 = (RadioButton) findViewById(R.id.rbtn_layer2);
            rbtn_Layer3 = (RadioButton) findViewById(R.id.rbtn_layer3);
            rbtn_Layer1.setOnClickListener(SeesawDialog.this);
            rbtn_Layer2.setOnClickListener(SeesawDialog.this);
            rbtn_Layer3.setOnClickListener(SeesawDialog.this);
        }
    }
}

//public class OverrideDialog extends Dialog implements View.OnClickListener {
//
//    public Activity context;
//
//    public OverrideDialog(Activity a){
//        super(a);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.dialog_seesaws);
//
//    }
//
//    public void onClick(View v){
//        switch (v.getId()) {
//            case R.id.tbtn_outcome:
//                break;
//        }
//    }
//}