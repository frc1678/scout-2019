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
        fullQRDataProcess();

        String compressedData = InputManager.matchKey + "|";
        //used to make sure that the Qr's header is formatted correctly - aLCFound
        boolean aLCFound = false;   String aLCKey = ""; String aLCValue = "";
        try {
            JSONArray timeStampData = pMatchData.getJSONArray(InputManager.matchKey);
            Iterator<?> uncompressedKeys = InputManager.mOneTimeMatchData.keys();

            while(uncompressedKeys.hasNext()){
                String currentKey = uncompressedKeys.next()+"";
                String currentValue = InputManager.mOneTimeMatchData.get(currentKey)+"";

                Cst.compressValues.put(InputManager.mScoutName, InputManager.mScoutLetter);

                if(Cst.noCommaCompressKeys.containsKey(currentKey) && Cst.compressValues.containsKey(currentValue)) {
                    if(currentKey.equals("autoLineCrossed")){
                        aLCFound = true;
                        aLCKey = currentKey;
                        aLCValue = currentValue;
                    }else{
                        compressedData = compressedData + Cst.noCommaCompressKeys.get(currentKey) + Cst.compressValues.get(currentValue);
                    }
                }
            }

            if(aLCFound){
                compressedData = compressedData + Cst.noCommaCompressKeys.get(aLCKey) + Cst.compressValues.get(aLCValue) + "_";
            }

            for (int i = 0; i < timeStampData.length(); i++) {
                JSONObject tData = timeStampData.getJSONObject(i);

                String currentKey = tData.keys().next()+"";
                String currentValue = tData.get(currentKey)+"";

                if(Cst.compressKeys.containsKey(currentKey)){
                    compressedData = compressedData + Cst.compressKeys.get(currentKey) + currentValue + ",";
                }
            }

            if(InputManager.mOneTimeMatchData.has("climb")){
                String currentKey = "climb";
                JSONObject currentValue = InputManager.mOneTimeMatchData.getJSONObject(currentKey);
                String currentKey11 = "Attempt";
                JSONObject currentValue11 = currentValue.getJSONObject(currentKey11);
                String currentKey12 = "Actual";
                JSONObject currentValue12 = currentValue.getJSONObject(currentKey12);
                String currentKey21 = "liftSelf";
                String currentKey22 = "otherRobotsLifted";


                compressedData = compressedData + Cst.compressKeys.get(currentKey)
                        + Cst.compressKeys.get(currentKey11) + Cst.compressKeys.get(currentKey21)
                        + Cst.compressValues.get(currentValue11.getBoolean(currentKey21)+"")
                        + Cst.compressKeys.get(currentKey22)
                        + currentValue11.getInt(currentKey22)+""
                        + Cst.compressKeys.get(currentKey12) + Cst.compressKeys.get(currentKey21)
                        + Cst.compressValues.get(currentValue12.getBoolean(currentKey21)+"")
                        + Cst.compressKeys.get(currentKey22)
                        + currentValue12.getInt(currentKey22)+"";

                Log.e("COMPRESSDATA", compressedData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return compressedData;
    }
}