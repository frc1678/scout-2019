package citruscircuits.scout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superActivities.DialogMaker;
import citruscircuits.scout._superDataClasses.AppCc;
import citruscircuits.scout.utils.AutoDialog;

import static citruscircuits.scout.A0A.dr_blueCycle;
import static citruscircuits.scout.A0A.dr_redCycle;

//Written by the Daemon himself ~ Calvin
public class A1A extends DialogMaker implements View.OnClickListener{

    public Button btn_topLeftSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(AppCc.getSp("mapOrientation",99) != 99){
            if(AppCc.getSp("mapOrientation", 99) == 0){
                setContentView(R.layout.activity_map_new_rb);
            }else {
                setContentView(R.layout.activity_map_new_br);
            }
        }else{
            AppCc.setSp("mapOrientation", 0);
            setContentView(R.layout.activity_map_new_rb);
        }

        Fragment fragment = new AutoDialog();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if(InputManager.mAllianceColor.equals("Red")) {
            transaction.add(R.id.red_auto, fragment, "FRAGMENT");
        }
        else if(InputManager.mAllianceColor.equals("Blue")) {
            transaction.add(R.id.blue_auto, fragment, "FRAGMENT");
        }

        transaction.commit();
        setContentView(R.layout.activity_map_new_br);

//        btn_topLeftSwitch = findViewById(R.id.topleft_switch);
//        btn_topLeftSwitch.setOnClickListener(A1A.this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.topleft_switch:
                initSeesawDialog(this, false, false);
                break;
        }
    }

    public void onClickTeleop(View view) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("FRAGMENT");
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

}
