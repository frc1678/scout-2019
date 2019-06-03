package citruscircuits.scout.Managers;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import citruscircuits.scout._superDataClasses.Cst;

import static java.lang.String.valueOf;

public class OutputManager extends InputManager{
    private static final int DISCOVER_DURATION = 300;
    private static final int REQUEST_BLU = 1;

    public static void sendMatchDataViaBt(Activity activity){
        //TODO Store File

        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);
        activity.startActivityForResult(discoveryIntent, REQUEST_BLU);
    }

    public static String compressMatchData(JSONObject pMatchData) {
        getScoutLetter();

        String compressedData = InputManager.matchKey + "|";

        try {
            JSONArray timeStampData = pMatchData.getJSONArray(InputManager.matchKey);
            Iterator<?> uncompressedKeys = InputManager.mOneTimeMatchData.keys();

            Cst.compressValues.put(InputManager.mScoutName, InputManager.mScoutLetter);

            while(uncompressedKeys.hasNext()){
                String currentKey = uncompressedKeys.next()+"";
                String currentValue = InputManager.mOneTimeMatchData.get(currentKey)+"";

                if(Cst.initialCompressKeys.containsKey(currentKey) && Cst.compressValues.containsKey(currentValue) && !(currentKey.equals("scoutName") && (InputManager.mScoutLetter + "").equals("null"))) {
                    compressedData = compressedData + Cst.initialCompressKeys.get(currentKey) + Cst.compressValues.get(currentValue) + ",";
                } else if(Cst.initialCompressKeys.containsKey(currentKey) && !(currentValue.equals("") || currentValue.equals("0") || currentValue.equals("null") || (currentKey.equals("scoutName") && (InputManager.mScoutLetter + "").equals("null")))) {
                    compressedData = compressedData + Cst.initialCompressKeys.get(currentKey) + currentValue + ",";
                }
            }

            compressedData = compressedData + "_";

            for (int i = 0; i < timeStampData.length(); i++) {
                JSONObject tData = timeStampData.getJSONObject(i);

                String compressedDic = "";

                Iterator<?> tDataKeys = tData.keys();

                while(tDataKeys.hasNext()){
                    String currentKey = tDataKeys.next()+"";
                    String currentValue = tData.get(currentKey)+"";

                    if(Cst.compressKeys.containsKey(currentKey) && Cst.compressValues.containsKey(currentValue)){
                        compressedDic = compressedDic + Cst.compressKeys.get(currentKey) + Cst.compressValues.get(currentValue);
                    } else if(Cst.compressKeys.containsKey(currentKey)) {
                        compressedDic = compressedDic + Cst.compressKeys.get(currentKey) + currentValue;
                    }
                }
                compressedData = compressedData + compressedDic + ",";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return compressedData;
    }
}