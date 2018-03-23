package com.gelostech.pocketbartender.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gelostech.pocketbartender.R;
import com.gelostech.pocketbartender.models.HomeModel;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tirgei on 3/4/18.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Context context;
    private List<HomeModel> cocktails;
    private OnSearchResultClick resultClick;

    public interface OnSearchResultClick{
        void onSearchResultClick(HomeModel model);
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name;

        public SearchViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_thumb);
            name = itemView.findViewById(R.id.search_item_name);
        }

        public void bind(final HomeModel model, final OnSearchResultClick click){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onSearchResultClick(model);
                }
            });
        }
    }

    public SearchAdapter(Context context, OnSearchResultClick resultClick){
        this.context = context;
        this.cocktails = new ArrayList<>();
        this.resultClick = resultClick;
    }

    public void addCocktail(HomeModel model){
        cocktails.add(model);
        notifyDataSetChanged();
    }

    public void clear(){
        cocktails.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);

        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        HomeModel model = cocktails.get(position);

        Glide.with(context).setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.loading)).load(model.getImageUrl()).thumbnail(0.05f).into(holder.imageView);
        holder.name.setText(model.getName());
        holder.bind(model, resultClick);

    }

    @Override
    public int getItemCount() {
        return cocktails.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
