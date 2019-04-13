package citruscircuits.scout.Managers;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout.utils.AppUtils;

import static citruscircuits.scout._superDataClasses.Cst.SCOUT_NAMES;

public class InputManager {
    //Tasks
    // (1) Store Permanent Data In SDCARD In MainActivity
    // (2) Store Temporary Data During Scouting Process

    //QR Assignment Variables
    public static String mQRString;
    public static String mQRStringFinal;
    public static String mScoutLetter;
    public static int mSPRRanking;

    public static Integer group3InitialSPR;

    public static Integer numScouts;
    public static Integer groupNumber;
    public static Integer position;
    public static Integer initialSPR;
    public static Integer groupSize;

    public static int numFoul;
    public static int cyclesDefended;
    public static int finalMatchNum;


    //Match Data Holders
    //Below holds match data
    public static JSONArray mRealTimeMatchData = new JSONArray();
    //keys that only appear once in data
    public static JSONObject mOneTimeMatchData;
    //Below is finaldata to be inputted to QR
    public static JSONObject mRealTimeInputtedData;

    //Main Inputs
    public static String matchKey = "1678Q3-13";
    public static String mAllianceColor = "";
    public static String mScoutName = "unselected";
    public static String mTabletType = "unselected";

    public static int mScoutId = 0;
    public static int mMatchNum = 0;
    public static int mTeamNum = 0;
    public static int mCycleNum = 0;

    //Map-Scouting Variables
    public static String mHabStartingPositionOrientation = "";
    public static Integer mHabStartingPositionLevel= 0;
    public static String mPreload = "";
    public static boolean isNoShow= false;
    public static Integer mTimerStarted = 0;
    public static boolean mCrossedHabLine = false;

    public static String mAppVersion = "4.0";
    public static String mAssignmentMode = "";
    public static Integer mAssignmentFileTimestamp = 0;
    public static String mDatabaseURL;
    public static String mSandstormEndPosition = "";

    public static void storeUserData() {
        AppCc.setSp("allianceColor", mAllianceColor);
        AppCc.setSp("scoutName", mScoutName);
        AppCc.setSp("scoutId", mScoutId);
        AppCc.setSp("matchNum", mMatchNum);
        AppCc.setSp("teamNum", mTeamNum);
        AppCc.setSp("cycleNum", mCycleNum);
        AppCc.setSp("tabletType", mTabletType);
        AppCc.setSp("assignmentMode", mAssignmentMode);
    }

    //when storing User DAta dont use variables - just use SP
    public static void recoverUserData() {
        mAllianceColor = AppCc.getSp("allianceColor", "");
        mScoutName = AppCc.getSp("scoutName", "unselected");
        mScoutId = AppCc.getSp("scoutId", 0);
        mMatchNum = AppCc.getSp("matchNum", 0);
        mTeamNum = AppCc.getSp("teamNum", 0);
        mCycleNum = AppCc.getSp("cycleNum", 0);
        mTabletType = AppCc.getSp("tabletType", "");
        mAssignmentMode = AppCc.getSp("assignmentMode", "");
        mDatabaseURL = AppCc.getSp("databaseURL", "");
    }

    //Backup method for acquiring scout pre-match data from assignements.txt file
    public static void getBackupData() {
        InputManager.mTeamNum = 0;

        if (mMatchNum > 0) {
            final HashMap<Integer, Integer> referenceDictionary = new HashMap<>();
              Integer referenceEvenInteger = 1;
              Integer referenceOddInteger = 1;

            for (int i = 1; i <= 18; i++) {
                Log.e("counterValue", String.valueOf(i));
                if (i <= 6) {
                    referenceDictionary.put(i,i);
                }
                else if (i > 6) {
                    if ((i) % 2 == 0) {
                        referenceDictionary.put(i, referenceEvenInteger);
                        Log.e("evenDicValue",String.valueOf(referenceEvenInteger));
                        referenceEvenInteger++;
                    }
                    else if ((i) % 2 == 1) {
                        referenceDictionary.put(i, referenceOddInteger);
                        Log.e("oddDicValue",String.valueOf(referenceOddInteger));
                        referenceOddInteger++;
                    }
                }
            }
            Log.e("referenceDic", String.valueOf(referenceDictionary));
            String sortL1key = "matches";
            Log.e("match?!",String.valueOf(sortL1key));
            String sortL2key = String.valueOf(mMatchNum);
            Integer sortL3pre = referenceDictionary.get(mScoutId);
            Log.e("matchnum?!",String.valueOf(sortL2key));

            String sortL3KeyFinal = String.valueOf((sortL3pre + mMatchNum) % 6 + 1);

            Log.e("sortL3KeyFinal",sortL3KeyFinal);

            try {
                JSONObject backupData = new JSONObject(AppUtils.retrieveSDCardFile("assignments.txt"));

                //Finds the final match number
                finalMatchNum = backupData.getJSONObject(sortL1key).length();

                backupData = backupData.getJSONObject(sortL1key).getJSONObject(sortL2key);

                Log.e("backUpData", String.valueOf(backupData));

                mAllianceColor = backupData.getJSONObject(sortL3KeyFinal).getString("alliance");
                mTeamNum = backupData.getJSONObject(sortL3KeyFinal).getInt("number");

                Log.e("teamNumber", String.valueOf(mTeamNum));

                AppCc.setSp("allianceColor", mAllianceColor);
                AppCc.setSp("teamNum", mTeamNum);

                InputManager.mAssignmentMode = "backup";
                AppCc.setSp("assignmentMode", InputManager.mAssignmentMode);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> getScoutNames() {
        String sortL1key = "letters";
        ArrayList<String> finalNamesList = new ArrayList<String>();

        String filePath = Environment.getExternalStorageDirectory().toString() + "/bluetooth";
        String fileName = "assignments.txt";

        File f = new File(filePath, fileName);

        Log.i("FILEEXISTS", f.exists() + "");

        if (f.exists()) {
            try {
                JSONObject names = new JSONObject(AppUtils.retrieveSDCardFile("assignments.txt"));
                names = names.getJSONObject(sortL1key);

                JSONArray namesArray = names.names();
                ArrayList<String> backupNames = new ArrayList<String>();

                for (int i = 0; i < namesArray.length(); i++) {
                    String finalNames = namesArray.getString(i);
                    finalNamesList.add(finalNames);
                }

                Collections.sort(finalNamesList, String.CASE_INSENSITIVE_ORDER);

                for (int i = finalNamesList.size() - 1; i >= 0; i--) {
                    if (finalNamesList.get(i).contains("Backup")) {
                        backupNames.add(finalNamesList.get(i));
                        finalNamesList.remove(i);
                    }
                }

                Collections.sort(backupNames, String.CASE_INSENSITIVE_ORDER);

                JSONArray backupArray = new JSONArray(backupNames);

                for (int i = 0; i < backupArray.length(); i++) {
                    String moreNames = backupArray.getString(i);
                    finalNamesList.add(moreNames);
                }

                Log.i("finalNames", finalNamesList.toString());

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (!f.exists()) {

            for (int i = 1; i <= 52; i++) {
                finalNamesList.add("Backup " + i);
            }
        }

        return finalNamesList;
    }

    //Assign robots to scouts based on SPR Ranking from scanned QR.
    public static void getQRAssignment(String resultText) {
        InputManager.mTeamNum = 0;

        mQRString = resultText;
        Log.e("StringValue", mQRString);

        //Isolate QRString with scout letters and SPR Rankings.
        mQRStringFinal = resultText.substring(mQRString.indexOf("|") + 1, resultText.length());
        numScouts = mQRStringFinal.length();

        Log.i("FINALQRSTRING", mQRStringFinal);

        //Set mSPRRanking
        fullQRDataProcess();

        Log.i("SPRRANKING", "" + mSPRRanking);

        //Set groupIIIInitialSPR based on the number of scouts (odd or even)
        //in order to properly distribute robots between groups of scouts.
        group3InitialSPR = (int) Math.floor((numScouts - 6) / 2.) + 7;

        Log.e("numScouts", String.valueOf(numScouts));

        //Assign scout to scout group based on SPR Ranking.
        //Groups are used to evenly distribute robots to scouts for most accurate data.
        //Assign initial SPR as the first SPR Rank in the scout group.
        if (mSPRRanking <= 6) {
            groupNumber = 1;
            initialSPR = 1;
            //Change below if fewer than 6 scouts are scouting
            groupSize = 6;
        }
        else if (mSPRRanking < group3InitialSPR) {
            groupNumber = 2;
            initialSPR = 7;
            groupSize = group3InitialSPR - 7;
        }
        else {
            groupNumber = 3;
            initialSPR = group3InitialSPR;
            groupSize = numScouts - (group3InitialSPR - 1);
        }

        AppCc.setSp("groupNumber", groupNumber);
        AppCc.setSp("initialSPR", initialSPR);
        AppCc.setSp("groupSize", groupSize);

        //Assign robot position.
        getQRData();
    }

    //Method for acquiring scout pre-match data from assignments.txt
    public static void fullQRDataProcess() {
        if (mMatchNum > 0 && mCycleNum >= 0) {
            mSPRRanking = 0;

            String tPrevScoutLetter;

            Log.e("QR Process", "Called");

            String ssort1L1key = "letters";

            String filePath = Environment.getExternalStorageDirectory().toString() + "/bluetooth";
            String fileName = "assignments.txt";

            File f = new File(filePath, fileName);

            if (f.exists()) {
                try {
                    String qrDataString = AppUtils.retrieveSDCardFile("assignments.txt");

                    JSONObject qrData = new JSONObject(qrDataString);
                    mScoutLetter = qrData.getJSONObject(ssort1L1key).getString(mScoutName);

                    if (mQRString != null) {

                        if (mQRStringFinal.contains(mScoutLetter)) {
                            mSPRRanking = mQRStringFinal.indexOf(mScoutLetter) + 1;
                            tPrevScoutLetter = mScoutLetter;

                            AppCc.setSp("prevScoutLetter", tPrevScoutLetter);
                        }

                        else {
                            tPrevScoutLetter = AppCc.getSp("prevScoutLetter", "");

                            if (!tPrevScoutLetter.equals("")) {
                                mSPRRanking = mQRStringFinal.indexOf(mScoutLetter) + 1;
                            }

                        }
                        AppCc.setSp("sprRanking", mSPRRanking);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (!f.exists()) {
                String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                int nameIndex = SCOUT_NAMES.indexOf(mScoutName);
                char scoutLetter = alphabet.charAt(nameIndex);
                mScoutLetter = scoutLetter + "*";
            }
        }
    }

    public static void getQRData() {
        //Algorithm to assign scouts to position of robot (robot 1 - 6)
        //depending on SPR Ranking and Match Number.
        //Used to distribute scouts so they do not scout with the same scouts as frequently.
        Log.i("group3", String.valueOf(group3InitialSPR));
        Log.i("groupnum", String.valueOf(AppCc.getSp("groupNumber", 0)));
        Log.i("initialSPR",String.valueOf(AppCc.getSp("initialSPR", 0)));
        Log.i("SPRTotal",String.valueOf(AppCc.getSp("sprRanking", 0)));

        position = (mMatchNum * (AppCc.getSp("groupNumber", 0)) + (AppCc.getSp("sprRanking", 0) - AppCc.getSp("initialSPR", 0))) % AppCc.getSp("groupSize", 1) + 1;

        Log.i("POSITION", position + "");

        String sortL1key = "matches";
        String sortL2key = String.valueOf(mMatchNum);
        String sortL3key = String.valueOf(position);

        Log.i("sortL1key?!",String.valueOf(sortL1key));
        Log.i("sortL2key?!",String.valueOf(sortL2key));
        Log.i("sortL3key?!",String.valueOf(sortL3key));
        Log.i("SPRRanking?!",String.valueOf(mSPRRanking));

        if (mSPRRanking > 0) {
            try {
                JSONObject backupData = new JSONObject(AppUtils.retrieveSDCardFile("assignments.txt"));

                Log.i("entirefile", String.valueOf(backupData.getJSONObject(sortL1key)));

                //Finds the final match number
                finalMatchNum = backupData.getJSONObject(sortL1key).length();

                backupData = backupData.getJSONObject(sortL1key).getJSONObject(sortL2key);

                Log.i("dataBackup", String.valueOf(backupData));

                Log.i("position???", String.valueOf(sortL3key));
                Log.i("backUpData", String.valueOf(backupData));

                mAllianceColor = backupData.getJSONObject(sortL3key).getString("alliance");
                mTeamNum = backupData.getJSONObject(sortL3key).getInt("number");

                AppCc.setSp("allianceColor", mAllianceColor);
                AppCc.setSp("teamNum", mTeamNum);

                //Set assignment mode as QR
                InputManager.mAssignmentMode = "QR";
                AppCc.setSp("assignmentMode", "QR");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initMatchKey() {
        matchKey = mTeamNum + "Q" + mMatchNum + "-" + mScoutId;
    }
}
