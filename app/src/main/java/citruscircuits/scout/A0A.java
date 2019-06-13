package citruscircuits.scout;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout._superDataClasses.Cst;
import citruscircuits.scout.utils.AppUtils;
import citruscircuits.scout.utils.QRScan;

import static citruscircuits.scout.utils.AppUtils.readFile;

public class A0A extends DialogMaker {
    public RelativeLayout tabletDialogLayout;

    public static Drawable dr_redCycle, dr_blueCycle;
    public Drawable map_orientation_rb, map_orientation_br;

    public static Button btn_triggerBackupPopup;

    public Button btn_mapOrientation;
    public Boolean isNotFirst = false;

    public static ImageButton imgv_cycleBackground;
    public static ImageView QRImage;

    PopupWindow pw_backupWindow, pw_nameWindow, pw_idWindow, pw_resendMatchWindow;

    public ListView lv_scoutNames, lv_scoutIds, lv_resendMatch;

    //Set user data UI
    public static EditText et_matchNum;
    public static TextView tv_cycleNum, tv_teamNum, tv_versionNum, tv_assignmentFileTimestamp;
    public static Button btn_triggerResendMatches;
    public static Spinner sp_triggerScoutNamePopup, sp_triggerScoutIDPopup;

    public ArrayAdapter<String> mResendMatchesArrayAdapter;

    LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set UI of the entire activity.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dr_redCycle = getResources().getDrawable(R.drawable.main_cycle_red);
        dr_blueCycle = getResources().getDrawable(R.drawable.main_cycle_blue);

        map_orientation_rb = getResources().getDrawable(R.drawable.main_map_orientation_rb);
        map_orientation_br = getResources().getDrawable(R.drawable.main_map_orientation_br);

        mLayoutInflater = (LayoutInflater) A0A.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        tabletDialogLayout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.main_dialog_tablet_type, null);

        InputManager.mRealTimeMatchData = new JSONArray();
        InputManager.mOneTimeMatchData = new JSONObject();
        InputManager.mRealTimeInputtedData = new JSONObject();


        //Retrieve and set assignment file timestamp from assignments.txt
        String filePath = Environment.getExternalStorageDirectory().toString() + "/bluetooth";
        String fileName = "assignments.txt";

        File f = new File(filePath, fileName);

        if (f.exists()) {
            try {
                JSONObject timestamp = new JSONObject(AppUtils.retrieveSDCardFile("assignments.txt"));
                InputManager.mAssignmentFileTimestamp = timestamp.getInt("timestamp");

                Log.i("Assignment Timestamp", InputManager.mAssignmentFileTimestamp + "");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Set Assignment Mode, QR String, Scout ID, and Final Match Number from stored data
        InputManager.mAssignmentMode = AppCc.getSp("assignmentMode", "");
        InputManager.mQRString = AppCc.getSp("qrString", "");
        InputManager.finalMatchNum = AppCc.getSp("finalMatchNum", 0);
        InputManager.mScoutId = AppCc.getSp("scoutId", 0);

        InputManager.getScoutNames();
        //Calls all Views, Popups, and Listeners in Main Activity
        initViews();
        initPopups();
        initListeners();

        InputManager.recoverUserData();
        updateUserData();

        updateListView();
        listenForResendClick();

        //Display assignment type on assignment system popup button
        if (!InputManager.mAssignmentMode.equals("")) {
            btn_triggerBackupPopup.setText(InputManager.mAssignmentMode);
        }
    }

    //Set all UI text values
    public static void updateUserData() {
        setCycleBackgroundColor(InputManager.mAllianceColor);
        et_matchNum.setText(String.valueOf(InputManager.mMatchNum));
        tv_cycleNum.setText(String.valueOf(InputManager.mCycleNum));
        tv_teamNum.setText(String.valueOf(InputManager.mTeamNum));
        tv_versionNum.setText(String.valueOf("Version: " + InputManager.mAppVersion));
        tv_assignmentFileTimestamp.setText("File Timestamp: " + InputManager.mAssignmentFileTimestamp);
        sp_triggerScoutNamePopup.setSelection(((ArrayAdapter<String>)sp_triggerScoutNamePopup.getAdapter()).getPosition(InputManager.mScoutName));
        sp_triggerScoutIDPopup.setSelection(((ArrayAdapter<Integer>)sp_triggerScoutIDPopup.getAdapter()).getPosition(InputManager.mScoutId));
    }
    //Set Cycle Background color
    public static void setCycleBackgroundColor(String color) {
        switch (color) {
            case "red":
                imgv_cycleBackground.setBackground(dr_redCycle);
                break;
            case "blue":
                imgv_cycleBackground.setBackground(dr_blueCycle);
                break;
        }
    }
    //Method to set background orientation when button is pressed and to declare any textviews
    public void initViews() {
        btn_mapOrientation = findViewById(R.id.btn_map_orientation);
        if (AppCc.getSp("mapOrientation", 99) != 99) {
            if (AppCc.getSp("mapOrientation", 99) == 0) {
                btn_mapOrientation.setBackground(map_orientation_rb);
            }
            else {
                btn_mapOrientation.setBackground(map_orientation_br);
            }
        }
        else {
            AppCc.setSp("mapOrientation", 0);
            btn_mapOrientation.setBackground(map_orientation_rb);
        }

        imgv_cycleBackground = findViewById(R.id.imgv_cycleNumBackground);

        et_matchNum = findViewById(R.id.et_matchNum);

        tv_cycleNum = findViewById(R.id.tv_cycleNum);
        tv_teamNum = findViewById(R.id.tv_teamNum);
        tv_versionNum = findViewById(R.id.tv_versionNum);
        tv_assignmentFileTimestamp = findViewById(R.id.tv_fileTimestamp);
    }
    //Add listeners to map and matchNum editor.
    public void initListeners() {
        //Changes the map position when map button is held
        btn_mapOrientation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (AppCc.getSp("mapOrientation", 99) != 99) {
                    if (AppCc.getSp("mapOrientation", 99) == 0) {
                        AppCc.setSp("mapOrientation", 1);
                        btn_mapOrientation.setBackground(map_orientation_br);
                    }
                    else {
                        AppCc.setSp("mapOrientation", 0);
                        btn_mapOrientation.setBackground(map_orientation_rb);
                    }
                }
                else {
                    AppCc.setSp("mapOrientation", 0);
                    btn_mapOrientation.setBackground(map_orientation_rb);
                }
                return true;
            }
        });

        et_matchNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do nothing, necessary for TextChangedListeners.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do nothing, necessary for TextChangedListeners.
            }

            //Updates match number after altered
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    return;
                }

                int matchNum = AppUtils.StringToInt(editable.toString());

                InputManager.mMatchNum = matchNum;
                AppCc.setSp("matchNum", matchNum);

                if (InputManager.mAssignmentMode.equals("QR")) {
                    //Update assigned robot based on match number.
                    InputManager.getQRData();

                    setCycleBackgroundColor(InputManager.mAllianceColor);
                    tv_teamNum.setText(String.valueOf(InputManager.mTeamNum));
                }
                else if (InputManager.mAssignmentMode.equals("backup")) {
                    InputManager.getBackupData();

                    setCycleBackgroundColor(InputManager.mAllianceColor);
                    tv_teamNum.setText(String.valueOf(InputManager.mTeamNum));
                }
            }
        });
    }
    //Create the backup, scout name, and ID popups
    public void initPopups() {
        //Declare backup popup
        btn_triggerBackupPopup = (Button) findViewById(R.id.btn_triggerBackupPopup);
        if (InputManager.mTabletType.equals("fire")) {
            pw_backupWindow = new PopupWindow((LinearLayout) mLayoutInflater.inflate(R.layout.main_popup_dropdown_backup, null), 200, 300, true);
        }
        else if (InputManager.mTabletType.equals("black")) {
            pw_backupWindow = new PopupWindow((LinearLayout) mLayoutInflater.inflate(R.layout.main_popup_dropdown_backup, null), ViewGroup.LayoutParams.MATCH_PARENT, 300, true);
        }
        else {
            pw_backupWindow = new PopupWindow((LinearLayout) mLayoutInflater.inflate(R.layout.main_popup_dropdown_backup, null), 400, 300, true);
        }
        pw_backupWindow.setBackgroundDrawable(new ColorDrawable());
        btn_triggerBackupPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw_backupWindow.showAtLocation((RelativeLayout) findViewById(R.id.user_layout), Gravity.LEFT,0, 0);
            }
        });

        //Declare scout name popup
        sp_triggerScoutNamePopup = (Spinner) findViewById(R.id.btn_triggerScoutNamePopup);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this, R.layout.main_popup_header_name, Cst.SCOUT_NAMES);

        sp_triggerScoutNamePopup.setAdapter(nameAdapter);

        sp_triggerScoutNamePopup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                InputManager.mScoutName = sp_triggerScoutNamePopup.getSelectedItem().toString();
                AppCc.setSp("scoutName", InputManager.mScoutName);

                if (InputManager.mAssignmentMode.equals("QR")) {
                    //Update assigned robot based on new scout name
                    InputManager.getQRAssignment(InputManager.mQRString);
                }

                updateUserData();
            }
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing, but necessary for spinner
            }
        });

        //Declare Scout ID popup
        sp_triggerScoutIDPopup = (Spinner) findViewById(R.id.btn_triggerScoutIDPopup);
        ArrayAdapter<Integer> idAdapter = new ArrayAdapter<Integer>(this, R.layout.main_popup_header_id, Cst.SCOUT_IDS);

        sp_triggerScoutIDPopup.setEnabled(false);

        sp_triggerScoutIDPopup.setAdapter(idAdapter);

        imgv_cycleBackground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isNotFirst = true;
                sp_triggerScoutIDPopup.performClick();
                return true;
            }
        });
        //Saves scout ID when selected
        sp_triggerScoutIDPopup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                if (isNotFirst) {
                    InputManager.mScoutId = (int) sp_triggerScoutIDPopup.getSelectedItem();
                    AppCc.setSp("scoutId", InputManager.mScoutId);

                    if (InputManager.mAssignmentMode.equals("backup")) {
                        //Update assigned robot based on new scout ID
                        InputManager.getBackupData();
                    }

                    updateUserData();

                    initTabletTypeDialog(A0A.this);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing, but necessary for spinner
            }
        });

        //Declare all resend popups
        btn_triggerResendMatches = (Button) findViewById(R.id.btn_accessData);
        LinearLayout resendMatchesLayout = (LinearLayout) mLayoutInflater.inflate(R.layout.main_popup_dropdown_resend, null);
        if (InputManager.mTabletType.equals("fire")) {
            pw_resendMatchWindow = new PopupWindow(resendMatchesLayout, 100, ViewGroup.LayoutParams.MATCH_PARENT, true);
        } else if (InputManager.mTabletType.equals("black")) {
            pw_resendMatchWindow = new PopupWindow(resendMatchesLayout, 400, ViewGroup.LayoutParams.MATCH_PARENT, true);
        } else {
            pw_resendMatchWindow = new PopupWindow(resendMatchesLayout, 450, ViewGroup.LayoutParams.MATCH_PARENT, true);
        }
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

    //Open QR scanner when clicked
    public void onClickQrBackup(View view) {
        open(QRScan.class, null, false, false);
        pw_backupWindow.dismiss();
    }

    //Set backup values in file when backup pressed
    public void onClickFileBackup(View view) {
        InputManager.getBackupData();

        setCycleBackgroundColor(InputManager.mAllianceColor);
        tv_teamNum.setText(String.valueOf(InputManager.mTeamNum));
        pw_backupWindow.dismiss();
        btn_triggerBackupPopup.setText("Backup");
    }

    //Opens override dialog when pressed
    public void onClickOverrideBackup(View view) {
        initOverrideDialog(A0A.this);
        pw_backupWindow.dismiss();
    }

    //Updates resend dropdown information and formats it
    public void updateListView() {
        final File dir;
        dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/scout_data");
        if (!dir.mkdir()) {
            Log.i("File Exists", "Failed to make Directory. Unimportant");
        }

        final File[] files = dir.listFiles();

        if (files == null) {
            return;
        }

        mResendMatchesArrayAdapter.clear();

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

    //Opens previous match data when a resend is clicked
    public void listenForResendClick() {
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
    //Creates the layout and opens a QR dialog
    public void openQRDialog(String qrString) {
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
    //Creates dimensions for QR code and opens it
    public void displayQR(String qrCode) {
        try {
            //Set size of QR code.
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);

            int width = point.x;
            int height = point.y;
            int smallestDimension = width < height ? width : height;

            //Set parameters for QR code.
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap =new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            createQRCode(qrCode, charset, hintMap, smallestDimension, smallestDimension);
        }
        catch (Exception ex) {
            Log.e("QrGenerate",ex.getMessage());
        }
    }

    //Sets specifications to create the QR code
    public void createQRCode(String qrCodeData,String charset, Map hintMap, int qrCodeheight, int qrCodewidth) {
        try {
            //Generates qr code in bitmatrix type
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);

            //Converts bitmatrix to bitmap
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

            //Set bitmap to image view.
            QRImage.setImageBitmap(null);
            QRImage.setImageBitmap(bitmap);
        }
        catch (Exception er) {
            Log.e("QrGenerate", er.getMessage());
        }
    }
    //Button to move to the pregame activity
    public void onClickStartScouting(View view) {
        if (InputManager.mTabletType.equals("") || InputManager.mScoutName.equals("unselected") || InputManager.mTabletType.equals("unselected") || InputManager.mMatchNum == 0 || InputManager.mTeamNum == 0 || InputManager.mScoutId == 0) {
            Toast.makeText(getBaseContext(), "There is null information!", Toast.LENGTH_SHORT).show();
        }
        else if (InputManager.mMatchNum > InputManager.finalMatchNum && !InputManager.mAssignmentMode.equals("override")) {
            //Doesn't let a Scout scout if they have a match number that doesn't exist
            Toast.makeText(getBaseContext(), "Please Input a Valid Match Number", Toast.LENGTH_SHORT).show();
        }
        else {
            open(A0B.class, null, false, true);
        }
    }
}

