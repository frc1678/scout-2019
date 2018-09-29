package citruscircuits.scout.Managers;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citruscircuits.scout.A0A;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout.utils.AppUtils;

//Written by the Daemon himself ~ Calvin
public class InputManager {
    //Tasks
    // (1) Store Permanent Data In SDCARD In MainActivity
    // (2) Store Temporary Data During Scouting Process

    //QR String
    public static String mQRString;
    public static String mScoutLetter;
    public static int mSPRRanking;

    //Match Data Holders
    public static JSONObject mRealTimeMatchData;
    public static JSONObject mRealTimeInputtedData;

    //Main Inputs
    public static String mAllianceColor = "";

    public static String mScoutName = "unselected";
    public static int mScoutId = 0;

    public static int mMatchNum = 0;
    public static int mTeamNum = 0;

    public static int mCycleNum = 0;

    //Map-Scouting Variables
    public static String mStartingPosition;

    public static JSONArray climbDataArray = new JSONArray();
    public static ArrayList<HashMap<String, Object>> climbDataList = new ArrayList<HashMap<String, Object>>();

    public static JSONArray autoAllianceSwitchDataArray = new JSONArray();
    public static JSONArray teleAllianceSwitchDataArray = new JSONArray();
    public static JSONArray teleOpponentSwitchDataArray = new JSONArray();

    public static ArrayList<HashMap<String, Object>> autoAllianceSwitchDataList = new ArrayList<HashMap<String, Object>>();
    public static ArrayList<HashMap<String, Object>> teleAllianceSwitchDataList = new ArrayList<HashMap<String, Object>>();
    public static ArrayList<HashMap<String, Object>> teleOpponentSwitchDataList = new ArrayList<HashMap<String, Object>>();

    public static JSONArray autoScaleDataArray = new JSONArray();
    public static JSONArray teleScaleDataArray = new JSONArray();

    public static ArrayList<HashMap<String, Object>> autoScaleDataList = new ArrayList<HashMap<String, Object>>();
    public static ArrayList<HashMap<String, Object>> teleScaleDataList = new ArrayList<HashMap<String, Object>>();

    public static JSONArray alliancePlatformTakenAuto = new JSONArray();
    public static JSONArray alliancePlatformTakenTele = new JSONArray();
    public static JSONArray opponentPlatformTakenTele = new JSONArray();

    public static JSONArray dropTimes = new JSONArray();
    public static JSONArray incapTimes = new JSONArray();
    public static JSONArray unincapTimes = new JSONArray();

    public static int numGroundPyramidIntakeAuto = 0;
    public static int numGroundPyramidIntakeTele = 0;
    public static int numElevatedPyramidIntakeAuto = 0;
    public static int numElevatedPyramidIntakeTele = 0;
    public static int numSpill = 0;
    public static int numFoul = 0;

    public static JSONArray vaultDataArray = new JSONArray();
    public static int totalCubesVaulted = 0;
    public static boolean vaultOpen = false;

    public static void storeUserData(){
        AppCc.setSp("allianceColor", mAllianceColor);
        AppCc.setSp("scoutName", mScoutName);
        AppCc.setSp("scoutId", mScoutId);
        AppCc.setSp("matchNum", mMatchNum);
        AppCc.setSp("teamNum", mTeamNum);
        AppCc.setSp("cycleNum", mCycleNum);
    }

    //when storing User DAta dont use variables - just use SP
    public static void recoverUserData(){
        mAllianceColor = AppCc.getSp("allianceColor", "");
        mScoutName = AppCc.getSp("scoutName", "unselected");
        mScoutId = AppCc.getSp("scoutId", 0);
        mMatchNum = AppCc.getSp("matchNum", 0);
        mTeamNum = AppCc.getSp("teamNum", 0);
        mCycleNum = AppCc.getSp("cycleNum", 0);
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
        mRealTimeInputtedData.put("teamNumber", mTeamNum);
        mRealTimeInputtedData.put("matchNumber", mMatchNum);
        mRealTimeInputtedData.put("scoutName", mScoutName);

        mRealTimeInputtedData.put("startingPosition", mStartingPosition);

        mRealTimeInputtedData.put("climb", climbDataArray);

        mRealTimeInputtedData.put("allianceSwitchAttemptAuto", autoAllianceSwitchDataArray);
        mRealTimeInputtedData.put("allianceSwitchAttemptTele", teleAllianceSwitchDataArray);
        mRealTimeInputtedData.put("opponentSwitchAttemptTele", teleOpponentSwitchDataArray);

        mRealTimeInputtedData.put("scaleAttemptAuto", autoScaleDataArray);
        mRealTimeInputtedData.put("scaleAttemptTele", teleScaleDataArray);

        mRealTimeInputtedData.put("drop", dropTimes);
        mRealTimeInputtedData.put("incap", incapTimes);
        mRealTimeInputtedData.put("unincap", unincapTimes);

        mRealTimeInputtedData.put("alliancePlatformIntakeAuto", alliancePlatformTakenAuto);
        mRealTimeInputtedData.put("alliancePlatformIntakeTele", alliancePlatformTakenTele);
        mRealTimeInputtedData.put("opponentPlatformIntakeTele", opponentPlatformTakenTele);

        mRealTimeInputtedData.put("numGroundPyramidIntakeAuto", numGroundPyramidIntakeAuto);
        mRealTimeInputtedData.put("numGroundPyramidIntakeTele", numGroundPyramidIntakeTele);
        mRealTimeInputtedData.put("numElevatedPyramidIntakeAuto", numElevatedPyramidIntakeAuto);
        mRealTimeInputtedData.put("numElevatedPyramidIntakeTele", numElevatedPyramidIntakeTele);

        mRealTimeInputtedData.put("numSpill", numSpill);
        mRealTimeInputtedData.put("numFoul", numFoul);

        mRealTimeMatchData.put(mTeamNum + "Q" + mMatchNum, mRealTimeInputtedData);
        AppCc.setSp("currentMatchDataKey", mTeamNum + "Q" + mMatchNum);
        AppCc.setSp("mRealTimeMatchData", mRealTimeMatchData.toString());
    }

    public static void recoverRealTimeMatchData() throws JSONException, NullPointerException {
        mRealTimeMatchData = AppCc.getJSONSp("mRealTimeMatchData");
        mRealTimeInputtedData = mRealTimeMatchData.getJSONObject(AppCc.getSp("currentMatchDataKey", ""));

        mTeamNum = mRealTimeInputtedData.getInt("teamNumber");
        mMatchNum = mRealTimeInputtedData.getInt("matchNumber");
        mScoutName = mRealTimeInputtedData.getString("scoutName");

        mStartingPosition = mRealTimeInputtedData.getString("startingPosition");

        climbDataArray = mRealTimeInputtedData.getJSONArray("climb");

        autoAllianceSwitchDataArray = mRealTimeInputtedData.getJSONArray("allianceSwitchAttemptAuto");
        teleAllianceSwitchDataArray = mRealTimeInputtedData.getJSONArray("allianceSwitchAttemptTele");
        teleOpponentSwitchDataArray = mRealTimeInputtedData.getJSONArray("opponentSwitchAttemptTele");

        autoScaleDataArray = mRealTimeInputtedData.getJSONArray("scaleAttemptAuto");
        teleScaleDataArray = mRealTimeInputtedData.getJSONArray("scaleAttemptTele");

        alliancePlatformTakenAuto = mRealTimeInputtedData.getJSONArray("alliancePlatformIntakeAuto");
        alliancePlatformTakenTele = mRealTimeInputtedData.getJSONArray("alliancePlatformIntakeTele");
        opponentPlatformTakenTele = mRealTimeInputtedData.getJSONArray("opponentPlatformIntakeTele");

        numGroundPyramidIntakeAuto = mRealTimeInputtedData.getInt("numGroundPyramidIntakeAuto");
        numGroundPyramidIntakeTele = mRealTimeInputtedData.getInt("numGroundPyramidIntakeTele");
        numElevatedPyramidIntakeAuto = mRealTimeInputtedData.getInt("numElevatedPyramidIntakeAuto");
        numElevatedPyramidIntakeTele = mRealTimeInputtedData.getInt("numElevatedPyramidIntakeTele");
    }

    //Backup method for acquiring scout pre-match data from backupAssignements.txt file
    public static void getBackupData(){
        if(mMatchNum > 0){
            String sortL1key = "match"+mMatchNum;
            String sortL2key = "scout"+mScoutId;

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

            try {
                String qrDataString = AppUtils.retrieveSDCardFile("QRAssignments.txt");

                JSONObject qrData = new JSONObject(qrDataString);
                mScoutLetter = qrData.getJSONObject(ssort1L1key).getString(mScoutName);

                if(mQRString != null){
                    if(mQRString.contains(mScoutLetter)){
                        mSPRRanking = mQRString.indexOf(mScoutLetter) - mQRString.indexOf("|");
                        tPrevScoutLetter = mScoutLetter;

                        AppCc.setSp("prevScoutLetter", tPrevScoutLetter);
                    }else{
                        tPrevScoutLetter = AppCc.getSp("prevScoutLetter", "");
                        if(!tPrevScoutLetter.equals("")){
                            mSPRRanking = mQRString.indexOf(mScoutLetter) - mQRString.indexOf("|");
                        }
                    }

                    AppCc.setSp("sprRanking", mSPRRanking);

                    getQRData();
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
}