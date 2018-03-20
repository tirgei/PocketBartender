package com.gelostech.pocketbartender.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.gelostech.pocketbartender.R;
import com.gelostech.pocketbartender.commoners.PagerAdapter;
import com.gelostech.pocketbartender.fragments.FavesFragment;
import com.gelostech.pocketbartender.fragments.HomeFragment;
import com.gelostech.pocketbartender.fragments.SearchFragment;
import com.gelostech.pocketbartender.fragments.SettingsFragment;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener,
        AHBottomNavigation.OnNavigationPositionListener, ViewPager.OnPageChangeListener{
    private Boolean isDoubleBack = false;
    private FragmentTransaction ft;
    private FragmentManager fm;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private FavesFragment favesFragment;
    private SettingsFragment settingsFragment;

    @BindView(R.id.bottom_navbar) AHBottomNavigation navigationBar;
    @BindView(R.id.main_vp) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();
        setupViewPager(viewPager);
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

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), this);
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        favesFragment = new FavesFragment();
        settingsFragment = new SettingsFragment();

        adapter.addAllFrags(homeFragment, searchFragment, favesFragment, settingsFragment);
        adapter.addAllTitles("Home", "Search", "Faves", "Settings");

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        viewPager.setCurrentItem(position);
        return true;
    }

    @Override
    public void onPositionChange(int y) {
        viewPager.setCurrentItem(y);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //navigationBar.setCurrentItem(position);
    }

    @Override
    public void onPageSelected(int position) {
        navigationBar.setCurrentItem(position);



    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
