package com.gelostech.pocketbartender.activities

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.gelostech.pocketbartender.R
import com.gelostech.pocketbartender.utils.PagerAdapter
import com.gelostech.pocketbartender.fragments.FavesFragment
import com.gelostech.pocketbartender.fragments.HomeFragment
import com.gelostech.pocketbartender.fragments.SearchFragment
import com.gelostech.pocketbartender.fragments.SettingsFragment
import com.gelostech.pocketbartender.utils.setFont
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), AHBottomNavigation.OnTabSelectedListener, AHBottomNavigation.OnNavigationPositionListener,
        ViewPager.OnPageChangeListener {
    private var isDoubleBack: Boolean? = false
    private var homeFragment: HomeFragment? = null
    private var searchFragment: SearchFragment? = null
    private var favesFragment: FavesFragment? = null
    private var settingsFragment: SettingsFragment? = null
    private var scrollToTopListener: ScrollToTopListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupViewPager()
        checkPermission()
    }

    private fun initViews() {

        val home = IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_home_outline).color(Color.GRAY).sizeDp(20)
        val search = IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_search).color(Color.GRAY).sizeDp(20)
        val faves = IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_heart_outline).color(Color.GRAY).sizeDp(20)
        val settings = IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_gear_outline).color(Color.GRAY).sizeDp(20)

        bottomNavBar.addItem(AHBottomNavigationItem("home", home))
        bottomNavBar.addItem(AHBottomNavigationItem("search", search))
        bottomNavBar.addItem(AHBottomNavigationItem("faves", faves))
        bottomNavBar.addItem(AHBottomNavigationItem("settings", settings))

        bottomNavBar.defaultBackgroundColor = Color.parseColor("#FFFFFF")
        bottomNavBar.inactiveColor = Color.parseColor("#4d4d4d")
        bottomNavBar.accentColor = Color.parseColor("#009900")
        bottomNavBar.isBehaviorTranslationEnabled = false
        bottomNavBar.titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
        bottomNavBar.setOnTabSelectedListener(this)
        bottomNavBar.setOnNavigationPositionListener(this)

    }

    private fun setupViewPager() {
        val adapter = PagerAdapter(supportFragmentManager, this)
        homeFragment = HomeFragment()
        searchFragment = SearchFragment()
        favesFragment = FavesFragment()
        settingsFragment = SettingsFragment()

        adapter.addAllFrags(homeFragment, searchFragment, favesFragment, settingsFragment)
        adapter.addAllTitles("Home", "Search", "Faves", "Settings")

        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(this)
        viewPager.offscreenPageLimit = 3
    }

    private fun checkPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener{
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                    }
                }).check()
    }

    override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
        if (wasSelected)
            scrollToTopListener!!.scrollToTop()
        else
            viewPager!!.setCurrentItem(position, true)

        return true
    }

    override fun onPositionChange(y: Int) {
        viewPager!!.setCurrentItem(y, true)
    }


    override fun onBackPressed() {
        if (viewPager.currentItem == 1 && !searchFragment!!.onBackPressed()) {
            return

        } else {
            if (!isDoubleBack!!) {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

                isDoubleBack = true
                Handler().postDelayed({ isDoubleBack = false }, 1500)

            } else {
                super.onBackPressed()
                finish()
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //navigationBar.setCurrentItem(position);
    }

    override fun onPageSelected(position: Int) {
        bottomNavBar.currentItem = position
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    interface ScrollToTopListener {
        fun scrollToTop()
    }

    fun scrollRv(scrollToTopListener: ScrollToTopListener) {
        this.scrollToTopListener = scrollToTopListener
    }
}
