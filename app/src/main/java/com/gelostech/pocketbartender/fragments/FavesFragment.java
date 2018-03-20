package com.gelostech.pocketbartender.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gelostech.pocketbartender.R;
import com.gelostech.pocketbartender.activities.CocktailActivity;
import com.gelostech.pocketbartender.adapters.FavesAdapter;
import com.gelostech.pocketbartender.commoners.DatabaseHelper;
import com.gelostech.pocketbartender.models.FavesModel;
import com.gelostech.pocketbartender.models.HomeModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavesFragment extends Fragment {
    private View view;
    private final String font1 = "fonts/COCKB___.TTF";
    private final String font2 = "fonts/COCKTAIL.TTF";
    private FavesAdapter favesAdapter;
    private List<HomeModel> faves;
    private DatabaseHelper db;

    @BindView(R.id.no_faves_tv) TextView noFaves;
    @BindView(R.id.faves_toolbar) Toolbar toolbar;
    @BindView(R.id.faves_title) TextView favesTitle;
    @BindView(R.id.faves_rv) RecyclerView rv;
    @BindView(R.id.faves_loading) ProgressBar progressBar;

    public FavesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         if(view == null){
             view = inflater.inflate(R.layout.fragment_faves, container, false);
             ButterKnife.bind(this, view);

             db = new DatabaseHelper(getActivity());
             initViews();
             loadFaves();
         }

         return view;
    }

    private void initViews(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
        favesTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), font1));
        
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        faves = new ArrayList<>();

    }

    private void loadFaves(){
        faves = db.fetchFaves(getActivity());

        if(faves.size() == 0){
            progressBar.setVisibility(View.GONE);
            noFaves.setVisibility(View.VISIBLE);
        } else {
            favesAdapter = new FavesAdapter(getActivity(), faves, new FavesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(HomeModel model, int viewID, int position) {
                    switch (viewID){
                        case 0:
                            Toast.makeText(getActivity(), "liked", Toast.LENGTH_SHORT).show();
                            break;

                        case 1:
                            model.setCocktailThumb(null);

                            Intent intent = new Intent(getActivity(), CocktailActivity.class);
                            intent.putExtra("cocktail", model);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.enter_b, R.anim.exit_a);
                            break;

                        default:
                            break;
                    }
                }
            });
            rv.setAdapter(favesAdapter);

            rv.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }


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
