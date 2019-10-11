package com.gelostech.pocketbartender.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.ButterKnife
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.gelostech.pocketbartender.R
import com.gelostech.pocketbartender.adapters.CocktailIngredientsAdapter
import com.gelostech.pocketbartender.adapters.MoreAdapter
import com.gelostech.pocketbartender.commoners.BartenderSingleton
import com.gelostech.pocketbartender.commoners.BartenderUtil
import com.gelostech.pocketbartender.commoners.DatabaseHelper
import com.gelostech.pocketbartender.models.CocktailModel
import com.gelostech.pocketbartender.models.HomeModel
import com.gelostech.pocketbartender.utils.Connectivity
import com.gelostech.pocketbartender.utils.action
import com.gelostech.pocketbartender.utils.loadUrl
import com.gelostech.pocketbartender.utils.snack
import com.google.gson.Gson
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.activity_cocktail.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.*

class CocktailActivity : AppCompatActivity() {
    private var favedItem: Drawable? = null
    private var unfavedItem: Drawable? = null
    private var downloadItem: Drawable? = null
    private var ingredientsAdapter: CocktailIngredientsAdapter? = null
    private var faved = false
    private var moreCocktails: MutableList<HomeModel>? = null
    private var moreAdapter: MoreAdapter? = null
    private var fave: MenuItem? = null
    private var download: MenuItem? = null
    private var db: DatabaseHelper? = null
    private var id: Int = 0
    private var name: String? = null
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail)
        ButterKnife.bind(this)

        val model = intent.getSerializableExtra("cocktail") as HomeModel
        db = DatabaseHelper(this)
        id = model.id
        name = model.name
        url = model.imageUrl
        faved = BartenderUtil.containsCocktail(this, id.toString())

        initViews(model)
        favedItem = IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_heart).sizeDp(22).color(ContextCompat.getColor(applicationContext, R.color.faveItem))
        unfavedItem = IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_heart).sizeDp(22).color(Color.GRAY)
        downloadItem = IconicsDrawable(this).icon(Ionicons.Icon.ion_arrow_down_a).sizeDp(22).color(Color.GRAY)
        loadCocktail(model.id)

    }

    private fun initViews(model: HomeModel) {
        setSupportActionBar(cocktailToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_close_empty).sizeDp(16).color(Color.GRAY))
        cocktailToolbarLayout?.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        cocktailToolbarLayout?.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

        supportActionBar?.title = model.name
        cocktailImage.loadUrl(model.imageUrl)

        moreCocktails = ArrayList()
        ingredientsAdapter = CocktailIngredientsAdapter(this)
        moreAdapter = MoreAdapter(this, moreCocktails, MoreAdapter.OnClickListener { model ->
            val intent = Intent(this@CocktailActivity, CocktailActivity::class.java)
            intent.putExtra("cocktail", model)
            startActivity(intent)
            overridePendingTransition(R.anim.enter_b, R.anim.exit_a)
        })

        cocktailIngredientsRv?.setHasFixedSize(true)
        cocktailIngredientsRv?.layoutManager = LinearLayoutManager(this)
        cocktailIngredientsRv?.adapter = ingredientsAdapter

        cocktailMoreRv?.setHasFixedSize(true)
        cocktailMoreRv?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        cocktailMoreRv?.adapter = moreAdapter

    }

    private fun loadCocktail(id: Int) {
        if(Connectivity.isConnected(this)){
            cocktailDetails.visibility = View.VISIBLE

            val fetchCocktail = StringRequest(Request.Method.GET, BartenderUtil.COCKTAIL + id, Response.Listener { response ->
                try {
                    val cocktailArray = JSONObject(response).getJSONArray("drinks")
                    val cocktailObject = cocktailArray.getJSONObject(0)
                    val model = Gson().fromJson(cocktailObject.toString(), CocktailModel::class.java)

                    for (i in 1..15) {
                        val ingredient = cocktailObject.getString("strIngredient$i")
                        val measure = cocktailObject.getString("strMeasure$i").replace("\n", " ")

                        if (!ingredient.isEmpty() && !ingredient.contains("null")) {
                            if (!measure.isEmpty())
                                ingredientsAdapter?.addIngredient(measure + ingredient)
                            else
                                ingredientsAdapter?.addIngredient(ingredient)
                        }
                    }

                    cocktailSteps?.text = model.strInstructions.replace(". ", ".\n")
                    suggestions(cocktailObject.getString("strIngredient1"), cocktailObject.getInt("idDrink"))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener {

            })

            BartenderSingleton.getInstance(this).addToRequestQueue(fetchCocktail)

        } else {
            contentView?.snack("No internet connection", 8000) {
                action("Turn on", R.color.orange) {startActivity(Intent(WifiManager.ACTION_PICK_WIFI_NETWORK))}
            }
        }

    }

    private fun suggestions(main: String, id: Int) {
        val fetchCocktails = JsonObjectRequest(Request.Method.GET, BartenderUtil.MORE_COCKTAILS + main, null, Response.Listener { response ->
            try {
                val cocktailArray = response.getJSONArray("drinks")

                for (i in 0 until cocktailArray.length()) {
                    val cocktailObject = cocktailArray.getJSONObject(i)

                    val cocktail = HomeModel()
                    cocktail.name = cocktailObject.getString("strDrink")
                    cocktail.imageUrl = cocktailObject.getString("strDrinkThumb")
                    cocktail.id = cocktailObject.getInt("idDrink")

                    if (cocktail.id != id)
                        moreCocktails?.add(cocktail)

                }

                if (moreCocktails?.size == 0)
                    cocktailMoreNull?.visibility = View.VISIBLE
                else
                    cocktailMoreRv?.visibility = View.VISIBLE


            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { })

        BartenderSingleton.getInstance(this).addToRequestQueue(fetchCocktails)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cocktail_toolbar_menu, menu)
        download = menu.getItem(0)
        fave = menu.getItem(1)

        fave?.icon = if (faved) favedItem else unfavedItem
        download?.icon = downloadItem
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()

            R.id.fave_cocktail -> favoriteItem()
            R.id.download_image -> sharePost()

            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun favoriteItem() {
        if(faved){
            fave?.icon = unfavedItem
            db?.deleteRow(id.toString(), DatabaseHelper.FAVES_TABLE)
            BartenderUtil.removeFaveCocktail(this, id.toString())

            contentView?.snack("$name removed from favorites"){}
            faved = false

        } else {
            fave?.icon = favedItem
            val bitmap = (cocktailImage?.drawable as BitmapDrawable).bitmap
            db?.insertIntoDb(id, name, url, bitmap)
            BartenderUtil.addFaveCocktail(this, id.toString())

            contentView?.snack("$name added to favorites"){}
            faved = true
        }
    }

    private fun sharePost() {
        val bitmap = (cocktailImage?.drawable as BitmapDrawable).bitmap

        val file = File(Environment.getExternalStorageDirectory().toString() + File.separator + "Bartender")
        if(!file.exists()) file.mkdirs()

        val fileName = "Cocktail-" + System.currentTimeMillis() + ".jpg"

        val newImage = File(file, fileName)
        if(newImage.exists()) file.delete()
        try {
            val out = FileOutputStream(newImage)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()

            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(this, arrayOf(newImage.absolutePath), null, null)
            } else {
                this.sendBroadcast( Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(newImage)))
            }
            contentView?.snack("Image saved"){}

        } catch (e: Exception){
            Log.d(javaClass.simpleName, e.localizedMessage)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_a, R.anim.exit_b)
    }

}
