package citruscircuits.scout;

import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import citruscircuits.scout._superActivities.AppTc;

public class A0A extends AppTc {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


    }

    public void onClickStartScouting(View view){
        open(A1A.class, false);
    }


//        Button scoutNameButton;
//        Button scoutIDButton;
//        Button backupButton;
//        PopupWindow popupWindow;
//        RelativeLayout relativeLayout;
//        LayoutInflater layoutInflater;
//        private final MainActivity context=this;
//        @Override
//        protected void onCreate (Bundle savedInstanceState){
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//            scoutNameButton= (Button) findViewById(R.id.scoutNameButton);
//            scoutNameButton.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view){
//                    PopupMenu namePopupMenu= new PopupMenu(MainActivity.this, scoutNameButton);
//                    namePopupMenu.getMenuInflater().inflate(R.menu.name_popup_menu, namePopupMenu.getMenu());
//                    namePopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item){
//                            scoutNameButton.setText(item.getTitle());
//                            return true;
//                        }
//                    });
//                    namePopupMenu.show();
//                }
//            });
//            scoutIDButton= (Button) findViewById(R.id.scoutIDButton);
//            scoutIDButton.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view){
//                    PopupMenu idPopupMenu= new PopupMenu(MainActivity.this, scoutIDButton);
//                    idPopupMenu.getMenuInflater().inflate(R.menu.number_popup_menu, idPopupMenu.getMenu());
//                    idPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item){
//                            scoutIDButton.setText(item.getTitle());
//                            return true;
//                        }
//                    });
//                    idPopupMenu.show();
//                }
//            });
//            backupButton=(Button) findViewById(R.id.backUpButton);
//            relativeLayout= (RelativeLayout) findViewById(R.id.mainActivity);
//            backupButton.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view){
////                    Intent intent=new Intent(MainActivity.this, backupUI.class);
////                    MainActivity.this.startActivity(intent);
//                    layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//                    ViewGroup container= (ViewGroup) layoutInflater.inflate(R.layout.backupbutton,null);
//                    popupWindow = new PopupWindow(container, 600, 600, true);
//                    popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 90, 360);
//                    container.setOnTouchListener(new View.OnTouchListener(){
//                        @Override
//                        public boolean onTouch(View view, MotionEvent motionEvent){
//                            popupWindow.dismiss();
//                            return true;
//                        }
//                    });
////Hi Ben
//                }
//            });
//        }
}

