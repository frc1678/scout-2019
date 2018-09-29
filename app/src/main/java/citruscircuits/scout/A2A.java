package citruscircuits.scout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import citruscircuits.scout.Managers.InputManager;
import citruscircuits.scout._superActivities.DialogMaker;

import static java.lang.String.valueOf;

public class A2A extends DialogMaker {

    EditText et_matchNum;
    EditText et_teamNum;

    RadioButton rb_right_sp;
    RadioButton rb_center_sp;
    RadioButton rb_left_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacheck);

        et_matchNum = findViewById(R.id.matchET);
        et_teamNum = findViewById(R.id.teamET);

        rb_right_sp = findViewById(R.id.right);
        rb_center_sp = findViewById(R.id.center);
        rb_left_sp = findViewById(R.id.left);

        et_matchNum.setText(valueOf(InputManager.mMatchNum));
        et_teamNum.setText(valueOf(InputManager.mTeamNum));

        if(InputManager.mStartingPosition.equals("right")) {
            rb_right_sp.setChecked(true);
        }
        else if(InputManager.mStartingPosition.equals("center")) {
            rb_center_sp.setChecked(true);
        }
        else if(InputManager.mStartingPosition.equals("left")) {
            rb_left_sp.setChecked(true);
        }
    }

    public void onClickEndScouting(View view) { open(A0A.class, null, false, false); }

}
