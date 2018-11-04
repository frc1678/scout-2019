package citruscircuits.scout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout.Managers.OutputManager;
import citruscircuits.scout._superActivities.DialogMaker;

import static java.lang.String.valueOf;

public class A2A extends DialogMaker {

    EditText et_matchNum;
    EditText et_teamNum;

    RadioButton rb_right_sp;
    RadioButton rb_center_sp;
    RadioButton rb_left_sp;

    ImageView tQRView;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacheck);
        context = this;

        et_matchNum = findViewById(R.id.matchET);
        et_teamNum = findViewById(R.id.teamET);

        rb_right_sp = findViewById(R.id.right);
        rb_center_sp = findViewById(R.id.center);
        rb_left_sp = findViewById(R.id.left);

        et_matchNum.setText(valueOf(InputManager.mMatchNum));
        et_teamNum.setText(valueOf(InputManager.mTeamNum));

        if(InputManager.mStartingPosition.equals("right")) {
            rb_right_sp.setChecked(true);
        }
        else if(InputManager.mStartingPosition.equals("center")) {
            rb_center_sp.setChecked(true);
        }
        else if(InputManager.mStartingPosition.equals("left")) {
            rb_left_sp.setChecked(true);
        }
    }

    //TODO make sure it works after data is inputted
    public void onClickUpdateQR(View view) throws JSONException{
        InputManager.mRealTimeInputtedData = new JSONObject();
        InputManager.mRealTimeInputtedData.put(InputManager.matchKey, InputManager.mRealTimeMatchData);
        Log.e("Match DATA before COMP", InputManager.mRealTimeMatchData.toString());
        Log.e("Input DATA before COMP", InputManager.mRealTimeInputtedData.toString());
        showMatchQR(OutputManager.compressMatchData(InputManager.mRealTimeInputtedData)); }

    public void onClickEndScouting(View view) { open(A0A.class, null, false, false); }

    public void showMatchQR(String qrString){
        tQRView = (ImageView) findViewById(R.id.qrView);
        displayQR(qrString);
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
            tQRView.setImageBitmap(null);
            tQRView.setImageBitmap(bitmap);
        }catch (Exception er){
            Log.e("QrGenerate",er.getMessage());
        }
    }

    public void onBackPressed() {
        final Activity activity = this;
        new AlertDialog.Builder(this)
                .setTitle("WARNING")
                .setMessage("GOING BACK WILL CAUSE LOSS OF DATA")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}