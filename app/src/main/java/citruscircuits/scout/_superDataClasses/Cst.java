package citruscircuits.scout._superDataClasses;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import citruscircuits.scout.Managers.InputManager;

//Written by the Daemon himself ~ Calvin
public interface Cst {
    String SHARED_PREF = "scout_sp";

    List<String> SCOUT_NAMES = InputManager.getScoutNames();

    List<Integer> SCOUT_IDS = Arrays.asList(1, 2, 3, 4, 5, 6 ,7 , 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18);

    //manual compression keys that aren't separated by commas
    Map<String, String> initialCompressKeys = new HashMap<String, String>() {{
        put("startingLevel", "a");
        put("crossedHabLine", "b");
        put("startingLocation", "c");
        put("preload", "d");
        put("isNoShow", "f");
        put("timerStarted", "g");
        put("currentCycle", "h");
        put("scoutID", "j");
        put("scoutName", "k");
        put("appVersion", "m");
        put("assignmentMode", "n");
        put("assignmentFileTimestamp", "p");
        put("sandstormEndPosition", "G");
    }};

    Map<String, String> compressKeys = new HashMap<String, String>() {{
        //New constants

        put("type", "r");
        put("time", "s");

        put("piece", "t");
        put("zone", "u");
        put("didSucceed", "v");
        put("wasDefended", "w");
        put("structure", "x");
        put("side", "y");
        put("shotOutOfField", "H");
        put("level", "z");

        put("cyclesDefended", "J");

        put("attempted", "B");
        put("actual", "C");
        put("self", "D");
        put("robot1", "E");
        put("robot2", "F");
    }};

    //replace with new datapoints
    Map<String, String> compressValues = new HashMap<String, String>() {{
        //New constants
        put("true", "T");
        put("false", "F");
        put("left", "A");
        put("mid", "B");
        put("right", "C");
        put("far", "D");
        put("orange", "E");
        put("lemon", "G");
        put("none", "H");
        put("QR", "J");
        put("backup", "K");
        put("override", "L");
        put("intake", "M");
        put("placement", "N");
        put("drop", "P");
        put("foul", "Q");
        put("climb", "R");
        put("incap", "S");
        put("unincap", "U");
        put("startDefense", "t");
        put("endDefense", "u");
        put("zone1Left", "V");
        put("zone1Right", "W");
        put("zone2Left", "X");
        put("zone2Right", "Y");
        put("zone3Left", "Z");
        put("zone3Right", "a");
        put("zone4Left", "b");
        put("zone4Right", "c");
        put("leftLoadingStation", "d");
        put("rightLoadingStation", "e");
        put("leftRocket", "f");
        put("rightRocket", "g");
        put("cargoShip", "h");
        put("near", "s");
    }};
}