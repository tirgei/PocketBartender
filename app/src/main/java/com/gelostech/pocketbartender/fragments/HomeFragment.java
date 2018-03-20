package com.gelostech.pocketbartender.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gelostech.pocketbartender.R;
import com.gelostech.pocketbartender.activities.CocktailActivity;
import com.gelostech.pocketbartender.adapters.HomeAdapter;
import com.gelostech.pocketbartender.commoners.BartenderSingleton;
import com.gelostech.pocketbartender.commoners.BartenderUtil;
import com.gelostech.pocketbartender.models.HomeModel;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private View view;
    private HomeAdapter homeAdapter;
    private List<HomeModel> cocktails;
    private BartenderUtil util;
    private final String font1 = "fonts/COCKB___.TTF";
    private final String font2 = "fonts/COCKTAIL.TTF";
    private static final int ALCOHOLIC = 0, NON_ALCOHOLIC = 1;

    @BindView(R.id.home_toolbar) Toolbar toolbar;
    @BindView(R.id.home_rv) RecyclerView rv;
    @BindView(R.id.home_loading) ProgressBar progressBar;
    @BindView(R.id.home_title) TextView title;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view == null){
            view = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this, view);

            initViews();
            loadCocktails(ALCOHOLIC);

        }

        return view;
    }

    private void initViews(){
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
        title.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), font1));

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv.addItemDecoration(new ItemSpacing(getActivity(), R.dimen.spacing));

        cocktails = new ArrayList<>();
        homeAdapter = new HomeAdapter(getActivity(), cocktails, new HomeAdapter.OnClickListener() {
            @Override
            public void onClick(HomeModel model) {
                Intent intent = new Intent(getActivity(), CocktailActivity.class);
                intent.putExtra("cocktail", model);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_b, R.anim.exit_a);
            }
        });
        rv.setAdapter(homeAdapter);

        util = new BartenderUtil();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.filter_home);
        item.setIcon(new IconicsDrawable(getActivity()).icon(Ionicons.Icon.ion_ios_settings).sizeDp(24).color(ContextCompat.getColor(getActivity(), R.color.colorAccent)));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.home_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.filter_home:
                filter();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadCocktails(int type){
        if(type == 0){
            cocktails.clear();
            JsonObjectRequest fetchCocktails = new JsonObjectRequest(Request.Method.GET, BartenderUtil.HOME_ALCOHOLIC, null, new Response.Listener<JSONObject>() {
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

                            cocktails.add(getRandomIndex(cocktails.size()), cocktail);

                        }

                        homeAdapter.notifyDataSetChanged();
                        rv.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });


            BartenderSingleton.getInstance(getActivity()).addToRequestQueue(fetchCocktails);
        }
        else if(type == 1){

            cocktails.clear();
            JsonObjectRequest fetchCocktails = new JsonObjectRequest(Request.Method.GET, BartenderUtil.HOME_NON_ALCOHOLIC, null, new Response.Listener<JSONObject>() {
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

                            cocktails.add(getRandomIndex(cocktails.size()), cocktail);

                        }

                        homeAdapter.notifyDataSetChanged();
                        rv.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            BartenderSingleton.getInstance(getActivity()).addToRequestQueue(fetchCocktails);
        }


    }

    private void filter(){
        final CharSequence[] items = { "Alcoholic", "Non-alcoholic"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Filter cocktails");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                rv.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                loadCocktails(item);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public int getRandomIndex(int size){
        return ((int) (Math.random() * size));
    }


    public class ItemSpacing extends RecyclerView.ItemDecoration{
        private int space;

        public ItemSpacing(int space){
            this.space = space;
        }

        public ItemSpacing(Context context, int space){
            this(context.getResources().getDimensionPixelSize(space));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.set(space, space, space, space);
        }
    }

}
