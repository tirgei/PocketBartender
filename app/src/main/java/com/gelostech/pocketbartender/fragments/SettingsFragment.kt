package com.gelostech.pocketbartender.fragments


import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.gelostech.pocketbartender.R

import butterknife.BindView
import butterknife.ButterKnife
import com.gelostech.pocketbartender.activities.MainActivity
import com.gelostech.pocketbartender.commoners.BartenderUtil
import com.gelostech.pocketbartender.utils.setFont
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        setHasOptionsMenu(true)
        (activity as MainActivity).setSupportActionBar(view.settingsToolbar)
        (activity as MainActivity).supportActionBar?.title = null
        view.settingsToolbarTitle.setFont(BartenderUtil.FONT)
        view.settingsToolbarTitle.text = getString(R.string.settings)

        initViews()
        return view
    }

    private fun initViews() {
        if(activity!!.supportFragmentManager.findFragmentByTag("Preferences_frag") == null){
            activity!!.supportFragmentManager
                    .beginTransaction()
                    .add(R.id.preferences_holder, PreferencesFragment(), "Preferences_frag")
                    .commit()
        }
    }


}
