package citruscircuits.scout.utils;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppUtils {
    public static File bluetoothDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/bluetooth");

    public static JSONObject toJSONObject(String data){
        if(data.isEmpty()){
            return new JSONObject();
        }
        try{
            return new JSONObject(data);
        }catch(JSONException je){
            je.printStackTrace();
            return new JSONObject();
        }
    }

    public static int StringToInt(String s){
        return Integer.parseInt(s);
    }

    public static void makeToast(Context context, String text, int size){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        setToastSize(toast, size);
        toast.show();
    }

    public static void setToastSize(Toast toast, int size){
        ViewGroup vGroup = (ViewGroup) toast.getView();
        TextView toastTxt = (TextView) vGroup.getChildAt(0);
        toastTxt.setTextSize(size);
    }

    public static String retrieveSDCardFile(String pFileName){
        Log.e("Retrieve Called", pFileName);

        if (!bluetoothDir.exists()) {
            bluetoothDir.mkdir();
        }
        final File[] files = bluetoothDir.listFiles();

        Log.e("NULL CHECK! 1", files.toString());

        try{
            if(!(files == null)){
                for(File tfile: files){
                    if(tfile.getName().equals(pFileName)){
                        return readFile(tfile.getPath());
                    }
                }
            }
        }catch(NullPointerException ne){
            Log.e("NULL POINTER", "getting file path");
        }

        return null;
    }

    public static String readFile(String pPathName) {
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pPathName))));
        } catch (IOException ioe) {
            Log.e("File Error", "Failed To Open File");
            return null;
        }
        String dataOfFile = "";
        String buf;
        try {
            //adds the content till all of it is added
            while ((buf = bReader.readLine()) != null) {
                dataOfFile = dataOfFile.concat(buf + "\n");
            }
        } catch (IOException ioe) {
            Log.e("File Error", "Failed To Read From File");
            return null;
        }
        Log.i("fileData", dataOfFile);
        return dataOfFile;
    }
}