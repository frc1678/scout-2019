package citruscircuits.scout._superDataClasses;

import android.app.Application;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import citruscircuits.scout.utils.AppUtils;

public class AppCc extends Application{
    public static AppCc INSTANCE;

    public void onCreate(){
        super.onCreate();
        INSTANCE = AppCc.this;
        Log.e("INSTANCE REF Checker", INSTANCE.toString());
    }

    //Gets any value saved into shared preferences.
    public final static SharedPreferences getSp(){
        Log.e("INSTANCE REF SharePref", INSTANCE.toString());
        return INSTANCE.getSharedPreferences(Cst.SHARED_PREF, Activity.MODE_PRIVATE);
    }

    //Gets the shared preference for string values
    public static String getSp(String key, String defaultValue){
        return AppCc.getSp().getString(key, defaultValue);
    }

    //Gets the shared preference for integer values
    public static int getSp(String key, int defaultValue){
        return AppCc.getSp().getInt(key, defaultValue);
    }

    //Gets the shared preference for JSON values
    public static JSONObject getJSONSp(String key){
        return AppUtils.toJSONObject(AppCc.getSp(key, ""));
    }

    //Sets shared preferences for string values
    public static void setSp(String key, String value) {
        AppCc.getSp().edit().putString(key, value).apply();
    }

    //Sets shared preferences for integer values
    public static void setSp(String key, int value) {
        AppCc.getSp().edit().putInt(key, value).apply();
    }
}