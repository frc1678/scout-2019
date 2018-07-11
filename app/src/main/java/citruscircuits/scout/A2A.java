package citruscircuits.scout;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import citruscircuits.scout._superActivities.DialogMaker;

public class A2A extends DialogMaker {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataview);
    }

    public void onClickEndScouting(View view) { open(A0A.class, null, false, false); }

}
