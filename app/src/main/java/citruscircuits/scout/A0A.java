package citruscircuits.scout;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout.utils.QRScan;

//Written by the Daemon himself ~ Calvin
public class A0A extends DialogMaker {

    public static Drawable dr_redCycle, dr_blueCycle;

    Button btn_triggerBackupPopup, btn_triggerScoutIDPopup, btn_triggerScoutNamePopup;

    public static ImageView imgv_cycleBackground;

    PopupWindow pw_backupWindow, pw_idWindow, pw_nameWindow;

    public static EditText et_matchNum;

    public static TextView tv_cycleNum, tv_teamNum;

    LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dr_redCycle = getResources().getDrawable(R.drawable.cycle_red_dashed_background);
        dr_blueCycle = getResources().getDrawable(R.drawable.cycle_blue_dashed_background);

        initViews();

        mLayoutInflater = (LayoutInflater) A0A.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        btn_triggerBackupPopup = (Button) findViewById(R.id.btn_triggerBackupPopup);
        pw_backupWindow = new PopupWindow((LinearLayout) mLayoutInflater.inflate(R.layout.popup_backup, null), 340, 300, true);
        pw_backupWindow.setBackgroundDrawable(new ColorDrawable());
        btn_triggerBackupPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw_backupWindow.showAtLocation((RelativeLayout) findViewById(R.id.user_layout), Gravity.LEFT,0, 0);
            }
        });
    }

    public void onClickStartScouting(View view){
        open(A1A.class, null,false, true);
    }

    public void onClickQrBackup(View view) {
        open(QRScan.class, null, false, false);
        pw_backupWindow.dismiss();
    }

    public void onClickFileBackup(View view) { pw_backupWindow.dismiss(); }

    public void onClickOverrideBackup(View view) {
        initOverrideDialog(A0A.this);
        pw_backupWindow.dismiss();
    }

    public void initViews(){
        imgv_cycleBackground = findViewById(R.id.imgv_cycleNumBackground);

        et_matchNum = findViewById(R.id.et_matchNum);

        tv_cycleNum = findViewById(R.id.tv_cycleNum);
        tv_teamNum = findViewById(R.id.tv_teamNum);

    }

    public static void updateUserData(){
        setCycleBackgroundColor(InputManager.mAllianceColor);
        et_matchNum.setText(String.valueOf(InputManager.mMatchNum));
        tv_cycleNum.setText(String.valueOf(InputManager.mCycleNum));
        tv_teamNum.setText(String.valueOf(InputManager.mTeamNum));

        //TODO update scout Name and ID too
    }

    public static void setCycleBackgroundColor(String color){
        switch (color){
            case "Red":
                imgv_cycleBackground.setBackground(dr_redCycle);
                break;
            case "Blue":
                imgv_cycleBackground.setBackground(dr_blueCycle);
                break;
        }
    }

//        Butt on scoutNameButton;
//        Button scoutIDButton;
//        Button backupButton;
//        PopupWindow popupWindow;
//        RelativeLayout relativeLayout;
//        LayoutInflater layoutInflater;
//        private final MainActivity context=this;
//        @Override
//        protected void onCreate (Bundle savedInstanceState){
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//            scoutNameButton= (Button) findViewById(R.id.scoutNameButton);
//            scoutNameButton.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view){
//                    PopupMenu namePopupMenu= new PopupMenu(MainActivity.this, scoutNameButton);
//                    namePopupMenu.getMenuInflater().inflate(R.menu.name_popup_menu, namePopupMenu.getMenu());
//                    namePopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item){
//                            scoutNameButton.setText(item.getTitle());
//                            return true;
//                        }
//                    });
//                    namePopupMenu.show();
//                }
//            });
//            scoutIDButton= (Button) findViewById(R.id.scoutIDButton);
//            scoutIDButton.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view){
//                    PopupMenu idPopupMenu= new PopupMenu(MainActivity.this, scoutIDButton);
//                    idPopupMenu.getMenuInflater().inflate(R.menu.number_popup_menu, idPopupMenu.getMenu());
//                    idPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item){
//                            scoutIDButton.setText(item.getTitle());
//                            return true;
//                        }
//                    });
//                    idPopupMenu.show();
//                }
//            });
//            backupButton=(Button) findViewById(R.id.backUpButton);
//            relativeLayout= (RelativeLayout) findViewById(R.id.mainActivity);
//            backupButton.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view){
////                    Intent intent=new Intent(MainActivity.this, backupUI.class);
////                    MainActivity.this.startActivity(intent);
//                    layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//                    ViewGroup container= (ViewGroup) layoutInflater.inflate(R.layout.backupbutton,null);
//                    popupWindow = new PopupWindow(container, 600, 600, true);
//                    popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 90, 360);
//                    container.setOnTouchListener(new View.OnTouchListener(){
//                        @Override
//                        public boolean onTouch(View view, MotionEvent motionEvent){
//                            popupWindow.dismiss();
//                            return true;
//                        }
//                    });
////Hi Ben
//                }
//            });
//        }

}

