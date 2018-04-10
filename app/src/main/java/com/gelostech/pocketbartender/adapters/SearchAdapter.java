package com.gelostech.pocketbartender.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HomeModel> cocktails;
    private OnSearchResultClick resultClick;
    private OnSearchHistoryClick historyClick;

    public interface OnSearchResultClick{
        void onSearchResultClick(HomeModel model);
    }

    public interface OnSearchHistoryClick{
        void onSearchHistoryClick(HomeModel model);
    }

    public class SearchHistoryViewHolder extends RecyclerView.ViewHolder{
        private ImageView historyIcon;
        private TextView name;

        public SearchHistoryViewHolder(View itemView) {
            super(itemView);
            historyIcon = itemView.findViewById(R.id.search_history_icon);
            historyIcon.setImageDrawable(new IconicsDrawable(context).icon(Ionicons.Icon.ion_ios_reload).color(ContextCompat.getColor(context, R.color.dark_gray)).sizeDp(20));
            name = itemView.findViewById(R.id.history_title);
        }

        public void bind(final HomeModel model, final OnSearchHistoryClick onSearchHistoryClick){
            name.setText(model.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSearchHistoryClick.onSearchHistoryClick(model);
                }
            });
        }

    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name;

        public SearchViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_thumb);
            name = itemView.findViewById(R.id.search_item_name);
        }

        public void bind(final HomeModel model, final OnSearchResultClick onSearchResultClick){
            Glide.with(context).setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.loading)).load(model.getImageUrl()).thumbnail(0.05f).into(imageView);
            name.setText(model.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSearchResultClick.onSearchResultClick(model);
                }
            });
        }
    }

    public SearchAdapter(Context context, OnSearchResultClick resultClick, OnSearchHistoryClick historyClick){
        this.context = context;
        this.cocktails = new ArrayList<>();
        this.resultClick = resultClick;
        this.historyClick = historyClick;
    }

    public void addCocktail(HomeModel model){
        cocktails.add(model);
        notifyDataSetChanged();
    }

    public void clear(){
        cocktails.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0){
            return new SearchHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false));
        } else {
            return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case 0:
                ((SearchHistoryViewHolder)holder).bind(cocktails.get(position), historyClick);
                break;

            case 1:
                ((SearchViewHolder)holder).bind(cocktails.get(position), resultClick);
                break;

            default:
                break;


        }

    }

    @Override
    public int getItemCount() {
        return cocktails.size();
    }

    @Override
    public int getItemViewType(int position) {
        HomeModel model = cocktails.get(position);

        if(model.getType() == 0)
            return 0;
        else
            return 1;
    }
}
