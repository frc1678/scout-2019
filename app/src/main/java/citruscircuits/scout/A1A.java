package citruscircuits.scout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import citruscircuits.scout._superActivities.AppTc;
import citruscircuits.scout._superActivities.DialogMaker;

public class A1A extends DialogMaker implements View.OnClickListener{

    public Button btn_topLeftSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btn_topLeftSwitch = findViewById(R.id.topleft_switch);
        btn_topLeftSwitch.setOnClickListener(A1A.this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.topleft_switch:
                initSeesawDialog(this, false, false);
                break;
        }
    }

}
