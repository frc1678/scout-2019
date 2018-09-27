package citruscircuits.scout;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout._superDataClasses.Cst;
import citruscircuits.scout.utils.AppUtils;
import citruscircuits.scout.utils.QRScan;

//Written by the Daemon himself ~ Calvin
public class A0A extends DialogMaker {

    public static Drawable dr_redCycle, dr_blueCycle;
    public Drawable map_orientation_rb, map_orientation_br;

    public static Button btn_triggerBackupPopup, btn_triggerScoutNamePopup, btn_triggerScoutIDPopup;

    public Button btn_mapOrientation;

    public static ImageView imgv_cycleBackground;

    PopupWindow pw_backupWindow, pw_nameWindow, pw_idWindow;

    public ListView lv_scoutNames, lv_scoutIds;
    public ScoutNameListAdapter mScoutNameListAdapter;
    public ScoutIdListAdapter mScoutIdListAdapter;

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
        map_orientation_rb = getResources().getDrawable(R.drawable.btn_map_orientation_rb);
        map_orientation_br = getResources().getDrawable(R.drawable.btn_map_orientation_br);

        mLayoutInflater = (LayoutInflater) A0A.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        initViews();
        initPopups();
        initListeners();

        InputManager.recoverUserData();
        updateUserData();
    }

    //OnClick Methods
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

    public static void updateUserData(){
        setCycleBackgroundColor(InputManager.mAllianceColor);
        et_matchNum.setText(String.valueOf(InputManager.mMatchNum));
        tv_cycleNum.setText(String.valueOf(InputManager.mCycleNum));
        tv_teamNum.setText(String.valueOf(InputManager.mTeamNum));
        btn_triggerScoutNamePopup.setText(InputManager.mScoutName);
        btn_triggerScoutIDPopup.setText(String.valueOf(InputManager.mScoutId));
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

    public void initViews(){
        btn_mapOrientation = findViewById(R.id.btn_map_orientation);
        if(AppCc.getSp("mapOrientation",99) != 99){
            if(AppCc.getSp("mapOrientation", 99) == 0){
                btn_mapOrientation.setBackground(map_orientation_rb);
            }else {
                btn_mapOrientation.setBackground(map_orientation_br);
            }
        }else{
            AppCc.setSp("mapOrientation", 0);
            btn_mapOrientation.setBackground(map_orientation_rb);
        }

        imgv_cycleBackground = findViewById(R.id.imgv_cycleNumBackground);

        et_matchNum = findViewById(R.id.et_matchNum);

        tv_cycleNum = findViewById(R.id.tv_cycleNum);
        tv_teamNum = findViewById(R.id.tv_teamNum);

    }

    public void initListeners(){
        btn_mapOrientation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(AppCc.getSp("mapOrientation",99) != 99){
                    if(AppCc.getSp("mapOrientation", 99) == 0){
                        AppCc.setSp("mapOrientation", 1);
                        btn_mapOrientation.setBackground(map_orientation_br);
                    }else {
                        AppCc.setSp("mapOrientation", 0);
                        btn_mapOrientation.setBackground(map_orientation_rb);
                    }
                }else{
                    AppCc.setSp("mapOrientation", 0);
                    btn_mapOrientation.setBackground(map_orientation_rb);
                }
                return true;
            }
        });

        et_matchNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    return;
                }

                int matchNum = AppUtils.StringToInt(editable.toString());

                InputManager.mMatchNum = matchNum;
                AppCc.setSp("matchNum", matchNum);
            }
        });
    }

    public void initPopups(){
        //BACKUP POPUP
        btn_triggerBackupPopup = (Button) findViewById(R.id.btn_triggerBackupPopup);
        pw_backupWindow = new PopupWindow((LinearLayout) mLayoutInflater.inflate(R.layout.popup_backup, null), 400, 300, true);
        pw_backupWindow.setBackgroundDrawable(new ColorDrawable());
        btn_triggerBackupPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw_backupWindow.showAtLocation((RelativeLayout) findViewById(R.id.user_layout), Gravity.LEFT,0, 0);
            }
        });

        //SCOUT NAME POPUP
        btn_triggerScoutNamePopup = (Button) findViewById(R.id.btn_triggerScoutNamePopup);
        LinearLayout nameLayout = (LinearLayout) mLayoutInflater.inflate(R.layout.popup_scout_name, null);
        pw_nameWindow = new PopupWindow(nameLayout, 400, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pw_nameWindow.setBackgroundDrawable(new ColorDrawable());

        lv_scoutNames = nameLayout.findViewById(R.id.lv_scoutNames);
        mScoutNameListAdapter = new ScoutNameListAdapter();
        lv_scoutNames.setAdapter(mScoutNameListAdapter);

        btn_triggerScoutNamePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw_nameWindow.showAtLocation((RelativeLayout) findViewById(R.id.user_layout), Gravity.RIGHT,200, 0);
                mScoutNameListAdapter.notifyDataSetChanged();
            }
        });

        //SCOUT ID POPUP
        btn_triggerScoutIDPopup = (Button) findViewById(R.id.btn_triggerScoutIDPopup);
        LinearLayout idLayout = (LinearLayout) mLayoutInflater.inflate(R.layout.popup_scout_ids, null);
        pw_idWindow = new PopupWindow(idLayout, 200, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pw_idWindow.setBackgroundDrawable(new ColorDrawable());

        lv_scoutIds = idLayout.findViewById(R.id.lv_scoutIds);
        mScoutIdListAdapter = new ScoutIdListAdapter();
        lv_scoutIds.setAdapter(mScoutIdListAdapter);

        btn_triggerScoutIDPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw_idWindow.showAtLocation((RelativeLayout) findViewById(R.id.user_layout), Gravity.RIGHT,0, 0);
                mScoutIdListAdapter.notifyDataSetChanged();
            }
        });
    }

    public class ScoutNameListAdapter extends BaseAdapter{

        public ScoutNameListAdapter(){
        }

        @Override
        public int getCount() {
            return Cst.SCOUT_NAMES.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return Cst.SCOUT_NAMES.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            convertView = mLayoutInflater.inflate(R.layout.cell_scout_name, null);

            final String name = getItem(position).toString();

            final Button nameButton = convertView.findViewById(R.id.btn_nameCell);
            nameButton.setText(name);
            nameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputManager.mScoutName = name;
                    AppCc.setSp("scoutName", name);

                    updateUserData();

                    pw_nameWindow.dismiss();
                }
            });

            return convertView;
        }
    }

    public class ScoutIdListAdapter extends BaseAdapter{

        public ScoutIdListAdapter(){
        }

        @Override
        public int getCount() {
            return Cst.SCOUT_IDS.size();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return Cst.SCOUT_IDS.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            convertView = mLayoutInflater.inflate(R.layout.cell_scout_id, null);

            final int id = (Integer) getItem(position);

            final Button idButton = convertView.findViewById(R.id.btn_idCell);
            idButton.setText(String.valueOf(id));
            idButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputManager.mScoutId = id;
                    AppCc.setSp("scoutId", id);

                    updateUserData();

                    pw_idWindow.dismiss();
                }
            });

            return convertView;
        }
    }
}

