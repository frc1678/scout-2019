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

    public static String compressMatchData(JSONObject pMatchData) {
        String compressedData = "";
        try {
            //FIRST, COMPRESS THE DATAPOINTS THAT HAVE NO NESTED KEYS
            Log.e("CHECKPOINT 1", "released");
            JSONObject uncompressedData = pMatchData;
            String headerKey = getHeaderKey(uncompressedData.keys());

            //All Data without the headerkey - like without the initial key ex:1678Q3-12
            JSONObject uncompressedUnderHeaderKey = new JSONObject(uncompressedData.getString(headerKey));

            //going to be the result of all data compressed - placeholder for storing compressed data
            JSONObject compressed = new JSONObject();

            Iterator<?> uncompressedKeys = uncompressedUnderHeaderKey.keys();

            while (uncompressedKeys.hasNext()) {
                Log.e("CHECKPOINT 2", "released");
                String key = (String) uncompressedKeys.next();
                if(!Cst.nestedKeys.contains(key)){
                    Log.e("CHECKPOINT 3", "released");
                    if(!Cst.contains(key)){
                        if (Cst.compressValues.containsKey(uncompressedUnderHeaderKey.get(key).toString())){
                            Log.e("CHECKPOINT 4", "released");
                            if(uncompressedUnderHeaderKey.get(key).toString().equals("true") || uncompressedUnderHeaderKey.get(key).toString().equals("false")){
                                Log.e("CHECKPOINT 5", "released");
                                compressed.put(Cst.compressKeys.get(key), Double.parseDouble(Cst.compressValues.get(uncompressedUnderHeaderKey.get(key).toString())));
                            }else{
                                compressed.put(Cst.compressKeys.get(key), Cst.compressValues.get(uncompressedUnderHeaderKey.get(key).toString()));
                            }
                        } else {
                            if(key.equals("scoutName")){
                                compressed.put(Cst.compressKeys.get(key), uncompressedUnderHeaderKey.get(key).toString());
                            }else{
                                compressed.put(Cst.compressKeys.get(key), Double.parseDouble(uncompressedUnderHeaderKey.get(key).toString()));
                            }
                        }
                    }else{
                        JSONArray list = uncompressedUnderHeaderKey.getJSONArray(key);
                        JSONArray compressedList = new JSONArray();
                        for(int i = 0; i < list.length(); ++i){
                            compressedList.put(Integer.parseInt(Cst.compressValues.get(list.get(i).toString())));
                        }
                        compressed.put(Cst.compressKeys.get(key),compressedList);
                    }
                }
            }
            //THEN, COMPRESS DATAPOINTS WITH NESTED KEYS
            for(int i = 0; i < Cst.nestedKeys.size(); i++){
                JSONArray listOfDicts = new JSONArray();
                String nestedKey = Cst.nestedKeys.get(i);
                if(!nestedKey.equals("climb")) {
                    if (uncompressedUnderHeaderKey.has(nestedKey)) {
                        Log.e("nestedKey", nestedKey);
                        JSONArray keyWithNestedKeys = uncompressedUnderHeaderKey.getJSONArray(nestedKey);
                        for (int j = 0; j < keyWithNestedKeys.length(); ++j){
                            JSONObject compressedJ1 = new JSONObject();
                            JSONObject j1 = keyWithNestedKeys.getJSONObject(j);
                            Iterator<?> j1Keys = j1.keys();
                            while (j1Keys.hasNext()) {
                                String key = (String) j1Keys.next();
                                if (Cst.compressValues.containsKey(j1.get(key).toString())) {
                                    if (j1.get(key).toString().equals("true") || j1.get(key).toString().equals("false")) {
                                        compressedJ1.put(Cst.compressKeys.get(key), Double.parseDouble(Cst.compressValues.get(j1.get(key).toString())));
                                    } else {
                                        compressedJ1.put(Cst.compressKeys.get(key), Cst.compressValues.get(j1.get(key).toString()));
                                    }
                                } else {
                                    compressedJ1.put(Cst.compressKeys.get(key), Double.parseDouble(j1.get(key).toString()));
                                }
                            }
                            listOfDicts.put(compressedJ1);
                            compressed.put(Cst.compressKeys.get(nestedKey), listOfDicts);
                        }
                    }
                }else{
                    //CLIMB DATA HAS DOUBLE NESTED KEYS
                    if(uncompressedUnderHeaderKey.has(nestedKey)){
                        Log.e("nestedKey", nestedKey);
                        try{
                            for (int k = 0; k < uncompressedUnderHeaderKey.getJSONArray(nestedKey).length(); ++k){
                                JSONObject compressedJ2 = new JSONObject();
                                JSONObject climbData = uncompressedUnderHeaderKey.getJSONArray(nestedKey).getJSONObject(k);
                                JSONObject tempCompressed = new JSONObject();
                                String climbTitle = getHeaderKey(climbData.keys());
                                JSONObject climbDetails = new JSONObject(climbData.get(climbTitle).toString());
                                Iterator<?> j1Keys = climbDetails.keys();
                                while (j1Keys.hasNext()) {
                                    String key = (String) j1Keys.next();
                                    if (Cst.compressValues.containsKey(climbDetails.get(key).toString())) {
                                        if(climbDetails.get(key).toString().equals("true") || climbDetails.get(key).toString().equals("false")){
                                            compressedJ2.put(Cst.compressKeys.get(key), Double.parseDouble(Cst.compressValues.get(climbDetails.get(key).toString())));
                                        }else{
                                            compressedJ2.put(Cst.compressKeys.get(key), Cst.compressValues.get(climbDetails.get(key).toString()));
                                        }
                                    } else {
                                        compressedJ2.put(Cst.compressKeys.get(key), Double.parseDouble(climbDetails.get(key).toString()));
                                    }
                                }
                                tempCompressed.put(Cst.compressKeys.get(climbTitle), compressedJ2);
                                listOfDicts.put(tempCompressed);
                                compressed.put(Cst.compressKeys.get(nestedKey), listOfDicts);
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
            compressedData = headerKey + "|" + compressed.toString().substring(1, compressed.toString().length()-1).replace("\"", "").replace(" ", "");
            Log.e("FINAL", compressedData.toString());
        } catch (JSONException JE) {
            JE.printStackTrace();
            Log.e("CHECKPOINT", "SOMETHING WENT WRONG");
        }
        return compressedData;
    }

    public static String getHeaderKey(Iterator<?> keys) {
        String key = "";
        while (keys.hasNext()) {
            key = (String) keys.next();
        }
        return key;
    }

}