package citruscircuits.scout._superActivities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import citruscircuits.scout.MainActivity;
import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout.R;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout.utils.AppUtils;

public class DialogMaker extends AppTc {

    //Shows the override dialog
    public void initOverrideDialog(Activity a) {
        OverrideDialog mOverrideDialog = new OverrideDialog(a);
        mOverrideDialog.show();
    }
    //Shows the tablet type dialog
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

            //Declare formatting for overrride dialog.
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.main_dialog_override);
            this.setCanceledOnTouchOutside(false);

            btn_done = findViewById(R.id.btn_done);

            OverrideDialog.this.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    //Sets team number to whatever is selected in override
                    try {
                        InputManager.mTeamNum = AppUtils.StringToInt(et_overrideTeamNum.getText().toString());
                    } catch (Exception e) {
                    }

                    //Sets team color to whatever is selected in override
                    if(rb_red.isChecked()){
                        InputManager.mAllianceColor = "red";
                    }else if(rb_blue.isChecked()){
                        InputManager.mAllianceColor = "blue";
                    }

                    //Saves data inputted in override
                    InputManager.storeUserData();
                    MainActivity.updateUserData();
                }
            });

            initViews();
            //Saves assignment mode as override when done button is pressed
            btn_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputManager.mAssignmentMode = "override";
                    AppCc.setSp("assignmentMode", "override");
                    MainActivity.btn_triggerBackupPopup.setText("Override");
                    OverrideDialog.this.cancel();
                }
            });
        }
        //Format for all views in override dialog
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
            //Declares tabletDialog format
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.main_dialog_tablet_type);
            this.setCanceledOnTouchOutside(false);

            btn_done = findViewById(R.id.btn_done);

            //Saves tablet type as one of three tablet types
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
                    MainActivity.updateUserData();
                }
            });

            initViews();

            btn_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {TabletDialog.this.cancel();
                }
            });
        }
        //Creates buttons for green, black, and fire tablet in tabletDialog
        public void initViews() {
            rb_greenTablet = findViewById(R.id.green_tablet);
            rb_blackTablet = findViewById(R.id.black_tablet);
            rb_fireTablet = findViewById(R.id.fire_tablet);
        }
    }
}