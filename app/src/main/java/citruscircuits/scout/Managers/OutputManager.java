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

//Written by the Daemon himself ~ Calvin
public class OutputManager extends InputManager{
    private static final int DISCOVER_DURATION = 300;
    private static final int REQUEST_BLU = 1;

    public static void sendMatchDataViaBt(Activity activity){
        //TODO Store File

        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);
        activity.startActivityForResult(discoveryIntent, REQUEST_BLU);
    }

    //TODO Ensure that Alliance Color, Starting Position, Started With Cube, and Auto Line Crossed are recorded in this respective order
    public static String compressMatchData(JSONObject pMatchData) {
        String compressedData = InputManager.matchKey + "|";

        try {
            JSONObject matchWOKeyData = pMatchData.getJSONObject(InputManager.matchKey);
            Iterator<?> uncompressedKeys = matchWOKeyData.keys();

            while(uncompressedKeys.hasNext()){
                Log.e("MatchDATA", compressedData);
                String currentKey = uncompressedKeys.next()+"";
                String currentValue = matchWOKeyData.get(currentKey)+"";

                if(Cst.noCommaCompressKeys.containsKey(currentKey) && Cst.compressValues.containsKey(currentValue)) {
                    if(currentKey.equals("autoLineCrossed")){
                        compressedData = compressedData + "_" + Cst.noCommaCompressKeys.get(currentKey) + Cst.compressValues.get(currentValue);
                    }else{
                        compressedData = compressedData + Cst.noCommaCompressKeys.get(currentKey) + Cst.compressValues.get(currentValue);
                    }
                }else if(Cst.compressKeys.containsKey(currentKey)){
                    compressedData = compressedData + "," + Cst.compressKeys.get(currentKey) + currentValue;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return compressedData;
    }
}