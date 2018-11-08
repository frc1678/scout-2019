package citruscircuits.scout._superDataClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Written by the Daemon himself ~ Calvin
public interface Cst {
    String SHARED_PREF = "scout_sp";

    List<String> SCOUT_NAMES = Arrays.asList("Aakash", "Aidan", "Apurva", "Ariel", "Asha", "Brynne",
            "Carl", "Carter", "Connor", "Eithne", "Eli", "Ellie Bu", "Ellie K", "Emily", "Erik", "Ethan",
            "Freddy", "Gemma", "Hanson", "Helena", "Jude", "Justin", "Kate", "Louise", "Michael", "Mingyo",
            "Nathan", "Nick", "Nithmi", "Penelope", "Rachel", "Sam", "Teo", "Zachary", "Backup 1", "Backup 2",
            "Backup 3", "Backup 4", "Backup 5", "Backup 6", "Backup 7", "Backup 8", "Backup 9", "Backup 10");

    List<Integer> SCOUT_IDS = Arrays.asList(1, 2, 3, 4, 5, 6 ,7 , 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18);

    //manual compression keys that aren't separated by commas
    Map<String, String> noCommaCompressKeys = new HashMap<String, String>() {{
        put("allianceColor", "R");
        put("startingPosition", "P");
        put("startedWCube", "C");

        put("autoLineCrossed", "a");
    }};

    Map<String, String> compressKeys = new HashMap<String, String>() {{
        //New constants

        put("beginIncap", "i");
        put("endIncap", "n");
        put("drop", "d");
        put("spill", "s");
        put("scaleFoul", "f");

        put("climb", "e");
        put("Attempt", "t");
        put("Actual", "c");
        put("liftSelf", "q");
        put("otherRobotsLifted", "b");

        put("intake1", "j");
        put("intake2", "k");
        put("intake3", "l");
        put("intake4", "m");

        put("exchangeScore", "x");
        put("score1", "u");
        put("score2", "t");
        put("score3", "v");
        put("score4", "w");
        put("score5", "y");
        put("score6", "z");
    }};

    //replace with new datapoints
    Map<String, String> compressValues = new HashMap<String, String>() {{
        //New constants
        put("blue", "0");
        put("red", "1");
        put("", "0");
        put("left", "1");
        put("center", "2");
        put("right", "3");
        put("false", "0");
        put("true", "1");
    }};

    ArrayList<String> nestedKeys = new ArrayList<>(Arrays.asList(
            "allianceSwitchAttemptTele"
    ));
}