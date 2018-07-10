package citruscircuits.scout.utils;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class AppUtils {
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
}