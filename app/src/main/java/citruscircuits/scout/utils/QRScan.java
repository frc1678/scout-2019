package citruscircuits.scout.utils;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.hardware.camera2.CameraManager;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import citruscircuits.scout.A0A;
import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout.R;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;

public class QRScan extends DialogMaker implements QRCodeReaderView.OnQRCodeReadListener{

    QRCodeReaderView qrCodeReader;

    public String resultStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 300);
        }

        qrCodeReader = (QRCodeReaderView) findViewById(R.id.scan_area);
        qrCodeReader.setOnQRCodeReadListener(QRScan.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 300:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initCam();
                }
                return;
        }

        AppUtils.makeToast(this, "CAMERA PERMISSIONS WERE DENIED", 50);
        open(A0A.class, null, true, false);
    }

    public void onBackClick(View v){
        open(A0A.class, null, true, false);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        resultStr = text;
        String prevStr = AppCc.getSp("resultStr", "");

        if(!resultStr.contains("|")) {
            AppUtils.makeToast(this, "The QR Code is wrong, no PIPE!", 50);

            resultStr = prevStr;
            return;
        }

        int cycleNum = 0;
        try { cycleNum = AppUtils.StringToInt(resultStr.substring(0, resultStr.indexOf("|"))); }
        catch (Exception e){ e.printStackTrace(); }

        if(!prevStr.equals("")){
            int prevCycleNum = AppUtils.StringToInt(prevStr.substring(0, prevStr.indexOf("|")));;

            if(prevCycleNum >= cycleNum){
                AppUtils.makeToast(this, "QR's CYCLE NUMBER: " + cycleNum + ", Your CYCLE NUMBER: " + prevCycleNum, 50);

                resultStr = prevStr;
                cycleNum = prevCycleNum;
            } else if(cycleNum <= 0) {
                AppUtils.makeToast(this, "INVALID CYCLE NUMBER:" + cycleNum, 50);

                resultStr = prevStr;
                cycleNum = prevCycleNum;
            }else{
                AppUtils.makeToast(this,"CYCLE NUMBER: " + cycleNum, 50);
            }
        }else{
            try{
                AppUtils.makeToast(this, "CYCLE NUMBER: " + cycleNum, 50);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        InputManager.mCycleNum = cycleNum;
        AppCc.setSp("resultStr", resultStr);
        AppCc.setSp("cycleNum", cycleNum);

        open(A0A.class, null, true, false);
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

    private void initCam() throws NullPointerException{
        qrCodeReader.setQRDecodingEnabled(true);
        qrCodeReader.setTorchEnabled(true);
        qrCodeReader.setAutofocusInterval(3000L);

        if(InputManager.mScoutId >= 7){
            qrCodeReader.setBackCamera();
        }else{
            qrCodeReader.setFrontCamera();
        }
    }
}
