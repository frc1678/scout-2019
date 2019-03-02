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
import android.widget.ToggleButton;

public class StormDialog extends Fragment {

    public static View view;
    public static Button btn_startTimer;
    public static ToggleButton tb_hab_run;
    public static Button teleButton;

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
        if(A1A.cancelStormChecker){
            btn_startTimer.setPressed(true);
            Log.e("wok", "wofansegnasgk");
            btn_startTimer.setText("RESET TIMER");
            Log.e("wok", "wokjsdfakkk");
            if (InputManager.mAllianceColor.equals("red")) {
                btn_startTimer.setBackgroundResource(R.drawable.storm_reset_red_selector);
            } else if (InputManager.mAllianceColor.equals("blue")) {
                btn_startTimer.setBackgroundResource(R.drawable.storm_reset_blue_selector);
            }
            A1A.timerCheck = true;
            A1A.startTimer = false;
            A1A.tb_incap.setEnabled(true);
            A1A.cancelStormChecker=false;
            A1A.tb_defense.setEnabled(false);
            A1A.btn_climb.setEnabled(false);
            tb_hab_run.setEnabled(true);
            tb_hab_run.setChecked(true);
            teleButton.setEnabled(true);
        }
        return view;
    }
}
