package com.gelostech.pocketbartender.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.gelostech.pocketbartender.R;
import com.gelostech.pocketbartender.activities.CocktailActivity;
import com.gelostech.pocketbartender.adapters.SearchAdapter;
import com.gelostech.pocketbartender.commoners.BartenderSingleton;
import com.gelostech.pocketbartender.commoners.BartenderUtil;
import com.gelostech.pocketbartender.commoners.PillNode;
import com.gelostech.pocketbartender.models.HomeModel;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import org.jetbrains.annotations.NotNull;
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
public class SearchFragment extends Fragment implements FloatingSearchView.OnQueryChangeListener, FloatingSearchView.OnSearchListener,
        FloatingSearchView.OnHomeActionClickListener, FloatingSearchView.OnMenuItemClickListener, SearchSuggestionsAdapter.OnBindSuggestionCallback{
    private View view;
    private SearchAdapter searchAdapter;

    @BindView(R.id.search_view) FloatingSearchView searchView;
    @BindView(R.id.search_rv) RecyclerView rv;
    @BindView(R.id.search_pill) PillNode pillNode;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view == null){
            view = inflater.inflate(R.layout.fragment_search, container, false);
            ButterKnife.bind(this, view);

            initViews();
        }

        return view;
    }

    private void initViews(){
        searchView.setClearBtnColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        searchView.setOnQueryChangeListener(this);
        searchView.setOnMenuItemClickListener(this);
        searchView.setOnBindSuggestionCallback(this);
        searchView.setOnHomeActionClickListener(this);
        searchView.setOnSearchListener(this);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        searchAdapter = new SearchAdapter(getActivity(), new SearchAdapter.OnSearchResultClick() {
            @Override
            public void onSearchResultClick(HomeModel model) {
                Intent intent = new Intent(getActivity(), CocktailActivity.class);
                intent.putExtra("cocktail", model);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_b, R.anim.exit_a);
            }
        });
        rv.setAdapter(searchAdapter);

        pillNode.addPills("name", "ingredient", "ordinary drink", "cocktail");
        pillNode.setPillListener(new PillNode.PillListener() {
            @Override
            public void onPillClicked(@NotNull String pill, int pos) {

            }
        });

    }


    private void searchCocktails(String name){
        JsonObjectRequest fetchCocktails = new JsonObjectRequest(Request.Method.GET, BartenderUtil.SEARCH_URL + name, null, new Response.Listener<JSONObject>() {
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

                        searchAdapter.addCocktail(cocktail);
                    }


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


    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        //searchView.swapSuggestions(suggestions);
    }

    @Override
    public void onHomeClicked() {
        searchView.clearQuery();
        Toast.makeText(getActivity(), "Back clicked", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActionMenuItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.filter_search:
                //item.setIcon(new IconicsDrawable(getActivity()).icon(Ionicons.Icon.ion_ios_settings).sizeDp(20).color(ContextCompat.getColor(getActivity(),R.color.colorAccent)));
                //Toast.makeText(getActivity(), "sbdfbdn", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {

    }


    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

    }

    @Override
    public void onSearchAction(String currentQuery) {
        searchAdapter.clear();
        searchCocktails(currentQuery);
    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = ContextCompat.getDrawable(context,R.drawable.recycler_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

}
