package citruscircuits.scout._superActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import citruscircuits.scout.R;

public class AppTc extends AppCompatActivity{

    public void open(Class<? extends Activity> a, Context c, boolean finish, boolean animate){
        Intent intent = new Intent();

        if(c != null){
            intent.setClass(c, a);
        }else{
            intent.setClass(this, a);
        }
        startActivity(intent);

        if(finish){
            finish();
        }

        if(animate){
            openTransition();
        }
    }

    public void openTransition() {
        this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void finish(){
        super.finish();
    }
}
