package citruscircuits.scout.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import citruscircuits.scout.A1A;
import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout.R;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

public class StormDialog extends Fragment {

    public static View view;
    public static Button btn_startTimer;
    public static ToggleButton tb_hab_run;
    public static Button teleButton;
    public static RadioGroup preload2;
    public static RadioButton preloadCargo2;
    public static RadioButton preloadPanel2;
    public static RadioButton preloadNone2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        InputManager.mSandstormEndPosition = "";
        if(InputManager.mAllianceColor.equals("red")) {
            view = inflater.inflate(R.layout.activity_storm_red, container, false);
            btn_startTimer = view.findViewById(R.id.btn_timer);
            tb_hab_run = view.findViewById(R.id.tgbtn_storm_run);
            teleButton =view.findViewById(R.id.btn_to_teleop);
        }
        else if(InputManager.mAllianceColor.equals("blue")) {
            view = inflater.inflate(R.layout.activity_storm_blue, container, false);
            btn_startTimer = view.findViewById(R.id.btn_timer);
            tb_hab_run = view.findViewById(R.id.tgbtn_storm_run);
            teleButton =view.findViewById(R.id.btn_to_teleop);
        }

        preload2 = (RadioGroup) view.findViewById(R.id.preload2);
        preloadCargo2 = (RadioButton) view.findViewById(R.id.preloadCargo2);
        preloadPanel2 = (RadioButton) view.findViewById(R.id.preloadPanel2);
        preloadNone2 = (RadioButton) view.findViewById(R.id.preloadNone2);

        //Make preload buttons start the same as previous screen.
        if (InputManager.mPreload.equals("orange")) {
            preloadCargo2.setChecked(true);
        }
        else if (InputManager.mPreload.equals("lemon")) {
            preloadPanel2.setChecked(true);
        }
        else if (InputManager.mPreload.equals("none")) {
            preloadNone2.setChecked(true);
        }

        return view;
    }
}
