package com.gelostech.pocketbartender.fragments


import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.gelostech.pocketbartender.R
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.github.javiersantos.materialstyleddialogs.enums.Style
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.ionicons_typeface_library.Ionicons


/**
 * A simple [Fragment] subclass.
 */
class PreferencesFragment : PreferenceFragmentCompat(){

    @SuppressLint("NewApi")
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)

        aboutApp()
        more()
        sendEmail()
        rateApp()
        shareApp()

    }

    private fun aboutApp() {
        val about = findPreference("about")
        about.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            MaterialStyledDialog.Builder(context)
                    .setTitle("Pocket Bartender")
                    .setDescription("This app is powered by TheCocktailDB. Checkout www.thecocktaildb.com for more info")
                    .setStyle(Style.HEADER_WITH_ICON)
                    .setHeaderColor(R.color.colorAccent)
                    .withIconAnimation(false)
                    .withDarkerOverlay(true)
                    .setIcon(IconicsDrawable(context).icon(Ionicons.Icon.ion_beer).color(ContextCompat.getColor(context!!, R.color.colorPrimary)).sizeDp(20))
                    .setPositiveText("DISMISS")
                    .onPositive { dialog, _ -> dialog.dismiss() }
                    .show()

            true
        }
    }

    private fun more() {
        val more = findPreference("more")
        more.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val uri = Uri.parse(resources.getString(R.string.developer_id))
            val devAccount = Intent(Intent.ACTION_VIEW, uri)

            devAccount.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(devAccount)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse(resources.getString(R.string.developer_id))))
            }

            true
        }
    }

    private fun sendEmail() {
        val getDev = findPreference("contact_dev")
        getDev.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto: devtirgei@gmail.com")
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pocket Bartender")
            startActivity(Intent.createChooser(emailIntent, "Send feedback"))

            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun rateApp() {
        val rate = findPreference("rate_app")
        rate.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val uri = Uri.parse(resources.getString(R.string.play_store_link) + activity!!.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)

            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse(resources.getString(R.string.play_store_link) + activity!!.packageName)))
            }

            true
        }
    }

    private fun shareApp() {
        val invites = findPreference("send_link")
        invites.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, activity!!.getString(R.string.app_name))
            val message = resources.getString(R.string.invite_body) + "\n\n" + resources.getString(R.string.play_store_link) + activity!!.packageName
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(intent, "Share app via.."))

            true
        }
    }


}
