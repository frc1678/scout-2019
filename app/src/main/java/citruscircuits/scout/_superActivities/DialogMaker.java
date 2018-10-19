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
public class DialogMaker extends AppTc {

    public void initOverrideDialog(Activity a) {
        OverrideDialog mOverrideDialog = new OverrideDialog(a);
        mOverrideDialog.show();
    }

    public class OverrideDialog extends Dialog {

        private Activity context;

        private EditText et_overrideTeamNum;
        private RadioButton rb_red;
        private RadioButton rb_blue;

        public OverrideDialog(Activity a) {
            super(a);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_override);

            OverrideDialog.this.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    try {
                        InputManager.mTeamNum = AppUtils.StringToInt(et_overrideTeamNum.getText().toString());
                    } catch (Exception e) {
                    }

                    if(rb_red.isChecked()){
                        InputManager.mAllianceColor = "red";
                    }else if(rb_blue.isChecked()){
                        InputManager.mAllianceColor = "blue";
                    }

                    InputManager.storeUserData();
                    A0A.updateUserData();
                }
            });

            initViews();
        }

        public void initViews() {
            et_overrideTeamNum = findViewById(R.id.et_overrideTeamNum);

            rb_red = findViewById(R.id.red);
            rb_blue = findViewById(R.id.blue);
        }
    }
}