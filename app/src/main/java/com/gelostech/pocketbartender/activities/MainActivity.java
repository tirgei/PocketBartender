package com.gelostech.pocketbartender.activities;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.gelostech.pocketbartender.R;
import com.gelostech.pocketbartender.fragments.FavesFragment;
import com.gelostech.pocketbartender.fragments.FavesFragment_ViewBinding;
import com.gelostech.pocketbartender.fragments.HomeFragment;
import com.gelostech.pocketbartender.fragments.SearchFragment;
import com.gelostech.pocketbartender.fragments.SettingsFragment;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener, AHBottomNavigation.OnNavigationPositionListener{
    private Boolean isDoubleBack = false;
    private FragmentTransaction ft;

    @BindView(R.id.bottom_navbar) AHBottomNavigation navigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();
        getSupportFragmentManager().beginTransaction().add(R.id.view_holder, new HomeFragment()).commit();
    }

    private void initViews(){
        final Drawable home = new IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_home_outline).color(Color.GRAY).sizeDp(20);
        final Drawable search = new IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_search).color(Color.GRAY).sizeDp(20);
        final Drawable faves = new IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_heart_outline).color(Color.GRAY).sizeDp(20);
        final Drawable settings = new IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_gear_outline).color(Color.GRAY).sizeDp(20);

        navigationBar.addItem(new AHBottomNavigationItem("home", home));
        navigationBar.addItem(new AHBottomNavigationItem("search", search));
        navigationBar.addItem(new AHBottomNavigationItem("faves", faves));
        navigationBar.addItem(new AHBottomNavigationItem("settings", settings));

        navigationBar.setDefaultBackgroundColor(Color.parseColor("#FFFFFF"));
        navigationBar.setInactiveColor(Color.parseColor("#4d4d4d"));
        navigationBar.setAccentColor(Color.parseColor("#009900"));
        navigationBar.setBehaviorTranslationEnabled(false);
        navigationBar.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        navigationBar.setOnTabSelectedListener(this);
        navigationBar.setOnNavigationPositionListener(this);
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        switch (position){
            case 0:
                switchView(new HomeFragment());
                break;

            case 1:
                switchView(new SearchFragment());
                break;

            case 2:
                switchView(new FavesFragment());
                break;

            case 3:
                switchView(new SettingsFragment());
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onPositionChange(int y) {

    }

    private void switchView(Fragment fragment){
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.view_holder, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if(!isDoubleBack){
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            isDoubleBack = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isDoubleBack = false;
                }
            }, 1500);

        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




}
