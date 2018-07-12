package citruscircuits.scout;

import android.os.Bundle;
import android.view.View;

import citruscircuits.scout._superActivities.DialogMaker;

public class A2A extends DialogMaker {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacheck);
    }

    public void onClickEndScouting(View view) { open(A0A.class, null, false, false); }

}
