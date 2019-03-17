package citruscircuits.scout.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import citruscircuits.scout.A0A;
import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout.R;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout._superDataClasses.Cst;

public class QRScan extends DialogMaker implements QRCodeReaderView.OnQRCodeReadListener{

    Spinner name_spinner;

    QRCodeReaderView qrCodeReader;

    public String resultStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 300);
        }

        qrCodeReader = (QRCodeReaderView) findViewById(R.id.scan_area);
        qrCodeReader.setOnQRCodeReadListener(QRScan.this);

        initCam();
    }

    //Request camera permissions if not already granted.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 300:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initCam();
                }
                return;
        }

        AppUtils.makeToast(this, "CAMERA PERMISSIONS WERE DENIED", 50);
        open(A0A.class, null, true, false);
    }

    //Initiates the camera
    private void initCam() throws NullPointerException {
        qrCodeReader.setQRDecodingEnabled(true);
        qrCodeReader.setTorchEnabled(true);
        qrCodeReader.setAutofocusInterval(3000L);

        if (InputManager.mTabletType.equals("black") || InputManager.mTabletType.equals("fire")) {
            qrCodeReader.setBackCamera();
        }else{
            qrCodeReader.setFrontCamera();
        }
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        alertScout();

        //Set QRString to scanned QR in order to retrieve scout SPR Ranking.
        resultStr = text;
        String prevStr = AppCc.getSp("resultStr", "");

        //Update assigned robot based on scout name and newly scanned QR.
        InputManager.getQRAssignment(resultStr);

        if (!resultStr.contains("|")) {
            AppUtils.makeToast(this, "The QR Code is wrong, no PIPE!", 50);

            resultStr = prevStr;
            return;
        }

        int cycleNum = 0;
        try {
            cycleNum = AppUtils.StringToInt(resultStr.substring(0, resultStr.indexOf("|")));
        }
        catch (Exception e) { e.printStackTrace();
        }

        if (!prevStr.equals("")) {
            int prevCycleNum = AppUtils.StringToInt(prevStr.substring(0, prevStr.indexOf("|")));

            if (prevCycleNum >= cycleNum) {
                AppUtils.makeToast(this, "QR's CYCLE NUMBER: " + cycleNum + ", Your CYCLE NUMBER: " + prevCycleNum, 50);

                resultStr = prevStr;
                cycleNum = prevCycleNum;
            }
            else if (cycleNum <= 0) {
                AppUtils.makeToast(this, "INVALID CYCLE NUMBER:" + cycleNum, 50);

                resultStr = prevStr;
                cycleNum = prevCycleNum;
            }
            else {
                AppUtils.makeToast(this,"CYCLE NUMBER: " + cycleNum, 50);
            }
        }
        else {
            try {
                AppUtils.makeToast(this, "CYCLE NUMBER: " + cycleNum, 50);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        InputManager.mCycleNum = cycleNum;
        Log.e("QR STRING IN SCAN", resultStr);

        AppCc.setSp("resultStr", resultStr);
        AppCc.setSp("cycleNum", cycleNum);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReader.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReader.stopCamera();
    }

    //Alert scout of QR scan, make spinner appear for scout to confirm/select name.
    public void alertScout() {
        Log.i("SCOUTNAME", InputManager.mScoutName);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_name_confirmation, null);
        TextView nameView= (TextView) dialogView.findViewById(R.id.nameView);
        nameView.setText(InputManager.mScoutName);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Cst.SCOUT_NAMES);
        name_spinner = (Spinner) dialogView.findViewById(R.id.nameList);

        name_spinner.setAdapter(spinnerAdapter);
        name_spinner.setSelection(((ArrayAdapter<String>)name_spinner.getAdapter()).getPosition(InputManager.mScoutName));

        name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long arg3) {
                //Do nothing, but necessary for spinner
            }
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing, but necessary for spinner
            }
        });

        if (InputManager.mScoutName != null) {
            nameView.setText(InputManager.mScoutName);
        }
        else {
            nameView.setText("");
        }

        //Create and display name selection/confirmation alert dialog.
        AlertDialog scoutNameAlertDialog;
        scoutNameAlertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("")
                .setMessage("Are you this person?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        InputManager.mScoutName = name_spinner.getSelectedItem().toString();
                        AppCc.setSp("scoutName", InputManager.mScoutName);
                        InputManager.mAssignmentMode = "QR";
                        AppCc.setSp("assignmentMode", InputManager.mAssignmentMode);
                        open(A0A.class, null, true, false);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
        scoutNameAlertDialog.show();
        scoutNameAlertDialog.setCanceledOnTouchOutside(false);
    }

    public void onBackClick(View v) {
        open(A0A.class, null, true, false);
    }
}
