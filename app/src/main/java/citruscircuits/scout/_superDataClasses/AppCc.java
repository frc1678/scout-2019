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
        Log.e("INSTANCE REF", INSTANCE.toString());
    }

    public final static SharedPreferences getSp(){
        Log.e("INSTANCE REF", INSTANCE.toString());
        return INSTANCE.getSharedPreferences(Cst.SHARED_PREF, Activity.MODE_PRIVATE);
    }

    public static String getSp(String key, String defaultValue){
        return AppCc.getSp().getString(key, defaultValue);
    }

    public static int getSp(String key, int defaultValue){
        return AppCc.getSp().getInt(key, defaultValue);
    }

    public static JSONObject getJSONSp(String key){
        return AppUtils.toJSONObject(AppCc.getSp(key, ""));
    }

    public static void setSp(String key, String value) {
        AppCc.getSp().edit().putString(key, value).apply();
    }

    public static void setSp(String key, int value) {
        AppCc.getSp().edit().putInt(key, value).apply();
    }
}