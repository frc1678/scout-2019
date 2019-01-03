package citruscircuits.scout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout.Managers.OutputManager;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout._superDataClasses.Cst;
import citruscircuits.scout.utils.AppUtils;
import citruscircuits.scout.utils.QRScan;

import static citruscircuits.scout.utils.AppUtils.readFile;

//Written by the Daemon himself ~ Calvin
public class A0A extends DialogMaker {

    public static Drawable dr_redCycle, dr_blueCycle;
    public Drawable map_orientation_rb, map_orientation_br;

    public static Button btn_triggerBackupPopup;

    public Button btn_mapOrientation;

    public static ImageView imgv_cycleBackground;
    public static ImageView QRImage;

    PopupWindow pw_backupWindow, pw_nameWindow, pw_idWindow, pw_resendMatchWindow;

    public ListView lv_scoutNames, lv_scoutIds, lv_resendMatch;
    public ScoutNameListAdapter mScoutNameListAdapter;
    public ScoutIdListAdapter mScoutIdListAdapter;

    //user data UI
    public static EditText et_matchNum;
    public static TextView tv_cycleNum, tv_teamNum;
    public static Button btn_triggerScoutNamePopup, btn_triggerScoutIDPopup, btn_triggerResendMatches;

    public ArrayAdapter<String> mResendMatchesArrayAdapter;

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

        InputManager.getScoutNames();

        initViews();
        initPopups();
        initListeners();

        InputManager.recoverUserData();
        updateUserData();

        updateListView();
        listenForResendClick();
    }

    //OnClick Methods
    public void onClickStartScouting(View view){
        open(A1A.class, null,false, true);
    }

    public static void updateUserData(){
        setCycleBackgroundColor(InputManager.mAllianceColor);
        et_matchNum.setText(String.valueOf(InputManager.mMatchNum + 1));
        tv_cycleNum.setText(String.valueOf(InputManager.mCycleNum));
        tv_teamNum.setText(String.valueOf(InputManager.mTeamNum));
        btn_triggerScoutNamePopup.setText(InputManager.mScoutName);
        btn_triggerScoutIDPopup.setText(String.valueOf(InputManager.mScoutId));
    }

    public static void setCycleBackgroundColor(String color){
        switch (color){
            case "red":
                imgv_cycleBackground.setBackground(dr_redCycle);
                break;
            case "blue":
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

    public void updateUserViews(){
        try{//TODO make sure that InputManager's recover user data is called when restarting activity
            et_matchNum.setText(InputManager.mMatchNum);
            tv_teamNum.setText(InputManager.mTeamNum);
            tv_cycleNum.setText(InputManager.mCycleNum);

            btn_triggerScoutNamePopup.setText(InputManager.mScoutName);
            btn_triggerScoutIDPopup.setText(InputManager.mScoutId);
        }catch(NullPointerException ne){
            ne.printStackTrace();
        }
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

                //TODO update values automatically if in backup/QR mode
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

        btn_triggerScoutIDPopup.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pw_idWindow.showAtLocation((RelativeLayout) findViewById(R.id.user_layout), Gravity.RIGHT,0, 0);
                mScoutIdListAdapter.notifyDataSetChanged();
                return true;
            }
        });

        //RESEND MATCHES POPUP
        btn_triggerResendMatches = (Button) findViewById(R.id.btn_accessData);
        LinearLayout resendMatchesLayout = (LinearLayout) mLayoutInflater.inflate(R.layout.popup_resend, null);
        pw_resendMatchWindow = new PopupWindow(resendMatchesLayout, 400, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pw_resendMatchWindow.setBackgroundDrawable(new ColorDrawable());
        lv_resendMatch = resendMatchesLayout.findViewById(R.id.lv_resendMatches);
        mResendMatchesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lv_resendMatch.setAdapter(mResendMatchesArrayAdapter);
        btn_triggerResendMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw_resendMatchWindow.showAtLocation((RelativeLayout) findViewById(R.id.user_layout), Gravity.LEFT,0, 0);
                mResendMatchesArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onClickQrBackup(View view) {
        open(QRScan.class, null, false, false);
        pw_backupWindow.dismiss();
    }

    public void onClickFileBackup(View view) {
        InputManager.getBackupData();

//        updateUserViews();
        pw_backupWindow.dismiss();
    }

    public void onClickOverrideBackup(View view) {
        initOverrideDialog(A0A.this);
        pw_backupWindow.dismiss();
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

                    InputManager.fullQRDataProcess();

                    updateUserData();

                    pw_nameWindow.dismiss();
                }
            });

            return convertView;
        }
    }

    public void updateListView() {
        final File dir;
        dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/scout_data");
        if (!dir.mkdir()) {
            Log.i("File Info", "Failed to make Directory. Unimportant");
        }
        final File[] files = dir.listFiles();
        if(files == null){
            return;
        }
        mResendMatchesArrayAdapter.clear();
        Log.e("DEBUGGING", files.toString());
        for (File tmpFile : files) {
            mResendMatchesArrayAdapter.add(tmpFile.getName());
        }
        mResendMatchesArrayAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                File lhsFile = new File(dir, lhs);
                File rhsFile = new File(dir, rhs);
                Date lhsDate = new Date(lhsFile.lastModified());
                Date rhsDate = new Date(rhsFile.lastModified());
                return rhsDate.compareTo(lhsDate);
            }
        });
        mResendMatchesArrayAdapter.notifyDataSetChanged();
    }
    public void listenForResendClick(){
        lv_resendMatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                name = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/scout_data/" + name;
                final String fileName = name;
                String content = readFile(fileName);
                openQRDialog(content);
            }
        });
    }
    public void openQRDialog(String qrString){
        final Dialog qrDialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        qrDialog.setCanceledOnTouchOutside(false);
        qrDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final LinearLayout qrDialogLayout = (LinearLayout) this.getLayoutInflater().inflate(R.layout.activity_qr_display, null);
        QRImage = (ImageView) qrDialogLayout.findViewById(R.id.QRCode_Display);
        displayQR(qrString);
        Button ok = (Button) qrDialogLayout.findViewById(R.id.okButton);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrDialog.dismiss();
            }
        });
        qrDialog.setCanceledOnTouchOutside(false);
        qrDialog.setContentView(qrDialogLayout);
        qrDialog.show();
    }
    public void displayQR(String qrCode){
        try {
            //setting size of qr code
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallestDimension = width < height ? width : height;
            //setting parameters for qr code
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap =new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            createQRCode(qrCode, charset, hintMap, smallestDimension, smallestDimension);
        } catch (Exception ex) {
            Log.e("QrGenerate",ex.getMessage());
        }
    }
    public  void createQRCode(String qrCodeData,String charset, Map hintMap, int qrCodeheight, int qrCodewidth){
        try {
            //generating qr code in bitmatrix type
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            //converting bitmatrix to bitmap
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //setting bitmap to image view
            QRImage.setImageBitmap(null);
            QRImage.setImageBitmap(bitmap);
        }catch (Exception er){
            Log.e("QrGenerate",er.getMessage());
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

