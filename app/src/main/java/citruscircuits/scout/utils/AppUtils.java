package citruscircuits.scout.utils;
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
}