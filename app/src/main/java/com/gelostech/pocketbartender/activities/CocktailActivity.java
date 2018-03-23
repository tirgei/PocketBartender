package com.gelostech.pocketbartender.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gelostech.pocketbartender.R;
import com.gelostech.pocketbartender.adapters.CocktailIngredientsAdapter;
import com.gelostech.pocketbartender.adapters.HomeAdapter;
import com.gelostech.pocketbartender.adapters.MoreAdapter;
import com.gelostech.pocketbartender.commoners.BartenderSingleton;
import com.gelostech.pocketbartender.commoners.BartenderUtil;
import com.gelostech.pocketbartender.commoners.DatabaseHelper;
import com.gelostech.pocketbartender.models.CocktailModel;
import com.gelostech.pocketbartender.models.HomeModel;
import com.google.gson.Gson;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CocktailActivity extends AppCompatActivity {
    @BindView(R.id.cocktail_toolbar) Toolbar toolbar;
    @BindView(R.id.cocktail_image) ImageView imageView;
    @BindView(R.id.cocktail_ingredients_rv) RecyclerView rv;
    @BindView(R.id.cocktail_steps) TextView cocktailSteps;
    @BindView(R.id.cocktail_more_rv) RecyclerView moreRV;
    @BindView(R.id.cocktail_more_null) TextView noCocktail;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;

    private Drawable favedItem, unfavedItem;
    private CocktailIngredientsAdapter ingredientsAdapter;
    private Boolean faved = false;
    private List<HomeModel> moreCocktails;
    private MoreAdapter moreAdapter;
    private  MenuItem fave;
    private DatabaseHelper db;
    private int id;
    private String name, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail);
        ButterKnife.bind(this);

        Intent i = getIntent();
        HomeModel model = (HomeModel) i.getSerializableExtra("cocktail");
        db = new DatabaseHelper(this);
        id = model.getId();
        name = model.getName();
        url = model.getImageUrl();

        initViews(model);
        favedItem = new IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_heart).sizeDp(22).color(ContextCompat.getColor(getApplicationContext(), R.color.faveItem));
        unfavedItem = new IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_heart).sizeDp(22).color(Color.GRAY);
        loadCocktail(model.getId());


    }

    private void initViews(HomeModel model){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(new IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_close_empty).sizeDp(16).color(Color.GRAY));
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        getSupportActionBar().setTitle(model.getName());
        Glide.with(this).setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.loading)).load(model.getImageUrl()).thumbnail(0.1f).into(imageView);

        moreCocktails = new ArrayList<>();
        ingredientsAdapter = new CocktailIngredientsAdapter(this);
        moreAdapter = new MoreAdapter(this, moreCocktails, new MoreAdapter.OnClickListener() {
            @Override
            public void onClick(HomeModel model) {
                Intent intent = new Intent(CocktailActivity.this, CocktailActivity.class);
                intent.putExtra("cocktail", model);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_b, R.anim.exit_a);
            }
        });

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(ingredientsAdapter);

        moreRV.setHasFixedSize(true);
        moreRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        moreRV.setAdapter(moreAdapter);

    }

    private void loadCocktail(int id){
        JsonObjectRequest fetchCocktail = new JsonObjectRequest(Request.Method.GET, BartenderUtil.COCKTAIL + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray cocktailArray = response.getJSONArray("drinks");
                    JSONObject cocktailObject = cocktailArray.getJSONObject(0);
                    CocktailModel model = new Gson().fromJson(cocktailObject.toString(),CocktailModel.class);

                    for(int i=1; i<=15; i++){
                        String ingredient = cocktailObject.getString("strIngredient"+i);
                        String measure = cocktailObject.getString("strMeasure"+i).replace("\n", " ");

                        if(!ingredient.isEmpty() && !ingredient.contains("null")){
                            if(!measure.isEmpty())
                                ingredientsAdapter.addIngredient(measure + ingredient);
                            else
                                ingredientsAdapter.addIngredient(ingredient);
                        }
                    }

                    cocktailSteps.setText(model.getStrInstructions().replace(". ", ".\n"));
                    suggestions(cocktailObject.getString("strIngredient1"), cocktailObject.getInt("idDrink"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        BartenderSingleton.getInstance(this).addToRequestQueue(fetchCocktail);

    }

    private void suggestions(String main, final int id){
        JsonObjectRequest fetchCocktails = new JsonObjectRequest(Request.Method.GET, BartenderUtil.MORE_COCKTAILS + main, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray cocktailArray = response.getJSONArray("drinks");

                    for(int i=0; i<cocktailArray.length(); i++){
                        JSONObject cocktailObject = cocktailArray.getJSONObject(i);

                        HomeModel cocktail = new HomeModel();
                        cocktail.setName(cocktailObject.getString("strDrink"));
                        cocktail.setImageUrl(cocktailObject.getString("strDrinkThumb"));
                        cocktail.setId(cocktailObject.getInt("idDrink"));

                        if(cocktail.getId() != id)
                            moreCocktails.add(cocktail);

                    }

                    if(moreCocktails.size() == 0)
                        noCocktail.setVisibility(View.VISIBLE);
                    else
                        moreRV.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        BartenderSingleton.getInstance(this).addToRequestQueue(fetchCocktails);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        fave = menu.findItem(R.id.fave_cocktail);
        if(faved)
            fave.setIcon(favedItem);
        else
            fave.setIcon(unfavedItem);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cocktail_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.fave_cocktail:
                favoriteItem();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void favoriteItem(){
        fave.setIcon(favedItem);

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        db.insertIntoDb(id, name, url, bitmap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_a, R.anim.exit_b);
    }
}
