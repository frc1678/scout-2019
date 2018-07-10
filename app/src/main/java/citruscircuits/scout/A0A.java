package citruscircuits.scout;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout.utils.QRScan;

//Written by the Daemon himself ~ Calvin
public class A0A extends DialogMaker {

    Button btn_triggerBackupPopup, btn_triggerScoutIDPopup, btn_triggerScoutNamePopup;
    PopupWindow pw_backupWindow, pw_idWindow, pw_nameWindow;

    LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        pw_backupWindow.dismiss();

    }

//        Button scoutNameButton;
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

