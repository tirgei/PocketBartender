package com.gelostech.pocketbartender.fragments;


import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.gelostech.pocketbartender.R;
import com.gelostech.pocketbartender.adapters.SearchAdapter;
import com.gelostech.pocketbartender.models.HomeModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements FloatingSearchView.OnQueryChangeListener, FloatingSearchView.OnHomeActionClickListener{
    private View view;
    private SearchAdapter searchAdapter;
    private List<HomeModel> list;
    private List<SearchSuggestion> suggestions;

    @BindView(R.id.search_view) FloatingSearchView searchView;
    @BindView(R.id.search_rv) RecyclerView rv;

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
        searchView.setOnHomeActionClickListener(this);
        searchView.setOnQueryChangeListener(this);
        searchView.setClearBtnColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        list = new ArrayList<>();
        searchAdapter = new SearchAdapter(getActivity(), list);
        rv.setAdapter(searchAdapter);

        suggestions = new ArrayList<>();


    }


    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        searchView.swapSuggestions(suggestions);
    }

    @Override
    public void onHomeClicked() {
        searchView.clearQuery();
        Toast.makeText(getActivity(), "Back clicked", Toast.LENGTH_SHORT).show();
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
