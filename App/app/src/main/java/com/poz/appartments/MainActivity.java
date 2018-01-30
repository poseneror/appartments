package com.poz.appartments;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mapFrame, containerFrame;
    private MapFragment mapFragment;
    private boolean containedFragment = false;

    private int userId = -1;

    private boolean scrollingApartments;
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("Appatments", 0);
        userId = settings.getInt("user_id", -1);

        mapFrame = (LinearLayout) findViewById(R.id.map_frame);
        containerFrame = (LinearLayout) findViewById(R.id.container_frame);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
    }

    public void showFragment(Fragment fragment){
        if(fragment instanceof ApartmentDetailsFragment)
            scrollingApartments = true;
        else
            scrollingApartments = false;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment, "currentFragment");
        ft.commit();
        minimizeMap();
    }

    public void hideFragment(){
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("currentFragment");
        if(currentFragment != null){
            if(currentFragment instanceof NewApatmentFragment)
                mapFragment.updateMap();
            else if (currentFragment instanceof ApartmentDetailsFragment)
                scrollingApartments = false;
        }
        maximizeMap();
    }

//    @Override
//    public void onBackPressed() {
//        if(containedFragment = true){
//            if(getSupportFragmentManager().findFragmentByTag("ApartmentDetails") != null)
//                hideApartmentDetailsFragment();
//            else if(getSupportFragmentManager().findFragmentByTag("NewApartment") != null)
//                hideNewApartmentFragment();
//        } else {
//            finish();
//        }
//    }

    public void minimizeMap(){
        LinearLayout.LayoutParams map_params = (LinearLayout.LayoutParams)
                mapFrame.getLayoutParams();
        map_params.weight = 0.3f;
        mapFrame.setLayoutParams(map_params);

        LinearLayout.LayoutParams con_params = (LinearLayout.LayoutParams)
                containerFrame.getLayoutParams();
        con_params.weight = 0.7f;
        containerFrame.setLayoutParams(con_params);

        mapFragment.hideToolbar();
    }

    public void maximizeMap(){
        LinearLayout.LayoutParams map_params = (LinearLayout.LayoutParams)
                mapFrame.getLayoutParams();
        map_params.weight = 0.1f;
        mapFrame.setLayoutParams(map_params);

        LinearLayout.LayoutParams con_params = (LinearLayout.LayoutParams)
                containerFrame.getLayoutParams();
        con_params.weight = 0f;
        containerFrame.setLayoutParams(con_params);

        mapFragment.showToolbar();
    }
    // TEMP - until token system is online
    public boolean isScrollingApartments(){
        return scrollingApartments;
    }
    public void setUserId(int id){
        userId = id;
        settings.edit().putInt("user_id", id).apply();
    }
    public int getUserId(){
        return userId;
    }
}
