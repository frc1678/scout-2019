package citruscircuits.scout._superActivities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

    public void initTabletTypeDialog(Activity a) {
        TabletDialog mTabletDialog = new TabletDialog(a);
        mTabletDialog.show();
    }

    public class OverrideDialog extends Dialog {

        private Activity context;

        private EditText et_overrideTeamNum;
        private RadioButton rb_red;
        private RadioButton rb_blue;
        private Button btn_done;

        public OverrideDialog(Activity a) {
            super(a);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_override);
            this.setCanceledOnTouchOutside(false);

            btn_done = findViewById(R.id.btn_done);

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

            btn_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputManager.mAssignmentMode = "override";
                    AppCc.setSp("assignmentMode", InputManager.mAssignmentMode);
                    OverrideDialog.this.cancel();
                }
            });
        }

        public void initViews() {
            et_overrideTeamNum = findViewById(R.id.et_overrideTeamNum);

            rb_red = findViewById(R.id.red);
            rb_blue = findViewById(R.id.blue);
        }
    }

    public class TabletDialog extends Dialog {

        private Activity context;

        private RadioButton rb_greenTablet;
        private RadioButton rb_blackTablet;
        private RadioButton rb_fireTablet;
        private Button btn_done;

        public TabletDialog(Activity a) {
            super(a);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_tablet_type);
            this.setCanceledOnTouchOutside(false);

            btn_done = findViewById(R.id.btn_done);

            TabletDialog.this.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if(rb_blackTablet.isChecked()){
                        InputManager.mTabletType = "black";
                    }else if(rb_greenTablet.isChecked()){
                        InputManager.mTabletType = "green";
                    }else if(rb_fireTablet.isChecked()){
                        InputManager.mTabletType = "fire";
                    }

                    InputManager.storeUserData();
                    AppCc.setSp("tabletType", InputManager.mTabletType);
                    A0A.updateUserData();
                }
            });

            initViews();

            btn_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {TabletDialog.this.cancel();
                }
            });
        }

        public void initViews() {
            rb_greenTablet = findViewById(R.id.green_tablet);
            rb_blackTablet = findViewById(R.id.black_tablet);
            rb_fireTablet = findViewById(R.id.fire_tablet);
        }
    }
}