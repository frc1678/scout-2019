package citruscircuits.scout.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout.R;

public class AutoDialog extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(InputManager.mAllianceColor.equals("Red")) {
            view = inflater.inflate(R.layout.activity_auto_red, container, false);
        }
        else if(InputManager.mAllianceColor.equals("Blue")) {
            view = inflater.inflate(R.layout.activity_auto_blue, container, false);
        }
        return view;
    }
}
