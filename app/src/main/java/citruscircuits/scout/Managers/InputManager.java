package citruscircuits.scout.Managers;

import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import citruscircuits.scout.A0A;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout.utils.AppUtils;

import static citruscircuits.scout._superDataClasses.Cst.SCOUT_NAMES;

//Written by the Daemon himself ~ Calvin
public class InputManager {
    //Tasks
    // (1) Store Permanent Data In SDCARD In MainActivity
    // (2) Store Temporary Data During Scouting Process

    //QR String
    public static String mQRString;
    public static String mScoutLetter;
    public static int mSPRRanking;

    public static int numSpill;
    public static int numFoul;

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
    public static int mScoutId = 0;
    public static String mTabletType = "unselected";

    public static int mMatchNum = 0;
    public static int mTeamNum = 0;

    public static int mCycleNum = 0;

    //Map-Scouting Variables
    public static String mHabStartingPositionOrientation = "";
    public static Integer mHabStartingPositionLevel= 0;
    public static Integer mDriverStartingPosition = 0;
    public static String mPreload = "";
    public static boolean isNoShow= false;

    public static Integer mTimerStarted = 0;
    public static boolean mCrossedHabLine = false;
    public static String mAppVersion = "0.1";
    public static String mAssignmentMode = "";
    public static Integer mAssignmentFileTimestamp = 0;
    public static String mSandstormEndPosition = "";
    public static void storeUserData(){
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
    public static void recoverUserData(){
        mAllianceColor = AppCc.getSp("allianceColor", "");
        mScoutName = AppCc.getSp("scoutName", "unselected");
        mScoutId = AppCc.getSp("scoutId", 0);
        mMatchNum = AppCc.getSp("matchNum", 0);
        mTeamNum = AppCc.getSp("teamNum", 0);
        mCycleNum = AppCc.getSp("cycleNum", 0);
        mTabletType = AppCc.getSp("tabletType", "");
        mAssignmentMode = AppCc.getSp("assignmentMode", "");
    }

    public static void prepareUserDataForNextMatch(){
        mAllianceColor = "";
        //Scout Name Doesn't Change
        //Scout Id Doesn't Change
        mMatchNum++;
        mTeamNum = 0;
        //Cycle Number Doesn't Change

        storeUserData();
    }

    //TODO update the correct key input format later
    public static void updateRealTimeMatchData() throws JSONException, NullPointerException {

    }

    public static void recoverRealTimeMatchData() throws JSONException, NullPointerException {

    }

    //Backup method for acquiring scout pre-match data from backupAssignements.txt file
    public static void getBackupData(){
        if(mMatchNum > 0){
            String sortL1key = "match"+mMatchNum;
            String sortL2key = "scout"+mScoutId;

            InputManager.mAssignmentMode = "backup";

            try {
                JSONObject backupData = new JSONObject(AppUtils.retrieveSDCardFile("backupAssignments.txt"));
                backupData = backupData.getJSONObject(sortL1key).getJSONObject(sortL2key);

                mAllianceColor = backupData.getString("alliance");
                mTeamNum = backupData.getInt("team");

                A0A.updateUserData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> getScoutNames() {
        String sortL1key = "letters";
        ArrayList<String> finalNamesList = new ArrayList<String>();

        String filePath = Environment.getExternalStorageDirectory().toString() + "/bluetooth";
        String fileName = "QRAssignments.txt";

        File f = new File(filePath,fileName);

        Log.i("FILEEXISTS", f.exists()+"");

        if(f.exists()) {
            try {
                JSONObject names = new JSONObject(AppUtils.retrieveSDCardFile("QRAssignments.txt"));
                names = names.getJSONObject(sortL1key);

                JSONArray namesArray = names.names();
                ArrayList<String> backupNames = new ArrayList<String>();

                for(int i=0;i<namesArray.length();i++){
                    String finalNames = namesArray.getString(i);
                    finalNamesList.add(finalNames);
                }

                Collections.sort(finalNamesList, String.CASE_INSENSITIVE_ORDER);

                for(int i=finalNamesList.size()-1;i>0;i--){
                    if(finalNamesList.get(i).contains("Backup")) {
                        backupNames.add(finalNamesList.get(i));
                        finalNamesList.remove(i);
                    }
                }

                Collections.sort(backupNames, String.CASE_INSENSITIVE_ORDER);

                JSONArray backupArray = new JSONArray(backupNames);

                for(int i=0;i<backupArray.length();i++){
                    String moreNames = backupArray.getString(i);
                    finalNamesList.add(moreNames);
                }

                Log.i("finalNames", finalNamesList.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(!f.exists()) {
            for(int i=1;i<=52;i++) {
                finalNamesList.add("Backup" + i);
            }
        }

        return finalNamesList;
    }

    //Method for acquiring scout pre-match data from QRAssignments.txt
    public static void fullQRDataProcess(){
        if(mMatchNum > 0 && mCycleNum >= 0){
            mSPRRanking = 0;

            String tPrevScoutLetter;

            Log.e("QR Process", "Called");

//            String sortL1key = "matches";
//            String sortL2key = "match"+mMatchNum;
            //stands for side sort 1- for sorting other things in the same layer
            String ssort1L1key = "letters";

            String filePath = Environment.getExternalStorageDirectory().toString() + "/bluetooth";
            String fileName = "QRAssignments.txt";

            File f = new File(filePath,fileName);

            if(f.exists()) {
                try {
                    String qrDataString = AppUtils.retrieveSDCardFile("QRAssignments.txt");

                    JSONObject qrData = new JSONObject(qrDataString);
                    mScoutLetter = qrData.getJSONObject(ssort1L1key).getString(mScoutName);

                    if (mQRString != null) {
                        if (mQRString.contains(mScoutLetter)) {
                            mSPRRanking = mQRString.indexOf(mScoutLetter) - mQRString.indexOf("|");
                            tPrevScoutLetter = mScoutLetter;

                            AppCc.setSp("prevScoutLetter", tPrevScoutLetter);
                        } else {
                            tPrevScoutLetter = AppCc.getSp("prevScoutLetter", "");
                            if (!tPrevScoutLetter.equals("")) {
                                mSPRRanking = mQRString.indexOf(mScoutLetter) - mQRString.indexOf("|");
                            }
                        }

                        AppCc.setSp("sprRanking", mSPRRanking);

                        getQRData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(!f.exists()) {
                String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                int nameIndex = SCOUT_NAMES.indexOf(mScoutName);
                char scoutLetter = alphabet.charAt(nameIndex);
                mScoutLetter = scoutLetter + "";
            }
        }
    }

    public static void getQRData(){
        String sortL1key = "matches";
        String sortL2key = "match"+mMatchNum;

        if(mSPRRanking > 0){
            try {
                JSONObject backupData = new JSONObject(AppUtils.retrieveSDCardFile("QRAssignments.txt"));
                backupData = backupData.getJSONObject(sortL1key).getJSONObject(sortL2key).getJSONObject(mSPRRanking+"");

                mAllianceColor = backupData.getString("alliance");
                mTeamNum = backupData.getInt("team");

                AppCc.setSp("allianceColor", mAllianceColor);
                AppCc.setSp("teamNum", mTeamNum);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initMatchKey(){
        matchKey = mTeamNum + "Q" + mMatchNum + "-" + mScoutId;
    }

//    public static void completeInputData(){
//        for(int i = 0; i < mRealTimeMatchData.length(); i++){
//            mRealTimeInputtedData mRealTimeMatchData.get(i);
//
//        }
//    }
}